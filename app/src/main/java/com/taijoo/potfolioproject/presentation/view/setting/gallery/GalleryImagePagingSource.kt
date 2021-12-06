package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.taijoo.potfolioproject.data.model.GalleryData

class GalleryImagePagingSource(private val galleryCursor: GalleryCursor, private val context: Context,
                               private val type : Int, private val folder : String , private val pageSize : Int): PagingSource<Int, GalleryData>() {


    //시작하는 값
    private companion object {
        const val INIT_PAGE_INDEX = 1
    }

    //해당 페이지 되는 부분
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryData> {
        val page = params.key ?: INIT_PAGE_INDEX

        return try {

            val items = galleryCursor.getImagePath(context,page,params.loadSize,type,folder)

            val requestOptions: RequestOptions = RequestOptions()
                .centerCrop()
                .priority(Priority.LOW)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)


            for (data in items){

                Glide.with(context).load(data)
                    .apply(requestOptions)
                    .dontAnimate()
                    .preload()
            }

            LoadResult.Page(
                data = items,
                prevKey = if (page == INIT_PAGE_INDEX) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + (params.loadSize / pageSize)
//                    nextKey = if (items.isNullOrEmpty()) null else page + 1


            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, GalleryData>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}