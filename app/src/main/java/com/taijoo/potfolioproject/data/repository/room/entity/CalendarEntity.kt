package com.taijoo.potfolioproject.data.repository.room.entity

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Calendar")
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true) val cal_seq : Long,
    @ColumnInfo(name ="memo_seq") val memo_seq : Long,
    @ColumnInfo(name ="year" )val year : Int,
    @ColumnInfo(name ="month")val month : Int,
    @ColumnInfo(name ="day")val day : Int,
    @ColumnInfo(name ="date")val date : String){

    constructor(): this(0, 0,0,0,0,"")
}
