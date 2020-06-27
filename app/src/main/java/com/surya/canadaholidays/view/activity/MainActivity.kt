package com.surya.canadaholidays.view.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.surya.canadaholidays.R
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor

    private lateinit var navController: NavController

    private val TAG = MainActivity::getLocalClassName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        biometricManager = BiometricManager.from(this)
        checkBiometricsAvailability(biometricManager)
        authenticateUser()
    }

    /**
     * To authenticate user to view the details
     */
    private fun authenticateUser() {
         executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                                "Authentication error: $errString", Toast.LENGTH_SHORT)
                                .show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(applicationContext,
                                getString(R.string.authentication_success), Toast.LENGTH_SHORT)
                                .show()
                        navController = Navigation.findNavController(this@MainActivity,R.id.nav_host_fragment)
                        NavigationUI.setupActionBarWithNavController(this@MainActivity,navController)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, getString(R.string.authentication_failed),
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometric_title))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setNegativeButtonText(getString(R.string.biometric_negative_button))
                .build()

        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * Check device has biometrics hardware
     */
    private fun checkBiometricsAvailability(biometricManager: BiometricManager) {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.d(TAG, getString(R.string.biometrics_success))
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, getString(R.string.biometrics_error_hardware))
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, getString(R.string.biometrics_error_hardware_unavailable))
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(TAG, getString(R.string.biometrics_not_enrolled))
        }
    }

    // To enable back navigation up arrow in fragments
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)
    }
}