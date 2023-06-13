package com.example.companiesapplication.domian



import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companiesapplication.domian.use_cases.AddNewItem
import com.example.companiesapplication.domian.use_cases.DeleteItemUseCase
import com.example.companiesapplication.shared.DataState
import com.example.companiesapplication.shared.ItemEvent
import com.example.companiesapplication.shared.ItemModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val addNewItemUseCase: AddNewItem,
    private val deleteItemUseCase: DeleteItemUseCase
): ViewModel() {


    val searchItems by lazy {
        MutableLiveData<MutableList<ItemModel>>(mutableListOf())
    }
    var searchItemsIndex = mutableListOf<Int>()
    val companiesList by lazy {
        MutableLiveData<MutableList<ItemModel>>()
    }
    val isSearchClick by lazy {
        MutableLiveData(false)
    }
    var currentDialogItem = ItemModel()
    private lateinit var sharedPreferences:SharedPreferences
    val itemEvent by lazy {
        MutableLiveData<ItemEvent>()
    }

    val _items by lazy {
        MutableLiveData<DataState<MutableList<ItemModel>>>()
    }
    var currentSearchPosition = 0

   fun init(sharedPreferences: SharedPreferences){
       this.sharedPreferences = sharedPreferences
       companiesList.value = mutableListOf()
       getItems()
   }



    fun validateDoneClick():Boolean{
        var isValid = true
        if (currentDialogItem.name == "" || currentDialogItem.status == ""){
            isValid = false
        }
        return isValid
    }

    fun addItem(){
        addNewItemUseCase.invoke(companiesList.value!!, currentDialogItem).onEach {
            itemEvent.value = it
        }.launchIn(viewModelScope)
    }

    fun handleSearch(string:String):MutableList<ItemModel> {
        currentSearchPosition = 0
        searchItemsIndex = mutableListOf()
        searchItems.value = mutableListOf()
       companiesList.value?.onEachIndexed { index, item ->

           if (item.name.lowercase().contains(string.lowercase())){
               searchItemsIndex.add(index)
//               item.isHighLighted = true
               searchItems.value!!.add(ItemModel(item.id, item.name, item.status, true))
           }else{
               searchItems.value!!.add(item)
           }
       }



        return searchItems.value!!
    }

    fun updateItems() {
       viewModelScope.launch {
           repository.updateItems(companiesList.value!!)
       }
    }



    private fun getItems() {
          viewModelScope.launch {
              repository.getItems().onEach {
                  _items.value = it
              }.launchIn(viewModelScope)
          }

    }

    fun deleteItem() {
       deleteItemUseCase.invoke(companiesList.value!!, currentDialogItem).onEach {
           itemEvent.value = it
       }.launchIn(viewModelScope)

    }
    fun copyToClipboard(context: Context, text: CharSequence){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied name to clipboard", Toast.LENGTH_SHORT).show()
    }
}


