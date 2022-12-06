package com.litmethod.android.ui.Onboarding.WelcomeScreen

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityWelcomeBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Dashboard.DashBoardActivity
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity
import com.litmethod.android.ui.Onboarding.SignUpScreen.SignUpActivity
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle


class WelcomeActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityWelcomeBinding
    private var welcomeViewPagerAdapter: WelcomeViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        setViewPager()
        clickListener()
    }
    private fun setViewPager(){
        welcomeViewPagerAdapter = WelcomeViewPagerAdapter(this,this@WelcomeActivity)
        binding.viewpager.adapter = welcomeViewPagerAdapter

        binding.indicatorView.apply {
            setSliderColor(resources.getColor(R.color.grey), resources.getColor(R.color.red))
            setSliderWidth(resources.getDimension(R.dimen.dp_30))
            setSliderHeight(resources.getDimension(R.dimen.dp_4))
            setSlideMode(IndicatorSlideMode.SMOOTH)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setPageSize(binding.viewpager!!.adapter!!.itemCount)
            notifyDataChanged()
        }
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.indicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicatorView.onPageSelected(position)
            }
        })
    }

    private fun clickListener(){
        binding.btnLogIn.setOnClickListener(this)
        binding.btnFreeTrial.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_log_in ->{
                //intentActivityWithFinish(this@WelcomeActivity, LoginActivity::class.java)
                intentActivityWithFinish(this@WelcomeActivity, LoginActivity::class.java)
            }
            R.id.btn_free_trial ->{
                intentActivityWithFinish(this@WelcomeActivity, SignUpActivity::class.java)
            }
        }
    }
}