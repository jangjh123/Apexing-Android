package jyotti.apexing.apexing_android.ui.fragment.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jyotti.apexing.apexing_android.data.model.store.StoreItem
import jyotti.apexing.apexing_android.data.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(private val repository: StoreRepository) : ViewModel() {
    private val storeData = MutableLiveData<List<StoreItem>>()

    fun getStoreLiveData() = storeData


    init {
        getStoreData()
    }

    private fun getStoreData() {
        repository.fetchStoreData(
            onSuccess = {
                storeData.postValue(it)
            },
            onError = {
                getStoreData()
            },
            onFailure = {
                getStoreData()
            }
        )
    }
}