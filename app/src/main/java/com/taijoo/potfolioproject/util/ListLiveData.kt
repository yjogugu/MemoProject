package com.taijoo.potfolioproject.util

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<MutableList<T>>() {
    private val temp = mutableListOf<T>()

    init {
        value = temp
    }

    fun add(item: T) {
        temp.add(item)
        value = temp
    }

    fun add(index : Int,item: T) {
        temp.add(index,item)
        value = temp
    }

    fun addAll(items: List<T>) {
        temp.addAll(items)
        value = temp
    }

    fun remove(item: T) {
        temp.remove(item)
        value = temp
    }

    fun remove(item: Int) {
        temp.removeAt(item)
        value = temp
    }

    fun set(index: Int ,item : T ){
        temp.set(index,item)
        value = temp
    }

    fun clear() {
        temp.clear()
        value = temp
    }

    fun size() : Int{
        return temp.size
    }

}