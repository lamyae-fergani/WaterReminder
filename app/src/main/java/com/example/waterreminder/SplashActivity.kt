package com.example.waterreminder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splashRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ivLogo = findViewById<View>(R.id.ivLogo)
        val tvAppName = findViewById<View>(R.id.tvAppName)
        val tvSlogan = findViewById<View>(R.id.tvSlogan)
        val progressBar = findViewById<View>(R.id.progressBar)

        ivLogo.scaleX = 0.95f
        ivLogo.scaleY = 0.95f
        ivLogo.alpha = 0f

        tvAppName.translationY = 30f
        tvAppName.alpha = 0f

        tvSlogan.translationY = 30f
        tvSlogan.alpha = 0f

        progressBar.alpha = 0f

        val logoAlpha = ObjectAnimator.ofFloat(ivLogo, View.ALPHA, 0f, 1f)
        val logoScaleX = ObjectAnimator.ofFloat(ivLogo, View.SCALE_X, 0.95f, 1f)
        val logoScaleY = ObjectAnimator.ofFloat(ivLogo, View.SCALE_Y, 0.95f, 1f)

        val nameAlpha = ObjectAnimator.ofFloat(tvAppName, View.ALPHA, 0f, 1f)
        val nameTranslate = ObjectAnimator.ofFloat(tvAppName, View.TRANSLATION_Y, 30f, 0f)

        val sloganAlpha = ObjectAnimator.ofFloat(tvSlogan, View.ALPHA, 0f, 1f)
        val sloganTranslate = ObjectAnimator.ofFloat(tvSlogan, View.TRANSLATION_Y, 30f, 0f)

        val progressAlpha = ObjectAnimator.ofFloat(progressBar, View.ALPHA, 0f, 1f)

        AnimatorSet().apply {
            play(logoAlpha).with(logoScaleX).with(logoScaleY)
            play(nameAlpha).with(nameTranslate).after(logoAlpha)
            play(sloganAlpha).with(sloganTranslate).after(nameAlpha)
            play(progressAlpha).after(sloganAlpha)
            duration = 500
            start()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPrefs = getSharedPreferences("water_reminder_prefs", MODE_PRIVATE)
            val onboardingCompleted = sharedPrefs.getBoolean("onboarding_completed", false)
            val authManager = AuthManager(this)

            val targetIntent = when {
                !onboardingCompleted -> Intent(this, OnboardingActivity::class.java)
                !authManager.isLoggedIn() -> Intent(this, LoginActivity::class.java)
                else -> Intent(this, MainActivity::class.java)
            }

            startActivity(targetIntent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 2200)
    }
}
