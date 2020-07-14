package com.surya.canadaholidays.view.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.surya.canadaholidays.BuildConfig
import com.surya.canadaholidays.R
import com.surya.canadaholidays.application.CanadaHolidaysApplication
import com.surya.canadaholidays.view.fragment.HolidayListFragmentArgs


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    /**
     * Initialising navigation components
     */
    private fun setUpNavigation() {
        navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this@MainActivity, navController)
        navController.addOnDestinationChangedListener(mainDestinationChangedListener)
    }

    /**
     * To enable back navigation up arrow in fragments
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    /**
     * Fragment navigation
     */
    private val mainDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            // compare destination id
            title = when (destination.id) {
                R.id.provincesListFragment -> getString(R.string.Provinces)
                R.id.holidayListFragment ->
                    arguments?.let {
                        HolidayListFragmentArgs.fromBundle(it).province?.nameEn
                    }
                else -> ""
            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.share_app -> {
                shareApp()
                true
            }
            R.id.rate_app -> {
                rateApp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Redirection to rate the app in play store
     */
    private fun rateApp() {
        val uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Enabling the share feature to other apps
     */
    private fun shareApp() {
        ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setChooserTitle(CanadaHolidaysApplication.appContext.getString(R.string.app_share_desc))
            .setText("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            .startChooser()
    }

}