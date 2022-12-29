package com.litmethod.android.ui.root

import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.navigation.NavigationBarView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityDashBoardBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.AccountScreenFragment
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ClassesFragment
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.SettingScreen.SettingScreensFragment
import com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen.HomeScreenFragment
import com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen.LiveScreenFragment
import com.litmethod.android.utlis.DataPreferenceObject


class DashBoardActivity : BaseActivity() {
    lateinit var binding: ActivityDashBoardBinding
    lateinit var dataPreferenceObject: DataPreferenceObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        setUpUi()
        dataPreferenceObject = DataPreferenceObject(this)
        getTheToken()

        val intentString = intent.getStringExtra("nextActivty")
        if (intentString=="filterScreen"){
            loadFragment(ClassesFragment())
        }
    }

    fun getTheToken(){
        dataPreferenceObject.getTheData("userToken").asLiveData().observe(this) {
            BaseResponseDataObject.token = it.toString()
            BaseResponseDataObject.accessToken = it.toString()
        }
    }

    private fun setUpUi() {
        var menuView = binding.bottomNav.getChildAt(0) as BottomNavigationMenuView
        try {
            for (i in 0 until menuView.childCount) {
                if (i == 2) {
                    var iconView = menuView.getChildAt(i) as BottomNavigationItemView
                    val displayMetrics = resources.displayMetrics
                    iconView.setIconSize(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 40f,
                            displayMetrics
                        ).toInt()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loadFragment(HomeScreenFragment())
        binding.bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeScreenFragment())
                }
                R.id.classs -> {
                    loadFragment(ClassesFragment())
                }
                R.id.live -> {
                    loadFragment(LiveScreenFragment())
                }
                R.id.settings -> {
                    loadFragment(SettingScreensFragment())
                }
                R.id.account -> {
                    loadFragment(AccountScreenFragment())
                }
            }
            true
        })
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}