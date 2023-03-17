package com.lee.covidmap.data.model.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CovidCenterResultDTO(
    val currentCount: Int,
    @SerializedName("data")
    val list : List<Center>,
    val matchCount: Int,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)

@Keep
data class Center(
    val address: String,
    val centerName: String,
    val centerType: String,
    val createdAt: String,
    val facilityName: String,
    val id: Int,
    val lat: String,
    val lng: String,
    val org: String,
    val phoneNumber: String,
    val sido: String,
    val sigungu: String,
    val updatedAt: String,
    val zipCode: String
)