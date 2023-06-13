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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companiesapplication.*
import com.example.companiesapplication.databinding.FragmentMainBinding
import com.example.companiesapplication.domian.MainViewModel
import com.example.companiesapplication.shared.DataState
import com.example.companiesapplication.shared.ItemEvent
import com.example.companiesapplication.shared.ItemModel
import dagger.hilt.android.AndroidEntryPoint


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
            println(viewModel.currentSearchPosition)
            println(viewModel.searchItemsIndex)
            if (viewModel.currentSearchPosition >= 0 && viewModel.currentSearchPosition < viewModel.searchItemsIndex.size && viewModel.searchItemsIndex.isNotEmpty()){
                initRecyclerView(viewModel.searchItems.value!!)
                binding.recyclerView.scrollToPosition(viewModel.searchItemsIndex[viewModel.currentSearchPosition])
//                adapter.updateItem(viewModel.searchItemsIndex[viewModel.currentSearchPosition])
                viewModel.currentSearchPosition += 1
            }else{
                Toast.makeText(requireContext(), "not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.arrowUp.setOnClickListener {
            println(viewModel.currentSearchPosition)
            if (viewModel.currentSearchPosition >  0 && viewModel.searchItemsIndex.isNotEmpty()){
                viewModel.currentSearchPosition -= 1
                binding.recyclerView.scrollToPosition(viewModel.searchItemsIndex[viewModel.currentSearchPosition])
            }else{
                Toast.makeText(requireContext(), "not found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun handleSearch() {
        binding.SearchEditText.addTextChangedListener {
            if (it?.toString()?.trim()?.isBlank()!!){
                initRecyclerView(viewModel.companiesList.value!!)
            }else{
                viewModel.handleSearch(it?.toString()?.trim()!!)
//                initRecyclerView(viewModel.handleSearch(it?.toString()?.trim()!!))
            }
        }
    }

    private fun onSearchClick() {
        binding.searchButton.setOnClickListener {
            viewModel.isSearchClick.value = true
        }
        binding.arrowBack.setOnClickListener {
            viewModel.isSearchClick.value = false
            binding.SearchEditText.setText("")
        }
    }

    private fun observeSearchClick() {
        viewModel.isSearchClick.observe(requireActivity()){
            binding.isSearchClick = it
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
                  binding.recyclerView.scrollToPosition(viewModel.companiesList.value?.size!! -1)
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