package com.taijoo.potfolioproject.presentation.view.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.messaging.FirebaseMessaging
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.repository.room.entity.User
import com.taijoo.potfolioproject.databinding.ActivitySplashBinding
import com.taijoo.potfolioproject.presentation.view.MainActivity
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModelFactory
import com.taijoo.potfolioproject.util.NetworkCheck


class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding
    lateinit var viewModel: MemoViewModel
    private val networkCheck = NetworkCheck()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)


        viewModel = ViewModelProvider(this@SplashActivity, MemoViewModelFactory(application))[MemoViewModel::class.java]

        binding.apply {
            lifecycleOwner = this@SplashActivity

        }

        init()
    }


    fun init(){



        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                //애니메이셔 최초 시작

                viewModel.userData.observe(this@SplashActivity,{
                    if(it == null){
                        viewModel.setUser(User(0,"이름","","","",false))
                    }
                    else{
                        if(networkCheck.isNetworkAvailable(applicationContext) && it.network_state){//네트워크 체크


                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if(!task.isSuccessful){
                                    return@OnCompleteListener
                                }

                                val token = task.result
                                    viewModel.updateToken(it.user_seq,token)
                            })
                        }
                    }
                })


            }

            override fun onAnimationEnd(p0: Animator?) {
                //애니메이션 종료
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()

            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {
                //애니메이션 다시 재생
            }

        })
    }


}