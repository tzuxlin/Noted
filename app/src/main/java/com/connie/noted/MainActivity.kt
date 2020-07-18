package com.connie.noted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.connie.noted.databinding.ActivityMainBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.login.UserManager
import com.connie.noted.util.CurrentFragmentType
import com.connie.noted.util.DrawerToggleType
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { getVmFactory() }


    private lateinit var binding: ActivityMainBinding

    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        setupDrawer()
        setupNavController()

        Log.d("Connie", "UserManager.user = ${UserManager.user.value}")

        viewModel.checkLogin()



    }


    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigation_noteFragment -> {
                    findNavController(R.id.myNavHostFragment).navigate(NaviDirections.actionGlobalNoteFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_boardFragment -> {
                    findNavController(R.id.myNavHostFragment).navigate(NaviDirections.actionGlobalBoardFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_exploreFragment -> {
                    findNavController(R.id.myNavHostFragment).navigate(NaviDirections.actionGlobalExploreFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_profileFragment -> {
                    findNavController(R.id.myNavHostFragment).navigate(NaviDirections.actionGlobalProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }

    /**
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment, it better than another design
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView]
     */
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.noteFragment -> CurrentFragmentType.NOTE
                R.id.boardFragment -> CurrentFragmentType.BOARD
                R.id.exploreFragment -> CurrentFragmentType.EXPLORE
                R.id.profileFragment -> CurrentFragmentType.PROFILE
//                R.id.boardPageFragment -> CurrentFragmentType.BOARDDETAIL
//                R.id.notePageFragment -> CurrentFragmentType.NOTEDETAIL
                else -> viewModel.currentFragmentType.value
            }
        }
    }


    /**
     * Set up [androidx.drawerlayout.widget.DrawerLayout] with [androidx.appcompat.widget.Toolbar]
     */
    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }


        // Observe current drawer toggle to set the navigation icon and behavior
        viewModel.currentDrawerToggleType.observe(this, Observer { type ->

            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
            binding.toolbar.setNavigationIcon(
                when (type) {
                    DrawerToggleType.BACK -> R.drawable.toolbar_back
                    else -> R.drawable.toolbar_menu
                }
            )

            actionBarDrawerToggle?.setToolbarNavigationClickListener {
                when (type) {
                    DrawerToggleType.BACK -> onBackPressed()
                    else -> {
                    }
                }
            }
        })
    }



}
