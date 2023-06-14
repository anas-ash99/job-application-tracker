package com.example.companiesapplication.shared

sealed class SearchEvent{

    object NotFound:SearchEvent()
    object Found: SearchEvent()
    data class Error(val e:Exception):SearchEvent()
    object Loading:SearchEvent()
}
