package com.udacity.asteroidradar.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.asteroidradar.util.SingleLiveEvent

/**
 * Base class for View Models to declare the common LiveData objects in one place
 */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    val navigationCommandSingleLiveEvent = SingleLiveEvent<NavigationCommand>()
    val showErrorMessageSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarIntSingleLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    val showToastSingleLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()
    val showToastIntSingleLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    var showLoadingSingleLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showNoDataSingleLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
}