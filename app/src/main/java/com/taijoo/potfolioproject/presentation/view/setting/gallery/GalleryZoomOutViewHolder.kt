package com.taijoo.potfolioproject.presentation.view.setting.gallery

import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.GalleryData
import com.taijoo.potfolioproject.databinding.ItemZoomOutLayoutBinding

class GalleryZoomOutViewHolder (var binding : ItemZoomOutLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryData: GalleryData){
        binding.data = galleryData

    }


}