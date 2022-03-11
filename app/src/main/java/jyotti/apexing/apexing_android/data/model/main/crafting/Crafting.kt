package jyotti.apexing.apexing_android.data.model.main.crafting

import com.google.gson.annotations.SerializedName

data class Crafting(
    @SerializedName(value = "bundleContent")
    val bundleContents: List<BundleContent>)
