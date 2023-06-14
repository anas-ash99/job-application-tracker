package com.example.companiesapplication.domian.use_cases

import android.util.Log
import com.example.companiesapplication.shared.ItemEvent
import com.example.companiesapplication.shared.ItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AddNewItem {


    fun invoke(companiesList:MutableList<ItemModel>, currentDialogItem:ItemModel):Flow<ItemEvent> = flow{


        try {
            if(companiesList.isEmpty()){
                currentDialogItem.id = 1
                companiesList.add(currentDialogItem)

                emit(ItemEvent.AddItem(currentDialogItem))
            }else{
                //////// add new item to not empty list
                if (!companiesList.any { it.id == currentDialogItem.id  }){
                    currentDialogItem.id = companiesList[companiesList.size - 1].id + 1
                    companiesList.add(currentDialogItem)
                    emit(ItemEvent.AddItem(currentDialogItem))
                }else{
                    //////// update exciting item
                    companiesList.onEachIndexed() {i, it->
                        if (it.id == currentDialogItem.id){
                            it.name = currentDialogItem.name
                            it.status = currentDialogItem.status
                            emit(ItemEvent.UpdateItem(i, it))

                        }
                    }
                }

            }
        }catch (e:Exception){
            emit(ItemEvent.Error(e))
            Log.e("add item", e.message, e)
        }


    }


}