package com.taijoo.potfolioproject.data.repository.room.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.taijoo.potfolioproject.data.repository.room.Converters.UserConverters

@Keep
@Entity(tableName = "User")
data class User (
        @PrimaryKey var user_seq : Int,
        @ColumnInfo(name ="user_name") var user_name: String,
        @ColumnInfo(name = "profile") var profile : String,
        @ColumnInfo(name = "user_register_name") var user_register_name : String,
        @ColumnInfo(name = "email") var email : String,
        @ColumnInfo(name = "network_state") var network_state : Boolean)  {

    constructor(): this(0, "","","","",false)

}




