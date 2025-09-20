package com.udacity.asteroidradar.features.main.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.NavigationCommand
import com.udacity.asteroidradar.databinding.ActivityMainBinding
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import com.udacity.asteroidradar.util.AppSharedMethods.applyWindowsPadding
import com.udacity.asteroidradar.util.AppSharedMethods.getCompatColor
import com.udacity.asteroidradar.util.AppSharedMethods.setStatusBarColorAndStyle
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModel()
    private lateinit var mNavController: NavController
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            root.applyWindowsPadding()
            setStatusBarColorAndStyle(getCompatColor(R.color.colorPrimary))
        }
        initListener()
        initViewModelObserver()
    }

    private fun initListener() {
        mNavController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        NavigationUI.setupActionBarWithNavController(this, mNavController)
        mAppBarConfiguration = AppBarConfiguration(mNavController.graph)
    }

    private fun initViewModelObserver() {
        mViewModel.navigationCommandSingleLiveEvent.observe(this) { command ->
            Timber.d("initViewModelObserver:command: $command")
            when (command) {
                is NavigationCommand.To -> mNavController.navigate(command.directions)
                is NavigationCommand.Back -> mNavController.popBackStack()
                is NavigationCommand.BackTo -> mNavController.popBackStack(
                    command.destinationId, false
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
    }
}
