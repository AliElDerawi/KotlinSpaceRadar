package com.udacity.asteroidradar.util

import android.app.Activity
import android.widget.Toast

object AppSharedMethods {

    private var mToast: Toast? = null

    fun Activity.showToast(message: Int, duration: Int = Toast.LENGTH_LONG) {
        mToast?.cancel()
        mToast = Toast.makeText(
            AsteroidStoreApp.getApp().applicationContext,
            getString(message),
            duration
        )
        mToast!!.show()
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
        mToast?.cancel()
        mToast = Toast.makeText(
            AsteroidStoreApp.getApp().applicationContext, message, duration
        )
        mToast!!.show()
    }

}