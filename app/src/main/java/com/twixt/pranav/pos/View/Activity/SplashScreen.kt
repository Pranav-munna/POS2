package com.twixt.pranav.pos.View.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.*
import com.twixt.pranav.pos.Controller.Responce.*
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import java.util.*
import kotlin.collections.ArrayList


class SplashScreen : AppCompatActivity()  {

    val handler = Handler()
    var cnt_category = 0
    var cnt_products = 0
    var cnt_modifiers = 1
    var i = 0
    private var database: SQLiteHelper? = null

    lateinit var message: TextView
    lateinit var ok: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        database = SQLiteHelper(this)
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        val popup = Dialog(this)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)))


        database!!.deleteModifiers()

        val cu1 = database!!.language
        cu1.moveToFirst()
        RequestHeaderFooter(this, ResponceProcessorHeadderFooter()).getHeaderFooter(SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).POS_SHOP_ID, "0"))
        RequestBackEndChange(this, ResponceProcessorBackend()).getBackEnd(SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).POS_USER_ID, "0"))
        if (isConnected) {
//            var count = 0
            if (SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).LOGIN_OK, "not_ok").equals("ok")) {
//                RequestLanguageCheck(this, ResponceProcessorLAnguageCheck()).checkLanguage(SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).POS_USER_ID, "0"))
                RequestPaymentTypes(this, ResponceprocessorPaymentTyp()).getPaymentTypes(SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).POS_SHOP_ID, "0"))

                if (!SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).POS_SHOP_ID, "00").equals("00")) {
//                    database!!.deleteCato()
                    RequestCategories(this, ResponceProcessorAdapterCategoriesLoading()).getCategories(cnt_category++,
                            SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).POS_SHOP_ID, "1"))
                }
            }

        } /*else {*/
        handler.postDelayed({
            if (!(this).isFinishing) {
                message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener {
                    popup.dismiss()
                    val intent = this@SplashScreen.packageManager.getLaunchIntentForPackage(this@SplashScreen.getPackageName())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                })
            }


        }, 12000)
//        }

    }

    inner class ResponceProcessorHeadderFooter : ProcessResponcceInterphase<ResponceHeaderFooter> {
        override fun processResponce(responce: ResponceHeaderFooter) {
            if (responce != null) {

                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).HEADER, responce.header.toString())
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).FOOTER, responce.footer.toString())

            }

        }

    }

    inner class ResponceProcessorBackend : ProcessResponcceInterphase<ResponceBackEndChange> {
        override fun processResponce(responce: ResponceBackEndChange) {
//            Toast.makeText(this@SplashScreen, responce.backend_change, Toast.LENGTH_SHORT).show()
            if (responce.backend_change.equals("1")) {
                //logout
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).LOGIN_OK, "not_ok")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).POS_USER_ID, "")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).POS_USER_NAME, "")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).POS_USER_MAIL, "")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).POS_SHOP_ID, "")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).POS_ROLE, "")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).POS_USER_IMAGE, "")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).LANGUAGE, "")
                database!!.deleteCart()
                database!!.deleteCato()
                database!!.deleteItems()

                SharedPreferenceHelper(this@SplashScreen).putInt(SharedPreferenceHelper(this@SplashScreen).CART_COUNT, 0)
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).PAYMENT_TYPE, "0")
                SharedPreferenceHelper(this@SplashScreen).putString(SharedPreferenceHelper(this@SplashScreen).TOTAL_AMOUNT, "0")

                val popup = Dialog(this@SplashScreen)

                message.setText("Backend change(update)...!!")
                popup.show()
                ok.setOnClickListener(View.OnClickListener {
                    popup.dismiss()

                    startActivity(Intent(this@SplashScreen, Login::class.java))
                    finish()

                })

            } else {
                handler.postDelayed({
                    //            if (SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).ACTIVATION_OK, "not_ok").equals("ok"))
                    if (SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).LOGIN_OK, "not_ok").equals("ok"))
                        if (SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).PIN, "").length == 4 && database!!.userSize.count != 1)
                            startActivity(Intent(this@SplashScreen, Pin::class.java))
                        else
                            startActivity(Intent(this@SplashScreen, Home::class.java))
                    else
                        startActivity(Intent(this@SplashScreen, Login::class.java))
//            else
//                startActivity(Intent(this, Activation::class.java))

                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    finish()
                }, 3000)
            }

        }

    }


    inner class ResponceProcessorAdapterCategoriesLoading : ProcessResponcceInterphase<Array<ResponceCategories>> {
        override fun processResponce(responce: Array<ResponceCategories>) {
            if (responce == null || responce.size == 0) {
                RequestModifiers(this@SplashScreen, ResponceProcessorModifiers()).getModifiers(cnt_modifiers++,
                        SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).POS_SHOP_ID, "0"))
                RequestAllProducts(this@SplashScreen, ResponceprocerssorAllProducts()).getProducts(SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).POS_SHOP_ID, "0"), cnt_products++)
            } else {
                val database: SQLiteHelper = SQLiteHelper(this@SplashScreen)

                val list = ArrayList(Arrays.asList(*responce))
                i = 0
                while (i < list.size) {
                    database!!.insertCategories(i.toString(), list[i].id.toString(), list[i].name.toString(), list[i].color.toString())
                    i++
                }
                if (i == list.size && i != 0) {
//                    Toast.makeText(this@SplashScreen, "01k", Toast.LENGTH_SHORT).show()
                    RequestCategories(this@SplashScreen, ResponceProcessorAdapterCategoriesLoading()).getCategories(cnt_category++,
                            SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).POS_SHOP_ID, "0"))
                }

//                database!!.insertCategories(position.toString(), items[position].id.toString(), items[position].name.toString(), items[position].color.toString())


            }

        }

    }

    inner class ResponceprocerssorAllProducts : ProcessResponcceInterphase<Array<ResponceProducts>> {
        override fun processResponce(responce: Array<ResponceProducts>) {
            if (responce == null && responce.size == 0) {


                /* handler.postDelayed({
                     //            if (SharedPreferenceHelper(this).getString(SharedPreferenceHelper(this).ACTIVATION_OK, "not_ok").equals("ok"))
                     if (SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).LOGIN_OK, "not_ok").equals("ok"))
                         if (SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).PIN, "").length == 4)
                             startActivity(Intent(this@SplashScreen, Pin::class.java))
                         else
                             startActivity(Intent(this@SplashScreen, Home::class.java))
                     else
                         startActivity(Intent(this@SplashScreen, Login::class.java))
 //            else
 //                startActivity(Intent(this, Activation::class.java))

                     overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                     finish()
                 }, 2000)*/
            } else {
                val database: SQLiteHelper = SQLiteHelper(this@SplashScreen)
                val list = ArrayList(Arrays.asList(*responce))
                i = 0
                while (i < list.size) {
                    database!!.insertItems(i.toString(),
                            list[i].id.toString(),
                            list[i].name.toString(),
                            list[i].price.toString(),
                            list[i].shape.toString(),
                            list[i].color.toString(),
                            list[i].image.toString(),
                            list[i].categoryId.toString(),
                            list[i].sellingType.toString(),
                            list[i].tax.toString())
                    i++
                }
                if (i == list.size && i != 0) {
                    RequestAllProducts(this@SplashScreen, ResponceprocerssorAllProducts()).getProducts(SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).POS_SHOP_ID, "0"), cnt_products++)
                }

            }
        }


    }

    inner class ResponceProcessorModifiers : ProcessResponcceInterphase<Array<ResponceModifier>> {
        override fun processResponce(responce: Array<ResponceModifier>) {
            if (responce != null && responce.size != 0) {
                val database: SQLiteHelper = SQLiteHelper(this@SplashScreen)
                val list = ArrayList(Arrays.asList(*responce))
//                Toast.makeText(this@SplashScreen, responce[0].modifier_name, Toast.LENGTH_SHORT).show()
                i = 0
                while (i < list.size) {
                    database!!.insertModifiers(
                            list[i].itemId.toString(),
                            list[i].modifier_id.toString(),
                            list[i].modifier_name.toString(),
                            list[i].option_id.toString(),
                            list[i].option_name.toString(),
                            list[i].modifier_price.toString())
                    i++
                }
                if (i == list.size && i != 0) {
                    RequestAllProducts(this@SplashScreen, ResponceprocerssorAllProducts()).getProducts(SharedPreferenceHelper(this@SplashScreen).getString(SharedPreferenceHelper(this@SplashScreen).POS_SHOP_ID, "0"), cnt_products++)
                }


            }
        }

    }

    inner class ResponceprocessorPaymentTyp : ProcessResponcceInterphase<Array<ResponcePaymentTypes>> {
        override fun processResponce(responce: Array<ResponcePaymentTypes>) {
            val cu = database!!.getLanguage()
            cu.moveToFirst()
            if (responce == null) {
                Toast.makeText(this@SplashScreen, cu.getString(cu.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                val database: SQLiteHelper? = SQLiteHelper(this@SplashScreen)
                val list = ArrayList(Arrays.asList(*responce))
                var i = 0

                if (database != null && list.size > 0) {
                    if (database.deletePaymentType())
                        SharedPreferenceHelper(this@SplashScreen).putInt(SharedPreferenceHelper(this@SplashScreen).PAY_COUNT, 0)

                    while (i < list.size) {
                        database.insertPaymentTypes(responce[i].id.toString(), responce[i].name.toString(), responce[i].type.toString())
                        i++
                    }
                }

            }
        }

    }
}
