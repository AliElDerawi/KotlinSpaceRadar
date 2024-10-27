package com.udacity.asteroidradar.features.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.NavigationCommand
import com.udacity.asteroidradar.databinding.ActivityMainBinding
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by inject()
    private lateinit var navController: NavController
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.title = getString(R.string.app_name)
        initListener()
        initViewModelObserver()

    }

    private fun initListener() {
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        NavigationUI.setupActionBarWithNavController(this, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    private fun initViewModelObserver() {
        mViewModel.navigationCommandSingleLiveEvent.observe(this) { command ->
            Timber.d("initViewModelObserver:command: $command")
            when (command) {
                is NavigationCommand.To -> navController.navigate(command.directions)
                is NavigationCommand.Back -> navController.popBackStack()
                is NavigationCommand.BackTo -> navController.popBackStack(
                    command.destinationId, false
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
