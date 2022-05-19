package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.taijoo.potfolioproject.data.model.GalleryData
import java.util.*
import kotlin.collections.ArrayList

class GalleryCursor {


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range", "InlinedApi")
    fun getImagePath(context: Context , page: Int, loadSize: Int ,folderType : Int, folder :String):List<GalleryData> {

        val imagePaths = ArrayList<GalleryData>()

        val uri = MediaStore.Files.getContentUri("external")//기본


        val columns = arrayOf( MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE)//Media 컬럼 어떤거 가져올지 정하기

        val selection : String
        val selectionArgs :Array<String>


        //전체 목록
        if(folderType == 0){
            selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"


            selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

            )

        }
        //폴더별 목록
        else{
            selection =
                "${MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME} = ? AND ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? " +
                        "OR ${MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME} = ? AND ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"

            selectionArgs = arrayOf(
                folder,
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                folder,
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

            )
        }


        val selectionBundle = bundleOf(
            ContentResolver.QUERY_ARG_OFFSET to (page-1) * loadSize,
//            ContentResolver.QUERY_ARG_OFFSET to page * loadSize,
            ContentResolver.QUERY_ARG_LIMIT to  loadSize,
            ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED),
            ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
            ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
            ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs
        )


        val cursor: Cursor? = context.contentResolver.query(
            uri,
            columns,
            selectionBundle,
            null

        )


        cursor.apply {

            if(cursor!!.moveToFirst()){
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))//파일 고유 번호
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE))//파일 이름
                    val date = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE))//생성 날짜
                    val type = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))//파일 타입

                    //갤러리에 이미지 불러오기
                    val photoUrl = Uri.withAppendedPath(uri,""+cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Files.FileColumns._ID)))

                    if(imagePaths.size == 0 ){//첫 아이템은 카메라 촬영 아이템
                        imagePaths.add(0,GalleryData(-1,Uri.parse(""),-1,"Camera","0000",0))
                    }
//                    else{
//                        imagePaths.add(GalleryData(id , photoUrl , type , title , date , 0))
//                    }
                    imagePaths.add(GalleryData(id , photoUrl , type , title , date , 0))

                } while (cursor.moveToNext())



                cursor.close()
            }

        }

        return imagePaths

    }

    //폴더 불러오기
    @SuppressLint("InlinedApi")
    fun getFolderListWithCount(context: Context): Map<String, Int> {
        val uri = MediaStore.Files.getContentUri("external")//기본
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val selection =
            "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"


        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

        )

        val selectionBundle = bundleOf(
            ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
            ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs
        )

        val cursor = context.contentResolver.query(uri, projection, selectionBundle, null)
        val folderMap: MutableMap<String, Int> = TreeMap<String, Int>()
        if (cursor != null) {

            var allCount = 0
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val folder = cursor.getString(columnIndex)
                try {
                    if(folderMap[folder] == null) folderMap[folder] = 0

                    folderMap[folder] = folderMap[folder]!!.plus(1)
                    allCount += 1

                } catch (e: Exception) {

                }
            }
            cursor.close()
            folderMap["전체폴더"] = allCount
        }
        return folderMap
    }
}