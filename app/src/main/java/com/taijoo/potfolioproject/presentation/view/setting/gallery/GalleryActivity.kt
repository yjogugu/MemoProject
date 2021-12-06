package com.taijoo.potfolioproject.presentation.view.setting.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.ActivityGalleryBinding
import android.Manifest.permission.READ_EXTERNAL_STORAGE

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.recyclerview.widget.GridLayoutManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.taijoo.potfolioproject.data.model.FolderData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class GalleryActivity : AppCompatActivity() , GalleryInterface {

    lateinit var binding : ActivityGalleryBinding
    lateinit var adapter : GalleryAdapter
    private val PERMISSION_REQUEST_CODE = 200

    private lateinit var galleryCursor: GalleryCursor

    lateinit var viewModel : GalleryViewModel

    private lateinit var folderListData : List<FolderData>
    private lateinit var folderList : List<String>

    private lateinit var galleryInterface: GalleryInterface

    private val pageSize = 50

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        galleryInterface = this

        binding = DataBindingUtil.setContentView(this,R.layout.activity_gallery)

        viewModel = ViewModelProvider(this@GalleryActivity)[GalleryViewModel::class.java]

        binding.apply {
            activity = this@GalleryActivity
        }

        galleryCursor = GalleryCursor()

        init()
    }

    @SuppressLint("NewApi", "DiscouragedPrivateApi")
    fun init(){
        binding.titleAppbar.psvTitle.visibility = View.VISIBLE

        folderListData = viewModel.getGalleryFolderData(galleryCursor)//Folder Item

        folderList = viewModel.getGalleryFolder(galleryCursor)//폴더 name 리스트

        binding.titleAppbar.psvTitle.setItems(folderList)

        binding.titleAppbar.psvTitle.selectItemByIndex(0)

        binding.titleAppbar.psvTitle.setIsFocusable(true)


        adapter = GalleryAdapter(this,0,galleryInterface)
        binding.rvGallery.setHasFixedSize(true)
        binding.rvGallery.layoutManager = GridLayoutManager(this,3)
        binding.rvGallery.adapter = adapter

        requestPermissions()

        psvTitleOnClick()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun psvTitleOnClick(){

        binding.titleAppbar.psvTitle.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            adapter.refresh()
            lifecycleScope.launch {
                if(newIndex == 0){
                    getImagePath(0,"")
                }
                else{
                    getImagePath(1,folderListData[newIndex].name)
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getImagePath(type: Int , folder : String) {
        viewModel.getGalleryPath(galleryCursor,type,folder,pageSize).collectLatest {
            adapter.submitData(it)//페이징 하는곳
        }

    }


    private fun checkPermission(): Boolean {
        // 이 방법에서는 권한이 부여되었는지 확인하고 결과를 반환합니다.
        val result = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestPermissions() {
        if (checkPermission()) {
            // 권한이 이미 부여된 경우 호출합니다.
            // 외부 스토리지에서 모든 이미지를 가져오는 방법입니다.
                lifecycleScope.launch {
                    getImagePath(0,"")
                }

//            getImageBuckets()
        } else {
            // 권한이 부여되지 않은 경우
            // 메서드를 호출하여 권한을 요청합니다.
            requestPermission()
        }
    }

    private fun requestPermission() {
        //아래 라인에서 외부 스토리지 사용 권한을 요청합니다.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 이 메서드는 권한이 부여된 후에 호출됩니다.
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> // 이 경우 권한이 수락되었는지 여부를 확인하는 중입니다.
                if (grantResults.isNotEmpty()) {

                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        // 권한이 수락되면 토스트 메시지를 표시합니다.
                        // 메소드를 호출하여 이미지 경로를 가져옵니다.
                        lifecycleScope.launch {
                            getImagePath(0,"")
                        }
                    } else {
                        // 권한이 거부되면 앱을 닫고 토스트 메시지를 표시합니다.
                        Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onImageClick(uri: Uri) {

        val intent : Intent = Intent()
        intent.putExtra("uri" , uri.toString())
        setResult(RESULT_OK,intent)
        finish()

    }


}