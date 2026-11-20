package com.example.waterreminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WaterIntakeAdapter(private val intakes: List<WaterIntake>) :
    RecyclerView.Adapter<WaterIntakeAdapter.IntakeViewHolder>() {

    class IntakeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIntakeTime: TextView = view.findViewById(R.id.tvIntakeTime)
        val tvIntakeAmount: TextView = view.findViewById(R.id.tvIntakeAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntakeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_water_intake, parent, false)
        return IntakeViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntakeViewHolder, position: Int) {
        val intake = intakes[position]
        holder.tvIntakeTime.text = intake.time
        holder.tvIntakeAmount.text = "+ ${intake.amountMl} ml"
    }

    override fun getItemCount(): Int = intakes.size
}
