package com.example.waterreminder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class TipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tips, container, false)

        val cardTip1 = view.findViewById<MaterialCardView>(R.id.cardTip1)
        val cardTip2 = view.findViewById<MaterialCardView>(R.id.cardTip2)
        val cardTip3 = view.findViewById<MaterialCardView>(R.id.cardTip3)
        val cardTip4 = view.findViewById<MaterialCardView>(R.id.cardTip4)
        val cardArticle1 = view.findViewById<MaterialCardView>(R.id.cardArticle1)
        val btnShare = view.findViewById<MaterialButton>(R.id.btnShare)

        val cards = listOf(cardTip1, cardTip2, cardTip3, cardTip4, cardArticle1)
        for ((index, card) in cards.withIndex()) {
            card.alpha = 0f
            card.translationY = 20f
            card.animate().alpha(1f).translationY(0f).setStartDelay((index * 80).toLong()).duration = 300
        }

        for (card in cards) {
            card.setOnClickListener {
                card.animate().scaleX(1.02f).scaleY(1.02f).setDuration(150).withEndAction {
                    card.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                }.start()
            }
        }

        btnShare.setOnClickListener {
            btnShare.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                btnShare.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        return view
    }
}
