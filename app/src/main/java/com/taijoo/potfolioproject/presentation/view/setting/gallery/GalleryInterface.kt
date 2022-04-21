package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.net.Uri

interface GalleryInterface {

    fun onImageClick(position : Int , list : ArrayList<Uri>)


}