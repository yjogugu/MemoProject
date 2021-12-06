package com.taijoo.potfolioproject.data.repository.Http

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taijoo.potfolioproject.data.repository.room.entity.User
import com.taijoo.potfolioproject.data.repository.room.Repository.UserRepository
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resumeWithException

//RestFulAPI 호출 하는곳
class Repository {

    private val api = RetrofitSender.getEndPoint()


    private val TAG = "Repository"

    //User 정보 가져오기

    fun getUserInfo(viewModel: MemoViewModel){

        val user: User = User()

        RetrofitSender.getEndPoint().select_user_info(0).enqueue(object :Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val jsonObject : JSONObject = JSONObject(response.body()!!.string())

                val result : Boolean = jsonObject.getBoolean("result")

                val dataJsonArray : JSONArray = jsonObject.getJSONArray("data")

                for (i in 0 until  dataJsonArray.length()){
                    val dataObject : JSONObject = dataJsonArray.getJSONObject(i)

                    val user_seq = dataObject.getInt("user_seq")
                    val user_name = dataObject.getString("user_name")
                    val user_profile = dataObject.getString("user_profile")


                    user.user_seq = user_seq
                    user.user_name = user_name
                    user.profile = user_profile


                    viewModel.setUser(user)

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG,"유저 데이터 가져오기 서버통신 실패" +t)
            }

        })

    }


    suspend fun insertUser(request : String,userName : String , userEmail : String , userRegisterName : String) : Response<ResponseBody>{

        return RetrofitSender.getEndPoint().insertUser(request,userName, userEmail,userRegisterName)
    }


    suspend fun updateToken(request: String , userSeq: Int , token: String)  : Response<ResponseBody>{

        return RetrofitSender.getEndPoint().updateToken(request,userSeq,token)
    }
    suspend fun uploadProfile(part : MultipartBody.Part, description : RequestBody , user_seq : Int) : Response<ResponseBody>{
        return RetrofitSender.getEndPoint().uploadProfile(part,description,user_seq)
    }


    suspend fun <T : Any> Call<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback<T> {
                // 응답을 받은경우의 호출
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val body = response.body()

                    } else {
                        continuation.resumeWithException(HttpException(response))
                    }
                }

                //호출이 실패한 경우
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }



}