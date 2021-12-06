package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ActivityGalleryZoomOutBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryZoomOutActivity : AppCompatActivity(){

    lateinit var binding : ActivityGalleryZoomOutBinding
    lateinit var adapter: GalleryAdapter
    lateinit var viewModel : GalleryViewModel
    private  var galleryCursor: GalleryCursor = GalleryCursor()

    var imgPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@GalleryZoomOutActivity,R.layout.activity_gallery_zoom_out)

        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]

        binding.apply {
            lifecycleOwner = this@GalleryZoomOutActivity
        }

        init()
    }

    @SuppressLint("NewApi")
    fun init(){

        imgPosition = intent.getIntExtra("position",0)//갤러리에서 선택한 포지션 받아오기


        val snapHelper : SnapHelper = PagerSnapHelper()
        adapter = GalleryAdapter(this,1)
        binding.rvZoomOut.setHasFixedSize(true)
        binding.rvZoomOut.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL , false)

        snapHelper.attachToRecyclerView(binding.rvZoomOut)

        binding.rvZoomOut.adapter = adapter

        lifecycleScope.launch {
            getImagePath(0,"")
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getImagePath(type: Int , folder : String) {
        viewModel.getGalleryPath(galleryCursor,type,folder,imgPosition).collectLatest {
            binding.rvZoomOut.scrollToPosition(imgPosition)
            adapter.submitData(it)//페이징 하는곳

        }

    }
}