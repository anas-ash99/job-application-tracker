package com.example.companiesapplication.shared.extention_funtions

import com.example.companiesapplication.shared.models.SpanIndex

object SetSpanIndex {

    fun String.setSpanIndex(span:String):SpanIndex{
        var start = this.lowercase().indexOf(span.lowercase())
        var end = start+ span.length
        return SpanIndex(start, end)
    }
}