package com.example.waterreminder

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var tilName: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var tilBirthdate: TextInputLayout
    private lateinit var etBirthdate: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvLoginLink: TextView

    private var selectedCalendar: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = AuthManager(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tilName = findViewById(R.id.tilName)
        etName = findViewById(R.id.etName)
        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)
        tilBirthdate = findViewById(R.id.tilBirthdate)
        etBirthdate = findViewById(R.id.etBirthdate)
        tilPassword = findViewById(R.id.tilPassword)
        etPassword = findViewById(R.id.etPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginLink = findViewById(R.id.tvLoginLink)

        etBirthdate.setOnClickListener {
            showDatePicker()
        }

        btnRegister.setOnClickListener {
            if (validateInputs()) {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()
                val birthdate = etBirthdate.text.toString()

                val success = authManager.registerUser(name, email, password, birthdate)
                if (success) {
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    tilEmail.error = getString(R.string.error_email_already_exists)
                    etEmail.requestFocus()
                }
            }
        }

        tvLoginLink.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCal = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            selectedCalendar = selectedCal
            val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            etBirthdate.setText(formattedDate)
            tilBirthdate.error = null
        }, year - 20, month, day)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val birthdate = etBirthdate.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        tilName.error = null
        tilEmail.error = null
        tilBirthdate.error = null
        tilPassword.error = null
        tilConfirmPassword.error = null

        if (name.isEmpty()) {
            tilName.error = getString(R.string.error_empty_name)
            etName.requestFocus()
            return false
        }

        if (name.length < 2) {
            tilName.error = getString(R.string.error_short_name)
            etName.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            tilEmail.error = getString(R.string.error_empty_email)
            etEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = getString(R.string.error_invalid_email)
            etEmail.requestFocus()
            return false
        }

        if (birthdate.isEmpty() || selectedCalendar == null) {
            tilBirthdate.error = getString(R.string.error_empty_birthdate)
            etBirthdate.requestFocus()
            return false
        }

        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - selectedCalendar!!.get(Calendar.YEAR)
        if (today.get(Calendar.YEAR) == selectedCalendar!!.get(Calendar.YEAR)) {
            age = 0
        } else if (today.get(Calendar.YEAR) > selectedCalendar!!.get(Calendar.YEAR)) {
            if (today.get(Calendar.MONTH) < selectedCalendar!!.get(Calendar.MONTH) ||
                (today.get(Calendar.MONTH) == selectedCalendar!!.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < selectedCalendar!!.get(Calendar.DAY_OF_MONTH))) {
                age--
            }
        }

        if (age <= 18) {
            tilBirthdate.error = getString(R.string.error_under_age)
            etBirthdate.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            tilPassword.error = getString(R.string.error_empty_password)
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            tilPassword.error = getString(R.string.error_short_password)
            etPassword.requestFocus()
            return false
        }

        if (confirmPassword != password) {
            tilConfirmPassword.error = getString(R.string.error_password_mismatch)
            etConfirmPassword.requestFocus()
            return false
        }

        return true
    }
}
