package com.dicoding.nutriseeapp.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.nutriseeapp.R
import com.dicoding.nutriseeapp.fragment.CameraFragment
import com.dicoding.nutriseeapp.fragment.HistoryDetailFragment
import com.dicoding.nutriseeapp.fragment.HistoryFragment
import com.dicoding.nutriseeapp.fragment.HomeFragment
import com.dicoding.nutriseeapp.fragment.LoadingFragment
import com.dicoding.nutriseeapp.fragment.ResultFragment
import com.dicoding.nutriseeapp.fragment.SearchFragment
import com.dicoding.nutriseeapp.fragment.UploadFragment
import com.dicoding.nutriseeapp.fragment.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fabCamera: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        fabCamera = findViewById(R.id.fab_camera)

        loadFragment(HomeFragment())

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.nav_history -> {
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.nav_user -> {
                    loadFragment(UserFragment())
                    true
                }
                else -> false
            }
        }

        fabCamera.setOnClickListener {
            loadFragment(CameraFragment())
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
            if (currentFragment is CameraFragment || currentFragment is UploadFragment || currentFragment is HistoryDetailFragment || currentFragment is ResultFragment || currentFragment is LoadingFragment) {
                bottomNavigation.visibility = View.GONE
                fabCamera.visibility = View.GONE
            } else {
                bottomNavigation.visibility = View.VISIBLE
                fabCamera.visibility = View.VISIBLE
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()

        // Hide bottom navigation and FAB if the fragment is CameraFragment or UploadFragment
        if (fragment is CameraFragment || fragment is UploadFragment || fragment is HistoryDetailFragment || fragment is ResultFragment || fragment is LoadingFragment) {
            bottomNavigation.visibility = View.GONE
            fabCamera.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
            fabCamera.visibility = View.VISIBLE
        }
    }
}
