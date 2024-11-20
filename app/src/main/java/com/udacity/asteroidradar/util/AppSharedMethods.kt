package com.udacity.asteroidradar.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

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

    fun Activity.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(findViewById(android.R.id.content), message, duration).show()
    }

    fun Activity.showSnackBar(message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(findViewById(android.R.id.content), getString(message), duration).show()
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

    fun View.applyWindowsPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun Activity.setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Set the listener to customize the status bar area
            window.decorView.apply {
                setOnApplyWindowInsetsListener { v, insets ->
                    val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                    val navigationBarInsets = insets.getInsets(WindowInsets.Type.navigationBars())
                    // Create a view for the status bar background
                    val statusBarBackground = View(this@setStatusBarColor).apply {
                        setBackgroundColor(color)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            statusBarInsets.top // Height matches the status bar inset
                        )
                    }
                    // Create a view for the navigation bar background
                    val navigationBarBackground = View(this@setStatusBarColor).apply {
                        setBackgroundColor(color)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            navigationBarInsets.bottom // Height matches the navigation bar inset
                        )
                    }
                    // Add the view to the decorView
                    (this as ViewGroup).apply {
                        if (statusBarBackground.parent == null) {
                            addView(statusBarBackground)
                        }
                        if (navigationBarBackground.parent == null) {
                            addView(navigationBarBackground)
                        }
                    }
                    insets
                }
                // Request insets to trigger the listener
                requestApplyInsets()
            }

            if (color == Color.BLACK || ColorUtils.calculateLuminance(color) < 0.5) {
                // If the color is dark, use light icons
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                // If the color is light, use dark icons
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }

            if (ColorUtils.calculateLuminance(color) < 0.5) {
                // Dark color for navigation bar -> Light icons
                window.insetsController?.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
            } else {
                // Light color for navigation bar -> Dark icons
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

        } else {
            // Fallback for older versions
            window.apply {
                statusBarColor = color
                navigationBarColor = color
                decorView.systemUiVisibility = when {
                    ColorUtils.calculateLuminance(statusBarColor) >= 0.5 -> {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                    ColorUtils.calculateLuminance(navigationBarColor) >= 0.5 -> {
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    }
                    else -> 0 // No flags for dark icons
                }
            }
        }
    }

    fun Context.getCompatColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

}