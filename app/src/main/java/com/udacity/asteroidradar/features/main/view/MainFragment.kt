package com.udacity.asteroidradar.features.main.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.data.BaseFragment
import com.udacity.asteroidradar.data.NavigationCommand
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import com.udacity.asteroidradar.features.main.adapter.AsteroidItemAdapter
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment() {

    private lateinit var mBinding: FragmentMainBinding
    override val mViewModel: MainViewModel by inject()

    private lateinit var mActivity: FragmentActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            mActivity = context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentMainBinding.inflate(inflater)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel
        setHasOptionsMenu(true)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAsteroidRecyclerView()
        initViewModelObserver()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        mViewModel.updateFilter(
            when (item.itemId) {
                R.id.show_week_menu -> AsteroidApiFilter.SHOW_WEEK
                R.id.show_today_menu -> AsteroidApiFilter.SHOW_TODAY
                else -> AsteroidApiFilter.SHOW_SAVED
            }
        )
        return true

    }

    private fun initAsteroidRecyclerView() {

        mBinding.asteroidRecycler.adapter =
            AsteroidItemAdapter(AsteroidItemAdapter.AsteroidClickListener {
                mViewModel.navigationCommand.value =
                    NavigationCommand.To(MainFragmentDirections.actionShowDetail(it))
            })

    }

    private fun initViewModelObserver() {
    }
}
