package com.example.companiesapplication.domian.use_cases

import com.example.companiesapplication.shared.models.ItemModel
import org.junit.Before


class AddNewItemTest{

   private lateinit var addNewItem:AddNewItem
   private lateinit var list:MutableList<ItemModel>
   private lateinit var itemToAdd: ItemModel

   @Before
   fun setup(){
       addNewItem = AddNewItem()
       itemToAdd = ItemModel(0, "new item", "applied", false)
       list = mutableListOf(ItemModel(1,"name 1", "applied", false))
   }




}