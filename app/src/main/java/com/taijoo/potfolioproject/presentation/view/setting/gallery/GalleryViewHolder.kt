package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.GalleryData
import com.taijoo.potfolioproject.databinding.ItemGalleryImageBinding
import java.text.SimpleDateFormat


class GalleryViewHolder(var binding : ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryData : GalleryData, imgList : ArrayList<Uri>, galleryInterface :GalleryInterface){

        binding.uri = galleryData.uri.toString()

        if(galleryData.type == 3){
            binding.visible = View.VISIBLE
            binding.date = dataTime(galleryData.uri)
        }
        else{
            binding.visible = View.GONE
        }


        binding.cbImage.setOnCheckedChangeListener(null)

        binding.cbImage.isChecked = galleryData.isCheck != 0



        binding.cbImage.setOnCheckedChangeListener { compoundButton, isCheck ->

            if(isCheck){
                galleryData.isCheck = 1
                imgList.add(galleryData.uri)

                if(imgList.size > 8){
                    binding.cbImage.isChecked = false
                    galleryData.isCheck = 0
                    imgList.remove(galleryData.uri)
                }

            }
            else{
                galleryData.isCheck = 0
                imgList.remove(galleryData.uri)
            }


            Log.e("여기","ㅇ "+imgList)
            galleryInterface.onImageClick(imgList)

        }

        binding.idIVImage.setOnClickListener {
            binding.cbImage.isChecked = !binding.cbImage.isChecked
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