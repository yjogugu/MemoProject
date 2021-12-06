package com.taijoo.potfolioproject.data.repository.Http

import com.taijoo.potfolioproject.data.model.MainData
import com.taijoo.potfolioproject.data.model.MemoData
import okhttp3.MultipartBody
import okhttp3.RequestBody

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface API_Interface {


    @FormUrlEncoded
    @POST("test_count.php")
    fun test_count1(@Field("user_seq") user_seq: Int): Call<MainData>

    @FormUrlEncoded
    @POST("test_count.php")
    fun test_count(@Field("user_seq") user_seq: Int): Call<MemoData>

    //유저 정보 가져오기
    @FormUrlEncoded
    @POST("select_user_info.php")
    fun select_user_info(@Field("user_seq") user_seq: Int): Call<ResponseBody>


    //유저 서버 연동
    @FormUrlEncoded
    @POST("insert_user.php")
    suspend fun insertUser(@Field("request") request: String , @Field("user_name") user_name: String , @Field("user_email") user_email: String,
                   @Field("user_register_name") user_register_name: String ):Response<ResponseBody>

    //토큰 값 변경 서버 연동
    @FormUrlEncoded
    @POST("insert_user.php")
    suspend fun updateToken(@Field("request") request: String , @Field("user_seq") user_seq: Int , @Field("token") token: String ):Response<ResponseBody>


    // 유저 프로필 이미지 서버 업로드
    @Multipart
    @POST("upload_profile_image.php")
    suspend fun uploadProfile(@Part file: MultipartBody.Part, @Part("name") requestBody: RequestBody? ,@Part("user_seq") user_seq: Int ): Response<ResponseBody>

}