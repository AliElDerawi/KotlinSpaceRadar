package com.udacity.asteroidradar.data

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.util.AppSharedMethods.showSnackBar
import com.udacity.asteroidradar.util.AppSharedMethods.showToast

/**
 * Base Fragment to observe on the common LiveData objects
 */
abstract class BaseFragment : Fragment() {

    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */
    abstract val mViewModel: BaseViewModel
    private lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    override fun onStart() {
        super.onStart()

        with(mViewModel) {
            showErrorMessageSingleLiveEvent.observe(viewLifecycleOwner) {
                showToast(it)
            }
            showToastSingleLiveEvent.observe(viewLifecycleOwner) {
                showToast(it)
            }
            showToastIntSingleLiveEvent.observe(viewLifecycleOwner) {
                showToast(mActivity.getString(it))
            }
            showSnackBarSingleLiveEvent.observe(viewLifecycleOwner) {
               mActivity.showSnackBar(it)
            }
            showSnackBarIntSingleLiveEvent.observe(viewLifecycleOwner) {
                mActivity.showSnackBar(it)
            }
            showLoadingSingleLiveEvent.observe(viewLifecycleOwner) {
                if (it) {
                    showWaiteDialog()
                } else {
                    hideWaiteDialog()
                }
            }
        }
    }

    private fun showWaiteDialog() {

    }

    private fun hideWaiteDialog() {

    }
}