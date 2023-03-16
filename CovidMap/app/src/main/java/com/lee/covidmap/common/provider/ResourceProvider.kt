package com.lee.covidmap.common.provider

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 리소스를 제공해주는 Provider class
 * **/
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context : Context
) {
    fun getString(id : Int) = context.getString(id)
}