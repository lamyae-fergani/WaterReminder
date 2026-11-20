package com.example.waterreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class AddWaterBottomSheet(private val onAmountSelected: (Int) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_water, container, false)

        view.findViewById<MaterialButton>(R.id.btnAdd150).setOnClickListener {
            onAmountSelected(150)
            dismiss()
        }
        view.findViewById<MaterialButton>(R.id.btnAdd250).setOnClickListener {
            onAmountSelected(250)
            dismiss()
        }
        view.findViewById<MaterialButton>(R.id.btnAdd330).setOnClickListener {
            onAmountSelected(330)
            dismiss()
        }
        view.findViewById<MaterialButton>(R.id.btnAdd500).setOnClickListener {
            onAmountSelected(500)
            dismiss()
        }

        return view
    }
}
