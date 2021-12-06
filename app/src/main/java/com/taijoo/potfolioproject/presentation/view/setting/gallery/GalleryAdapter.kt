package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.taijoo.potfolioproject.data.model.GalleryData
import com.taijoo.potfolioproject.databinding.ItemGalleryImageBinding
import com.taijoo.potfolioproject.databinding.ItemZoomOutLayoutBinding


class GalleryAdapter() : PagingDataAdapter<GalleryData,RecyclerView.ViewHolder>(DIFF) {

    var context : Context? = null
    var Viewtype : Int = 0
    var galleryInterface : GalleryInterface? = null

    constructor(context : Context , Viewtype : Int) : this() {
        this.context = context
        this.Viewtype = Viewtype
    }

    constructor(context : Context , Viewtype : Int ,galleryInterface : GalleryInterface ) : this() {
        this.context = context
        this.Viewtype = Viewtype
        this.galleryInterface = galleryInterface
    }

    companion object {
        private val DIFF = object: DiffUtil.ItemCallback<GalleryData>() {
            override fun areItemsTheSame(oldItem: GalleryData, newItem: GalleryData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GalleryData, newItem: GalleryData): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (Viewtype){
            0 ->{
                val itemGalleryViewHolder : ItemGalleryImageBinding =
                    ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)


                return GalleryViewHolder(itemGalleryViewHolder)
            }

            1 -> {
                val itemGalleryZoomViewHolder : ItemZoomOutLayoutBinding =
                    ItemZoomOutLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)


                return GalleryZoomOutViewHolder(itemGalleryZoomViewHolder)

            }


            else -> throw IllegalStateException("ProfileDetails_Adapter View Type Create Error")
        }



    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //갤러리 이미지 보여주기
        if(holder is GalleryViewHolder){

            holder.bind(getItem(position)!!)

            holder.binding.idIVImage.setOnClickListener {
                galleryInterface?.onImageClick(getItem(position)!!.uri)
            }

            holder.binding.llZoomOut.setOnClickListener {
                val intent = Intent(context , GalleryZoomOutActivity::class.java)
                intent.putExtra("position",position)
                context?.startActivity(intent)
            }
        }
        //갤러리에서 확대했을때 이미지 보여주기
        else if(holder is GalleryZoomOutViewHolder){
            holder.bind(getItem(position)!!)
        }
    }

}