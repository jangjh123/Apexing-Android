package jyotti.apexing.apexing_android.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import jyotti.apexing.apexing_android.R
import jyotti.apexing.apexing_android.databinding.FragmentItemDetailBinding

class ItemDetailFragment(
    private val name: String,
    private val imageUrl: String
) : DialogFragment() {
    private lateinit var binding: FragmentItemDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_item_detail, container, false)
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
            tvItemName.text = name
            Glide.with(binding.root)
                .load(imageUrl)
                .into(ivItemDetail)

            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}