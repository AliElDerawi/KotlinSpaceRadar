package com.udacity.asteroidradar.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.channels.Channel

/**
 * Base class for View Models to declare the common Channel and StateFlow objects in one place
 */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    val navigationCommandChannel = Channel<NavigationCommand>(Channel.BUFFERED)
    val showErrorMessageChannel = Channel<String>(Channel.BUFFERED)
    val showSnackBarChannel = Channel<String>(Channel.BUFFERED)
    val showSnackBarIntChannel = Channel<Int>(Channel.BUFFERED)
    val showToastChannel = Channel<String>(Channel.BUFFERED)
    val showToastIntChannel = Channel<Int>(Channel.BUFFERED)

    var showLoadingChannel = Channel<Boolean>(Channel.BUFFERED)

    val showNoDataChannel = Channel<Boolean>(Channel.BUFFERED)
}