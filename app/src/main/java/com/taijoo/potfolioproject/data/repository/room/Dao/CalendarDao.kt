package com.taijoo.potfolioproject.data.repository.room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taijoo.potfolioproject.data.model.CalendarData
import com.taijoo.potfolioproject.data.model.CalendarMemoData
import com.taijoo.potfolioproject.data.repository.room.entity.CalendarEntity
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendarEntity: CalendarEntity)

    @Query("SELECT DISTINCT Memo.icon_seq,Memo.memo_title,Memo.memo_content,Memo.icon_color_position,Calendar.year,Calendar.month,Calendar.day " +
            "FROM Calendar LEFT JOIN Memo ON Calendar.memo_seq = Memo.icon_seq WHERE Calendar.year = :year AND Calendar.month = :month AND Calendar.day = :day ORDER BY Calendar.date DESC")
    fun selectMemo(year : Int , month : Int , day : Int): List<CalendarMemoData>

    @Query("DELETE FROM Calendar WHERE Calendar.year = :year AND Calendar.month = :month AND Calendar.day = :day AND memo_seq = :memo_seq")
    suspend fun deleteCalendar(year : Int , month : Int , day : Int ,memo_seq:Int)

    @Query("SELECT DISTINCT Calendar.day , Calendar.year ,Calendar.month FROM Calendar ORDER BY year ASC , month ASC , day ASC")
    fun selectCalendar() : Flow<List<CalendarData>>

}