package com.example.waterreminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnboardingAdapter(private val items: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIllustration: ImageView = view.findViewById(R.id.ivIllustration)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)

        fun bind(item: OnboardingItem) {
            ivIllustration.setImageResource(item.imageResId)
            tvTitle.text = item.title
            tvDescription.text = item.description

            // Subtle fade-in & translation animation on item bind
            ivIllustration.alpha = 0f
            ivIllustration.translationY = 40f
            ivIllustration.animate().alpha(1f).translationY(0f).duration = 400

            tvTitle.alpha = 0f
            tvTitle.translationY = 30f
            tvTitle.animate().alpha(1f).translationY(0f).setStartDelay(100).duration = 400

            tvDescription.alpha = 0f
            tvDescription.translationY = 30f
            tvDescription.animate().alpha(1f).translationY(0f).setStartDelay(200).duration = 400
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
