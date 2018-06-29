package com.twixt.pranav.pos.View.Activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentItems

class Items : AppCompatActivity() {

    lateinit var my_toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        val name = intent.getStringExtra("CAT_NAME")
        if (savedInstanceState == null) {
            val id = intent.getStringExtra("CAT_ID")

            val bundle = Bundle()
            bundle.putString("CAT_ID", id)
            bundle.putString("CAT_NAME", name)
            val fragmentItems = FragmentItems()
            fragmentItems.arguments = bundle

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_items, fragmentItems, "Items")
                    .commit()
        }


        my_toolbar = findViewById(R.id.my_toolbar)




        if (my_toolbar == null) return
        my_toolbar.setNavigationIcon(R.drawable.ic_drawable_arrow_back)
        setSupportActionBar(my_toolbar)

        my_toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(name)
        my_toolbar.setTitleTextColor(Color.WHITE)

    }

    /*override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.getItemId()) {
            R.id.search -> {
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.mnuCart -> {
                Toast.makeText(this,"Seasdgsrch",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Cart::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }*/

    /*override fun onStop() {
        super.onStop()
        Toast.makeText(this, "items 5", Toast.LENGTH_SHORT).show()

        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // The first in the list of RunningTasks is always the foreground task.
        val foregroundTaskInfo = am.getRunningTasks(1)[0]
        if (foregroundTaskInfo.topActivity.packageName != this.packageName) {
            // The app is exiting no other activity of your app is brought to front
            *//*val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            System.exit(0)*//*
            this.finishAffinity()
        }
    }*/

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom)
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.window.decorView.applicationWindowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
