package com.litmethod.android.ui.Onboarding.SplashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivitySplashScreenBinding
import com.litmethod.android.models.GetCustomers.GetCutomerRequest
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.SplashScreenRepository
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.DashBoardActivity
import com.litmethod.android.ui.Onboarding.ProfileScreen.ProfileActivity
import com.litmethod.android.ui.Onboarding.SplashScreen.ViewmModel.SpalshScreenViewModelFactory
import com.litmethod.android.ui.Onboarding.SplashScreen.ViewmModel.SplashScreenViewModel
import com.litmethod.android.ui.Onboarding.WelcomeScreen.WelcomeActivity
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DataPreferenceObject

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var viewModel: SplashScreenViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var dataPereREnceObject: DataPreferenceObject
    var token =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
       setUpUi()
        viewModelSetup()
    }

    private fun setUpUi() {

        dataPereREnceObject = DataPreferenceObject(this)
        getToken()
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, SpalshScreenViewModelFactory(SplashScreenRepository(retrofitService),this)).get(
                SplashScreenViewModel::class.java
            )
        loginResponse()
    }

    private fun  getToken(){
        dataPereREnceObject.getTheData("userToken").asLiveData().observe(this){
            Log.d("theacceestoke","the accesss ${it.toString()}")
            if (it.toString().isNotEmpty()){
                Log.d("theacceestoke","the accesss ${it.toString()}")
                token = it
                viewModel.checkGetCustomer(it.toString(), GetCutomerRequest(AppConstants.getCustomer))
            } else
            {
                intentActivityWithFinish(this@SplashScreenActivity, WelcomeActivity::class.java)
            }


        }
    }

    private fun loginResponse(){
        viewModel.getCustomerResponse.observe(this, Observer {
            Log.d("singetoneobject","thr object is ${it}")
            if (it.serverResponse.statusCode == 200){
                if (it.result.profileDetails.onbordingStatus ==true){
                    BaseResponseDataObject.profilePageData=it.result.profileDetails
                    BaseResponseDataObject.accessToken = token

                    intentActivityWithFinish(this@SplashScreenActivity, DashBoardActivity::class.java)

                }else{
                    BaseResponseDataObject.profilePageData=it.result.profileDetails
                    BaseResponseDataObject.accessToken = token
                    intentActivityWithFinish(this@SplashScreenActivity, ProfileActivity::class.java)
                }

            }else{
                intentActivityWithFinish(this@SplashScreenActivity, WelcomeActivity::class.java)
//                toastMessageShow("SomeThing went wrong")
            }


        })

        viewModel.errorMessage.observe(this, Observer {
            toastMessageShow(it.toString())
        })
    }

    open fun intentActivityWithFinish(thisActivity: Context, cls:Class<*>) {
        val i = Intent(thisActivity, cls)
        startActivity(i)
        finish()
        overridePendingTransition(
            R.anim.slide_from_right,
            R.anim.slide_to_left
        )
    }
    fun toastMessageShow(msg:String){
        val toast = Toast.makeText(applicationContext,msg, Toast.LENGTH_SHORT)
        toast.show()
    }
}