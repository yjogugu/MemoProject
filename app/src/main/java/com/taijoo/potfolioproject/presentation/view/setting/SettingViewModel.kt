package com.taijoo.potfolioproject.presentation.view.setting

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.repository.Http.Repository
import com.taijoo.potfolioproject.data.repository.Http.RetrofitSender
import com.taijoo.potfolioproject.data.repository.room.Database.UserDB
import com.taijoo.potfolioproject.data.repository.room.Repository.UserRepository
import com.taijoo.potfolioproject.data.repository.room.entity.User
import com.taijoo.potfolioproject.util.IP
import com.taijoo.potfolioproject.util.SnackbarCustom
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val server_api : Repository = Repository()

    private val userDatabase = UserDB.getInstance(application)!!

    private val userDao = userDatabase.userDao()

    private val user_api : UserRepository = UserRepository(userDao)

    var userData :LiveData<User> = user_api.getAll()

    var observerType = 0


    @DelicateCoroutinesApi
    @SuppressLint("NewApi")
    fun serverInsertUser(view : View, userName : String, userEmail : String, userRegisterName : String , token : String){
        viewModelScope.launch {
            val response = server_api.insertUser("UserInsert",userName, userEmail, userRegisterName)
            when (response.isSuccessful){
                true ->{
                    val jsonObject : JSONObject = JSONObject(response.body()!!.string())

                    val userDataJson = jsonObject.getJSONObject("user_data")

                    if(userDataJson.getBoolean("result")){
                        val userSeq = userDataJson.getInt("user_seq")
                        val user_name = userDataJson.getString("user_name")
                        val userProfile = userDataJson.getString("user_profile")
                        val user_register_name = userDataJson.getString("user_register_name")
                        val email = userDataJson.getString("email")

                        server_api.updateToken("TokenUpdate",userSeq,token)//토큰값 변경

                        //프로필이 없을때
                        if(userData.value!!.profile == ""){

                            val localProfile = IP.SERVER_ADDRESS_UPLOADS+"Profile/Image/"+userProfile

                            setUser(User(userSeq,user_name,localProfile,user_register_name,email,!userData.value!!.network_state))
                        }
                        else{
                            setUser(User(userSeq,user_name,userData.value!!.profile,user_register_name,email,!userData.value!!.network_state))

                            if(!userData.value!!.profile.contains("http://")){
                                imageUpload(userSeq.toString(),email , userData.value!!.profile)//프로필 이미지 업로드
                            }

                        }


                    }
                    else{
                        SnackbarCustom().snackBar(view,view.context.getString(R.string.Snackbar_server_link_false_v2)).show()
                    }

                }
                false ->{
                    Log.e("여기","ㄴㄴ")
                }
            }

        }

    }


    //로컬 유저 이름 변경
    fun setUserName(name : String){
        viewModelScope.launch {
            user_api.updateName(name)
        }
    }

    //로컬 유저 사진 변경
    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.P)
    fun setUserProfile(profile : String){
        viewModelScope.launch {
            user_api.updateProfile(profile)

            if(userData.value!!.network_state){//서버가 연결되어 있을때만 서버 업로드
                imageUpload(userData.value!!.user_seq.toString(),userData.value!!.email , profile)//프로필 이미지 업로드
            }

        }
    }

    //서버 연동후 로컬 유저 데이터 변경
    private fun setUser(user : User){
        viewModelScope.launch {
            observerType = 1
            user_api.update(user)
        }
    }

    //로컬 서버연동 상태값 변경
    fun setUserNetworkState(updateUserNetworkState : Boolean ){
        viewModelScope.launch {
            observerType = 1
            user_api.updateNetworkState(updateUserNetworkState)
        }
    }

    //프로필 서버 업로드 GlobalScope 비동기식
    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.P)
     fun imageUpload(userSeq : String ,email : String , profile : String){

        GlobalScope.launch(Dispatchers.IO) {

            val image_name = "IMG" + "_" + userSeq + "_" + email + ".jpg"

            val f: File = File(context.cacheDir, image_name)

            f.createNewFile()

            val bos = ByteArrayOutputStream()
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver , Uri.parse(profile)))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos) //압축하기


            val bitmapdata = bos.toByteArray()


            val fos: FileOutputStream = FileOutputStream(f)

            fos.write(bitmapdata)
            fos.flush()
            fos.close()

            val fileReqBody: RequestBody = f.asRequestBody()
            val part: MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file", f.name, fileReqBody)
            val description: RequestBody = f.asRequestBody()

            server_api.uploadProfile(part,description,userSeq.toInt())//서버에 프로필사진 업로드드

        }

    }
}