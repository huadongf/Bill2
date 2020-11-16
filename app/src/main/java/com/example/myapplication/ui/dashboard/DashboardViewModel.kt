package com.example.myapplication.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _year = MutableLiveData<String>().apply {
        value = "2020"
    }
    private val _month = MutableLiveData<String>().apply {
        value = "11"
    }
    private val _day = MutableLiveData<String>().apply {
        value = "13"
    }
    val month: LiveData<String> = _month
    val year: LiveData<String> = _year
    val day: LiveData<String> = _day
    fun changeyear(a:String){
        _year.value=a
    }
    fun changemonth(a:String){
        _month.value=a
    }
    fun changeday(a:String){
        _day.value=a
    }
}