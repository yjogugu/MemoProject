package com.taijoo.potfolioproject.util

class ColorDialogItem{
    var position :Int = 0
    var color : String? = null
    var check : Boolean = false


    constructor(position :Int , color :String){
        this.position = position
        this.color = color
    }

}