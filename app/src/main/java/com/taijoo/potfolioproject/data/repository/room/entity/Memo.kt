package com.taijoo.potfolioproject.data.repository.room.entity

import androidx.room.*
import com.taijoo.potfolioproject.data.repository.room.Converters.UserConverters

@Entity(tableName = "Memo" , ignoredColumns = ["icon_delete_check"])
data class Memo(
        @PrimaryKey(autoGenerate = true) var icon_seq : Long,
        @ColumnInfo(name ="user_seq") var user_seq: Int,
        @ColumnInfo(name = "icon_color_position") var icon_color_position : Int,
        @ColumnInfo(name = "icon_position") var icon_position : Int,
        @ColumnInfo(name = "icon_delete_type") var icon_delete_type : Int,
        @ColumnInfo(name = "memo_title") var memo_title : String,
        @ColumnInfo(name = "memo_content") var memo_content : String,
        @ColumnInfo(name = "date") var date : String):MemoCheck(){

    constructor(user_seq:Int,icon_color_position: Int,icon_position: Int,icon_delete_type: Int,
                memo_title: String,memo_content: String,date: String)

            : this(0,user_seq,icon_color_position,icon_position,icon_delete_type,memo_title,memo_content,date)

}

open class MemoCheck{
    var icon_delete_check = 0
}
