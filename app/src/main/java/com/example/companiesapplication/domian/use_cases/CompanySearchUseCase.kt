package com.example.companiesapplication.domian.use_cases

import com.example.companiesapplication.shared.ItemModel
import com.example.companiesapplication.shared.SearchEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CompanySearchUseCase {


    fun invoke(string:String,companiesList:MutableList<ItemModel>, searchItemsIndex:MutableList<Int>):Flow<SearchEvent> = flow{

        emit(SearchEvent.Loading)
        try {
            var isFoundAny = false

            searchItemsIndex.removeAll(searchItemsIndex)
            if (string.isBlank()){
                companiesList.onEach {
                    it.isHighLighted = false
                }
            }else{
                companiesList.onEachIndexed { index, item ->
                    if (item.name.lowercase().contains(string.lowercase())){
                        searchItemsIndex.add(index)
                        item.isHighLighted = true
                        isFoundAny = true
                    }else{
                        item.isHighLighted = false
                    }
                }

            }
            if (isFoundAny){

                emit(SearchEvent.Found)
            }else{
                emit(SearchEvent.NotFound)
            }

        }catch (e:Exception){
            emit(SearchEvent.Error(e))
        }


    }
}