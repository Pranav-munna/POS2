package com.twixt.pranav.pos.View.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.twixt.pranav.pos.Controller.SQLiteHelper
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragementModifier
import java.text.DecimalFormat

/**
 * Created by Pranav on 2/21/2018.
 */
class RvAdapterModifier(var context: Context, val fragementModifier: FragementModifier) : RecyclerView.Adapter<RvAdapterModifier.ViewHolder>() {
    private lateinit var databaseforModifier: SQLiteHelper
    var item_id = ""
    var count = ""
    var title = ""
    var modifier_items = ""
    lateinit var tv_ttl: TextView
    var formatter = DecimalFormat("#,###,##0.00")
    lateinit var mfragementModifier: ModifierCallBack

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val layoutInflator = LayoutInflater.from(context)
        return ViewHolder(layoutInflator.inflate(R.layout.rv_modifier, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val cu_modifier = databaseforModifier.getModifier(position, item_id)
        cu_modifier.moveToFirst()

        val cu_item = databaseforModifier.getitem(item_id.toInt())
        cu_item.moveToFirst()

        var mainstring = cu_item.getString(cu_item.getColumnIndex("MODIFIER")).split("!&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        while (i < (mainstring.size - 1)) {
            var sub_strig = mainstring[i].split("@.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (sub_strig[2].equals(cu_modifier.getString(cu_modifier.getColumnIndex("OPTION_ID")))) {
                holder!!.tv_modifier_name.setTextColor(Color.RED)
                holder!!.tv_modifier_amount.setTextColor(Color.RED)
            }
            i++
        }


        if (!(title.equals(cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_NAME"))))) {
            holder!!.tv_modifier_group_name.visibility = View.VISIBLE
            title = cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_NAME"))
        }
        holder!!.tv_modifier_group_name.setText(cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_NAME")))
        holder!!.tv_modifier_name.setText(cu_modifier.getString(cu_modifier.getColumnIndex("OPTION_NAME")))
        holder!!.tv_modifier_amount.setText(formatter.format(cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_PRICE")).replace(",", ".").toDouble()))

        cu_modifier.close()
        cu_item.close()
    }

    override fun getItemCount(): Int {
        return count.toInt()
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var tv_modifier_group_name: TextView
        lateinit var tv_modifier_name: TextView
        lateinit var tv_modifier_amount: TextView
        lateinit var relativelayout_id: RelativeLayout

        init {
            tv_modifier_group_name = view.findViewById(R.id.tv_modifier_group_name)
            tv_modifier_name = view.findViewById(R.id.tv_modifier_name)
            tv_modifier_amount = view.findViewById(R.id.tv_modifier_amount)
            relativelayout_id = view.findViewById(R.id.relativelayout_id)

            val cu_item = databaseforModifier.getitem(item_id.toInt())
            cu_item.moveToFirst()


            relativelayout_id.setOnClickListener {
                try {

                    val cu_modifier = databaseforModifier.getModifier(adapterPosition, item_id)
                    cu_modifier.moveToFirst()

                    val cu_item = databaseforModifier.getitem(item_id.toInt())
                    cu_item.moveToFirst()

                    modifier_items = cu_modifier.getString(cu_modifier.getColumnIndex("OPTION_NAME")) + "@." +
                            cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_PRICE")) + "@." +
                            cu_modifier.getString(cu_modifier.getColumnIndex("OPTION_ID")) + "!& "



                    if (tv_modifier_amount.currentTextColor == Color.RED) {
                        tv_modifier_name.setTextColor(Color.DKGRAY)
                        tv_modifier_amount.setTextColor(Color.DKGRAY)
                        databaseforModifier.updateModifiers(cu_item.getString(cu_item.getColumnIndex("MODIFIER")).replace(modifier_items, ""), item_id)

//                        fragementModifier.set("0", item_id)
                        mfragementModifier.addModifiers("0", item_id, databaseforModifier, tv_ttl)
//                    Toast.makeText(context, cu_item.getString(cu_item.getColumnIndex("MODIFIER")).replace(modifier_items, ""), Toast.LENGTH_SHORT).show()

                    } else {
                        tv_modifier_name.setTextColor(Color.RED)
                        tv_modifier_amount.setTextColor(Color.RED)

                        databaseforModifier.updateModifiers(cu_item.getString(cu_item.getColumnIndex("MODIFIER")) + modifier_items,
                                item_id)

//                    Toast.makeText(context, cu_item.getString(cu_item.getColumnIndex("MODIFIER")) + modifier_items, Toast.LENGTH_SHORT).show()

//                        fragementModifier.set(cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_PRICE")), item_id)
                        mfragementModifier.addModifiers(cu_modifier.getString(cu_modifier.getColumnIndex("MODIFIER_PRICE")), item_id, databaseforModifier, tv_ttl)

                    }
                    cu_modifier.close()
                    cu_item.close()
                } catch (e: Exception) {
                }

            }

cu_item.close()
        }

    }

    fun set(id: String, cnt: String, databaseforModifiers: SQLiteHelper, tv_total: TextView) {
        tv_ttl = tv_total
        databaseforModifier = databaseforModifiers
        item_id = id
        count = cnt
    }

    fun setCallbackModifiers(fragementModifier: FragementModifier) {
        mfragementModifier = fragementModifier
    }

    public interface ModifierCallBack {
        fun addModifiers(price: String, item_id: String, databaseforModifier: SQLiteHelper, tv_ttl: TextView)
    }

}