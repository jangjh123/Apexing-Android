package jyotti.apexing.apexing_android.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import jyotti.apexing.apexing_android.BuildConfig.KEY_API
import jyotti.apexing.apexing_android.data.model.store.StoreItem
import jyotti.apexing.apexing_android.data.remote.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StoreRepository @Inject constructor(val firebaseDatabase: FirebaseDatabase) {
    inline fun fetchStoreData(
        crossinline onSuccess: (List<StoreItem>) -> Unit,
        crossinline onError: () -> Unit,
        crossinline onFailure: () -> Unit
    ) {
//        networkManager.getClient().fetchStoreData(KEY_API).enqueue(object :
//            Callback<List<StoreItem>> {
//            override fun onResponse(
//                call: Call<List<StoreItem>>,
//                response: Response<List<StoreItem>>
//            ) {
//                when (response.code()) {
//                    200 -> {
//                        onSuccess(response.body()!!)
//                    }
//                    else -> {
//                        onError()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<StoreItem>>, t: Throwable) {
//                onFailure()
//            }
//
//        })
//        databaseRef.child("Store").
    }
}