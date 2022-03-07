package jyotti.apexing.apexing_android.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun postValue(t: T?) {
        pending.set(true)
        super.postValue(value)
    }

    fun call() {
        postValue(null)
    }
    companion object {
        private val TAG = "SingleLiveEvent"
    }
}