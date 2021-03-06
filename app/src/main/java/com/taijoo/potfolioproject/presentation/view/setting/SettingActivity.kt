package com.taijoo.potfolioproject.presentation.view.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.util.Pair
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.taijoo.potfolioproject.MyApplication
import com.taijoo.potfolioproject.MyService
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.model.SettingData
import com.taijoo.potfolioproject.databinding.ActivitySettingBinding
import com.taijoo.potfolioproject.presentation.view.setting.gallery.GalleryActivity
import com.taijoo.potfolioproject.util.ArrayListUtil
import com.taijoo.potfolioproject.util.DescendingObj
import com.taijoo.potfolioproject.util.ZoomInActivity
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.taijoo.potfolioproject.presentation.view.MainActivity
import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.messaging.FirebaseMessaging
import com.taijoo.potfolioproject.presentation.view.friend.FriendListActivity
import com.taijoo.potfolioproject.util.SnackbarCustom
import com.taijoo.potfolioproject.util.dialog.CustomDefaultDialog
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat


class SettingActivity : AppCompatActivity(), SettingInterface {

    lateinit var binding : ActivitySettingBinding
    lateinit var settingViewModel: SettingViewModel
    var currentNightMode = 0
    lateinit var adapter : SettingAdapter

    var item = ArrayList<SettingData>()

    lateinit var mGoogleSignInClient  : GoogleSignInClient//??????????????? ???????????????

    companion object{
        const val TAG = "SettingActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_setting)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting)

        settingViewModel = ViewModelProvider(this@SettingActivity)[SettingViewModel::class.java]

        currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()//??????????????? ?????? ??????

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.apply {
            activity = this@SettingActivity
            viewModel = settingViewModel
            lifecycleOwner = this@SettingActivity
        }

        init()
    }

    fun init(){

        item.add(SettingData(0,0,getString(R.string.setting_profile_name_change),R.drawable.basics_profile))

        settingViewModel.userData.observe(this) {
            if (settingViewModel.observerType == 0) {//??????

                if (it.network_state) {//????????? ?????? ????????????
                    item.add(
                        SettingData(
                            0,
                            1,
                            getString(R.string.setting_server_add),
                            R.drawable.leak_add,
                            it.network_state
                        )
                    )
                    item.add(
                        SettingData(
                            0,
                            2,
                            getString(R.string.friendManaging),
                            R.drawable.ic_friend
                        )
                    )
//                    item.add(SettingData(0,2,getString(R.string.opensource),R.drawable.ic_edit))
                } else {//????????? ????????? ???????????????
                    item.add(
                        SettingData(
                            0,
                            1,
                            getString(R.string.setting_server_remove),
                            R.drawable.leak_remove
                        )
                    )
                }

                adapter.setData(item, 0)


            } else if (settingViewModel.observerType == 1) {//?????? ???????????? ???????????????

                if (it.network_state) {//????????? ?????? ????????????
                    item[1] = SettingData(
                        0,
                        1,
                        getString(R.string.setting_server_add),
                        R.drawable.leak_add,
                        it.network_state
                    )

                    item.add(
                        SettingData(
                            0,
                            2,
                            getString(R.string.friendManaging),
                            R.drawable.ic_friend
                        )
                    )
//                    item.add(SettingData(0,2,getString(R.string.opensource),R.drawable.ic_edit))

                    adapter.setData(item, 1)


                } else {//????????? ????????? ???????????????
                    item[1] = SettingData(
                        0,
                        1,
                        getString(R.string.setting_server_remove),
                        R.drawable.leak_remove,
                        it.network_state
                    )

                    val arrayListUtil = ArrayListUtil<SettingData>()
                    item = arrayListUtil.arrayRemove(item)
                    adapter.setData(item, 2)

                }

            } else if (settingViewModel.observerType == 99) {//?????? ????????? ??????
                adapter.notifyItemChanged(1, false)
            }

            settingViewModel.observerType = -1

        }


        adapter = SettingAdapter(item,this)

        binding.rvTest.setHasFixedSize(true)
        binding.rvTest.layoutManager = LinearLayoutManager(this)
        binding.rvTest.adapter = adapter

        //????????????
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


    }

    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.P)
    fun onGalleryClick(){
        val intent : Intent = Intent(this, GalleryActivity::class.java)
        //startActivityForResult ?????? ??????
        resultLauncher.launch(intent)

    }

    fun onProfileClick(){
        val intent = Intent(this,ZoomInActivity::class.java)
        intent.putExtra("uri",settingViewModel.userData.value!!.profile)

        val options = ActivityOptions.makeSceneTransitionAnimation(this,
            Pair.create(binding.ivProfile, getString(R.string.icon_main_animation)))

        ActivityCompat.startActivity(this, intent, options.toBundle())

    }

    //ActivityResult ????????? ????????? ????????? ?????? ???????????? ?????? ?????????????????? ????????? ?????????
    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.P)
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data!!.getStringExtra("uri")

            settingViewModel.setUserProfile(uri!!)

        }

    }

    //ActivityResult ????????? ????????? ????????? ?????? ???????????? ?????? ?????????????????? ????????? ?????????
    @DelicateCoroutinesApi
    private var googleResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)

        }
        else{
            adapter.notifyItemChanged(1,false)

        }
    }




    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.Q)
    fun setEditText() { //Edit Text ???????????????
        val container = FrameLayout(this) //????????? ???????????? ??????
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin =
            this.resources.getDimensionPixelSize(R.dimen.dialog_margin) // params??? margin ??????
        params.rightMargin =
            this.resources.getDimensionPixelSize(R.dimen.dialog_margin) //params??? margin ??????

        val builderEdittext = EditText(this) //????????? ????????? ??????
        val filterArray = arrayOfNulls<InputFilter>(1)

        filterArray[0] = LengthFilter(10) //????????? ??????

        val drawable = builderEdittext.background // get current EditText drawable

        drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor("#AAAAAA"), BlendModeCompat.SRC_ATOP)

        builderEdittext.background = drawable // edittext ?????? ?????? ?????????
        builderEdittext.setHintTextColor(Color.parseColor("#AAAAAA"))

        when(currentNightMode){
            Configuration.UI_MODE_NIGHT_NO->{
                builderEdittext.setTextColor(Color.parseColor("#000000"))
            }
            Configuration.UI_MODE_NIGHT_YES->{
                builderEdittext.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        builderEdittext.filters = filterArray
        builderEdittext.maxLines = 20

        builderEdittext.hint = binding.collapsingToolbar.title

        builderEdittext.layoutParams = params //EditText??? params ?????? ????????????


        container.addView(builderEdittext) //FrameLayout??? EditText
        val builder = AlertDialog.Builder(this)
        builder.setTitle(" ")
        builder.setView(container)

        builder.setPositiveButton(
            R.string.ok
        ) { _, _ ->
            settingViewModel.setUserName(builderEdittext.text.toString())
        }

        builder.setNegativeButton(
            R.string.cancel
        ) { dialog, _ -> dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

        when(currentNightMode){
            Configuration.UI_MODE_NIGHT_NO->{
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"))
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"))
            }
            Configuration.UI_MODE_NIGHT_YES->{
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
            }
        }


    }

    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(type : Int) {
        when(type){
            0 -> {//?????????,?????? ??????
                proFileAndNameDialog()
            }
            1 -> {//?????? ??????
                if(!settingViewModel.userData.value!!.network_state){
                    signIn()//?????? ?????????

                }
                else{
                    val customDefaultDialog = CustomDefaultDialog(this,getString(R.string.customDialog_title_1),getString(R.string.customDialog_content_1))

                    customDefaultDialog.setDialogListener(object : CustomDefaultDialog.CustomDialogListener{
                        override fun onCheckClick() {
                            signOut()
                        }

                        override fun onNoClick() {
                            adapter.notifyItemChanged(1,false)
                        }

                    })
                    customDefaultDialog.show()


                }


            }
            2->{//????????????
                val intent = Intent(this , FriendListActivity::class.java)
                startActivity(intent)
            }
        }
        //????????? ?????? ?????? ??????


    }




    @RequiresApi(Build.VERSION_CODES.Q)
    fun proFileAndNameDialog(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("??????")

        builder.setItems(R.array.ProfileAndName, DialogInterface.OnClickListener { dialog, pos ->

            when (pos){
                0->{//????????? ?????? ?????????
                    onGalleryClick()
                }
                1->{//?????? ?????????
                    setEditText()
                }
            }
        })

        val alertDialog = builder.create()
        alertDialog.show()
    }


    //?????? ?????????
    @DelicateCoroutinesApi
    private fun signIn() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        googleResultLauncher.launch(signInIntent)

    }

    //?????? ????????????
   private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                settingViewModel.setUserNetworkState(!settingViewModel.userData.value!!.network_state)

                SnackbarCustom().snackBar(binding.collapsingToolbar,getString(R.string.Snackbar_server_link_close)).show()

            }
    }

    @DelicateCoroutinesApi
    @SuppressLint("NewApi")
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto = acct.photoUrl

                //?????? ??????
                val channel = NotificationChannel("notification", "?????????", NotificationManager.IMPORTANCE_HIGH)
                channel.description = "notification"
                channel.setShowBadge(true)
                channel.enableLights(true)
                channel.enableVibration(true)
                getSystemService(NotificationManager::class.java).createNotificationChannel(channel)


                SnackbarCustom().snackBar(binding.collapsingToolbar,getString(R.string.Snackbar_server_link_open)).show()


                FirebaseMessaging.getInstance().token.addOnCompleteListener(
                    OnCompleteListener { task ->
                        if(!task.isSuccessful){
                            return@OnCompleteListener
                        }

                        val token = task.result

                        settingViewModel.serverInsertUser(binding.collapsingToolbar,settingViewModel.userData.value!!.user_name,personEmail!!,personName!!,token)
                    })



            }

            else{
                SnackbarCustom().snackBar(binding.collapsingToolbar,getString(R.string.Snackbar_server_link_false)).show()
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }




}