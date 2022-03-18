package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.databinding.FragmentCustomDialogBinding

class CustomDialogFragment(
    private val text: String,
    private val buttonText: String,
    private val onClickButton: () -> Unit
) :
    DialogFragment() {
    private lateinit var binding: FragmentCustomDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_custom_dialog, container, false)
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
        binding.tvText.text = text
        binding.tvBtnText.text = buttonText
        binding.btnDialog.setOnClickListener {
            onClickButton()
        }
    }
}