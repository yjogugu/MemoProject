package com.taijoo.potfolioproject.util

import java.lang.Exception

class ArrayListUtil<T : Any?> {

    fun arrayRemove(arrayList: ArrayList<T>) : ArrayList<T>{
        val array = ArrayList<T>()

        for (i in 0 until arrayList.size){
            if(i != 0 && i != 1){
                array.add(arrayList[i])
            }
        }
        arrayList.removeAll(array)

        return arrayList
    }


}