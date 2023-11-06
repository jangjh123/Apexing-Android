package jyotti.apexing.apexing_android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import jyotti.apexing.apexing_android.ui.component.ProgressFragment
import java.lang.Exception

abstract class BaseActivity<VB : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    lateinit var binding: VB
    private val progressFragment = ProgressFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        init()
        startProcess()
    }

    protected open fun isProgressShowing(): Boolean {
        return progressFragment.isVisible
    }

    protected open fun setObservers() {

    }

    protected open fun init() {
        initViewDataBinding()
    }

    protected open fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected open fun startProcess() {

    }

    protected open fun showProgress() {
        if (!progressFragment.isAdded) {
            try {
                progressFragment.show(supportFragmentManager, "progress")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected open fun dismissProgress() {
        if (progressFragment.isAdded) {
            try {
                progressFragment.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
    }
}