package com.example.companiesapplication.domian.use_cases

import android.util.Log
import com.example.companiesapplication.shared.ItemEvent
import com.example.companiesapplication.shared.models.ItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteItemUseCase {

    fun invoke(list: MutableList<ItemModel>, currentDialogItem: ItemModel):Flow<ItemEvent> = flow {
        try {

            val index = list.indexOf(currentDialogItem)
            emit(ItemEvent.DeleteItem(index))
            list.removeAt(index)
            list.onEachIndexed { index1, item ->
                if (index1 >= index){
                    item.id = index1 + 1
                    emit(ItemEvent.UpdateItem(index1, item))
                }
            }
        }catch (e:Exception){
            emit(ItemEvent.Error(e))
            Log.e("delete item", e.message, e)
        }
    }
}