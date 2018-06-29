package com.twixt.pranav.pos.View.Activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.twixt.pranav.pos.R
import com.twixt.pranav.pos.View.Fragment.FragmentHome


class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)

        /*val myListener = object : Foreground.Listener {
            override fun onBecameForeground() {
                Toast.makeText(this@Home, "for g", Toast.LENGTH_SHORT).show()
            }

            override fun onBecameBackground() {
                Toast.makeText(this@Home, "back g", Toast.LENGTH_SHORT).show()
            }
        }*/
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_activity, FragmentHome(), "Home")
                    .commit()
        }

    }

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

    /*override fun onUserLeaveHint() {

        Toast.makeText(this, "KEYCODE_HOME", Toast.LENGTH_SHORT).show()
        super.onUserLeaveHint()
    }*/

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        Toast.makeText(this, keyCode.toString(), Toast.LENGTH_SHORT).show()

        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(this, "KEYCODE_HOME", Toast.LENGTH_SHORT).show()
//            return true
        }
        *//*if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(this, "KEYCODE_BACK", Toast.LENGTH_SHORT).show()
            onBackPressed()
            return true;
        }*//*
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            Toast.makeText(this, "KEYCODE_MENU", Toast.LENGTH_SHORT).show()
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
