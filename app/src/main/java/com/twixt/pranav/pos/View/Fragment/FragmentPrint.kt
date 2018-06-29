package com.twixt.pranav.pos.View.Fragment

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Pranav on 2/8/2018.
 */
class FragmentPrint : Fragment() {
    private var mWebView: WebView? = null
    var i = 0


    var b_name = "tst_Acme Corp."
    var add_2 = "tst_John Doe"
    var mail = "tst_john@example.com"

    var cvr = "tst_john@example.com"
    var phone = "tst_john@example.com"

    var date = "tst_Date"
    var cashier = "tst_Owner"
    var receiptNo = "XXX"
    var Item = "Testitem1 ## 1х 00.00 ## 00.00 ## modifiers !& Testitem2 ## 2 х 00.00 ## 00.00 ## modifiers"
    var Item_refund = "Testitem1 ## 1х 00.00 ## 00.00 ## modifiers "
    var discount = "tst_10%"
    var discount_amount = "9.99"
    var total = "20.01"


    var str = ""
    var modifiers = ""
    var str_refund_items = ""
    var str_refund = " "
    var tax = "10"
    var refunded_if = ""
    var refunded_amount = "12"

    var formatter = DecimalFormat("#,###,##0.00")


    private lateinit var databaseforpin: SQLiteHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_print, container, false)
        cashier = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_NAME, "Owner")
        var c = Calendar.getInstance().time
        var df = SimpleDateFormat("dd-MMM-yyyy")

        var header = getString(R.string.app_name)
        var footer = getString(R.string.app_name)



        date = df.format(c).toString()
        b_name = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).BUSINESS_NAME, "").equals("null")) {
            ""
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).BUSINESS_NAME, "")
        }
        header = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).HEADER, "").equals("")) {
            getString(R.string.app_name)
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).HEADER, getString(R.string.app_name))
        }
        footer = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).FOOTER, "").equals("")) {
            getString(R.string.app_name)
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).FOOTER, getString(R.string.app_name))
        }
        add_2 = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).ADDRESS, "").equals("null")) {
            ""
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).ADDRESS, "")
        }
        mail = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_MAIL, "").equals("null")) {
            ""
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_MAIL, "")
        }
        cvr = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CVR, "").equals("null")) {
            ""
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CVR, "")
        }
        phone = if (SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PHONE, "").equals("null")) {
            ""
        } else {
            SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PHONE, "")
        }

        /* add_2 = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).ADDRESS, "")
         mail = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).POS_USER_MAIL, "")
         cvr = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).CVR, "")
         phone = SharedPreferenceHelper(activity).getString(SharedPreferenceHelper(activity).PHONE, "")*/

        databaseforpin = SQLiteHelper(context)

        val cu_language = databaseforpin.language
        cu_language.moveToFirst()

        str_refund = "<tr class=\"heading\"><td>" +
                cu_language.getString(cu_language.getColumnIndex("REFUNDED")) + "</td><td>" +
                cu_language.getString(cu_language.getColumnIndex("PRICE")) + "</td></tr>"

//        Log.e("find oriantation", activity.getResources().getConfiguration().orientation.toString() + "    " + ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


//        if(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT==)
        try {
            Item = arguments.getString("PRINT_ITEMS")
            Item_refund = arguments.getString("PRINT_ITEMS_REFUND")
            date = arguments.getString("PRINT_DATE")
            discount = arguments.getString("PRINT_DISCOUNT")
            discount_amount = if (arguments.getString("PRINT_DISCOUNT_AMOUNT").equals("")) "0.00" else arguments.getString("PRINT_DISCOUNT_AMOUNT")
            total = arguments.getString("PRINT_TOTAL")
            receiptNo = arguments.getString("PRINT_RECEIPT_NO")
            tax = arguments.getString("TAX")
            refunded_amount = arguments.getString("REFUNDED_DISCOUNT")

//            Toast.makeText(activity, Item, Toast.LENGTH_SHORT).show()

            val cu = databaseforpin.discountTypes(discount)
            cu.moveToFirst()



            if (discount.equals("1"))
                discount = cu.getString(cu.getColumnIndex("DISCOUNT")) + " %"
            else
                discount = cu.getString(cu.getColumnIndex("DISCOUNT"))

            cu.close()
        } catch (e: Exception) {
        }

        try {
            if (refunded_amount != "") {
                refunded_if = "<tr class=\"item\"><td>" +
                        cu_language.getString(cu_language.getColumnIndex("REFUNDEDDISCOUNT")) + "</td><td>" +
                        formatter.format(arguments.getString("REFUNDED_DISCOUNT").toDouble()) + "</td></tr>"
            } else refunded_if = ""
        } catch (e: Exception) {
            Log.e("search_here", e.toString())
        }

        var one_item = Item.split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var one_item_refund = Item_refund.split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val webView = WebView(activity)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                try {
                    createWebPrintJob(view)
                } catch (e: Exception) {
                }
                mWebView = null
            }
        }

        var i = 0
        str = ""
        while (i < one_item.size) {
            var inner_items = one_item[i].split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            str = str + "<tr class=\"item\"><td>" +
                    try {
                        inner_items!![0]
                    } catch (e: Exception) {
                        i++
                        continue
                        Log.e("print rr help", " 88 " + e.toString())
                    } + "<div style=\"text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                    try {
                        inner_items!![1]
                    } catch (e: Exception) {
                    } + "</div><div style=\"font-size:12px;text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                    if (inner_items.size == 3) {
                        " "
                    } else {
                        try {
                            inner_items!![3]
                        } catch (e: Exception) {
                        }
                    } + " </div></td><td>" +

                    try {
                        formatter.format(inner_items!![2].replace(",", ".").toDouble()) + ""
                    } catch (e: Exception) {
                    }

            "</td></tr>"
            i++
        }


        if (!(Item_refund.length <= 0)) {
            var j = 0
            str_refund_items = ""
            while (j < one_item_refund.size) {
                var inner_items = one_item_refund[j].split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                str_refund_items = str_refund_items + "<tr class=\"item\"><td>" +
                        inner_items[0] + "<div style=\"text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                        inner_items[1] + "</div><div style=\"font-size:12px;text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                        if (inner_items.size == 3) {
                            " "
                        } else {
                            inner_items[3]
                        } + " </div></td><td>" +
                        formatter.format(inner_items[2].replace(",", ".").toDouble()) + "</td></tr>"
                j++
            }
            str_refund = str_refund + " " + str_refund_items

        } else {
            str_refund = ""
        }


        val htmlDocument = "<!doctype html> <html><head><meta charset=\"utf-8\"><title>Invoice</title> " +
                "<style>.font-small{font-size: 10px;font-weight: 200;color: #a9a3a3;}" +
                ".invoice-box { max-width: 450px;margin: auto;padding: 30px;border: 1px solid #eee;box-shadow: 0 0 10px rgba(0, 0, 0, .15);font-size: 16px;line-height: 24px; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;color: #555;} .invoice-box table { width: 100%;line-height: inherit;text-align: left; }.invoice-box table td {padding: 5px;vertical-align: top;} .invoice-box table tr td:nth-child(2) {text-align: right; }.invoice-box table tr.top table td {padding-bottom: 20px; } .invoice-box table tr.top table td.title font-size: 45px;line-height: 45px;color: #333;}.invoice-box table tr.information table td {padding-bottom: 40px;}.invoice-box table tr.heading td {background: #eee;border-bottom: 1px solid #ddd;font-weight: bold;}.invoice-box table tr.details td { padding-bottom: 20px;}.invoice-box table tr.item td{border-bottom: 1px solid #eee;} .invoice-box table tr.item.last td {border-bottom: none;}.invoice-box table tr.total td:nth-child(2) {border-top: 2px solid #eee;font-weight: bold;} @media only screen and (max-width: 600px) { .invoice-box table tr.top table td {width: 100%;display: block;text-align: center;}.invoice-box table tr.information table td {width: 100%;display: block;text-align: center;}}* RTL *.rtl {direction: rtl;font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;}.rtl table {text-align: right;}.rtl table tr td:nth-child(2) {text-align: left;}.bottom-line{border-bottom: 1px dashed #cacaca;padding-bottom: 20px;}.aligncenter {text-align: center;}.alignright {text-align: right;}</style></head><body><div class=\"invoice-box\"><table cellpadding=\"0\" cellspacing=\"0\"><tr class=\"top\"><td colspan=\"2\"><table><tr class=\"information\">" +
                "<td class=\"aligncenter bottom-line\">" +
                header +
                " </td></tr><td class=\"bottom-line\">" +
                cu_language.getString(cu_language.getColumnIndex("INVOICE")) + " #:" + receiptNo + " <br></td></tr></table></td></tr><tr class=\"information\"><td colspan=\"2\"><table><tr><td>" +
                cu_language.getString(cu_language.getColumnIndex("CASHIER")) + ": " + cashier + "<br>" +
                cu_language.getString(cu_language.getColumnIndex("DATE")) + ": " + date + "</td><td>" +
                b_name + "<br>" +
                add_2 + "<br>" +
                cvr + "<br>" +
                phone + "<br>" +
                mail + "</td></tr></table></td></tr><tr class=\"heading\"><td>" +
                cu_language.getString(cu_language.getColumnIndex("ITEM")) + "</td><td>" +
                cu_language.getString(cu_language.getColumnIndex("PRICE")) + "</td></tr>" + str + str_refund +
                "<tr class=\"heading\"> <td> " +
                cu_language.getString(cu_language.getColumnIndex("DISCOUNT")) + "</td>  <td> </td>  </tr>" +


                refunded_if +


                "<tr class=\"item \"><td>" +
                cu_language.getString(cu_language.getColumnIndex("DISCOUNT")) + "<div style=\"text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                discount + "</div></td><td>" +
                formatter.format(discount_amount.toDouble()) + "</td></tr>" +
                " <tr class=\"total\"> <td></td> <td> " + cu_language.getString(cu_language.getColumnIndex("TOTAL")) + ": " +
                /* String.format("%.2f",*/ formatter.format(total.replace(",", ".").toDouble())/*.toDouble())*/ +
                if (!tax.equals("")) {
                    "<div style=\"text-overflow:ellipsis;color:#999999;max-width:400px\">(" +
                            cu_language.getString(cu_language.getColumnIndex("INCLUSIVEOFTAXES")) + ":" + formatter.format(tax.replace(",", ".").toDouble()) + ")</div> "
                } else {
                    ""
                } + " </td> </tr>   <tr class=\"top\"><td colspan=\"2\"><table><tr><td class=\"bottom-line\"></td></tr><tr class=\"footer\"><td class=\"font-small\">" +
                footer +
                "</td></tr></table></td></tr>   </table>  </div> </body>   </html>"

        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)
        mWebView = webView


//        doWebViewPrint()
        cu_language.close()
        return mView
    }


    private fun doWebViewPrint() {
        /*val webView = WebView(activity)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {

                createWebPrintJob(view)

                mWebView = null
            }
        }
        var i = 0
        str = ""
        while (i < one_item.size) {
            var inner_items = one_item[i].split("##".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            str = str + "<tr class=\"item\"><td>" +
                    inner_items[0] + "<div style=\"text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                    inner_items[1] + "</div></td><td>" +
                    inner_items[2] + "</td></tr>"
            i++
        }
        val htmlDocument = "<!doctype html> <html><head><meta charset=\"utf-8\"><title>Invoice</title> <style>.invoice-box { max-width: 450px;margin: auto;padding: 30px;border: 1px solid #eee;box-shadow: 0 0 10px rgba(0, 0, 0, .15);font-size: 16px;line-height: 24px; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;color: #555;} .invoice-box table { width: 100%;line-height: inherit;text-align: left; }.invoice-box table td {padding: 5px;vertical-align: top;} .invoice-box table tr td:nth-child(2) {text-align: right; }.invoice-box table tr.top table td {padding-bottom: 20px; } .invoice-box table tr.top table td.title font-size: 45px;line-height: 45px;color: #333;}.invoice-box table tr.information table td {padding-bottom: 40px;}.invoice-box table tr.heading td {background: #eee;border-bottom: 1px solid #ddd;font-weight: bold;}.invoice-box table tr.details td { padding-bottom: 20px;}.invoice-box table tr.item td{border-bottom: 1px solid #eee;} .invoice-box table tr.item.last td {border-bottom: none;}.invoice-box table tr.total td:nth-child(2) {border-top: 2px solid #eee;font-weight: bold;} @media only screen and (max-width: 600px) { .invoice-box table tr.top table td {width: 100%;display: block;text-align: center;}.invoice-box table tr.information table td {width: 100%;display: block;text-align: center;}}*/
        /** RTL **//*.rtl {direction: rtl;font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;}.rtl table {text-align: right;}.rtl table tr td:nth-child(2) {text-align: left;}.bottom-line{border-bottom: 1px dashed #cacaca;padding-bottom: 20px;}.aligncenter {text-align: center;}.alignright {text-align: right;}</style></head><body><div class=\"invoice-box\"><table cellpadding=\"0\" cellspacing=\"0\"><tr class=\"top\"><td colspan=\"2\"><table><tr><td class=\"bottom-line\">" +
                "Invoice #:" + receiptNo + " <br></td></tr></table></td></tr><tr class=\"information\"><td colspan=\"2\"><table><tr><td>" +
                "Cashier: " + cashier + "<br>" +
                "Date: " + date + "</td><td>" +
                b_name + "<br>" +
                add_2 + "<br>" +
                mail + "</td></tr></table></td></tr><tr class=\"heading\"><td>Item</td><td>Price</td></tr>" + str +
                "<tr class=\"heading\"> <td> Discount </td>  <td> </td>  </tr>" +
                "<tr class=\"item last\"><td>Discount<div style=\"text-overflow:ellipsis;overflow:hidden;color:#999999;max-width:400px\">" +
                discount + "</div></td><td>" +
                discount_amount + "</td></tr> <tr class=\"total\"> <td></td> <td> Total: " +
                total + " </td> </tr> </table> </div></body></html>"

        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)
        mWebView = webView*/
    }

    private fun createWebPrintJob(webView: WebView) {
        val printManager = activity
                .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter()

        val jobName = getString(R.string.app_name) + " Document"
        val printJob = printManager.print(jobName, printAdapter,
                PrintAttributes.Builder().build())

    }

    override fun onResume() {
        super.onResume()
        if (i > 0) {
            activity.finish()
            i = 0
        } else
            i++
    }
}

