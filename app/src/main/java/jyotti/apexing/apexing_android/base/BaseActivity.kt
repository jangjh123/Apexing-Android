package jyotti.apexing.apexing_android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import jyotti.apexing.apexing_android.ui.component.DialogFragment
import java.lang.Exception

abstract class BaseActivity<VB : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    lateinit var binding: VB
    private val progressFragment = DialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        init()
        startProcess()
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
        progressFragment.show(supportFragmentManager, "progress")
    }

    protected open fun dismissProgress() {
        try {
            progressFragment.dismiss()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgress()
    }
}