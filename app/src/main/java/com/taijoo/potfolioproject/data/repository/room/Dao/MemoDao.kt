package com.taijoo.potfolioproject.data.repository.room.Dao

import androidx.room.*
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import kotlinx.coroutines.flow.Flow


@Dao
interface MemoDao {

    @Query("SELECT COUNT(*) FROM Memo")
    fun selectMemoCount():Flow<Int>


    //페이징 기법 불러오기
    @Query("SELECT * FROM Memo ORDER BY icon_position ASC , icon_seq DESC LIMIT :startSize,:loadSize")
    fun selectMemoPaging(startSize: Int, loadSize: Int): Flow<List<Memo>>

    //페이징 기법 불러오기
    @Query("SELECT * FROM Memo ORDER BY icon_position ASC , icon_seq DESC LIMIT :startSize,:loadSize")
    fun selectMemoPagingMainThread(startSize: Int, loadSize: Int): List<Memo>


    //페이징 기법 불러오기
    @Query("SELECT * FROM Memo ORDER BY icon_seq DESC LIMIT :loadSize OFFSET (:page-1) * :loadSize")
    suspend fun selectMemoPaging_v2(page: Int, loadSize: Int): List<Memo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(user: Memo)

    @Update
    fun updateMemo(memo: Memo)

    @Query("UPDATE Memo SET icon_color_position = :icon_color_position , memo_title = :memo_title ,memo_content = :memo_content  WHERE icon_seq = :icon_seq")
    fun updateMemo(icon_seq: Long , icon_color_position : Int , memo_title : String , memo_content : String)

    @Query("UPDATE Memo SET icon_position = :icon_position  WHERE icon_seq = :icon_seq")
    fun updateMemo(icon_position: Int, icon_seq: Long)


    @Query("DELETE FROM Memo WHERE icon_seq = :icon_seq")
    suspend fun deleteMemo(icon_seq: Long)


}