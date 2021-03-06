package com.taijoo.potfolioproject.presentation.view.setting.gallery

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.model.FolderData
import com.taijoo.potfolioproject.databinding.ActivityGalleryBinding
import com.taijoo.potfolioproject.presentation.view.setting.SettingActivity
import com.taijoo.potfolioproject.util.SnackbarCustom
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
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

    private var cameraUri = ""

    private val snackbarCustom = SnackbarCustom()

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

        folderList = viewModel.getGalleryFolder(galleryCursor)//?????? name ?????????

        binding.titleAppbar.psvTitle.setItems(folderList)

        binding.titleAppbar.psvTitle.selectItemByIndex(0)

        binding.titleAppbar.psvTitle.setIsFocusable(true)

        binding.titleAppbar.tvOk.visibility = View.VISIBLE


        adapter = GalleryAdapter(this,0,galleryInterface)
        binding.rvGallery.setHasFixedSize(true)
        binding.rvGallery.layoutManager = GridLayoutManager(this,3)
        binding.rvGallery.adapter = adapter

        requestPermissions()

        psvTitleOnClick()

        onOkClick()//?????? ?????? ??????
    }

    private fun onOkClick(){
        binding.titleAppbar.tvOk.setOnClickListener {

            val intent = Intent(this , SettingActivity::class.java)
            intent.putExtra("uri",viewModel.uriList[0].toString())
            setResult(RESULT_OK,intent)
            finish()
        }
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

    //????????? ????????????
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getImagePath(type: Int , folder : String) {
        viewModel.getGalleryPath(galleryCursor,type,folder,pageSize).collectLatest {
            adapter.setFolder(folder)
            adapter.submitData(it)//????????? ?????????
        }

    }


    private fun checkPermission(): Boolean {
        // ??? ??????????????? ????????? ?????????????????? ???????????? ????????? ???????????????.
        val result = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestPermissions() {
        if (checkPermission()) {
            // ????????? ?????? ????????? ?????? ???????????????.
            // ?????? ?????????????????? ?????? ???????????? ???????????? ???????????????.
                lifecycleScope.launch {
                    getImagePath(0,"")
                }

//            getImageBuckets()
        } else {
            // ????????? ???????????? ?????? ??????
            // ???????????? ???????????? ????????? ???????????????.
            requestPermission()
        }
    }

    private fun requestPermission() {
        //?????? ???????????? ?????? ???????????? ?????? ????????? ???????????????.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // ??? ???????????? ????????? ????????? ?????? ???????????????.
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> // ??? ?????? ????????? ?????????????????? ????????? ???????????? ????????????.
                if (grantResults.isNotEmpty()) {

                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        // ????????? ???????????? ????????? ???????????? ???????????????.
                        // ???????????? ???????????? ????????? ????????? ???????????????.
                        lifecycleScope.launch {
                            getImagePath(0,"")
                        }
                    } else {
                        // ????????? ???????????? ?????? ?????? ????????? ???????????? ???????????????.
                        Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onImageClick(position : Int, list: ArrayList<Uri>) {
        viewModel.uriList = list
        if(position == 0){//????????? ??????

            galleryItemSelect()

        }
        else{
            if(list.size >= 8){
                Toast.makeText(this,"?????? 8????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show()
            }
        }

    }


    //??????,?????? ?????? ??????
    @RequiresApi(Build.VERSION_CODES.Q)
    fun galleryItemSelect(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("??????")

        builder.setItems(R.array.GalleryItemSelect, DialogInterface.OnClickListener { dialog, pos ->

            when (pos){
                0->{//?????? ??????

                    try {
                        val photoFile = createImageFile()

                        val photoURI : Uri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
                        cameraUri = photoFile.absolutePath

                        //?????????
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                        resultLauncher.launch(intent)



                    } catch (ex : IOException) {
                        snackbarCustom.snackBar(binding.clGallery,getString(R.string.GalleryMessage1)).show()
                    }

                }
                1->{//?????? ??????

                    try {
                        val photoFile = createVideoFile()
                        val photoURI : Uri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)

                        cameraUri = photoURI.toString()

                        //?????????
                        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
                        videoResultLauncher.launch(intent)


                    } catch (ex : IOException) {
                        snackbarCustom.snackBar(binding.clGallery,getString(R.string.GalleryMessage1)).show()
                    }

                }
            }
        })

        val alertDialog = builder.create()
        alertDialog.show()
    }

    //????????? ??? ?????? ?????? ??????
    @RequiresApi(Build.VERSION_CODES.Q)
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val file = File(cameraUri)
            var bitmap : Bitmap? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    val source = ImageDecoder.createSource(contentResolver, Uri.fromFile(file))
                    bitmap = ImageDecoder.decodeBitmap(source)
                    saveBitmap(bitmap,randomFileName())

                    adapter.refresh()
                    lifecycleScope.launch {
                        getImagePath(0,"")
                    }
                }
            }
            catch (e : Exception){
                snackbarCustom.snackBar(binding.clGallery,getString(R.string.GalleryMessage1)).show()
            }

        }
        else{
            try {
                //?????? ?????? ??????
                val file = File(cameraUri)
                contentResolver.delete(Uri.fromFile(file), null, null)
            }
            catch (e : Exception){

            }

        }

    }

    //????????? ??? ?????? ?????? ??????
    @RequiresApi(Build.VERSION_CODES.Q)
    private var videoResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == Activity.RESULT_OK) {

            saveVideo(Uri.parse(cameraUri))
            adapter.refresh()
            lifecycleScope.launch {
                getImagePath(0,"")
            }

        }
        else{
            try {
                //?????? ?????? ??????
                val file = File(cameraUri)
                contentResolver.delete(Uri.fromFile(file), null, null)
            }
            catch (e : Exception){

            }
        }
    }

    //??????????????? ?????? ??????
    @Throws(IOException::class)
    private fun createImageFile() : File{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.KOREA).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_";
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        )
        return image
    }

    //??????????????? ?????? ??????
    @Throws(IOException::class)
    private fun createVideoFile() : File{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.KOREA).format(Date())
        val videoFileName = "MP4_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val video = File.createTempFile(
            videoFileName,  /* prefix */
            ".mp4",         /* suffix */
            storageDir      /* directory */
        )
        return video
    }

    //?????? ?????? ?????????
    private fun randomFileName(): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(System.currentTimeMillis())
    }

    //????????? ???????????? ??????
    @RequiresApi(Build.VERSION_CODES.Q)
    @Throws(IOException::class)
    fun saveBitmap(bitmap: Bitmap,  displayName: String): Uri {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/images")
        }
        var uri: Uri? = null

        return runCatching {
            with(contentResolver) {
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                    uri = it // Keep uri reference so it can be removed on failure

                    openOutputStream(it)?.use { stream ->
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream))
                            throw IOException("Failed to save bitmap.")
                    } ?: throw IOException("Failed to open output stream.")

                } ?: throw IOException("Failed to create new MediaStore record.")
            }
        }.getOrElse {
            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                contentResolver.delete(orphanUri, null, null)
            }
            throw it
        }
    }

    //????????? ???????????? ??????
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun saveVideo(uri3: Uri) {
        val videoFileName = "video_" + System.currentTimeMillis() + ".mp4"

        val valuesVideos: ContentValues = ContentValues()
        valuesVideos.put(MediaStore.Video.Media.RELATIVE_PATH, "DCIM/images")
        valuesVideos.put(MediaStore.Video.Media.TITLE, videoFileName)
        valuesVideos.put(MediaStore.Video.Media.DISPLAY_NAME, videoFileName)
        valuesVideos.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        valuesVideos.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        valuesVideos.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
        valuesVideos.put(MediaStore.Video.Media.IS_PENDING, 1)

        val resolver = contentResolver
        val collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) //?????? ?????? ??????????????? ?????? ?????? ????????? ??????
        val uriSavedVideo = resolver.insert(collection, valuesVideos)
        val pfd: ParcelFileDescriptor?
        try {
            pfd = contentResolver.openFileDescriptor(uriSavedVideo!!, "w")
            assert(pfd != null)
            val out = FileOutputStream(pfd!!.fileDescriptor)

            // ???????????? ?????? ?????? ??????????????? ?????? ????????? ???????????? ???????????????.
            val `in` = contentResolver.openInputStream(uri3)
            val buf = ByteArray(8192)
            var len: Int
            var progress = 0
            while (`in`!!.read(buf).also { len = it } > 0) {
                progress += len
                out.write(buf, 0, len)
            }
            out.close()
            `in`.close()
            pfd.close()
            valuesVideos.clear()
            valuesVideos.put(MediaStore.Video.Media.IS_PENDING, 0)
            valuesVideos.put(
                MediaStore.Video.Media.IS_PENDING,
                0
            ) //?????? ?????? ????????? 0?????? ?????? ????????? ???????????? ????????? ??? ??? ????????????.
            contentResolver.update(uriSavedVideo, valuesVideos, null, null)

        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "error: " + e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


}