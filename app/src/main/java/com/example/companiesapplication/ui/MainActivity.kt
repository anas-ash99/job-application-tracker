package com.example.companiesapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.companiesapplication.R
import com.example.companiesapplication.databinding.ActivityMainBinding
import com.example.companiesapplication.domian.MainViewModel
import com.example.companiesapplication.shared.extention_funtions.SetSpanIndex.setSpanIndex
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, MainFragment(), "fragment")
            commit()
        }



    }

    override fun onBackPressed() {
        if (viewModel.isSearchClick.value!!){
            viewModel.isSearchClick.value = false
        }else{
            super.onBackPressed()
        }
    }
}