package com.example.kotlinbookapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.kotlinbookapp.R
import com.example.kotlinbookapp.fragment.AboutAppFragment
import com.example.kotlinbookapp.fragment.DashboardFragment
import com.example.kotlinbookapp.fragment.FavouriteFragment
import com.example.kotlinbookapp.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toolbar : Toolbar
    lateinit var drawerlayout : DrawerLayout
    lateinit var navigationview : NavigationView
    lateinit  var framelayout : FrameLayout
    lateinit var coordinatorLayout : CoordinatorLayout

    var previousMenuItem : MenuItem? = null
     // val transaction  = supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         openFragment(DashboardFragment())
        supportActionBar?.title = "Dashboard"


        toolbar = findViewById(R.id.toolbar)
        drawerlayout = findViewById(R.id.drawerLayout)
        navigationview = findViewById(R.id.navigationView)
        framelayout = findViewById(R.id.frameLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        setupToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerlayout, R.string.open_drawer, R.string.close_drawer)
        drawerlayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        // set navigation item click listeners for the drawer items

        navigationview.setNavigationItemSelectedListener {

            // code to make the navigationview items to be checked upon selection
            // check if previousMenuItem is not null i.e. at least one Menu item has been clicked
            if(previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId) {

                R.id.dashboard -> {

                    openFragment(DashboardFragment())
                    supportActionBar?.title="Dashboard"
                    drawerlayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerlayout.closeDrawers()
                }
                R.id.favourite -> {
                   openFragment(FavouriteFragment())
                    supportActionBar?.title="Favourite"
                    drawerlayout.closeDrawers()
                }
                R.id.aboutapp -> {
                   openFragment(AboutAppFragment())
                    supportActionBar?.title="About App"
                    drawerlayout.closeDrawers()
                }

            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
          supportActionBar?.title="Dashboard"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home) {
            drawerlayout.openDrawer(GravityCompat.START)
        }
         return super.onOptionsItemSelected(item)


    }

    fun openFragment(fragment : Fragment) {
        // check if value of fragment is Dashboard
        // if so check it by default using navigationview.setCaheckedItem(R.id.menuitemId)

        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout,fragment)
                .commit()
        if(fragment == DashboardFragment()){
            navigationview.setCheckedItem(R.id.dashboard)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(fragment) {
            !is DashboardFragment -> {
                openFragment(DashboardFragment())
                supportActionBar?.title = "Dashboard"
            }
            else -> super.onBackPressed()
        }
    }
}