package com.example.myapplication.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.data.firebase.FirebaseDataSource
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.NavHeaderMainBinding
import com.example.myapplication.databinding.ToolbarAddonChatBinding
import com.example.myapplication.utils.SharedPreferencesUtil
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_chats,
                R.id.nav_groups,
                R.id.nav_settings,
                R.id.nav_users,
                R.id.nav_start
            ), drawerLayout
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_start -> binding.appBarMain.fab.visibility = View.GONE
                R.id.nav_login -> binding.appBarMain.fab.visibility = View.GONE
                R.id.nav_new_account -> binding.appBarMain.fab.visibility = View.GONE
                R.id.nav_settings -> binding.appBarMain.fab.visibility = View.GONE
                R.id.nav_chat -> binding.appBarMain.fab.visibility = View.GONE
                R.id.nav_profile -> binding.appBarMain.fab.visibility = View.GONE
                R.id.nav_users -> binding.appBarMain.fab.visibility = View.GONE
                else -> binding.appBarMain.fab.visibility = View.VISIBLE
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.appBarMain.fab.setOnClickListener { view ->
            navController.navigate(R.id.action_chats_to_users)
        }
        observeUserInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //TODO переписать на databinding
    private fun observeUserInfo() {
        val headerLayout = binding.navView.getHeaderView(0)
        viewModel.userInfo.observe(this, {
            headerLayout.findViewById<TextView>(R.id.profileName).text = it.displayName
            headerLayout.findViewById<TextView>(R.id.profileStatus).text = it.status
            val profileImage = findViewById<ImageView>(R.id.profileImage)

            when (it.profileImageUrl) {
                "" -> profileImage.setImageResource(R.drawable.ic_baseline_account_circle)
                else -> Picasso.get().load(it.profileImageUrl)
                    .error(R.drawable.ic_baseline_account_circle).into(profileImage)
            }
        })
    }

    fun showGlobalProgressBar(show: Boolean) {
        if (show) binding.appBarMain.mainProgressBar.visibility = View.VISIBLE
        else binding.appBarMain.mainProgressBar.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onPause() {
        super.onPause()
        FirebaseDataSource.dbInstance.goOffline()
    }

    override fun onResume() {
        FirebaseDataSource.dbInstance.goOnline()
        super.onResume()
    }
}