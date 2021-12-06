package com.taijoo.potfolioproject.util

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taijoo.potfolioproject.data.repository.room.Dao.MemoDao
import com.taijoo.potfolioproject.data.repository.room.entity.Memo



//리사이클러뷰 페이징 라이브러리
class MemoPagingSource(private val dao: MemoDao): PagingSource<Int, Memo>() {


    //시작하는 값
    private companion object {
        const val INIT_PAGE_INDEX = 1
    }

    //해당 페이지 되는 부분
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Memo> {
        val page = params.key ?: INIT_PAGE_INDEX

        return try {
            val items = dao.selectMemoPaging_v2(page, params.loadSize)

            LoadResult.Page(
                    data = items,
                    prevKey = if (page == INIT_PAGE_INDEX) null else page - 1,
                    nextKey = if (items.isEmpty()) null else page + (params.loadSize / 5)
//                    nextKey = if (items.isNullOrEmpty()) null else page + 1


            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Memo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}