package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _zhichu = MutableLiveData<String>().apply {
    }
    private val _riyong = MutableLiveData<String>().apply {
    }
    private val _qita = MutableLiveData<String>().apply {
    }
    private val _eat = MutableLiveData<String>().apply {
    }
    private val _game = MutableLiveData<String>().apply {
    }
    private val _study = MutableLiveData<String>().apply {
    }
    private val _yiliao = MutableLiveData<String>().apply {
    }
    private val _fushi = MutableLiveData<String>().apply {
    }
    private val _jiaotong = MutableLiveData<String>().apply {
    }
    val zhichu: LiveData<String> = _zhichu
    val game :LiveData<String> = _game
    val study:LiveData<String> = _study
    val yiliao:LiveData<String> = _yiliao
    val fushi:LiveData<String> = _fushi
    val jiaotong:LiveData<String> = _jiaotong
    val riyong: LiveData<String> = _riyong
    val eat: LiveData<String> = _eat
    val qitaa: LiveData<String> = _qita
    fun changezhichu(a:String){
        _zhichu.value=a
    }
    fun changeriyong(a:String){
        _riyong.value=a
    }
    fun changeqita(a:String){
        _qita.value=a
    }
    fun changeeat(a:String){
        _eat.value=a
    }
    fun changegame(a:String){
        _game.value=a
    }
    fun changestudy(a:String){
        _study.value=a
    }
    fun changeyiliao(a:String){
        _yiliao.value=a
    }
    fun changefushi(a:String){
        _fushi.value=a
    }
    fun changejiaotong(a:String){
        _jiaotong.value=a
    }
}