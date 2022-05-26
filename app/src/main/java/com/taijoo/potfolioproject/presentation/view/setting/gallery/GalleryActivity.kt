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

        folderList = viewModel.getGalleryFolder(galleryCursor)//폴더 name 리스트

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

        onOkClick()//사진 선택 완료
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

    //데이터 불러오기
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getImagePath(type: Int , folder : String) {
        viewModel.getGalleryPath(galleryCursor,type,folder,pageSize).collectLatest {
            adapter.setFolder(folder)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onImageClick(position : Int, list: ArrayList<Uri>) {
        viewModel.uriList = list
        if(position == 0){//카메라 선택

            galleryItemSelect()

        }
        else{
            if(list.size >= 8){
                Toast.makeText(this,"최대 8장까지 설정 가능합니다.",Toast.LENGTH_SHORT).show()
            }
        }

    }


    //사진,영상 촬영 선택
    @RequiresApi(Build.VERSION_CODES.Q)
    fun galleryItemSelect(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("촬영")

        builder.setItems(R.array.GalleryItemSelect, DialogInterface.OnClickListener { dialog, pos ->

            when (pos){
                0->{//사진 촬영

                    try {
                        val photoFile = createImageFile()

                        val photoURI : Uri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
                        cameraUri = photoFile.absolutePath

                        //이미지
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                        resultLauncher.launch(intent)



                    } catch (ex : IOException) {
                        snackbarCustom.snackBar(binding.clGallery,getString(R.string.GalleryMessage1)).show()
                    }

                }
                1->{//영상 촬영

                    try {
                        val photoFile = createVideoFile()
                        val photoURI : Uri = FileProvider.getUriForFile(this, "$packageName.provider", photoFile)

                        cameraUri = photoURI.toString()

                        //동영상
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

    //카메라 로 사진 찍은 이후
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
                //캐시 파일 삭제
                val file = File(cameraUri)
                contentResolver.delete(Uri.fromFile(file), null, null)
            }
            catch (e : Exception){

            }

        }

    }

    //카메라 로 사진 찍은 이후
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
                //캐시 파일 삭제
                val file = File(cameraUri)
                contentResolver.delete(Uri.fromFile(file), null, null)
            }
            catch (e : Exception){

            }
        }
    }

    //이미지파일 이름 생성
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

    //동영상파일 이름 생성
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

    //파일 이름 정하기
    private fun randomFileName(): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(System.currentTimeMillis())
    }

    //이미지 갤러리에 저장
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

    //동영상 갤러리에 저장
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
        val collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) //기본 외부 스토리지에 있는 모든 비디오 파일
        val uriSavedVideo = resolver.insert(collection, valuesVideos)
        val pfd: ParcelFileDescriptor?
        try {
            pfd = contentResolver.openFileDescriptor(uriSavedVideo!!, "w")
            assert(pfd != null)
            val out = FileOutputStream(pfd!!.fileDescriptor)

            // 여기에서 파일 입력 스트림으로 이미 저장된 비디오를 가져옵니다.
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
            ) //보류 중인 파일이 0으로 바뀔 때까지 앱에서만 파일을 볼 수 있습니다.
            contentResolver.update(uriSavedVideo, valuesVideos, null, null)

        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "error: " + e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


}