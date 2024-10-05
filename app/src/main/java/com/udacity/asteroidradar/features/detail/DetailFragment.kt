package com.udacity.asteroidradar.features.detail


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.shoestore.data.BaseFragment
import com.udacity.shoestore.data.BaseViewModel

class DetailFragment : BaseFragment() {
    override val mViewModel: BaseViewModel
        get() = TODO("Not yet implemented")


    private lateinit var mActivity: Activity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this


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
