package jyotti.apexing.apexing_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import jyotti.apexing.apexing_android.ui.component.ProgressFragment

abstract class BaseFragment<VB : ViewDataBinding>(private val layoutId: Int) : Fragment() {
    lateinit var binding: VB
    private val progressFragment = ProgressFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setObservers()
        showProgress()
        startProcess()

        return binding.root
    }

    protected open fun showProgress() {
        try {
            progressFragment.show(childFragmentManager, "progress")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun dismissProgress() {
        try {
            progressFragment.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    protected open fun isProgressShowing(): Boolean {
        return progressFragment.isVisible
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
    }

    protected open fun setObservers() {

    }

    protected open fun setOnFailureView(failureView: View, successView: View) {
        failureView.visibility = View.VISIBLE
        successView.visibility = View.GONE
        dismissProgress()
    }

    protected open fun setOnSuccessView(successView: View, failureView: View) {
        successView.visibility = View.VISIBLE
        failureView.visibility = View.GONE
    }

    protected open fun onClickRetryButton(successView: View, failureView: View) {
        setOnSuccessView(successView, failureView)
        showProgress()
        startProcess()
    }

    protected open fun startProcess() {
    }
}