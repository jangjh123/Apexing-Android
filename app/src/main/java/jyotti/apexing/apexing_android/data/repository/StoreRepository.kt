package jyotti.apexing.apexing_android.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import jyotti.apexing.apexing_android.data.model.store.Payment
import jyotti.apexing.apexing_android.data.model.store.StoreItem
import javax.inject.Inject

class StoreRepository @Inject constructor(val firebaseDatabase: FirebaseDatabase) {

    val curTime = System.currentTimeMillis() / 1000L

    inline fun fetchStoreData(
        crossinline onSuccess: (List<StoreItem>) -> Unit,
    ) {
        firebaseDatabase.getReference("Store").get().addOnSuccessListener {
            val list = ArrayList<StoreItem>()
            it.children.forEach { storeItem ->
                if (storeItem.child("expireTimeStamp").value.toString().toLong() > curTime) {
                    list.add(
                        StoreItem(
                            title = storeItem.child("title").value.toString(),
                            expireTimeStamp = storeItem.child("expireTimeStamp").value.toString().toLong(),
                            shopTime = storeItem.child("shopTime").value.toString(),
                            pricing = ArrayList<Payment>().apply {
                                if (storeItem.child("pricing1").child("quantity")
                                        .value.toString() != "0") {
                                    add(
                                        Payment(
                                            ref = storeItem.child("pricing1")
                                                .child("ref").value.toString(),
                                            quantity = storeItem.child("pricing1").child("quantity")
                                                .getValue<Int>()!!
                                        )
                                    )
                                }

                                if (storeItem.child("pricing2").child("quantity")
                                        .value.toString() != "0"
                                ) {
                                    add(
                                        Payment(
                                            ref = storeItem.child("pricing2")
                                                .child("ref").value.toString(),
                                            quantity = storeItem.child("pricing2").child("quantity")
                                                .getValue<Int>()!!
                                        )
                                    )
                                }
                            },
                            asset = storeItem.child("asset").value.toString(),
                        )
                    )
                }
            }

            onSuccess(list)
        }
    }
}