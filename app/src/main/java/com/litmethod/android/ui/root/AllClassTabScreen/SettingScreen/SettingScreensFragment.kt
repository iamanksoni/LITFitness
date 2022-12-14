package com.litmethod.android.ui.root.AllClassTabScreen.SettingScreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.litmethod.android.R
import com.litmethod.android.Webview.WebViewActivity
import com.litmethod.android.databinding.FragmentSettingScreensBinding
import com.litmethod.android.devicemanager.DeviceManagerActivity
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.SettingFragmentRepository
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.SettingScreen.ViewModel.SettingScreenViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.SettingScreen.ViewModel.SettingScreenViewModelFactory
import com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen.HomeScreenFragment
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.AppConstants.Companion.MEMBERSHIP_URL
import com.litmethod.android.utlis.AppConstants.Companion.URL_PRIVACY_POLICY
import com.litmethod.android.utlis.AppConstants.Companion.URL_SUPPORT
import com.litmethod.android.utlis.AppConstants.Companion.URL_TERMS_CONDITION
import com.litmethod.android.utlis.DataPreferenceObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SettingScreensFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentSettingScreensBinding
    lateinit var viewModel: SettingScreenViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var dataPereREnceObject: DataPreferenceObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingScreensBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        clickListener()
        viewModelSetup()
        dataPereREnceObject = DataPreferenceObject(requireContext())
    }


    private fun setupUi() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }

        binding.tvPrivacyPolicy.setOnClickListener {


            var intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(AppConstants.WEB_URL, URL_PRIVACY_POLICY)
            startActivity(intent)

        }
//
        binding.tvSupport.setOnClickListener {
            var intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(AppConstants.WEB_URL, URL_SUPPORT)
            startActivity(intent)
        }
//
        binding.tvTermCondition.setOnClickListener {
            var intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(AppConstants.WEB_URL, URL_TERMS_CONDITION)
            startActivity(intent)
        }
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, SettingScreenViewModelFactory(
                SettingFragmentRepository(retrofitService),requireContext())
            ).get(
                SettingScreenViewModel::class.java
            )
        loginResponse()
    }

    private fun clickListener(){
        binding.ibBackButton.setOnClickListener(this)
        binding.cvSignout.setOnClickListener(this)
        binding.tvHeartRate.setOnClickListener(this)
        binding.cvMembership.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
                ft.replace(R.id.container, HomeScreenFragment(), "NewFragmentTag")
                ft.commit()
            }
            R.id.cv_signout -> {
                viewModel.checkgetLogOut(
                    BaseResponseDataObject.accessToken,
                    LogOutRequest("logOut")
                )

            }
            R.id.tv_heart_rate -> {
                startActivity(Intent(context, DeviceManagerActivity::class.java))
            }


            R.id.cv_membership -> {
                Toast.makeText(context,"Included",Toast.LENGTH_SHORT).show()
                var subscriptionInt =
                    Intent(activity, WebViewActivity::class.java)
                subscriptionInt.putExtra(AppConstants.WEB_URL, MEMBERSHIP_URL)
                startActivity(subscriptionInt)
            }

        }
    }


    private  fun loginResponse(){
        viewModel.logOutResponse.observe(viewLifecycleOwner, Observer {
           if (it.serverResponse.statusCode==200){
             clearTheLocalDatastrore()
           }

            Log.d("theLogoutResponse","the logout response is $it")
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun  clearTheLocalDatastrore(){
     GlobalScope.launch {
         dataPereREnceObject.deleteAllData()
         val intent = Intent(requireContext(),LoginActivity::class.java)
         intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
         intent.putExtra("showBackButton","showbackbutton")
         startActivity(intent)

     }

    }

}