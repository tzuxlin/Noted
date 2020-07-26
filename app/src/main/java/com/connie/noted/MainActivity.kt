package com.connie.noted

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.GravityCompat.*
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.connie.noted.data.Note
import com.connie.noted.databinding.ActivityMainBinding
import com.connie.noted.ext.getVmFactory
import com.connie.noted.login.UserManager
import com.connie.noted.util.CurrentFilterType
import com.connie.noted.util.CurrentFragmentType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        binding.icon2changeViewType.setOnClickListener {

            when (viewModel.viewType.value) {
                0 -> viewModel.viewType.value = 1
                1 -> viewModel.viewType.value = 0
            }

        }

        binding.iconBack.setOnClickListener {
            findNavController(R.id.myNavHostFragment).navigateUp()
        }

        binding.iconNavDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(START)
        }

        setupDrawer()
        setupNavController()

        Log.d("Connie", "on Create UserManager.user = ${UserManager.user.value}")


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
                R.id.boardPageFragment -> CurrentFragmentType.BOARDDETAIL
                R.id.videoFragment, R.id.locationFragment, R.id.articleFragment -> CurrentFragmentType.NOTEDETAIL
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
//        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false
        binding.drawerNavView.setNavigationItemSelectedListener(this)


//        actionBarDrawerToggle = object : ActionBarDrawerToggle(
//            this,
//            binding.drawerLayout,
////            binding.toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        ) {
//            override fun onDrawerOpened(drawerView: View) {
//                super.onDrawerOpened(drawerView)
//
//            }
//        }.apply {
//            binding.drawerLayout.addDrawerListener(this)
//            syncState()
//        }


//        // Observe current drawer toggle to set the navigation icon and behavior
//        viewModel.currentDrawerToggleType.observe(this, Observer { type ->
//
//            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
//            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
//            binding.toolbar.setNavigationIcon(
//                when (type) {
//                    DrawerToggleType.BACK -> R.drawable.toolbar_back
//                    else -> R.drawable.toolbar_menu
//                }
//            )
//
//            actionBarDrawerToggle?.setToolbarNavigationClickListener {
//                when (type) {
//                    DrawerToggleType.BACK -> onBackPressed()
//                    else -> {
//                    }
//                }
//            }
//        })
    }


    private fun onSharedIntent() {

//        val noteFragment = supportFragmentManager.findFragmentById(R.id.noteFragment) as NoteFragment


        val receivedIntent = intent
        val receivedAction = receivedIntent.action
        val receivedType = receivedIntent.type
        if (receivedAction == Intent.ACTION_SEND) {

            // check mime type
            if (receivedType!!.startsWith("text/")) {
                val receivedText = receivedIntent
                    .getStringExtra(Intent.EXTRA_TEXT)

                receivedText?.let {
                    Log.e("ConnieCrawler", "receivedText = $it")

                    viewModel.urlString.value = it

                }

            } else if (receivedType.startsWith("image/")) {

                val receiveUri: Uri = receivedIntent
                    .getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri
                Log.e("ConnieCrawler", "receiveUri = $receiveUri")

            }
        } else if (receivedAction == Intent.ACTION_MAIN) {

            Log.e("ConnieCrawler", "onSharedIntent: nothing shared")

        } else {
            Log.e("ConnieCrawler", "onShareIntent: else situation")
        }
    }


    /**
     * Setup for NoteFragment and BoardPageFragment for navigating to certain Note page.
     */
    fun navigateToNote(note: Note) {

        Log.i("Connie", "Main activity: Note is clicked, $note")

        when (note.type) {
//            "Youtube" -> findNavController(R.id.myNavHostFragment).navigate(
//                NaviDirections.actionGlobalVideoFragment(
//                    note
//                )
//            )
//            "Location" -> findNavController(R.id.myNavHostFragment).navigate(
//                NaviDirections.actionGlobalLocationFragment(
//                    note
//                )
//            )
            else -> findNavController(R.id.myNavHostFragment).navigate(
                NaviDirections.actionGlobalArticleFragment(
                    note
                )
            )
        }

    }

    override fun onResume() {
        super.onResume()
        onSharedIntent()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        viewModel.currentFilterType.value = when (item.itemId) {

            R.id.drawer_liked -> {
                CurrentFilterType.LIKED
            }
            R.id.drawer_article -> {
                CurrentFilterType.ARTICLE
            }
            R.id.drawer_video -> {
                CurrentFilterType.VIDEO
            }
            R.id.drawer_location -> {
                CurrentFilterType.LOCATION
            }
            else -> {
                CurrentFilterType.ALL
            }
        }

        binding.drawerLayout.closeDrawer(START)

        return true
    }
}

