package com.example.companiesapplication.domian.use_cases

import com.example.companiesapplication.shared.ItemEvent
import com.example.companiesapplication.shared.ItemModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.junit.After
import org.junit.Before
import org.junit.Test


class AddNewItemTest{

   private lateinit var addNewItem:AddNewItem
   private lateinit var list:MutableList<ItemModel>
   private lateinit var itemToAdd:ItemModel

   @Before
   fun setup(){
       addNewItem = AddNewItem()
       itemToAdd = ItemModel(0, "new item", "applied", false)
       list = mutableListOf(ItemModel(1,"name 1", "applied", false))
   }




}