package com.example.waterreminder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: MaterialButton
    private lateinit var btnSkip: Button
    private lateinit var dots: Array<ImageView>

    private val prefsName = "water_reminder_prefs"
    private val keyOnboardingCompleted = "onboarding_completed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sharedPrefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        if (sharedPrefs.getBoolean(keyOnboardingCompleted, false)) {
            navigateToNext()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onboardingRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)

        dots = arrayOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3)
        )

        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.ic_onbg_drop,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc_1)
            ),
            OnboardingItem(
                R.drawable.ic_onbg_progress,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc_2)
            ),
            OnboardingItem(
                R.drawable.ic_onbg_reminder,
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc_3)
            )
        )

        val adapter = OnboardingAdapter(onboardingItems)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
                if (position == onboardingItems.size - 1) {
                    btnNext.text = getString(R.string.onboarding_start)
                    btnSkip.visibility = View.INVISIBLE
                } else {
                    btnNext.text = getString(R.string.onboarding_next)
                    btnSkip.visibility = View.VISIBLE
                }
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingItems.size - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                completeOnboarding()
            }
        }

        btnSkip.setOnClickListener {
            completeOnboarding()
        }
    }

    private fun updateDots(activeIndex: Int) {
        for (i in dots.indices) {
            if (i == activeIndex) {
                dots[i].setBackgroundResource(R.drawable.dot_selected)
            } else {
                dots[i].setBackgroundResource(R.drawable.dot_unselected)
            }
        }
    }

    private fun completeOnboarding() {
        val sharedPrefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(keyOnboardingCompleted, true).apply()
        navigateToNext()
    }

    private fun navigateToNext() {
        val authManager = AuthManager(this)
        val intent = if (authManager.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
