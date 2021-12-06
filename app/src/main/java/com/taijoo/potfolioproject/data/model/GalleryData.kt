package com.taijoo.potfolioproject.data.model

import android.graphics.Bitmap
import android.net.Uri

data class GalleryData(val id : Long ,val uri: Uri , val type : Int,val title : String , val  data : String)

data class FolderData(val title : String , val name : String , val count : Int)