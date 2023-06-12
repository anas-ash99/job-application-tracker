package com.example.companiesapplication.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.example.companiesapplication.shared.ItemModel
import com.example.companiesapplication.domian.MainViewModel
import com.example.companiesapplication.R

@SuppressLint("CutPasteId")
class ItemDialog(
    private val activity: MainActivity,
    private val viewModel: MainViewModel,
) {

    private val dialog =Dialog(activity)
    private var nameET:EditText
    private var statusET:EditText
    private var cancelButton:CardView
    private var doneButton:CardView
    private var deleteButton:CardView
    private val statusLayout:LinearLayout
    private val statusList:LinearLayout
    private val appliedText:TextView
    private val rejectedText:TextView
    private val interviewText:TextView
    private val selectedStatusText:TextView
    private val isStatusListClicked by lazy {
        MutableLiveData(false)
    }

    private var selectedStatus = ""

    init {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.CENTER)
        nameET = dialog.findViewById(R.id.name)
        statusET = dialog.findViewById(R.id.status)
        deleteButton = dialog.findViewById(R.id.deleteButton)
        cancelButton = dialog.findViewById(R.id.cancel_button)
        doneButton = dialog.findViewById(R.id.doneButton)
        statusLayout = dialog.findViewById(R.id.statusLayout)
        statusList = dialog.findViewById(R.id.statusList)
        interviewText = dialog.findViewById(R.id.interview)
        rejectedText = dialog.findViewById(R.id.rejected)
        appliedText = dialog.findViewById(R.id.applied)
        selectedStatusText = dialog.findViewById(R.id.selectedStatusText)
        showStatusList()
        onButtonClicks()
        onDeleteClick()
        observeStatusList()
        chooseStatus()
        dialog.setOnDismissListener {
            viewModel.currentDialogItem = ItemModel()
        }
    }

    private fun onDeleteClick() {
        deleteButton.setOnClickListener {
            viewModel.deleteItem()
            dialog.dismiss()
        }
    }

    private fun chooseStatus() {
        rejectedText.setOnClickListener {
            selectedStatus = "Rejected"
            selectedStatusText.text = "Rejected"
            isStatusListClicked.value = false
        }
        appliedText.setOnClickListener {
            selectedStatus = "Applied"
            selectedStatusText.text = "Applied"
            isStatusListClicked.value = false
        }

        interviewText.setOnClickListener {
            selectedStatus = "Interview"
            selectedStatusText.text = "Interview"
            isStatusListClicked.value = false
        }
    }

    private fun showStatusList() {
       statusLayout.setOnClickListener {
           isStatusListClicked.value = isStatusListClicked.value == false
       }
    }


    private fun observeStatusList(){
        isStatusListClicked.observe(activity){
            if (it){
                statusList.visibility = View.VISIBLE
            }else{
                statusList.visibility = View.GONE
            }
        }
    }

    fun showDialog(){
        if (viewModel.currentDialogItem.name.isBlank()){
            deleteButton.visibility =View.GONE
        }else{
            deleteButton.visibility = View.VISIBLE
        }
        nameET.setText(viewModel.currentDialogItem.name)
        nameET.setSelection(viewModel.currentDialogItem.name.length)
        selectedStatus = viewModel.currentDialogItem.status
        selectedStatusText.text = viewModel.currentDialogItem.status
        isStatusListClicked.value = false
        dialog.show()

   }

    private fun onButtonClicks() {
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        doneButton.setOnClickListener {
            viewModel.currentDialogItem.name = nameET.text.toString().trim()
            viewModel.currentDialogItem.status = selectedStatus
            if(viewModel.validateDoneClick()){
                viewModel.addItem()
                dialog.dismiss()
            }else{
                Toast.makeText(activity, "please enter valid values", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun takeInput(){
      nameET.addTextChangedListener {
          viewModel.currentDialogItem.name = it?.toString()?.trim()!!
      }

      statusET.addTextChangedListener {
          viewModel.currentDialogItem.status = it?.toString()?.trim()!!
      }
  }




}