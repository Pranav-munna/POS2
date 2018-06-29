package com.twixt.pranav.pos.View.Fragment

import android.Manifest
import android.app.Dialog
import android.app.Fragment
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.iid.FirebaseInstanceId
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestLogin
import com.twixt.pranav.pos.Controller.Responce.ResponceActivateStatus
import com.twixt.pranav.pos.Controller.Responce.ResponceLogin
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.SplashScreen

/**
 * Created by Pranav on 11/24/2017.
 */
class FragmentLogin : Fragment() {
    //    var wifi = false
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var forgot_password: TextView
    var cnt_category = 0
    var cnt_products = 0
    var i = 0
    lateinit var message: TextView
    lateinit var ok: Button

    lateinit var login: Button
    private var database: SQLiteHelper? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mView = inflater!!.inflate(R.layout.fragment_login, container, false)

        try {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        3)

            }
        }catch (e:Exception){

        }
        val popup = Dialog(activity)
        popup.setContentView(R.layout.fragement_message_dialog)
        message = popup.findViewById(R.id.message)
        ok = popup.findViewById(R.id.ok)
        popup.setCancelable(false)
        popup.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)))


        email = mView.findViewById(R.id.email)
        password = mView.findViewById(R.id.password)
        forgot_password = mView.findViewById(R.id.forgot_password)
        login = mView.findViewById(R.id.login)
        database = SQLiteHelper(activity)

        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        val cu1 = database!!.language
        cu1.moveToFirst()

        email.setHint(cu1.getString(cu1.getColumnIndex("EMAIL")).toString())
        password.setHint(cu1.getString(cu1.getColumnIndex("PASSCODE")).toString())
        login.setText(cu1.getString(cu1.getColumnIndex("LOGIN")).toString())
        forgot_password.setText(cu1.getString(cu1.getColumnIndex("FORGOT_PASSWORD")).toString())

//        Settings.Secure.ANDROID_ID
//        RequestcheckingNetwork(activity, ResponceprocesWifi()).getStatus()

        if (!isConnected) {

            message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
            popup.show()
            ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//            Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
        }

        /*forgot_password.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("FORGOT_PASSWORD")).toString(), Toast.LENGTH_SHORT).show()
        })*/

        login.setOnClickListener(View.OnClickListener {
            //            RequestcheckingNetwork(activity, ResponceprocesWifi()).getStatus()
            val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
//            Toast.makeText(activity, wifi.toString(), Toast.LENGTH_SHORT).show()
            if (!isConnected) {
                message.setText(cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("CHECK_NETWORK")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                //            Toast.makeText(activity, FirebaseInstanceId.getInstance().getToken().toString(), Toast.LENGTH_SHORT).show()
                Log.d(TAG, "From:  " + FirebaseInstanceId.getInstance().getToken().toString() + " ")

                if (email.text.trim().length <= 0 || password.text.trim().length <= 0) {

                    message.setText(cu1.getString(cu1.getColumnIndex("ENTER_ALL_FIELDS")).toString())
                    popup.show()
                    ok.setOnClickListener(View.OnClickListener { popup.dismiss() })

//                FragmentMessageDialog().show(activity.fragmentManager, "test")
//                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("ENTER_ALL_FIELDS")).toString(), Toast.LENGTH_SHORT).show()
                } else {
                    if (!(isValidEmail(email.text.trim().toString()))) {
                        message.setText(cu1.getString(cu1.getColumnIndex("INVALID_EMAIL")).toString())
                        popup.show()
                        ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                    Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("INVALID_EMAIL")).toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        RequestLogin(activity, ResponceprocessorLogin()).getLogin(email.text.trim().toString(), password.text.trim().toString(), FirebaseInstanceId.getInstance().getToken().toString())
                    }
                }

//                wifi = false
            }

        })

        forgot_password.setOnClickListener(View.OnClickListener {

            //            startActivity(Intent(activity, Pin::class.java))
        })
//cu1.close()
        return mView
    }


    inner class ResponceprocesWifi : ProcessResponcceInterphase<ResponceActivateStatus> {
        override fun processResponce(responce: ResponceActivateStatus) {
            if (responce != null) {
//                wifi = true
            }
        }

    }

    inner class ResponceprocessorLogin : ProcessResponcceInterphase<ResponceLogin> {
        override fun processResponce(responce: ResponceLogin) {
            val cu1 = database!!.language
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
            } else if (responce.status.toString().equals("true")) {

                var all_users = responce.users!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                var usercount = 0
                while (all_users.size > usercount) {
                    var one_users = all_users[usercount].split("::".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    try {
                        database!!.insertUsers(try {
                            one_users[0]
                        } catch (e: Exception) {
                            " "
                        }, try {
                            one_users[1]
                        } catch (e: Exception) {
                            " "
                        }, try {
                            one_users[2]
                        } catch (e: Exception) {
                            " "
                        }, try {
                            one_users[3]
                        } catch (e: Exception) {
                            " "
                        }, try {
                            one_users[4]
                        } catch (e: Exception) {
                            " "
                        }, try {
                            one_users[5]
                        } catch (e: Exception) {
                            " "
                        }, try {
                            one_users[6]
                        } catch (e: Exception) {
                            " "
                        })

                    } catch (e: Exception) {
                    }


//                    1=id       2=name    3=role       4=image  5=pin   6=address 7=phone

                    usercount++
                }


                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).LOGIN_OK, "ok")
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_ID, responce.id.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_NAME, responce.name.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_MAIL, responce.email.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_SHOP_ID, responce.shopId.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_ROLE, /*responce.role.toString()*/"0")
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).POS_USER_IMAGE, responce.profileImage.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).OFFLINE, responce.offlineAllowed.toString())

                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).ADDRESS, responce.address.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PHONE, responce.phone.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).CVR, responce.cvr.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).BUSINESS_NAME, responce.businessName.toString())

                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).HEADER, responce.header.toString())
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).FOOTER, responce.footer.toString())


//                if (responce.role.toString().equals("0"))
                SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PIN, responce.pincode.toString())
//                else
//                    SharedPreferenceHelper(activity).putString(SharedPreferenceHelper(activity).PIN, "")

//                RequestCategories(activity, ResponceProcessorAdapterCategoriesLoadingg()).getCategories(cnt_category++,
//                        SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "1"))
                startActivity(Intent(activity, SplashScreen::class.java))
                activity.finish()
            } else if (responce.email.toString().equals("")) {
                message.setText(cu1.getString(cu1.getColumnIndex("INVALID_EMAIL")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("INVALID_EMAIL")).toString(), Toast.LENGTH_SHORT).show()
            } else {
                message.setText(cu1.getString(cu1.getColumnIndex("PASSWORD_ERROR")).toString())
                popup.show()
                ok.setOnClickListener(View.OnClickListener { popup.dismiss() })
//                Toast.makeText(activity, cu1.getString(cu1.getColumnIndex("PASSWORD_ERROR")).toString(), Toast.LENGTH_SHORT).show()
            }
            cu1.close()
        }

        /*inner class ResponceProcessorAdapterCategoriesLoadingg : ProcessResponcceInterphase<Array<ResponceCategories>> {
            override fun processResponce(responce: Array<ResponceCategories>) {

                if (responce == null || responce.size == 0) {

//                    RequestAllProducts(activity, ResponceprocerssorAllProductsss()).getProducts(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"), cnt_products++)
                } else {
                    val database: SQLiteHelper = SQLiteHelper(activity)

                    val list = ArrayList(Arrays.asList(*responce))
                    i = 0

                    while (i < list.size) {
                        database!!.insertCategories(i.toString(), list[i].id.toString(), list[i].name.toString(), list[i].color.toString())
                        i++
                    }
                    if (i == list.size) {
//                    Toast.makeText(activity, "01k", Toast.LENGTH_SHORT).show()
                        RequestCategories(activity, ResponceProcessorAdapterCategoriesLoadingg()).getCategories(cnt_category++,
                                SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "1"))
                    }

//                database!!.insertCategories(position.toString(), items[position].id.toString(), items[position].name.toString(), items[position].color.toString())


                }
            }

        }

        inner class ResponceprocerssorAllProductsss : ProcessResponcceInterphase<Array<ResponceProducts>> {
            override fun processResponce(responce: Array<ResponceProducts>) {
                if (responce == null && responce.size == 0) {
//                    startActivity(Intent(activity, Home::class.java))
                } else {
                    val database: SQLiteHelper = SQLiteHelper(activity)
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
                                list[i].sellingType.toString())
                        i++
                    }
                    if (i == list.size) {
                        RequestAllProducts(activity, ResponceprocerssorAllProductsss()).getProducts(SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_SHOP_ID, "0"), cnt_products++)
                    }

                }

            }

        }*/

    }

    fun isValidEmail(e_mail: String): Boolean {
        return e_mail.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}".toRegex()) && e_mail.length <= 320
    }
}