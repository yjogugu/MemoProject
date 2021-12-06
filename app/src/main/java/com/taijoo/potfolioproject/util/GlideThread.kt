package com.taijoo.potfolioproject.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import java.util.concurrent.ExecutionException

internal class GlideThread : Runnable {

    var uri: String? = null
    var stopThread = false
    var context : Context

    constructor(context : Context) {
        this.context = context
    }

    constructor(uri: String? , context : Context) {
        this.uri = uri
        this.context = context
    }


    override fun run() {
        if (!stopThread) {
            try {

                Glide.with(context)
                    .downloadOnly()
                    .diskCacheStrategy(DiskCacheStrategy.DATA) // Cache resource before it's decoded
                    .load(uri)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get() // Called on background thread


            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun stop() {
        stopThread = true
    }
}