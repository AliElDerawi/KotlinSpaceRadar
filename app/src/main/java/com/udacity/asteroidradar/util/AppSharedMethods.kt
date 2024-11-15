package com.udacity.asteroidradar.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

object AppSharedMethods {

    private var mToast: Toast? = null

    fun Activity.showToast(message: Int, duration: Int = Toast.LENGTH_LONG) {
        mToast?.cancel()
        mToast = Toast.makeText(AsteroidStoreApp.getApp().applicationContext, getString(message), duration)
        mToast!!.show()
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
        mToast?.cancel()
        mToast = Toast.makeText(
            AsteroidStoreApp.getApp().applicationContext, message, duration
        )
        mToast!!.show()
    }

    fun Activity.getCompatColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun Activity.getCompatColorStateList(color: Int): ColorStateList {
        return ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources, color, null
            )
        )
    }

    fun Fragment.setTitle(title: String) {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = title
        }
    }

    fun Fragment.setDisplayHomeAsUpEnabled(bool: Boolean) {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                bool
            )
        }
    }
}