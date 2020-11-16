package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var Categroy:String, var Amount:Long,var Year: String,var Month:String,var Day:String,var Tupianid:Int,var id:Int,var times:String) {
    @PrimaryKey(autoGenerate = true)
    var idd: Long = 0
}
