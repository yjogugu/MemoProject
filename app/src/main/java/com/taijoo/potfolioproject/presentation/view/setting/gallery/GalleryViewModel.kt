package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taijoo.potfolioproject.data.model.FolderData
import com.taijoo.potfolioproject.data.model.GalleryData

import kotlinx.coroutines.flow.Flow

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


    //사진 , 동영상 불러오기
    fun getGalleryPath(galleryCursor : GalleryCursor, type : Int, folder : String , pageSize : Int) : Flow<PagingData<GalleryData>>{

        return Pager( config = PagingConfig( pageSize = pageSize, enablePlaceholders = false ), pagingSourceFactory =
        { GalleryImagePagingSource(galleryCursor,context , type , folder,pageSize) } ).flow.cachedIn(viewModelScope)

    }

    //이미지 , 동영상 폴더 불러오기
    fun getGalleryFolderData(galleryCursor : GalleryCursor) : List<FolderData>{
        val folderList : ArrayList<FolderData> = ArrayList()
        val getFolderList = galleryCursor.getFolderListWithCount(context)

        for(data in getFolderList){
            if(data.key != "전체폴더"){
                val folder = data.key + " " + data.value.toString()
                folderList.add(FolderData(folder , data.key , data.value))
            }

        }
        folderList.add(0, FolderData("전체폴더 "+getFolderList["전체폴더"].toString() , "전체폴더",
            getFolderList["전체폴더"]!!
        ))

        return folderList
    }

    //이미지 , 동영상 폴더 불러오기
    fun getGalleryFolder(galleryCursor: GalleryCursor): List<String> {
        val folderList : ArrayList<String> = ArrayList()
        val getFolderList = galleryCursor.getFolderListWithCount(context)

        for(data in getFolderList){
            if(data.key != "전체폴더"){
                val folder = data.key + " " + data.value.toString()
                folderList.add(folder)
            }

        }
        folderList.add(0, "전체폴더 "+getFolderList["전체폴더"].toString())

        return folderList
    }
}