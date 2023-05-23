package com.example.newsapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import com.example.newsapi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo

    private var needsAuthentication: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchSavedState(savedInstanceState = savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = BuildConfig.SOURCE_TITLE

        if (needsAuthentication) {
            initBiometric()
            checkBiometric()
        } else {
            binding.overlayView.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(AUTHENTICATE_STATE_TAG, needsAuthentication)
    }

    private fun fetchSavedState(savedInstanceState: Bundle?) {
        var value = true
        if (savedInstanceState != null) {
            value = savedInstanceState?.getBoolean(AUTHENTICATE_STATE_TAG) == true
        }

        needsAuthentication = value
    }

    private fun initBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                finish()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                binding.overlayView.visibility = View.GONE
                needsAuthentication = false
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = PromptInfo.Builder()
            .setTitle(this.resources.getString(R.string.verify_your_identity))
            .setSubtitle(this.resources.getString(R.string.use_your_fingerprint))
            .setNegativeButtonText(this.resources.getString(R.string.cancel))
            .build()
    }

    private fun checkBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.overlayView.visibility = View.GONE
                needsAuthentication = false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.overlayView.visibility = View.GONE
                needsAuthentication = false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.overlayView.visibility = View.GONE
                needsAuthentication = false
            }
        }
    }

    companion object {
        const val AUTHENTICATE_STATE_TAG = "authenticate"
    }
}