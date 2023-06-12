package com.example.companiesapplication.shared

import java.util.UUID

data class ItemModel(
    var id:Int = 0,
    var name:String = "",
    var status:String = "Applied"
):java.io.Serializable
