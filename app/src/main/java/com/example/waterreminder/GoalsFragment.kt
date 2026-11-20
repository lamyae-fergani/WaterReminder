package com.example.waterreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class GoalsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        val cardDailyGoal = view.findViewById<MaterialCardView>(R.id.cardDailyGoal)
        val btnSaveGoal = view.findViewById<MaterialButton>(R.id.btnSaveGoal)
        val cardHistory1 = view.findViewById<MaterialCardView>(R.id.cardHistory1)
        val cardHistory2 = view.findViewById<MaterialCardView>(R.id.cardHistory2)
        val cardHistory3 = view.findViewById<MaterialCardView>(R.id.cardHistory3)

        // Initial entry states
        cardDailyGoal.alpha = 0f
        cardDailyGoal.translationY = 30f
        cardHistory1.alpha = 0f
        cardHistory2.alpha = 0f
        cardHistory3.alpha = 0f

        // Animations
        cardDailyGoal.animate().alpha(1f).translationY(0f).duration = 400
        cardHistory1.animate().alpha(1f).setStartDelay(150).duration = 300
        cardHistory2.animate().alpha(1f).setStartDelay(250).duration = 300
        cardHistory3.animate().alpha(1f).setStartDelay(350).duration = 300

        btnSaveGoal.setOnClickListener {
            btnSaveGoal.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                btnSaveGoal.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
            Toast.makeText(requireContext(), getString(R.string.goal_saved_success), Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
