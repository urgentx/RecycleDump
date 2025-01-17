package com.urgentx.recycledump.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.transition.Fade
import android.transition.Scene
import android.transition.TransitionManager
import android.transition.Visibility
import android.view.*
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.urgentx.recycledump.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_scene1.*
import kotlinx.android.synthetic.main.main_scene2.*
import kotlinx.android.synthetic.main.nav_header_main2.*


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
        val sceneRoot = findViewById<ViewGroup>(R.id.mainBtn1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //We have access to Transition API, proceed with animating layout change.
            val scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.main_scene2, this)
            TransitionManager.go(scene2, Fade())
        } else {
            //Just display new buttons w/o animating.
            mainBtn1.visibility = View.GONE
            mainRecycleBtn.visibility = View.VISIBLE
            mainDumpBtn.visibility = View.VISIBLE
        }

        mainRecycleBtn.setOnClickListener({
            //Send user to categorise their item.
            startActivity(Intent(this, RecycleInfoActivity::class.java))
        })

        mainDumpBtn.setOnClickListener({
            //Send user to categorise their dump item.
            startActivity(Intent(this, DumpInfoActivity::class.java))
        })
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        mainDrawerUserName.text = FirebaseAuth.getInstance().currentUser?.email
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_help -> {
                startActivity(Intent(this, HelpActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_items -> {
                handleClick(1)
            }
            R.id.nav_home -> {
                for (fragment in supportFragmentManager.fragments) {
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
            }

            R.id.nav_collect -> {
                startActivity(Intent(this, CreateCollectorActivity::class.java))
            }
            R.id.nav_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "I'm disposing of my waste conveniently with RecycleDump. Check it out at: ")
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.nav_contact -> {
                val contactFragment = ContactFragment()
                contactFragment.show(supportFragmentManager, "contactFrag")
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

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleClick(clickType: Int) {

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
            fragmentManager.beginTransaction().replace(R.id.mainContainerBelowToolbar, fragment!!).addToBackStack("maincontainerfrag").commit()

        }
        drawer_layout.closeDrawer(Gravity.START, true)
    }
}
