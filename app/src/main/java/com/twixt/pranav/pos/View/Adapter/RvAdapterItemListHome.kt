package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.twixt.pranav.pos.Controller.ProcessResponcceInterphase
import com.twixt.pranav.pos.Controller.Request.RequestCategories
import com.twixt.pranav.pos.Controller.Responce.ResponceCategories
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.Controller.SharedPreferenceHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Activity.Items
import java.util.*


/**
 * Created by Pranav on 11/24/2017.
 */
class RvAdapterItemListHome(var context: Context) : RecyclerView.Adapter<RvAdapterItemListHome.ViewHolder>() {

    var items = ArrayList<ResponceCategories>()
    var count = 1
    var flag = 0
    var size = 0
    private var database: SQLiteHelper? = SQLiteHelper(context)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val layoutInflator = LayoutInflater.from(context)
        val view = layoutInflator.inflate(R.layout.rv_home, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (flag == 1) {
            val database: SQLiteHelper? = SQLiteHelper(context)

            database!!.insertCategories(position.toString(), items[position].id.toString(), items[position].name.toString(), items[position].color.toString())

            holder.color.setBackgroundColor(Color.parseColor("#" + items[position].color.toString()))
            holder.title.setText(items[position].name.toString())
            /* try {*/
            if (position == items.size - 1) {
                RequestCategories(context, ResponceProcessorAdapterCategoriesss()).getCategories(count++,
                        SharedPreferenceHelper(context).getString(SharedPreferenceHelper(context).POS_SHOP_ID, "0"))
            }
            /*} catch (e: Exception) {
            }*/
        } else {
            val cu = database!!.getcategories(position)
            cu.moveToFirst()
            holder.color.setBackgroundColor(Color.parseColor("#" + cu.getString(cu.getColumnIndex("COLOR")).toString()))
            holder.title.setText(cu.getString(cu.getColumnIndex("NAME")).toString())
//            Toast.makeText(context, size.toString() + cu.getString(cu.getColumnIndex("NAME")).toString(), Toast.LENGTH_SHORT).show()
            cu.close()
        }
    }

    inner class ResponceProcessorAdapterCategoriesss : ProcessResponcceInterphase<Array<ResponceCategories>> {
        override fun processResponce(responce: Array<ResponceCategories>) {
            if (responce != null && responce.size != 0) {
                val list = ArrayList(Arrays.asList(*responce))
                items.addAll(list)
                notifyItemRangeInserted(items.size - list.size, list.size)
            }

        }

    }

    /*inner class ResponceProcessorAdapterCategories : ProcessResponcceInterphase<Array<ResponceCategories>> {
        override fun processResponce(responce: Array<ResponceCategories>) {
            if (responce != null && responce.size != 0) {
                val list = ArrayList(Arrays.asList(*responce))
                items.addAll(list)
                notifyItemRangeInserted(items.size - list.size, list.size)
            }
        }

    }*/

    override fun getItemCount(): Int {
        return size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var color: ImageView
        var layout: RelativeLayout
        var title: TextView

        init {
            color = view.findViewById(R.id.colorss)
            layout = view.findViewById(R.id.layout)
            title = view.findViewById(R.id.title)

            layout.setOnClickListener(View.OnClickListener {

                try {
                    val intent = Intent(context, Items::class.java)
                    if (flag == 1) {
                        intent.putExtra("CAT_ID", items[adapterPosition].id.toString())
                        intent.putExtra("CAT_NAME", items[adapterPosition].name.toString())
                        context.startActivity(intent)
                    } else {
                        val cu = database!!.getcategories(adapterPosition)
                        cu.moveToFirst()
                        intent.putExtra("CAT_ID", cu.getString(cu.getColumnIndex("ID")).toString())
                        intent.putExtra("CAT_NAME", cu.getString(cu.getColumnIndex("NAME")).toString())
                        context.startActivity(intent)
                        cu.close()
                    }
                }catch (e:Exception){}


//                FragmentItems.set(items[adapterPosition].id)
//                context.startActivity(Intent(context, Items::class.java))
            })
        }
    }

    fun set(listt: ArrayList<ResponceCategories>) {
        items = listt
        flag = 1
        size = items.size

    }

    fun set1() {
        flag = 0
        size = database!!.getcategoriessize().getCount()
    }

}