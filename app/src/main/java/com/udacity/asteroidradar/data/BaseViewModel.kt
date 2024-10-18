package com.udacity.asteroidradar.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.asteroidradar.util.SingleLiveEvent

/**
 * Base class for View Models to declare the common LiveData objects in one place
 */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    val navigationCommand = SingleLiveEvent<NavigationCommand>()
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()
    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val showToastInt: SingleLiveEvent<Int> = SingleLiveEvent()
    var showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showNoData: SingleLiveEvent<Boolean> = SingleLiveEvent()
}