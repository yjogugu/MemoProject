package com.taijoo.potfolioproject.data.model

import androidx.annotation.Keep
import com.taijoo.potfolioproject.data.repository.room.entity.User

@Keep
class UserData {
    var id : Long = 0
    var type : Int = 0
    var user : User? = null


    constructor(id:Long , type : Int){
        this.id = id
        this.type = type
    }

    constructor(id:Long , type : Int , user: User? ){
        this.id = id
        this.type = type
        this.user = user
    }
}
