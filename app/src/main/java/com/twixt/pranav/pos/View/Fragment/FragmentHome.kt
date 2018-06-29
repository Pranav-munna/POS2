package com.twixt.pranav.pos.View.Fragment


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twixt.pranav.pos.BuildConfig
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.*
import com.twixt.pranav.pos.Controller.Responce.*
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.*
import com.twixt.pranav.pos.View.Adapter.AdapterHomeViewPAger
import com.twixt.pranav.pos.View.Adapter.RvAdapterCart
import com.twixt.pranav.pos.View.Adapter.RvAdapterItemListHome
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat
import java.util.*


/**
 * Created by Pranav on 11/24/2017.
 */

//private var database: SQLiteHelper? = null
var carts: TextView? = null
var check: TextView? = null
var recyclerview_list: RecyclerView? = null
var paymentss: Button? = null

class FragmentHome : Fragment(), RvAdapterCart.progressCartCallBAck {

    private lateinit var databaseForHome: SQLiteHelper

    override fun CartsetCallback(btnPay: Button?, s: String, context: Context, databaseForCarts: SQLiteHelper, cartt: TextView?) {

    }


    val handler = Handler()
    //    var wifi = false
    lateinit var drawer: DrawerLayout
    lateinit var selection: ImageButton
    lateinit var search: ImageButton
    lateinit var receipts_btn: ImageButton
    lateinit var settings_btn: ImageButton
    lateinit var tablayout: TabLayout
    lateinit var viewPager: ViewPager

    lateinit var progres: ProgressDialog

    lateinit var receipts: TextView
    lateinit var image: CircleImageView
    lateinit var name: TextView
    lateinit var sales: TextView
    lateinit var settings: TextView
    lateinit var title: TextView
    lateinit var pay_later: TextView
    lateinit var logout: TextView
    lateinit var app_id: TextView
    lateinit var search_et: EditText

    var formatter = DecimalFormat("#,###,##0.00")

    lateinit var message: TextView
    lateinit var ok: Button
    //tab

    lateinit var recyclerview_list_s: RecyclerView

    var total = 0.0
    var c_count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (activity != null)
            databaseForHome = SQLiteHelper(activity)

//        Foreground.init(getApplication())


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_home, container, false)
//        RequestcheckingNetwork(activity, ResponceprocessorWifi()).getStatus()
        app_id = mView.findViewById(R.id.app_id)
        drawer = mView.findViewById(R.id.drawer)
        selection = mView.findViewById(R.id.selection)
        tablayout = mView.findViewById(R.id.tablayout)
        viewPager = mView.findViewById(R.id.viewPager)
        carts = mView.findViewById(R.id.cart)
        receipts = mView.findViewById(R.id.receipts)
        image = mView.findViewById(R.id.image)
        name = mView.findViewById(R.id.name)
        check = mView.findViewById(R.id.check)
        sales = mView.findViewById(R.id.sales)
        settings = mView.findViewById(R.id.settings)
        search_et = mView.findViewById(R.id.search_et)
        search = mView.findViewById(R.id.search)
        recyclerview_list_s = mView.findViewById(R.id.recyclerview_list_s)
        logout = mView.findViewById(R.id.logout)
        title = mView.findViewById(R.id.title)
        pay_later = mView.findViewById(R.id.pay_later)

        app_id.setText("V.n " + BuildConfig.VERSION_NAME)
//        Toast.makeText(activity, "" + BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show()

        RequestBackEndChange(activity, ResponceProcessorBkend()).getBackEnd(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))

        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))

        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting


        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                val cu = databaseForHome.cartdatas(i)
                cu.moveToFirst()
                if (cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            }
            carts!!.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())
        } else {
            carts!!.setText("0")
        }


        /*if (isConnected) {
            RequestcheckingNetwork(activity, ResponceprocessorWifi()).getStatus()
        }*/

        if (isConnected /*&& wifi*/) {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LOGIN_OK, "not_ok").equals("ok")) {
                try {
                    RequestLanguageCheck(activity, ResponceProcessorLAnguageChecks()).checkLanguage(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))
                } catch (e: Exception) {
                }

            }
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_PAY, "0").equals("1")) {
                var i = 0
                while (i < databaseForHome.pendingPaySize.count) {
                    val cu = databaseForHome.getPendingPay(i)
                    cu.moveToFirst()
                    RequestPay(activity, ResponceprocessorPa()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                            cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                            cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                            cu.getString(cu.getColumnIndex("PAYMENT_TYPE")),
                            cu.getString(cu.getColumnIndex("ITEMS")))
                    i++
                    cu.close()
                }

                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_PAY, "0")
                databaseForHome.deletePay()
            }
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAY_LATER, "0").equals("1")) {
                var i = 0
                while (i < databaseForHome.pendingPayLaterSize.count) {
                    val cu = databaseForHome.getPendingPaylater(i)
                    cu.moveToFirst()
                    RequestPayLater(activity, ResponceprocessorPayLatr()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                            cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                            cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                            "0",
                            cu.getString(cu.getColumnIndex("ITEMS")),
                            cu.getString(cu.getColumnIndex("MAIL")),
                            cu.getString(cu.getColumnIndex("PRINT")))
                    i++
                    cu.close()
                }
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAY_LATER, "0")
                databaseForHome.deletePayLater()
            }

            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0").equals("1")) {
                var i = 0

                while (i < databaseForHome.pendingPaySplitSize.count) {
                    val cu = databaseForHome.getPendingPaySplit(i)
                    cu.moveToFirst()
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")), Toast.LENGTH_SHORT).show()

                    RequestPaySplit(activity, ResponceProcessorSplitPa()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                            cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                            cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                            cu.getString(cu.getColumnIndex("PAYMENT_TYPE")),
                            cu.getString(cu.getColumnIndex("ITEMS")),
                            cu.getString(cu.getColumnIndex("SPLIT_DETAILS")))
                    i++
                    cu.close()
                }

                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0")
                databaseForHome.deletePaySplit()
            }
//            wifi = false
        } else {
            handler.postDelayed({
                if (/*(!wifi) &&*/ SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                    startActivity(Intent(activity, ErrorPage::class.java))
                }
            }, 1000)


        }

        search.setOnClickListener(View.OnClickListener {
            if (search_et.visibility != View.VISIBLE)
                search_et.visibility = View.VISIBLE
            else {
                if (!search_et.text.toString().trim().equals("")) {
                    RequestCategoriesSearch(activity, ResponceprocessorSearchCaty()).getCategories(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").toString(),
                            search_et.text.toString())
                    search_et.setText("")
                }
                search_et.visibility = View.GONE
            }
//            search.isClickable = false
        })
        logout.setOnClickListener(View.OnClickListener {
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).LOGIN_OK, "not_ok")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_ID, "")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_NAME, "")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_MAIL, "")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_SHOP_ID, "")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_ROLE, "")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_IMAGE, "")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).LANGUAGE, "")
            databaseForHome.deleteCart()
            databaseForHome.deleteCato()
            databaseForHome.deleteItems()
            databaseForHome.deleteUsers()

            SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")

            val intent = activity.packageManager.getLaunchIntentForPackage(activity.getPackageName())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        })
        try {
            RequestPaymentTypes(activity, ResponceprocessorPaymentType()).getPaymentTypes(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
            RequestDescount(activity, ResponceprocessorDiscount()).getDiscounts(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
            Log.e("discount", " ok " + SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"))
        } catch (e: Exception) {
            Log.e("discount", e.toString() + " gone")
        }
        search_et.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!search_et.text.toString().trim().equals("")) {
                        RequestCategoriesSearch(activity, ResponceprocessorSearchCaty()).getCategories(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").toString(),
                                search_et.text.toString())
                        search_et.visibility = View.GONE
                        search_et.setText("")

                        search.isClickable = true
                    }
                    return true
                }
                return false
            }

        })

        val cu = databaseForHome.language
        cu.moveToFirst()

        if (!check!!.text.toString().equals("tab")) {
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TAB, "0")
            title.setText(cu.getString(cu.getColumnIndex("SALES")).toString())
        } else {
            SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TAB, "1")
        }
//        Toast.makeText(activity, databaseForHome.pendingPaySplitSize.count.toString() + " " + SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0"), Toast.LENGTH_SHORT).show()


        sales.setText(cu.getString(cu.getColumnIndex("SALES")).toString())
        receipts.setText(cu.getString(cu.getColumnIndex("RECEIPTS")).toString())
        settings.setText(cu.getString(cu.getColumnIndex("SETTINGS")).toString())
        logout.setText(cu.getString(cu.getColumnIndex("LOGOUT")).toString())
        pay_later.setText(cu.getString(cu.getColumnIndex("PAYLATER")).toString())

//        Toast.makeText(activity, cu.getString(cu.getColumnIndex("RECEIPTS")).toString(), Toast.LENGTH_SHORT).show()

//        sales.setText("")

        /* if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LANGUAGE, "") != "") {
             val cu = databaseForHome.getLanguage("sales")
             cu.moveToFirst()
             Toast.makeText(activity, cu.getString(cu.getColumnIndex("MYLANGUAGE")).toString(), Toast.LENGTH_LONG).show()
             Toast.makeText(activity, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LANGUAGE, ""), Toast.LENGTH_LONG).show()
         }*/

        //tab
        if (check!!.text.toString().equals("tab")) {
            recyclerview_list = mView.findViewById(R.id.recyclerview)
            paymentss = mView.findViewById(R.id.paymentss)
            receipts_btn = mView.findViewById(R.id.receipts_btn)
            settings_btn = mView.findViewById(R.id.settings_btn)

            if (paymentss!!.text.equals("Pay"))
                paymentss!!.setText(cu.getString(cu.getColumnIndex("PAY")).toString())
            //tab
            if (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0).toInt() > 0) {

                if (databaseForHome != null) {

                    for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                        val cu = databaseForHome.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databaseForHome.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                        cu_item.moveToFirst()
                        var t_amount = 0.0
                        var modifier_name = ""
                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        var i = 0
                        while (i < (mainstring.size - 1)) {
                            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            t_amount = t_amount + sub_strig[1].toDouble()
//                            modifier_name = modifier_name + "," + sub_strig[0]
                            i++
                        }

                        total = total + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())

                        cu.close()
                        cu_item.close()
                    }
                }
                val cu1 = databaseForHome.language
                cu1.moveToFirst()
                paymentss!!.setText(cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(total))
                val adapterCart_Rv = RvAdapterCart(activity, FragmentHome(), FragmentItems())
                recyclerview_list!!.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack2(this)
                recyclerview_list!!.getRecycledViewPool().setMaxRecycledViews(0, 0)
                adapterCart_Rv.setactivity(paymentss, 1, carts!!, databaseForHome)
                recyclerview_list!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                cu1.close()
            }

            receipts_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Receipts::class.java)) })
            settings_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Settings::class.java)) })
            paymentss!!.setOnClickListener(View.OnClickListener {
                val cu_lan = databaseForHome.language
                cu_lan.moveToFirst()
                if (!paymentss!!.text.equals(cu_lan.getString(cu_lan.getColumnIndex("PAY")).toString()))
                    startActivity(Intent(activity, Payment::class.java))
                cu_lan.close()
            })

        }
        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                val cu = databaseForHome.cartdatas(i)
                cu.moveToFirst()
                if (cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            }
            carts!!.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())
        } else {
            carts!!.setText("0")
        }

        /*val newTab = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null) as TextView
        newTab.setText("CATEGORIES")
        newTab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_categories, 0, 0, 0);
        val newTab2 = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null) as TextView
        newTab2.setText("FAVORITES")
        newTab2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stars, 0, 0, 0);*/
        tablayout.addTab(tablayout.newTab().setText(cu.getString(cu.getColumnIndex("CATEGORIES")).toString())/*.setIcon(R.drawable.ic_categories)*/)
        //tablayout.getTabAt(0)!!.setIcon(R.drawable.ic_categories)//setCustomView(newTab2)
        tablayout.addTab(tablayout.newTab().setText(cu.getString(cu.getColumnIndex("FAVORITES")).toString())/*.setIcon(R.drawable.ic_stars)*/)
//        tablayout.getTabAt(1)!!.setIcon(R.drawable.ic_stars)
        tablayout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.colorAccent))
        tablayout.setTabTextColors(ContextCompat.getColor(activity, R.color.colorPrimaryDark), ContextCompat.getColor(activity, R.color.text_white))


        val adapterHomeViewPAger = AdapterHomeViewPAger(fragmentManager, tablayout.tabCount)
        viewPager.adapter = adapterHomeViewPAger
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.setCurrentItem(tab!!.position)
            }
        })

        settings.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, Settings::class.java))
        })
        pay_later.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, PayLaterList::class.java))
        })

        selection.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(Gravity.LEFT)
        })

        carts!!.setOnClickListener(View.OnClickListener {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TAB, "0").equals("0"))
                startActivity(Intent(activity, Cart::class.java))
        })

        receipts.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, Receipts::class.java))
            drawer.closeDrawers()
        })

        Glide.with(activity)
                .asBitmap()
                .load(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_IMAGE, "http://kasy.dk/public/images/profileImage/default.jpg"))
                .into(image)

        name.setText(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_NAME, "ShopKeeper."))

        cu.close()
        return mView
    }

    inner class ResponceProcessorBkend : ProcessResponcceInterphase<ResponceBackEndChange> {
        override fun processResponce(responce: ResponceBackEndChange) {
            if (responce.backend_change.equals("1")) {
                //logout
                val popup = Dialog(activity)
                popup.setCancelable(true)
                message.setText("Backend change(update)...!!")
                popup.show()
                ok.setOnClickListener(View.OnClickListener {
                    popup.dismiss()

                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).LOGIN_OK, "not_ok")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_ID, "")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_NAME, "")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_MAIL, "")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_SHOP_ID, "")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_ROLE, "")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_IMAGE, "")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).LANGUAGE, "")
                    databaseForHome.deleteCart()
                    databaseForHome.deleteCato()
                    databaseForHome.deleteItems()

                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")



                    startActivity(Intent(activity, Login::class.java))
                    activity.finish()

                })
            }
        }

    }

    inner class ResponceprocessorPayLatr : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
        }

    }

    inner class ResponceprocessorPaymentType : ProcessResponcceInterphase<Array<ResponcePaymentTypes>> {
        override fun processResponce(responce: Array<ResponcePaymentTypes>) {
            try {
                val cu1 = databaseForHome.language
                cu1.moveToFirst()
                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                ok = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                if (responce == null) {
                    message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
                } else {
                    val list = ArrayList(Arrays.asList(*responce))
                    var i = 0

                    if (databaseForHome != null && list.size > 0) {
                        if (databaseForHome.deletePaymentType())
                            SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).PAY_COUNT, 0)

                        while (i < list.size) {
                            databaseForHome.insertPaymentTypes(responce[i].id.toString(), responce[i].name.toString(), responce[i].type.toString())
                            i++
                        }
                    }

                }

                cu1.close()
            } catch (e: Exception) {
            }

        }

    }

    inner class ResponceprocessorSearchCaty : ProcessResponcceInterphase<Array<ResponceCategories>> {
        override fun processResponce(responce: Array<ResponceCategories>) {
            val cu1 = databaseForHome.language
            cu1.moveToFirst()
            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            popup.setCancelable(false)
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            if (responce != null && responce.size > 0) {
                recyclerview_list_s.visibility = View.VISIBLE
                val list = ArrayList(Arrays.asList(*responce))
                val adapterItemList_Rv = RvAdapterItemListHome(activity)
                recyclerview_list_s.adapter = adapterItemList_Rv
                recyclerview_list_s.getRecycledViewPool().setMaxRecycledViews(0, 0)
                adapterItemList_Rv.set(list)
                recyclerview_list_s.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            } else {
                message.setText(cu1.getString(cu1.getColumnIndex("NO_SUCH_CATEGORY")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                T oast.makeText(activity, cu1.getString(cu1.getColumnIndex("NO_SUCH_CATEGORY")).toString() + responce.size, Toast.LENGTH_SHORT).show()
            }
            cu1.close()
        }

    }

    inner class ResponceprocessorPa : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
            val cu1 = databaseForHome.language
            cu1.moveToFirst()
            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            popup.setCancelable(false)
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            if (responce != null) {
                if (databaseForHome.deleteCart()) {
                    SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                    Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("PAYED")).toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                T oast . makeText (activity, cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            }
            cu1.close()
        }

    }

    inner class ResponceProcessorSplitPa : ProcessResponcceInterphase<ResponcePay> {
        override fun processResponce(responce: ResponcePay) {
            val cu1 = databaseForHome.language
            cu1.moveToFirst()
            val popup = Dialog(activity)
            popup.setContentView(R.layout.fragement_message_dialog)
            message = popup.findViewById(R.id.message)
            ok = popup.findViewById(R.id.ok)
            popup.setCancelable(false)
            popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
            if (responce == null) {
                message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                T oast . makeText (activity, cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                if (responce.status.toString().equals("success")) {

                    if (databaseForHome.deleteCart()) {
                        SharedPreferenceHelper(activity).putInt(SharedPreferenceHelper(activity).CART_COUNT, 0)
                        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAYMENT_TYPE, "0")
                        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).TOTAL_AMOUNT, "0")
//                        Toast.makeText(activity, "Payed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            cu1.close()
        }

    }

    override fun onResume() {
        super.onResume()

//        RequestcheckingNetwork(activity, ResponceprocessorWifi()).getStatus()

        RequestBackEndChange(activity, ResponceProcessorBkend()).getBackEnd(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))


        recyclerview_list_s.visibility = View.GONE
        search_et.visibility = View.GONE
        search.isClickable = true

        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                try {
                    val cu = databaseForHome.cartdatas(i)
                    cu.moveToFirst()
                    if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                        ++c_count
                    }
                    cu.close()
                } catch (e: Exception) {
                }

            }
            carts!!.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString())
        } else {
            val cu1 = databaseForHome.language
            cu1.moveToFirst()
            carts!!.setText("0")
            try {
                paymentss!!.setText(cu1.getString(cu1.getColumnIndex("PAY")).toString())
            } catch (e: Exception) {
            }
            cu1.close()
        }


//        carts!!.setText((SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0)).toString())
        total = 0.0
        if (check!!.text.toString().equals("tab")) {
            //tab
            if (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0).toInt() > 0) {
                if (databaseForHome != null) {
                    for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                        val cu = databaseForHome.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databaseForHome.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                        cu_item.moveToFirst()
                        var t_amount = 0.0
                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        var i = 0
                        while (i < (mainstring.size - 1)) {
                            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            t_amount = t_amount + sub_strig[1].toDouble()
                            i++
                        }
                        total = total + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())

                        cu.close()
                        cu_item.close()
                    }
                }
                val cu1 = databaseForHome.language
                cu1.moveToFirst()
                if (paymentss!!.text.equals("Pay"))
                    paymentss!!.setText(cu1.getString(cu1.getColumnIndex("PAY")).toString())
                paymentss!!.setText(cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(total))
                val adapterCart_Rv = RvAdapterCart(activity, FragmentHome(), FragmentItems())
                recyclerview_list!!.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack2(this)
                recyclerview_list!!.getRecycledViewPool().setMaxRecycledViews(0, 0)
                adapterCart_Rv.setactivity(paymentss, 1, carts!!, databaseForHome)
                recyclerview_list!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                cu1.close()
            }
        }


        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting


//        Toast.makeText(activity, wifi.toString(), Toast.LENGTH_SHORT).show()

        if (isConnected /*&& wifi*/) {

//            Toast.makeText(activity, "connected", Toast.LENGTH_SHORT).show()

            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LOGIN_OK, "not_ok").equals("ok")) {
                try {
                    RequestLanguageCheck(activity, ResponceProcessorLAnguageChecks()).checkLanguage(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))
                } catch (e: Exception) {
                }

            }
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_PAY, "0").equals("1")) {
                var i = 0
                while (i < databaseForHome.pendingPaySize.count) {
                    val cu = databaseForHome.getPendingPay(i)
                    cu.moveToFirst()
                    RequestPay(activity, ResponceprocessorPa()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                            cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                            cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                            cu.getString(cu.getColumnIndex("PAYMENT_TYPE")),
                            cu.getString(cu.getColumnIndex("ITEMS")))
                    i++
                    cu.close()
                }

                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_PAY, "0")
                databaseForHome.deletePay()
            }
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PAY_LATER, "0").equals("1")) {
                var i = 0
                while (i < databaseForHome.pendingPayLaterSize.count) {
                    val cu = databaseForHome.getPendingPaylater(i)
                    cu.moveToFirst()
                    RequestPayLater(activity, ResponceprocessorPayLatr()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                            cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                            cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                            "0",
                            cu.getString(cu.getColumnIndex("ITEMS")),
                            cu.getString(cu.getColumnIndex("MAIL")),
                            cu.getString(cu.getColumnIndex("PRINT")))
                    i++
                    cu.close()
                }
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PAY_LATER, "0")
                databaseForHome.deletePayLater()
            }

            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0").equals("1")) {
                var i = 0

                while (i < databaseForHome.pendingPaySplitSize.count) {
                    val cu = databaseForHome.getPendingPaySplit(i)
                    cu.moveToFirst()
//                    Toast.makeText(activity, cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")), Toast.LENGTH_SHORT).show()

                    RequestPaySplit(activity, ResponceProcessorSplitPa()).pay(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0").trim(),
                            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0").trim(),
                            cu.getString(cu.getColumnIndex("TOTAL_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_AMOUNT")),
                            cu.getString(cu.getColumnIndex("PAY_AMOUNT")),
                            cu.getString(cu.getColumnIndex("DISCOUNT_TYPE")),
                            cu.getString(cu.getColumnIndex("PAYMENT_TYPE")),
                            cu.getString(cu.getColumnIndex("ITEMS")),
                            cu.getString(cu.getColumnIndex("SPLIT_DETAILS")))
                    i++
                    cu.close()
                }

                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).NETWORK_SPLIT_PAY, "0")
                databaseForHome.deletePaySplit()
            }
//            wifi = false
        } else {
//            Toast.makeText(activity, "not connected", Toast.LENGTH_SHORT).show()
            handler.postDelayed({
                if (/*(!wifi) &&*/ SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                    startActivity(Intent(activity, ErrorPage::class.java))
                }
            }, 1000)

        }

    }

    /*inner class ResponceprocessorWifi : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
//                wifi = true
            }
        }

    }*/

    fun set(btnPay: Button?, s: String) {
        try {
            paymentss!!.setText(s.toString())
        } catch (e: Exception) {
            Log.i("something", s)
        }
//
    }

    inner class ResponceProcessorLAnguageChecks : ProcessResponcceInterphase<ResponceLanguageCheck> {
        override fun processResponce(responce: ResponceLanguageCheck) {
            if (responce != null) {
                try {
                    //                Toast.makeText(activity, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LANGUAGE, "0"), Toast.LENGTH_SHORT).show()
                    if (!SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LANGUAGE, "").equals(responce.defaultLanguage.toString())) {
//                    Toast.makeText(activity, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LANGUAGE, "0")+" ok", Toast.LENGTH_SHORT).show()
                        SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).LANGUAGE, responce.defaultLanguage.toString())
                        RequestLanguage(activity, ResponceProcessorLanguages()).getLanguage(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_ID, "0"))
                    }
//                Toast.makeText(activity, SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).LANGUAGE, ""), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {

                }

            }
        }
    }

    inner class ResponceProcessorLanguages : ProcessResponcceInterphase<ResponceLanguage> {
        override fun processResponce(responce: ResponceLanguage) {
            if (responce != null) {
                if (databaseForHome != null) {
                    databaseForHome.updateLanguage(responce.receipts, responce.refund, responce.sales,
                            responce.settings, responce.cart, responce.payment, responce.categories,
                            responce.favorites, responce.cashier, responce.total, responce.amount,
                            responce.total_amount, responce.select_payment, responce.inclusive_of_taxes, responce.passcode,
                            responce.cancel, responce.enter_your_pin, responce.quantity, responce.ok,
                            responce.remaning_amount, responce.pay, responce.split, responce.charge,
                            responce.rs, responce.check_network, responce.forgot_password, responce.enter_all_fields,
                            responce.invalid_email, responce.password_error, responce.no_items,
                            responce.offline_error, responce.email, responce.login, responce.appid,
                            responce.key, responce.activate, responce.activation, responce.invalid_activation,
                            responce.logout, responce.no_such_category, responce.payed, responce.select_payment_method,
                            responce.enter_amount, responce.amount_too_heigh, responce.refund_unavailable, responce.selected,
                            responce.added, responce.select_payment_toast, responce.applied, responce.invalidpin, responce.paymenttype, responce.offersapplied,
                            responce.send, responce.paylater, responce.sendreciept, responce.orders, responce.items,
                            responce.wait, responce.loading, responce.newsale, responce.allcategories, responce.all, responce.paylaterid, responce.print,
                            responce.viewz, responce.printtest, responce.modifier, responce.delete_modifier, responce.discount, responce.invoice,
                            responce.date, responce.item, responce.price, responce.refunded, responce.next, responce.noreciepts, responce.refundedReciept,
                            responce.cash, responce.change, responce.deselectPayment, responce.userSelect, responce.invalidEmail, responce.refundedDiscount
                    )
                }

                sales.setText(responce.sales.toString())
                title.setText(responce.sales.toString())
                receipts.setText(responce.receipts)
                settings.setText(responce.settings)
                logout.setText(responce.logout)
                pay_later.setText(responce.paylater)
                tablayout.getTabAt(0)!!.setText(responce.categories)
                tablayout.getTabAt(1)!!.setText(responce.favorites)
                if (check!!.text.toString().equals("tab")) {
                    if (paymentss!!.text.equals("Pay"))
                        paymentss!!.setText(responce.pay)
                }
            }
        }

    }

    inner class ResponceprocessorDiscount : ProcessResponcceInterphase<Array<ResponceDiscounts>> {
        override fun processResponce(responce: Array<ResponceDiscounts>) {
            val cu = databaseForHome.language
            cu.moveToFirst()


            try {
                val popup = Dialog(activity)
                popup.setContentView(R.layout.fragement_message_dialog)
                message = popup.findViewById(R.id.message)
                ok = popup.findViewById(R.id.ok)
                popup.setCancelable(false)
                popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))
                if (responce == null) {
                    message.setText(cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                T oast . makeText (activity, cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
            }


            if (responce == null) {
                /*message.setText(cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })*/
//                T oast . makeText (activity, cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                val list = ArrayList(Arrays.asList(*responce))
                var i = 0

                if (databaseForHome != null && list.size > 0) {
                    databaseForHome.deleteDiscount()
                    while (i < list.size) {
                        databaseForHome.insertDiscount(responce[i].label.toString(), responce[i].discount.toString(), responce[i].discountType.toString())
                        i++
                    }
                }

            }
            cu.close()
        }

    }

    fun itemdeleted(context: Context, databaseForCarts: SQLiteHelper) {

        var c_count = 0
        for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
            try {
                val cu = databaseForCarts.cartdatas(i)
                cu.moveToFirst()
                if (cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            } catch (e: Exception) {
            }

        }
        carts!!.setText((SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString())


    }

    fun incrementcart2(context: Context, databasefav: SQLiteHelper) {
        c_count = 0
        if (!(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                try {
                    val cu = databasefav.cartdatas(i)
                    cu.moveToFirst()

                    if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                        ++c_count
                    }
                    cu.close()
                } catch (e: Exception) {
                    Log.e("error_is_this ->", e.toString())
                }


            }
            carts!!.setText((SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString())
        } else {
            carts!!.setText("0")
        }
        total = 0.0
        if (check!!.text.toString().equals("tab")) {
            if (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0).toInt() > 0) {
                if (databasefav != null) {
                    for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                        val cu = databasefav.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databasefav.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                        cu_item.moveToFirst()
                        var t_amount = 0.0
                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var i = 0
                        while (i < (mainstring.size - 1)) {
                            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            t_amount = t_amount + sub_strig[1].toDouble()
                            i++
                        }
                        total = total + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())
                        cu.close()
                        cu_item.close()
                    }
                }
                val cu1 = databasefav.language
                cu1.moveToFirst()
                if (paymentss!!.text.equals("Pay"))
                    paymentss!!.setText(cu1.getString(cu1.getColumnIndex("PAY")).toString())
                Log.e("helphelp", total.toString())
                paymentss!!.setText(cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(total))
                val adapterCart_Rv = RvAdapterCart(context, FragmentHome(), FragmentItems())
                recyclerview_list!!.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack2(this)
                recyclerview_list!!.getRecycledViewPool().setMaxRecycledViews(0, 0)
                adapterCart_Rv.setactivity(paymentss, 1, carts!!, databasefav)
                recyclerview_list!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                cu1.close()
            }
        }
    }

    override fun StartProgressCart(progress: ProgressDialog) {
        progres = progress

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            progres.dismiss()
        } catch (e: Exception) {
        }
    }
}