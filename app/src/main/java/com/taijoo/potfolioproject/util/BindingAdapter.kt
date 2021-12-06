package com.taijoo.potfolioproject.util

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.taijoo.potfolioproject.data.model.UserData

import com.taijoo.potfolioproject.presentation.view.CustomFragmentStateAdapter
import com.taijoo.potfolioproject.presentation.view.MainActivity
import com.taijoo.potfolioproject.presentation.view.user.UserAdapter
import com.taijoo.potfolioproject.presentation.view.user.UserClickInterface
import nl.joery.animatedbottombar.AnimatedBottomBar
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.taijoo.potfolioproject.R


object BindingAdapter {


    //메인 뷰페이지 프래그먼트 바인딩
    @BindingAdapter(value = [ "vp_fragment", "mainActivity","animatedBottomBar"])
    @JvmStatic
    fun bindMainViewPager(viewPager2: ViewPager2,fragmentArray : ArrayList<Fragment>,activity : MainActivity , animatedBottomBar: AnimatedBottomBar){
        if(viewPager2.adapter == null){
            viewPager2.setHasTransientState(true)
            viewPager2.adapter = CustomFragmentStateAdapter(activity,fragmentArray)
            viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            animatedBottomBar.setupWithViewPager2(viewPager2)
        }
    }


    @BindingAdapter(value = ["bind_user_list","userClickInterface"])
    @JvmStatic
    fun bindUserList(recyclerView: RecyclerView, userList : MutableList<UserData>? , userClickInterface: UserClickInterface){

        if(recyclerView.adapter == null){
            val lm = LinearLayoutManager(recyclerView.context)
            val adapter = UserAdapter(userClickInterface)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = lm
            recyclerView.adapter = adapter

        }
        if (userList != null) {
            (recyclerView.adapter as UserAdapter).userList = userList
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }


    @BindingAdapter("profileImg")
    @JvmStatic
    fun bindViewProfile(imageView: ImageView, url : String?) {
        val requestOptions: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(false)
            .circleCrop()
            .priority(Priority.LOW)

        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.basics_profile)
            .fallback(R.drawable.basics_profile)
            .apply(requestOptions)
            .into(imageView)

    }

    @BindingAdapter("profileSettingImg")
    @JvmStatic
    fun bindViewLocalProfile(imageView: ImageView, url : String?) {

        val requestOptions: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(false)
            .centerCrop()
            .priority(Priority.LOW)


        Glide.with(imageView.context)
            .load(url)
            .fallback(R.drawable.basics_profile)
            .apply(requestOptions)
            .into(imageView)


    }

    @BindingAdapter("settingImg")
    @JvmStatic
    fun bindViewSetting(imageView: ImageView, url : Int?) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)

    }

    @BindingAdapter("galleryZoomImg")
    @JvmStatic
    fun bindViewGalleyZoom(imageView: ImageView, url : String?) {

        val requestOptions: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .skipMemoryCache(false)
            .centerInside()
            .priority(Priority.LOW)


        Glide.with(imageView.context)
            .asBitmap()
            .load(url)
            .apply(requestOptions)
            .thumbnail(0.1f)
            .into(imageView)

    }



    @BindingAdapter("galleryImg")
    @JvmStatic
    fun bindViewGalley(imageView: ImageView, url : String?) {

        val requestOptions: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(false)
            .centerCrop()
            .priority(Priority.LOW)

        Glide.with(imageView.context)
            .load(url)
            .apply(requestOptions)
            .thumbnail(0.1f)
            .dontAnimate()
            .into(imageView)


    }



    @BindingAdapter("background_color")
    @JvmStatic
    fun bindViewBackground(constraintLayout: ConstraintLayout, icon_color_position : Int?) {
        var colorDialogItem:ArrayList<ColorDialogItem> = ArrayList()//컬러 어레이
        val colorCode = Color_Code()//컬러 코드
        colorDialogItem = colorCode.setColor()

        constraintLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(
            colorDialogItem[icon_color_position!!].color))

    }

    @BindingAdapter("background_color_custom")
    @JvmStatic
    fun bindViewBackgroundCustom(constraintLayout: ConstraintLayout, icon_color_position : Int?) {
        var colorDialogItem:ArrayList<ColorDialogItem> = ArrayList()//컬러 어레이
        val colorCode = Color_Code()//컬러 코드
        colorDialogItem = colorCode.setColor()

        constraintLayout.setBackgroundColor(Color.parseColor(colorDialogItem[icon_color_position!!].color))

    }

    @BindingAdapter("background_color_linear")
    @JvmStatic
    fun bindViewBackgroundLinearLayout(linear: LinearLayout, icon_color_position : Int?) {
        var colorDialogItem:ArrayList<ColorDialogItem> = ArrayList()//컬러 어레이
        val colorCode = Color_Code()//컬러 코드
        colorDialogItem = colorCode.setColor()

        linear.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorDialogItem[icon_color_position!!].color))

    }

}