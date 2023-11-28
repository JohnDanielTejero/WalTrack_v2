package com.jdt.waltrackv2.view

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.ActivityMainBinding
import com.jdt.waltrackv2.utils.OnDataLoading
import com.jdt.waltrackv2.view.fragments.DashboardFragment
import com.jdt.waltrackv2.view.fragments.ExpensesFragment
import com.jdt.waltrackv2.view.fragments.IncomeFragment
import com.jdt.waltrackv2.view.fragments.WalletFragment


class MainActivity : AppCompatActivity(), OnDataLoading {
    private val THEME_PREFS = "theme_prefs"
    private val THEME_KEY = "theme_key"

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView : NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var actionBarDrawerToggle : ActionBarDrawerToggle
    private var isLoading : Boolean? = false
    private var currentFragment: Fragment? = null
    private var selectedFragmentId: Int = 0
    private lateinit var iconImageView: ImageView
    private var lastBackPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        var savedTheme = prefs.getInt(THEME_KEY, -1)

        if (savedTheme == -1) {
            savedTheme = if (isDarkModeEnabled()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            saveThemePreference(savedTheme)
        }

        AppCompatDelegate.setDefaultNightMode(savedTheme)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    val currentTime = System.currentTimeMillis()
                    val threshold = 2000

                    if (currentTime - lastBackPressedTime < threshold) {
                        finish()
                    } else {
                        lastBackPressedTime = currentTime
                        Toast.makeText(this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        // Register the callback with the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, callback)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.root
        navView = binding.navView
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.menu_dashboard -> {
                    replaceFragment(DashboardFragment())
                    supportActionBar?.title = "Dashboard"
                    navView.setCheckedItem(R.id.menu_dashboard)
                    selectedFragmentId = R.id.menu_dashboard
                }

                R.id.menu_expense -> {
                    replaceFragment(ExpensesFragment())
                    supportActionBar?.title = "Expense"
                    navView.setCheckedItem(R.id.menu_expense)
                    selectedFragmentId = R.id.menu_expense
                }

                R.id.menu_income -> {
                    replaceFragment(IncomeFragment())
                    supportActionBar?.title = "Income"
                    navView.setCheckedItem(R.id.menu_income)
                    selectedFragmentId = R.id.menu_income
                }

                R.id.menu_wallet -> {
                    replaceFragment(WalletFragment())
                    supportActionBar?.title = "Wallets"
                    navView.setCheckedItem(R.id.menu_wallet)
                    selectedFragmentId = R.id.menu_wallet
                }

                R.id.menu_contribute -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JohnDanielTejero/WalTrack_v2"))
                    startActivity(intent)
                }

                R.id.menu_community -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/6sa6DWY4DW"))
                    startActivity(intent)
                }

                R.id.menu_share -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/scl/fi/kvw8enhoo9owkli6po15n/WalTrackv2.apk?rlkey=qb46hjrntln10yhpgf6usdveq&dl=0"))
                    startActivity(intent)
                }



            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //val drawable = actionBarDrawerToggle.drawerArrowDrawable
        //drawable.color = ContextCompat.getColor(this, R.color.white)
        val drawable = actionBarDrawerToggle.drawerArrowDrawable
        val color = ContextCompat.getColor(this, R.color.white)
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBar?.setHomeAsUpIndicator(drawable)
        actionBarDrawerToggle.syncState()
        supportActionBar?.title = ""

        selectedFragmentId = savedInstanceState?.getInt("selectedFragmentId") ?: R.id.menu_dashboard
        initializeFragment(selectedFragmentId)

        iconImageView = binding.iconImageView
        updateIcon(isDarkModeEnabled())
        iconImageView.setOnClickListener { onIconClick() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedFragmentId", selectedFragmentId)
    }


    private fun onIconClick() {
        if (!isLoading!!) {
            val newMode = !isDarkModeEnabled()
            val newTheme = if (newMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            saveThemePreference(newTheme)
            AppCompatDelegate.setDefaultNightMode(newTheme)
            updateIcon(newMode)
            recreate()
        }
    }
    private fun updateIcon(isDarkMode: Boolean) {
        iconImageView.setImageResource(if (isDarkMode) R.drawable.ic_dark_mode else R.drawable.ic_light_mode)
    }

    private fun isDarkModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
    private fun initializeFragment(fragmentId: Int){
        var fragment: Fragment? = null
        when (fragmentId) {
            R.id.menu_dashboard -> {
                if (currentFragment !is DashboardFragment) {
                    fragment = DashboardFragment()
                    supportActionBar?.title = "Dashboard"
                    navView.setCheckedItem(R.id.menu_dashboard)
                    selectedFragmentId = R.id.menu_dashboard
                }
            }

            R.id.menu_expense -> {
                if (currentFragment !is ExpensesFragment) {
                    fragment = ExpensesFragment()
                    supportActionBar?.title = "Expense"
                    navView.setCheckedItem(R.id.menu_expense)
                    selectedFragmentId = R.id.menu_expense
                }
            }

            R.id.menu_income -> {
                if (currentFragment !is IncomeFragment) {
                    fragment = IncomeFragment()
                    supportActionBar?.title = "Income"
                    navView.setCheckedItem(R.id.menu_income)
                    selectedFragmentId = R.id.menu_income
                }
            }

            R.id.menu_wallet -> {
                if (currentFragment !is WalletFragment) {
                    fragment = WalletFragment()
                    supportActionBar?.title = "Wallets"
                    navView.setCheckedItem(R.id.menu_wallet)
                    selectedFragmentId = R.id.menu_wallet
                }
            }

            else -> {
                fragment = DashboardFragment()
                supportActionBar?.title = "Dashboard"
                navView.setCheckedItem(R.id.menu_dashboard)
                selectedFragmentId = R.id.menu_dashboard
            }

        }
        fragment?.let {
            replaceFragment(it)
        }
    }
    private fun saveThemePreference(themeMode: Int) {
        val prefs = getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
        prefs.edit().putInt(THEME_KEY, themeMode).apply()
    }

    private fun replaceFragment(f: Fragment){
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(binding.navHostFragmentContainer.id, f)
        ft.commit()
        supportFragmentManager.executePendingTransactions()
        currentFragment = f
    }

    override fun onDataLoadingStarted() {
        isLoading = true
        navView?.menu?.findItem(R.id.menu_dashboard)?.isEnabled = false
        navView?.menu?.findItem(R.id.menu_expense)?.isEnabled = false
        navView?.menu?.findItem(R.id.menu_income)?.isEnabled = false
        navView?.menu?.findItem(R.id.menu_wallet)?.isEnabled = false
    }

    override fun onDataLoadingFinished() {
        isLoading = false
        navView?.menu?.findItem(R.id.menu_dashboard)?.isEnabled = true
        navView?.menu?.findItem(R.id.menu_expense)?.isEnabled = true
        navView?.menu?.findItem(R.id.menu_income)?.isEnabled = true
        navView?.menu?.findItem(R.id.menu_wallet)?.isEnabled = true
    }

}