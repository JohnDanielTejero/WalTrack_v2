package com.jdt.waltrackv2.view

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.ActivityMainBinding
import com.jdt.waltrackv2.view.fragments.DashboardFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle : ActionBarDrawerToggle
    private var selectedFragmentId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.root
        navView = binding.navView
        toolbar = binding.toolbar

        setSupportActionBar(toolbar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        // Set the color of the hamburger icon
        val drawable = actionBarDrawerToggle.drawerArrowDrawable
        drawable.color = ContextCompat.getColor(this, R.color.white)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBar?.setHomeAsUpIndicator(drawable)
        actionBarDrawerToggle.syncState()
        supportActionBar?.title = ""

        selectedFragmentId = savedInstanceState?.getInt("selectedFragmentId") ?: R.id.menu_dashboard
        initializeFragment(selectedFragmentId)
    }

    private fun initializeFragment(fragmentId: Int){
        var fragment: Fragment? = null
        if (fragmentId == R.id.menu_dashboard) {
            fragment = DashboardFragment()
            supportActionBar?.title = "Dashboard"
            navView.setCheckedItem(R.id.menu_dashboard)
        }

        fragment?.let { replaceFragment(it) }
    }

    private fun replaceFragment(f: Fragment){
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(binding.navHostFragmentContainer.id, f)
        ft.commit()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedFragmentId", selectedFragmentId)
    }

}