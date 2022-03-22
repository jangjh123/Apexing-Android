package jyotti.apexing.apexing_android.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.databinding.FragmentSuggestDialogBinding
import jyotti.apexing.apexing_android.util.Utils

class SuggestDialogFragment(
    private val text: String,
    private inline val onClickConfirm: () -> Unit
) :
    DialogFragment() {
    private lateinit var binding: FragmentSuggestDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_suggest_dialog, container, false)
        isCancelable = true
        binding.root.background = ColorDrawable(Color.TRANSPARENT)

        initView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setBackgroundTransparent()
    }

    private fun setBackgroundTransparent() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initView() {
        with(binding) {
            tvText.text = text

            btnCancel.setOnClickListener {
                dismiss()
            }

            btnConfirm.setOnClickListener {
                onClickConfirm()
                dismiss()
            }
        }
    }
}