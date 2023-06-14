package com.example.companiesapplication.ui


import android.content.Context

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companiesapplication.*
import com.example.companiesapplication.databinding.FragmentMainBinding
import com.example.companiesapplication.domian.MainViewModel
import com.example.companiesapplication.shared.DataState
import com.example.companiesapplication.shared.ItemEvent
import com.example.companiesapplication.shared.ItemModel
import com.example.companiesapplication.shared.KeyboardManger.hideSoftKeyboard
import com.example.companiesapplication.shared.KeyboardManger.showSoftKeyboard
import com.example.companiesapplication.shared.SearchEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var dialog: ItemDialog
    private lateinit var adapter: RV_Adapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModel.init(context?.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)!!)
        dialog = ItemDialog(requireActivity() as MainActivity, viewModel)
        observeList()
        onAddItemClick()
        observeEvent()
        observeSearchClick()
        onSearchClick()
        handleSearch()
        onArrowsClick()
        return binding.root
    }

    private fun onArrowsClick() {


        binding.arrowDown.setOnClickListener {
            if ( viewModel.searchItemsIndex.isNotEmpty() && viewModel.searchItemsIndex.size -1 > viewModel.currentSearchPosition){
                viewModel.currentSearchPosition += 1
               binding.recyclerView.smoothScrollToPosition(viewModel.searchItemsIndex[viewModel.currentSearchPosition])
            }else {
                Toast.makeText(requireContext(), "Not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.arrowUp.setOnClickListener {
            if (viewModel.currentSearchPosition > 0){
                viewModel.currentSearchPosition -= 1
                binding.recyclerView.smoothScrollToPosition(viewModel.searchItemsIndex[viewModel.currentSearchPosition])
            }else{
                Toast.makeText(requireContext(), "Not found", Toast.LENGTH_SHORT).show()
            }
        }

    }



    private fun handleSearch() {

         binding.imageView3.setOnClickListener {
             viewModel.currentSearchPosition = 0
           viewModel.handleSearch3(binding.SearchEditText.text?.toString()?.trim()!!)
             if (viewModel.searchItemsIndex.isNotEmpty()){
                 binding.recyclerView.smoothScrollToPosition(viewModel.searchItemsIndex[0])
                 hideSoftKeyboard(binding.SearchEditText, requireContext())
                 viewModel.searchItemsIndex.onEach {i->
                     adapter.updateItem(i)

                 }
             }
         }
        viewModel.searchEvent.observe(requireActivity()){
            when(it){
                is SearchEvent.Error -> {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    viewModel.searchEvent.value = null
                }
                SearchEvent.Found -> {

                    viewModel.searchEvent.value = null
                }
                SearchEvent.Loading -> {}
                SearchEvent.NotFound -> {
                    Toast.makeText(requireContext(), "Not found", Toast.LENGTH_SHORT).show()
                    viewModel.searchEvent.value = null
                }
            }
        }

    }

    private fun onSearchClick() {
        binding.searchButton.setOnClickListener {
            viewModel.isSearchClick.value = true
        }
        binding.arrowBack.setOnClickListener {
            viewModel.isSearchClick.value = false

        }
    }

    private fun observeSearchClick() {
        viewModel.isSearchClick.observe(requireActivity()){
            binding.isSearchClick = it
            if (it){
                showSoftKeyboard(binding.SearchEditText, requireContext())
            }else{
                hideSoftKeyboard(binding.SearchEditText, requireContext())
                viewModel.reInitCompaniesList()
                binding.SearchEditText.setText("")
                viewModel.searchItemsIndex.onEach {  i->
                    adapter.updateItem(i)
                }
            }
        }
    }

    private fun onAddItemClick() {
        binding.cardView.setOnClickListener {
            dialog.showDialog()
        }
    }


    private fun initRecyclerView(items:MutableList<ItemModel>) {
        adapter = RV_Adapter(items, ::onRVItemClick, ::onItemLongPress)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeList() {

        viewModel._items.observe(requireActivity()){

            when(it){
                is DataState.Error -> {}
                DataState.Loading -> {}
                is DataState.Success -> {
                    viewModel.companiesList.value = it.data
                    initRecyclerView(it.data)
                }
            }
        }

    }



    private fun onItemLongPress(item: ItemModel){
        viewModel.copyToClipboard(requireContext(), item.name)
    }

  private fun observeEvent(){
      viewModel.itemEvent.observe(requireActivity()){
          when(it){
              is ItemEvent.AddItem -> {
                  adapter.addItem(it.item)
                  binding.recyclerView.smoothScrollToPosition(viewModel.companiesList.value?.size!! -1)
                  viewModel.updateItems()
              }
              is ItemEvent.DeleteItem -> {
                  adapter.deleteItem(it.index)
                  viewModel.updateItems()
              }
              is ItemEvent.Error -> {
                  Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
              }
              is ItemEvent.UpdateItem -> {
                  adapter.updateItem(it.index)
                  viewModel.updateItems()}
              else -> {}
          }
      }

  }

    private fun onRVItemClick(item: ItemModel){
        viewModel.currentDialogItem.name = item.name
        viewModel.currentDialogItem  = item
        dialog.showDialog()
    }






}