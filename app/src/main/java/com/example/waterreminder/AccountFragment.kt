package com.example.waterreminder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val ivProfileAvatar = view.findViewById<ImageView>(R.id.ivProfileAvatar)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val cardProfile = view.findViewById<MaterialCardView>(R.id.cardProfile)
        val cardSettings = view.findViewById<MaterialCardView>(R.id.cardSettings)
        val switchNotifications = view.findViewById<SwitchCompat>(R.id.switchNotifications)
        val tvEditGoal = view.findViewById<TextView>(R.id.tvEditGoal)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)

        val authManager = AuthManager(requireContext())
        tvUserName.text = authManager.getUserName()
        tvUserEmail.text = authManager.getUserEmail()

        ivProfileAvatar.alpha = 0f
        ivProfileAvatar.scaleX = 0.5f
        ivProfileAvatar.scaleY = 0.5f
        ivProfileAvatar.animate().alpha(1f).scaleX(1f).scaleY(1f).duration = 400

        cardProfile.alpha = 0f
        cardProfile.translationY = 20f
        cardProfile.animate().alpha(1f).translationY(0f).setStartDelay(100).duration = 400

        cardSettings.alpha = 0f
        cardSettings.translationY = 20f
        cardSettings.animate().alpha(1f).translationY(0f).setStartDelay(200).duration = 400

        btnLogout.alpha = 0f
        btnLogout.animate().alpha(1f).setStartDelay(300).duration = 300

        tvEditGoal.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.msg_coming_soon), Toast.LENGTH_SHORT).show()
        }

        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "activées" else "désactivées"
            Toast.makeText(requireContext(), "Notifications $status", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_dialog_title))
                .setMessage(getString(R.string.logout_dialog_message))
                .setPositiveButton(getString(R.string.dialog_confirm)) { _, _ ->
                    authManager.logout()
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton(getString(R.string.dialog_cancel), null)
                .show()
        }

        return view
    }
}
