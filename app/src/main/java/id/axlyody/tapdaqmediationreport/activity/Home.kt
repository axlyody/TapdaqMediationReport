/*
 * Home.kt
 * Copyright 2020 Axl Yody <axlyod@gmail.com>
 *
 */

package id.axlyody.tapdaqmediationreport.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import id.axlyody.tapdaqmediationreport.AppActivity
import id.axlyody.tapdaqmediationreport.R
import id.axlyody.tapdaqmediationreport.utils.setupWithNavController
import id.axlyody.tapdaqmediationreport.utils.start
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppActivity() {
    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigation()
    }

    private fun bottomNavigation() {
        home_bottom_navbutton.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.nav_overview,
                R.navigation.nav_apps,
                R.navigation.nav_mediation
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host,
            intent = intent
        ).apply {
            observe(this@Home, { navController ->
                NavigationUI.setupActionBarWithNavController(this@Home, navController)
            })

            currentNavController = this
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value?.navigateUp() ?: false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                start(About::class.java)
            }
            R.id.menu_logout -> {
                prefs.edit().remove("api_token").apply()
                start(Main::class.java)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}