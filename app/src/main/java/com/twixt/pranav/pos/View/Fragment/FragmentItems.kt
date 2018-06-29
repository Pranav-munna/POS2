package com.twixt.pranav.pos.View.Fragment


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.*
import com.twixt.pranav.pos.View.Adapter.RvAdapterCart
import com.twixt.pranav.pos.View.Adapter.RvAdapterItems
import java.text.DecimalFormat


/**
 * Created by Pranav on 11/27/2017.
 */

/*var cartMenu: MenuItem? = null
var payment: Button? = null
var c_count = 0

var a_cart: TextView? = null*/

var a_cartInItems: TextView? = null

class FragmentItems : Fragment(), RvAdapterCart.progressCartCallBAck, RvAdapterItems.ItemsCallback {
    override fun CartsetCallback(btnPay: Button?, s: String, context: Context, databaseForCarts: SQLiteHelper, cartt: TextView?) {

    }

    private lateinit var databaseforItems: SQLiteHelper
    //adapteritems
    lateinit var one: Button
    lateinit var two: Button
    lateinit var three: Button
    lateinit var four: Button
    lateinit var five: Button
    lateinit var six: Button
    lateinit var seven: Button
    lateinit var eight: Button
    lateinit var nine: Button
    lateinit var zero: Button
    lateinit var dot: Button
    lateinit var total: TextView
    lateinit var popup: Dialog
    var dot_comma_int = 1.1
    var dot_comma = ""
    var qty = 1
    lateinit var quen: TextView
    lateinit var delete: ImageButton


    lateinit var cartMenu: MenuItem
    lateinit var payment: Button
    var c_count = 0


    lateinit var recyclerview_list: RecyclerView
    lateinit var layout_rl: RelativeLayout

    lateinit var progres: ProgressDialog
    lateinit var check: TextView
    //    lateinit var a_cart: TextView
    lateinit var receipts_btn: ImageButton
    lateinit var sales_btn: ImageButton
    lateinit var settings_btn: ImageButton
    lateinit var a_back: ImageButton
    lateinit var message: TextView
    lateinit var a_title: TextView
    lateinit var all_cat: Spinner
    lateinit var ok: Button
    var cat_id = ""
    var totals = 0.0
    var pos = 0
    var formatter = DecimalFormat("#,###,##0.00")
    lateinit var recyclerview_list_cart: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater.inflate(R.layout.fragment_items, container, false)


        cat_id = arguments.getString("CAT_ID").toString()
        val cat_name = arguments.getString("CAT_NAME")
        if (activity != null)
            databaseforItems = SQLiteHelper(activity)
        val cu1 = databaseforItems.language
        cu1.moveToFirst()



        recyclerview_list = mView.findViewById(R.id.recyclerview)
        layout_rl = mView.findViewById(R.id.layout_rl)
        check = mView.findViewById(R.id.check)
        payment = mView.findViewById(R.id.payment)
        a_back = mView.findViewById(R.id.a_back)
        a_cartInItems = mView.findViewById(R.id.a_cart)
        a_title = mView.findViewById(R.id.a_title)
        all_cat = mView.findViewById(R.id.all_cat)
        recyclerview_list_cart = mView.findViewById(R.id.recyclerviewcart)


        var size = databaseforItems.getcategoriessize().count
        val catNames = arrayOfNulls<String>(size + 1)

        catNames[0] = cu1.getString(cu1.getColumnIndex("AL")).toString()//all

//        Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("AL")).toString(), Toast.LENGTH_SHORT).show()

        var j = 1
        while (j < size + 1) {

            val cu_cat = databaseforItems.getcategories(j - 1)
            cu_cat.moveToFirst()
            catNames[j] = j.toString()//cu_cat.getString(cu_cat.getColumnIndex("NAME")).toString()
//            Toast.makeText(activity, cu_cat.getString(cu_cat.getColumnIndex("NAME")), Toast.LENGTH_SHORT).show()
            catNames[j] = cu_cat.getString(cu_cat.getColumnIndex("NAME"))
            if (cat_name.equals(catNames[j]))
                pos = j
            cu_cat.close()
            j++
        }


        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).OFFLINE, "0").equals("0")) {
                startActivity(Intent(activity, ErrorPage::class.java))
            } else {
                val adapterItems_Rv = RvAdapterItems(activity, FragmentItems())
                recyclerview_list.adapter = adapterItems_Rv
                adapterItems_Rv.setCallback(this)
                recyclerview_list.recycledViewPool.setMaxRecycledViews(0, 0)
//                catId=cat_id
                adapterItems_Rv.set1(check, recyclerview_list_cart, payment, cat_id, a_cartInItems!!, databaseforItems/*,cartMenu*/)
                if (check.text.toString().equals("tab"))
                    recyclerview_list.layoutManager = GridLayoutManager(activity, 2) as RecyclerView.LayoutManager?
                else
                    recyclerview_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            }
        } /*else {

    RequestProducts(activity, ResponceProcessorItems()).getProducts(cat_id, 1)
}*/


        val adaptercat = ArrayAdapter<String>(activity, R.layout.spinner_items_blue, catNames)

        all_cat.adapter = adaptercat
        adaptercat.notifyDataSetChanged()
        all_cat.setSelection(pos)
        all_cat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val items = all_cat.selectedItem.toString()
                if (position != 0) {
                    val cu = databaseforItems.catdata(items)
                    cu.moveToFirst()
                    val adapterItems_Rv = RvAdapterItems(activity, FragmentItems())
                    recyclerview_list.adapter = adapterItems_Rv
                    adapterItems_Rv.setCallback(FragmentItems())
                    recyclerview_list.recycledViewPool.setMaxRecycledViews(0, 0)
//                    catId=cu.getString(cu.getColumnIndex("ID"))
                    adapterItems_Rv.set1(
                            check,
                            recyclerview_list_cart,
                            payment,
                            cu.getString(cu.getColumnIndex("ID")),
                            a_cartInItems!!,
                            databaseforItems/*,cartMenu*/
                    )
                    cu.close()
                } else {
                    val adapterItems_Rv = RvAdapterItems(activity, FragmentItems())
                    recyclerview_list.adapter = adapterItems_Rv
                    adapterItems_Rv.setCallback(FragmentItems())
                    recyclerview_list.recycledViewPool.setMaxRecycledViews(0, 0)
                    adapterItems_Rv.set3(
                            check,
                            recyclerview_list_cart,
                            payment,
                            a_cartInItems!!,
                            databaseforItems/*,cartMenu*/
                    )
                }
                if (check.text.toString().equals("tab"))
                    recyclerview_list.layoutManager = GridLayoutManager(activity, 2)
                else
                    recyclerview_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }

        }


        a_title.text = cat_name

        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                val cu = databaseforItems.cartdatas(i)
                cu.moveToFirst()
                if (cu.getString(cu.getColumnIndex("QUANTITY")).equals("0") && cu != null) {
                    ++c_count
                }
                cu.close()
            }
        }
        a_cartInItems!!.text = (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString()




        if (check.text.toString().equals("tab")) {

            receipts_btn = mView.findViewById(R.id.receipts_btn)
            sales_btn = mView.findViewById(R.id.sales_btn)
            settings_btn = mView.findViewById(R.id.settings_btn)


            val adapterCart_Rv = RvAdapterCart(activity, FragmentHome(), FragmentItems())
            recyclerview_list_cart.adapter = adapterCart_Rv
            recyclerview_list_cart.recycledViewPool.setMaxRecycledViews(0, 0)
            adapterCart_Rv.setItems(payment, 0, a_cartInItems!!, databaseforItems)
            adapterCart_Rv.setCallBack3(this)
            recyclerview_list_cart.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            //tab

            if (payment.text.equals("Pay"))
                payment.text = cu1.getString(cu1.getColumnIndex("PAY")).toString()
            if (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0).toInt() > 0) {

                if (databaseforItems != null) {

                    for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {

                        try {
                            val cu = databaseforItems.cartdatas(i)
                            cu.moveToFirst()

                            val cu_item = databaseforItems.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
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

                            totals = totals + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).replace(",", ".").toDouble())

                            cu.close()
                            cu_item.close()
                        } catch (e: Exception) {
                            Log.e("fragment items", e.toString())
                        }

                    }
                }
                /*val cu1 = databaseforItems.language
                cu1.moveToFirst()*/
                payment.text = cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(totals)

            }

            receipts_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Receipts::class.java)) })
            sales_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Home::class.java)) })
            settings_btn.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Settings::class.java)) })
            payment.setOnClickListener(View.OnClickListener { startActivity(Intent(activity, Payment::class.java)) })

        }

        a_back.setOnClickListener(View.OnClickListener { activity.onBackPressed() })
        a_cartInItems!!.setOnClickListener(View.OnClickListener { next() })

        cu1.close()
        return mView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

//        if (cartMenu == null)
        inflater!!.inflate(R.menu.items_toolbar, menu)

        cartMenu = menu!!.findItem(R.id.mnuCart)
        val view = cartMenu.actionView

        view.setOnClickListener(View.OnClickListener { next() })
        val cartLabel = view.findViewById<TextView>(R.id.cartLabel)


        c_count = 0
        if (!(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) == 0)) {
            for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                try {
                    val cu = databaseforItems.cartdatas(i)
                    cu.moveToFirst()
                    if (cu.getString(cu.getColumnIndex("QUANTITY")).equals("0") && cu != null) {
                        ++c_count
                    }
                    cu.close()
                } catch (e: Exception) {
                }

            }
        }
        cartLabel.text = (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0) - c_count).toString()
    }

    fun next() {
        if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).TAB, "0").equals("0"))
            startActivity(Intent(activity, Cart::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return super.onOptionsItemSelected(item)
        when (item!!.itemId) {
            R.id.search -> {
                Toast.makeText(activity, "Search", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun incrementcart(cm: MenuItem?, context: Context, checks: TextView?, recyclerview_list: RecyclerView?, payments: Button?, a_carts: TextView) {

        var formatter = DecimalFormat("#,###,##0.00")
        val view = cm!!.actionView
        val cartLabel = view.findViewById<TextView>(R.id.cartLabel)
        totals = 0.0
        if (checks!!.text.toString().equals("tab")) {
            //tab
            if (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0).toInt() > 0) {
                if (databaseforItems != null) {
                    for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                        val cu = databaseforItems.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databaseforItems.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
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

                        totals = totals + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())

                        cu_item.close()
                        cu.close()
                    }
                }
                val cu1 = databaseforItems.language
                cu1.moveToFirst()
                if (payments!!.text.equals("Pay"))
                    payments!!.text = cu1.getString(cu1.getColumnIndex("PAY")).toString()
                else
                    payments!!.text = cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(totals)
                val adapterCart_Rv = RvAdapterCart(context, FragmentHome(), FragmentItems())
                recyclerview_list!!.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack3(this)
                recyclerview_list!!.recycledViewPool.setMaxRecycledViews(0, 0)
                recyclerview_list!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                cu1.close()
            }

        }

        if (cartLabel != null) {
            c_count = 0
            if (!(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) == 0)) {
                for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                    val cu = databaseforItems.cartdatas(i)
                    cu.moveToFirst()
                    if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                        ++c_count
                    }
                    cu.close()
                }
            }
            cartLabel.text = (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString()
            a_cartInItems!!.text = (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString()
        }
    }

    fun incrementcart2(context: Context, checks: TextView?, recyclerview_lists: RecyclerView?, payments: Button?, a_carts: TextView, databaseforItem: SQLiteHelper) {
        databaseforItems = SQLiteHelper(context)
        totals = 0.0
        var formatter = DecimalFormat("#,###,##0.00")
        if (checks!!.text.toString().equals("tab")) {
            //tab
            if (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0).toInt() > 0) {
                databaseforItems = SQLiteHelper(context)
                if (databaseforItems != null) {
                    for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                        val cu = databaseforItems.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databaseforItems.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
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

                        totals = totals + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())

                        cu.close()
                        cu_item.close()

                    }
                }
                val cu1 = databaseforItems.language
                cu1.moveToFirst()
                if (payments!!.text.equals("Pay"))
                    payments!!.text = cu1.getString(cu1.getColumnIndex("PAY")).toString()
                else
                    payments!!.text = cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(totals)
                val adapterCart_Rv = RvAdapterCart(context, FragmentHome(), FragmentItems())
                recyclerview_lists!!.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack3(this)
                adapterCart_Rv.setItems(payments, 0, a_cartInItems!!, databaseforItem)
                recyclerview_lists!!.recycledViewPool.setMaxRecycledViews(0, 0)
                recyclerview_lists!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                cu1.close()
            }

        }
    }

    fun set(btnPay_itm: Button?, s: String) {
        try {
            btnPay_itm!!.text = s
        } catch (e: Exception) {
        }
    }

    fun itemdeleted(context: Context, a_carts: TextView, databaseforItem: SQLiteHelper) {
        totals = 0.0
/*val view = cartMenu.actionView
val cartLabel = view.findViewById<TextView>(R.id.cartLabel)*/
        c_count = 0
        if (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) != 0) {
            for (i in 0..(SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 1) - 1)) {
                val cu = databaseforItem.cartdatas(i)
                cu.moveToFirst()
                if (cu != null && cu.getString(cu.getColumnIndex("QUANTITY")).equals("0")) {
                    ++c_count
                }
                cu.close()
            }
        }
/*cartLabel.text = (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString()*/

/*Toast.makeText(context, (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString(), Toast.LENGTH_SHORT).show()*/
        a_cartInItems!!.text = (SharedPreferenceHelper(context).getInt(SharedPreferenceHelper(context).CART_COUNT, 0) - c_count).toString()


    }

    override fun onResume() {
        super.onResume()
        val cu1 = databaseforItems.language
        cu1.moveToFirst()

        totals = 0.0
        if (check.text.toString().equals("tab")) {
            //tab
            if (SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 0).toInt() > 0) {
                if (databaseforItems != null) {
                    for (i in 0..(SharedPreferenceHelper(activity).getInt(SharedPreferenceHelper(activity).CART_COUNT, 1) - 1)) {
                        val cu = databaseforItems.cartdatas(i)
                        cu.moveToFirst()

                        val cu_item = databaseforItems.getitem(cu.getString(cu.getColumnIndex("ITEM_ID")).toInt())
                        cu_item.moveToFirst()
                        var t_amount = 0.0
                        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        var i = 0
                        while (i < (mainstring.size - 1)) {
                            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            t_amount = t_amount + sub_strig[1].toDouble()
                            i++
                        }
                        totals = totals + ((t_amount + cu.getString(cu.getColumnIndex("PRICE")).toDouble()) * cu.getString(cu.getColumnIndex("QUANTITY")).toDouble())

                        cu.close()
                        cu_item.close()
                    }
                }
                /*val cu1 = databaseforItems.language
                cu1.moveToFirst()*/
                if (payment.text.equals("Pay"))
                    payment.text = cu1.getString(cu1.getColumnIndex("PAY")).toString()
                else
                    payment.text = cu1.getString(cu1.getColumnIndex("PAYMENT")).toString() + " " + formatter.format(totals)

                val adapterCart_Rv = RvAdapterCart(activity, FragmentHome(), FragmentItems())
                recyclerview_list_cart.adapter = adapterCart_Rv
                adapterCart_Rv.setCallBack3(this)
                recyclerview_list_cart.recycledViewPool.setMaxRecycledViews(0, 0)
                adapterCart_Rv.setItems(payment, 0, a_cartInItems!!, databaseforItems)
                recyclerview_list_cart.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }

        }
        cu1.close()
    }


    override fun StartProgressCart(progress: ProgressDialog) {
        progres = progress
    }

    override fun itemprogres(progress: ProgressDialog) {
        progres = progress
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            progres.dismiss()
        } catch (e: Exception) {
        }

    }

    override fun onStop() {
        super.onStop()
        try {
            progres.dismiss()
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            progres.dismiss()
        } catch (e: Exception) {
        }
    }


    override fun PassItemToCart(adapterPosition: Int, flag: String, context: Context, catId: String, checks: TextView?, recyclerview_lists: RecyclerView?, payments: Button?, a_carts: TextView, databaseforItem: SQLiteHelper) {
        var databaseforItemss = SQLiteHelper(context)
        popup = Dialog(context)
        popup.setContentView(R.layout.popup_kg)
        one = popup.findViewById(R.id.one)
        two = popup.findViewById(R.id.two)
        three = popup.findViewById(R.id.three)
        four = popup.findViewById(R.id.four)
        five = popup.findViewById(R.id.five)
        six = popup.findViewById(R.id.six)
        seven = popup.findViewById(R.id.seven)
        eight = popup.findViewById(R.id.eight)
        nine = popup.findViewById(R.id.nine)
        zero = popup.findViewById(R.id.zero)
        dot = popup.findViewById(R.id.dot)
        ok = popup.findViewById(R.id.ok)
        total = popup.findViewById(R.id.total)
        delete = popup.findViewById(R.id.delete)
        quen = popup.findViewById(R.id.quen)
        popup.setCancelable(true)
        qty = 1

        var dot_comma_int = 1.1
        var dot_comma = ""
        var formatter = DecimalFormat("#,###,##0.00")
        var cu1: Cursor
        if (flag.equals("2"))
            cu1 = databaseforItemss.getItems(adapterPosition)
        else
            cu1 = databaseforItemss.getItems(adapterPosition, catId)

        try {
            cu1.moveToFirst()
//                Toast.makeText(context,(cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),Toast.LENGTH_SHORT).show()

            if (flag.equals("0") || flag.equals("2")) {
                if (cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString().equals("1")) {

                    try {
                        if (formatter.format(dot_comma_int).toDouble() == 1.1) {
                            dot_comma = "."
                        }
                    } catch (e: Exception) {
                        dot_comma = ","
                    }
                    dot.text = dot_comma

                    val cu = databaseforItemss.language
                    cu.moveToFirst()
                    quen.text = cu.getString(cu.getColumnIndex("QUANTITY")).toString()
                    ok.text = cu.getString(cu.getColumnIndex("OK")).toString()
                    popup.show()
                    one.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "1"
                    })
                    two.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "2"
                    })
                    three.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "3"
                    })
                    four.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "4"
                    })
                    five.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "5"
                    })
                    six.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "6"
                    })
                    seven.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "7"
                    })
                    eight.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "8"
                    })
                    nine.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "9"
                    })
                    zero.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + "0"
                    })
                    dot.setOnClickListener(View.OnClickListener {
                        total.text = total.text.toString() + dot_comma
                    })
                    ok.setOnClickListener(View.OnClickListener {
                        try {
                            if (total.text.equals("") || total.text.toString().replace(",", ".").toDouble() <= 0.001) {
                                popup.dismiss()
                            } else if (databaseforItemss.insetCartData(cu1.getString(cu1.getColumnIndex("ID")).toString().toInt(),
                                            cu1.getString(cu1.getColumnIndex("NAME")).toString(),
                                            total.text.toString().replace(",", ".").toDouble(),
                                            cu1.getString(cu1.getColumnIndex("PRICE")).toString(),
                                            cu1.getString(cu1.getColumnIndex("SHAPE")).toString(),
                                            cu1.getString(cu1.getColumnIndex("COLOR")).toString(),
                                            cu1.getString(cu1.getColumnIndex("IMAGE")).toString(),
                                            cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString(), "",
                                            (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                            (cu1.getString(cu1.getColumnIndex("TAX")).toDouble() + 1).toString())) {
//                                Toast.makeText(context, cu1.getString(cu1.getColumnIndex("NAME")).toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                                popup.dismiss()
                                incrementcart2(context, checks, recyclerview_lists, payments, a_cartInItems!!, databaseforItemss)
                                itemdeleted(context, a_cartInItems!!, databaseforItemss)


                                val cu_modifier = databaseforItemss.getModifier(0, cu1.getString(cu1.getColumnIndex("ID")).toString())
                                cu_modifier.moveToFirst()

//                                Toast.makeText(context, databaseforItemss.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString(),Toast.LENGTH_SHORT).show()

                                if (cu_modifier.count != 0) {
                                    val next = Intent(context, Modifier::class.java)
                                    next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ID")).toString())
                                    next.putExtra("COUNT", databaseforItemss.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString())
                                    context.startActivity(next)
                                }

                                cu_modifier.close()
                                cu1.close()
                            }
                        } catch (e: Exception) {
                            total.text = ""
                            Log.e("error textout", e.toString())
                        }

                    })
                    delete.setOnClickListener(View.OnClickListener {
                        if (total.text.toString().length != 0)
                            total.text = total.text.toString().substring(0, total.text.toString().length - 1)
                        cu1.close()
                    })
                    cu.close()
                } else {
                    if (databaseforItemss != null) {
                        val cu = databaseforItemss.language
                        cu.moveToFirst()
                        if (databaseforItemss.insetCartData(cu1.getString(cu1.getColumnIndex("ID")).toInt(),
                                        cu1.getString(cu1.getColumnIndex("NAME")).toString(),
                                        qty.toDouble(),
                                        cu1.getString(cu1.getColumnIndex("PRICE")).toString(),
                                        cu1.getString(cu1.getColumnIndex("SHAPE")).toString(),
                                        cu1.getString(cu1.getColumnIndex("COLOR")).toString(),
                                        cu1.getString(cu1.getColumnIndex("IMAGE")).toString(),
                                        cu1.getString(cu1.getColumnIndex("SELLINGTYPE")).toString(), "",
                                        (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                        cu1.getString(cu1.getColumnIndex("TAX")).toString())) {
//                                Toast.makeText(context, cu1.getString(cu1.getColumnIndex("NAME")).toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                            incrementcart2(context, checks, recyclerview_lists, payments, a_cartInItems!!, databaseforItemss)
                            itemdeleted(context, a_cartInItems!!, databaseforItemss)

                            try {
                                val cu_modifier = databaseforItemss.getModifier(0, cu1.getString(cu1.getColumnIndex("ID")).toString())
                                cu_modifier.moveToFirst()

//                                Toast.makeText(context, databaseforItemss.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString(),Toast.LENGTH_SHORT).show()

                                if (cu_modifier.count != 0) {
                                    val next = Intent(context, Modifier::class.java)
                                    next.putExtra("ITEMID", cu1.getString(cu1.getColumnIndex("ID")).toString())
                                    next.putExtra("COUNT", databaseforItemss.getModifierSize(cu1.getString(cu1.getColumnIndex("ID")).toInt()).count.toString())
                                    context.startActivity(next)
                                }
                                cu_modifier.close()
                            } catch (e: Exception) {
                            }


                        }
                        cu.close()
                        cu1.close()
                    }
                }
            } /*else {
        if (items[adapterPosition].sellingType.toString().equals("1")) {
            popup.setContentView(R.layout.popup_kg)
            one = popup.findViewById(R.id.one)
            two = popup.findViewById(R.id.two)
            three = popup.findViewById(R.id.three)
            four = popup.findViewById(R.id.four)
            five = popup.findViewById(R.id.five)
            six = popup.findViewById(R.id.six)
            seven = popup.findViewById(R.id.seven)
            eight = popup.findViewById(R.id.eight)
            nine = popup.findViewById(R.id.nine)
            zero = popup.findViewById(R.id.zero)
            dot = popup.findViewById(R.id.dot)
            try {
                if (formatter.format(dot_comma_int).toDouble() == 1.1) {
                    dot_comma = "."
                }
            } catch (e: Exception) {
                dot_comma = ","
            }
            dot.setText(dot_comma)
            ok = popup.findViewById(R.id.ok)
            total = popup.findViewById(R.id.total)
            delete = popup.findViewById(R.id.delete)
            quen = popup.findViewById(R.id.quen)
            popup.setCancelable(true)
            val cu = databaseforItemss.getLanguage()
            cu.moveToFirst()
            quen.setText(cu.getString(cu.getColumnIndex("QUANTITY")).toString())
            ok.setText(cu.getString(cu.getColumnIndex("OK")).toString())
            popup.show()
            one.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "1")
            })
            two.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "2")
            })
            three.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "3")
            })
            four.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "4")
            })
            five.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "5")
            })
            six.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "6")
            })
            seven.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "7")
            })
            eight.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "8")
            })
            nine.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "9")
            })
            zero.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + "0")
            })
            dot.setOnClickListener(View.OnClickListener {
                total.setText(total.text.toString() + dot_comma)
            })
            ok.setOnClickListener(View.OnClickListener {
                try {
                    if (total.text.equals("")) {
                        popup.dismiss()
                    } else if (databaseforItemss.insetCartData(items[adapterPosition].id.toInt(),
                                    items[adapterPosition].name.toString(),
                                    total.text.toString().replace(",", ".").toDouble(),
                                    items[adapterPosition].price.toString(),
                                    items[adapterPosition].shape.toString(),
                                    items[adapterPosition].color.toString(),
                                    items[adapterPosition].image.toString(),
                                    items[adapterPosition].sellingType.toString(), "",
                                    (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                    items[adapterPosition].tax.toString())) {
//                                Toast.makeText(context, items[adapterPosition].name.toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                        popup.dismiss()
                        fragments.incrementcart(cm, context, checks, recyclerview_lists, payments)
                    }

                } catch (e: Exception) {
                    total.setText("")
                }
            })
            delete.setOnClickListener(View.OnClickListener {
                if (total.text.toString().length != 0)
                    total.setText(total.text.toString().substring(0, total.text.toString().length - 1))
            })
        } else {
            if (databaseforItemss != null) {
                val cu = databaseforItemss.getLanguage()
                cu.moveToFirst()
                if (databaseforItemss.insetCartData(items[adapterPosition].id.toInt(),
                                items[adapterPosition].name.toString(),
                                qty.toDouble(),
                                items[adapterPosition].price.toString(),
                                items[adapterPosition].shape.toString(),
                                items[adapterPosition].color.toString(),
                                items[adapterPosition].image.toString(),
                                items[adapterPosition].sellingType.toString(), "",
                                (cu1.getString(cu1.getColumnIndex("COUNT")).toInt() + 1).toString(),
                                items[adapterPosition].tax.toString())) {
//                                Toast.makeText(context, items[adapterPosition].name.toString() + " " + cu.getString(cu.getColumnIndex("ADDED")).toString(), Toast.LENGTH_SHORT).show()
                    fragments.incrementcart(cm, context, checks, recyclerview_lists, payments)
                }
            }
        }
    }*/

        } catch (e: Exception) {
            Log.e("error_is_this 1->", e.toString())
        }
    }

}