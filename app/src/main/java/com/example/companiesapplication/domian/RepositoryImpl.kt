package com.example.companiesapplication.domian

import android.content.SharedPreferences
import android.util.Log
import com.example.companiesapplication.shared.DataState
import com.example.companiesapplication.shared.ItemModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
        ):Repository {

    override suspend fun getItems(): Flow<DataState<MutableList<ItemModel>>> = flow {
        emit(DataState.Loading)
        try {
            val gson = Gson()
            val count = sharedPreferences.getInt("count", 0)
            val list = mutableListOf<ItemModel>()
            (1 .. count).onEach {
                val json:String? = sharedPreferences.getString("item $it", "")
                json?.let {
                    list.add(gson.fromJson(json, ItemModel::class.java))
                }

            }

            emit(DataState.Success(list))

        }catch (e:Exception){
           DataState.Error(e)
            Log.e("get items", e.message, e)
        }
    }

    override suspend fun updateItems(list:MutableList<ItemModel>) {
        try {
            val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
            val gson = Gson()
            prefsEditor.putInt("count", list.size)
            list.onEach {
                val json = gson.toJson(it)
                prefsEditor.putString("item ${it.id}", json)
                prefsEditor.apply()
            }
        }catch (e:Exception){
            Log.e("get items", e.message, e)
        }
    }


}