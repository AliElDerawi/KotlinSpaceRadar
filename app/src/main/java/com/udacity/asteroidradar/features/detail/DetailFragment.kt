package com.udacity.asteroidradar.features.detail


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.data.BaseFragment
import com.udacity.asteroidradar.data.BaseViewModel
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import org.koin.android.ext.android.inject

class DetailFragment : BaseFragment() {
    override val mViewModel: MainViewModel by inject()


    private lateinit var mActivity: FragmentActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        if (arguments != null) {
            val asteroid = DetailFragmentArgs.fromBundle(
                requireArguments()
            ).selectedAsteroid
            binding.asteroid = asteroid
        }


        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(mActivity)
            .setMessage(mActivity.getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
