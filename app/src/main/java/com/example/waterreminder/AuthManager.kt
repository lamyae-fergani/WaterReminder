package com.example.waterreminder

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("water_reminder_auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password"
        private const val KEY_USER_BIRTHDATE = "user_birthdate"
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun registerUser(name: String, email: String, password: String, birthdate: String): Boolean {
        val existingEmail = prefs.getString(KEY_USER_EMAIL, null)
        if (existingEmail != null && existingEmail.equals(email, ignoreCase = true)) {
            return false
        }
        prefs.edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email.trim())
            .putString(KEY_USER_PASSWORD, password)
            .putString(KEY_USER_BIRTHDATE, birthdate)
            .apply()
        return true
    }

    fun validateLogin(email: String, password: String): Boolean {
        val savedEmail = prefs.getString(KEY_USER_EMAIL, null)
        val savedPassword = prefs.getString(KEY_USER_PASSWORD, null)

        if (savedEmail.isNullOrEmpty() || savedPassword.isNullOrEmpty()) {
            return false
        }

        return savedEmail.equals(email.trim(), ignoreCase = true) && savedPassword == password
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "Utilisateur") ?: "Utilisateur"
    }

    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun logout() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }
}
