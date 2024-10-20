package com.udacity.asteroidradar.features.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.NavigationCommand
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by inject()
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
        initViewModelObserver()
    }

    private fun initListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initViewModelObserver() {
        mViewModel.navigationCommand.observe(this) { command ->

            Timber.d("initViewModelObserver:command: " + command.toString())

            when (command) {
                is NavigationCommand.To -> navController.navigate(command.directions)
                is NavigationCommand.Back -> onBackPressed()
                is NavigationCommand.BackTo -> navController.popBackStack(
                    command.destinationId, false
                )
            }
        }
    }
}
