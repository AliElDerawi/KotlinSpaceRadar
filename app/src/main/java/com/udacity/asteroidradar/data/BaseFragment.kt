package com.udacity.asteroidradar.data

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
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
            showErrorMessage.observe(viewLifecycleOwner) {
                showToast(it)
            }
            showToast.observe(viewLifecycleOwner) {
                showToast(it)
            }
            showSnackBar.observe(viewLifecycleOwner) {
                Snackbar.make(this@BaseFragment.requireView(), it, Snackbar.LENGTH_LONG).show()
            }
            showSnackBarInt.observe(viewLifecycleOwner) {
                Snackbar.make(
                    this@BaseFragment.requireView(), mActivity.getString(it), Snackbar.LENGTH_LONG
                ).show()
            }

            showToastInt.observe(viewLifecycleOwner) {
                showToast(mActivity.getString(it))
            }

            showLoading.observe(viewLifecycleOwner) {
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