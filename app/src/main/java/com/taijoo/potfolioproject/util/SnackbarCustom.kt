package com.taijoo.potfolioproject.util

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.taijoo.potfolioproject.R

class SnackbarCustom  {

    @SuppressLint("ShowToast")
    fun snackBar(view : View, text : String) : Snackbar{

        val snackBar = Snackbar.make(view,text,Snackbar.LENGTH_SHORT)
        snackBar.view.setBackgroundColor(view.context.getColor(R.color.color_333333))
        snackBar.setTextColor(view.context.getColor(R.color.white))

        return snackBar
    }
}