package com.urgentx.recycledump.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.Scene
import android.transition.TransitionManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.urgentx.recycledump.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_scene1.*
import kotlinx.android.synthetic.main.main_scene2.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        mainDisposeBtn.setOnClickListener({ handleDisposeBtnClick() })

        mainReportBtn.setOnClickListener({ startActivity(Intent(this, PlacesActivity::class.java)) })

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun handleDisposeBtnClick() {
        val sceneRoot = findViewById(R.id.mainBtn1) as ViewGroup

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //We have access to Transition API, proceed with animating layout change.
            val scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.main_scene2, this)
            TransitionManager.go(scene2, Fade())
        } else {
            //Just display new buttons w/o animating.
        }

        mainRecycleBtn.setOnClickListener({
            //Send user to categorise their item.
            startActivity(Intent(this, RecycleInfoActivity::class.java))
        })
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_items -> {
                handleClick(1)
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }

            R.id.nav_sign_out -> {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener({
                            // user is now signed out
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        })
                return true
            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun handleClick(clickType: Int) {

        // Handle navigation view item clicks here.
        if (clickType != 0) {
            var fragment: Fragment? = null
            var fragmentClass: Class<*>? = null
            if (clickType == 1) {
                fragmentClass = MyItemsFragment::class.java

            } else if (clickType == 2) {

            }

            try {
                fragment = fragmentClass!!.newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.mainContainerBelowToolbar, fragment).commit()

        }
        drawer_layout.closeDrawer(Gravity.START, true)
    }
}
