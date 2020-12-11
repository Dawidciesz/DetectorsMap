package com.fiesta.detector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fiesta.detector.fragments.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mapFragment = MapFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.clearAnimation()
        setCurrentFragment(mapFragment)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_map -> {
                setCurrentFragment(mapFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite_list -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pictures -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite_list -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()
        }
}
