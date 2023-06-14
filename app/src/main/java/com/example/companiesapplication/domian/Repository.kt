package com.example.companiesapplication.domian

import com.example.companiesapplication.shared.DataState
import com.example.companiesapplication.shared.models.ItemModel
import kotlinx.coroutines.flow.Flow


interface Repository {

    suspend fun getItems():Flow<DataState<MutableList<ItemModel>>>
    suspend fun updateItems(list:MutableList<ItemModel>)

}