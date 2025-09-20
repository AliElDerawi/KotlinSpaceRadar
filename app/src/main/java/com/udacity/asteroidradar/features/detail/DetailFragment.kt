package com.udacity.asteroidradar.features.detail


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.BaseFragment
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import com.udacity.asteroidradar.util.AppSharedMethods.setDisplayHomeAsUpEnabled
import com.udacity.asteroidradar.util.AppSharedMethods.setTitle

class DetailFragment : BaseFragment() {

    override val mViewModel: MainViewModel by activityViewModels()
    private lateinit var mActivity: FragmentActivity
    private lateinit var mBinding: FragmentDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDetailBinding.inflate(inflater)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                detailFragment = this@DetailFragment
                arguments?.let {
                    val asteroidModel = DetailFragmentArgs.fromBundle(
                        requireArguments()
                    ).selectedAsteroid
                    asteroid = asteroidModel
                }
            }
        setTitle(mBinding.asteroid?.codename ?: mActivity.getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(true)
        return mBinding.root
    }

    fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(mActivity)
            .setMessage(mActivity.getString(R.string.text_astronomical_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
