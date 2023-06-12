package com.example.companiesapplication.shared

sealed class ItemEvent{
    data class UpdateItem(val index:Int, val item: ItemModel): ItemEvent()
    data class AddItem(val item: ItemModel): ItemEvent()
    data class DeleteItem(val index:Int): ItemEvent()
    data class Error(val exception: Exception): ItemEvent()
    object Loading: ItemEvent()
}