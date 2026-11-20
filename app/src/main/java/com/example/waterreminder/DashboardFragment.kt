package com.example.waterreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {

    private var currentMl = 1500
    private val goalMl = 2000
    private val intakeList = mutableListOf(
        WaterIntake(250, "08:30"),
        WaterIntake(330, "11:00"),
        WaterIntake(500, "14:30"),
        WaterIntake(420, "16:15")
    )

    private lateinit var tvConsumptionValue: TextView
    private lateinit var progressBarConsumption: ProgressBar
    private lateinit var tvProgressPercent: TextView
    private lateinit var waterLevelView: WaterLevelView
    private lateinit var rvHistory: RecyclerView
    private lateinit var adapter: WaterIntakeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val tvDashboardTitle = view.findViewById<TextView>(R.id.tvDashboardTitle)
        val tvWelcomeMessage = view.findViewById<TextView>(R.id.tvWelcomeMessage)
        val cardConsumption = view.findViewById<MaterialCardView>(R.id.cardConsumption)
        tvConsumptionValue = view.findViewById(R.id.tvConsumptionValue)
        progressBarConsumption = view.findViewById(R.id.progressBarConsumption)
        tvProgressPercent = view.findViewById<TextView>(R.id.tvProgressPercent)
        waterLevelView = view.findViewById(R.id.waterLevelView)
        val btnAddWaterAction = view.findViewById<MaterialButton>(R.id.btnAddWaterAction)
        rvHistory = view.findViewById(R.id.rvHistory)

        val authManager = AuthManager(requireContext())
        val userName = authManager.getUserName()
        tvWelcomeMessage.text = "Bonjour $userName, bienvenue sur WaterReminder"

        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        adapter = WaterIntakeAdapter(intakeList)
        rvHistory.adapter = adapter

        updateUI()

        btnAddWaterAction.setOnClickListener {
            val bottomSheet = AddWaterBottomSheet { amount ->
                addWater(amount)
            }
            bottomSheet.show(parentFragmentManager, "AddWaterBottomSheet")
        }

        return view
    }

    private fun addWater(amountMl: Int) {
        currentMl += amountMl
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        intakeList.add(0, WaterIntake(amountMl, currentTime))
        adapter.notifyItemInserted(0)
        rvHistory.scrollToPosition(0)

        updateUI()

        if (currentMl >= goalMl) {
            Toast.makeText(requireContext(), "Objectif atteint !", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "$amountMl ml ajoutés", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        val progressFloat = (currentMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)
        val percent = ((currentMl.toFloat() / goalMl.toFloat()) * 100).toInt()

        waterLevelView.animateToProgress(progressFloat)

        val displayLitres = currentMl / 1000f
        tvConsumptionValue.text = String.format(Locale.getDefault(), "%.1f L", displayLitres)
        progressBarConsumption.progress = percent.coerceAtMost(100)

        if (currentMl >= goalMl) {
            val exceed = currentMl - goalMl
            if (exceed > 0) {
                tvProgressPercent.text = "Objectif dépassé de $exceed ml !"
            } else {
                tvProgressPercent.text = "Objectif atteint !"
            }
        } else {
            tvProgressPercent.text = "$percent % de l’objectif atteint"
        }
    }
}
