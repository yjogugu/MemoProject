package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.GalleryData
import com.taijoo.potfolioproject.databinding.ItemGalleryImageBinding
import java.lang.Exception
import java.text.SimpleDateFormat


class GalleryViewHolder(var binding : ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryData : GalleryData, imgList : ArrayList<Uri>, galleryInterface :GalleryInterface){

        binding.uri = galleryData.uri.toString()

        when(galleryData.type){
            -1->{//카메라
                binding.cbImage.visibility = View.GONE
            }
            1->{//사진
                binding.visible = View.GONE
                binding.cbImage.visibility = View.VISIBLE
            }
            3->{//동영상
                binding.visible = View.VISIBLE
                binding.cbImage.visibility = View.VISIBLE
                binding.date = dataTime(galleryData.uri)
            }
        }

        binding.cbImage.setOnCheckedChangeListener(null)

        binding.cbImage.isChecked = galleryData.isCheck != 0



        binding.cbImage.setOnCheckedChangeListener { compoundButton, isCheck ->

            if(isCheck && bindingAdapterPosition != 0){
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

            galleryInterface.onImageClick(bindingAdapterPosition,imgList)


        }

        binding.idIVImage.setOnClickListener {
            if(bindingAdapterPosition != 0){
                binding.cbImage.isChecked = !binding.cbImage.isChecked
            }
            else{
                galleryInterface.onImageClick(bindingAdapterPosition,imgList)
            }

        }

    }

    @SuppressLint("SimpleDateFormat")
    fun dataTime(uri: Uri) : String{
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(binding.idIVImage.context ,uri)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val timeInMillisec = time!!.toLong() //동영상 시간초 구하기

            val dateFormat = SimpleDateFormat("mm:ss")
            dateFormat.format(timeInMillisec)
        } catch (e : Exception){
            "0:00"
        }

    }

}