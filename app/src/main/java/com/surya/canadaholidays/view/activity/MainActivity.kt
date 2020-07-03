package com.surya.canadaholidays.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.surya.canadaholidays.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val TAG = MainActivity::getLocalClassName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this@MainActivity, navController)
    }

    // To enable back navigation up arrow in fragments
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}