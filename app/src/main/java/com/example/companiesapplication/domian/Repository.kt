package com.example.companiesapplication.domian

import android.content.SharedPreferences
import com.example.companiesapplication.shared.DataState
import com.example.companiesapplication.shared.ItemModel
import kotlinx.coroutines.flow.Flow


interface Repository {

    suspend fun getItems():Flow<DataState<MutableList<ItemModel>>>
    suspend fun updateItems(list:MutableList<ItemModel>)

}