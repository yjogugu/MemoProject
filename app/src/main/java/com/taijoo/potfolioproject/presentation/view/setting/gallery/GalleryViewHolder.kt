package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.taijoo.potfolioproject.data.model.GalleryData
import com.taijoo.potfolioproject.databinding.ItemGalleryImageBinding
import java.text.SimpleDateFormat


class GalleryViewHolder(var binding : ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryData : GalleryData){

//        binding.idIVImage.layout(0,0,0,0)
        binding.uri = galleryData.uri.toString()

        if(galleryData.type == 3){
            binding.visible = View.VISIBLE
            binding.date = dataTime(galleryData.uri)
        }
        else{
            binding.visible = View.GONE
        }



    }

    @SuppressLint("SimpleDateFormat")
    fun dataTime(uri: Uri) : String{
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(binding.idIVImage.context ,uri)
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeInMillisec = time!!.toLong() //동영상 시간초 구하기

        val dateFormat = SimpleDateFormat("mm:ss")
        return dateFormat.format(timeInMillisec)
    }

}