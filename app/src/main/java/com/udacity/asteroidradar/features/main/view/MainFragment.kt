package com.udacity.asteroidradar.features.main.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.data.BaseFragment
import com.udacity.asteroidradar.data.NavigationCommand
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.features.main.adapter.AsteroidItemAdapter
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import com.udacity.asteroidradar.util.AppSharedMethods.setDisplayHomeAsUpEnabled
import com.udacity.asteroidradar.util.AppSharedMethods.setTitle
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
        mBinding = FragmentMainBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            mLifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
        setTitle(mActivity.getString(R.string.app_name))
        setDisplayHomeAsUpEnabled(false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initAsteroidRecyclerView()
        initViewModelObserver()
    }

    private fun initMenu() {

        val menuHost: MenuHost = mActivity

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                with(mViewModel){
                    updateSelectedItem(0)
                    updateFilter(
                        when (menuItem.itemId) {
                            R.id.show_week_menu -> AsteroidApiFilter.SHOW_WEEK
                            R.id.show_today_menu -> AsteroidApiFilter.SHOW_TODAY
                            else -> AsteroidApiFilter.SHOW_SAVED
                        }
                    )
                }

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initViewModelObserver() {

    }

    private fun initAsteroidRecyclerView() {
        with(mViewModel){
            mBinding.asteroidRecycler.adapter =
                AsteroidItemAdapter(AsteroidModel.getAsteroidModelCallback()) { item, position ->
                    updateSelectedItem(position)
                    navigationCommandSingleLiveEvent.value = NavigationCommand.To(
                        MainFragmentDirections.actionShowDetail(item)
                    )
                }
        }
    }

}
