package com.taijoo.potfolioproject.data.model

import androidx.annotation.Keep

@Keep
class SettingData{
    var view_type : Int = 0
    var icon_type : Int = 0
    var title : String = ""
    var img : Int = 0
    var networkState : Boolean = false

    //icon_type = 0 : 프로필,이름변경 , 1: 서버연결
    constructor(view_type : Int , icon_type : Int,  title : String ,  img : Int){
        this.view_type = view_type
        this.icon_type = icon_type
        this.title = title
        this.img = img
    }

    constructor(view_type : Int , icon_type : Int,  title : String ,  img : Int , networkState : Boolean){
        this.view_type = view_type
        this.icon_type = icon_type
        this.title = title
        this.img = img
        this.networkState = networkState
    }
}

