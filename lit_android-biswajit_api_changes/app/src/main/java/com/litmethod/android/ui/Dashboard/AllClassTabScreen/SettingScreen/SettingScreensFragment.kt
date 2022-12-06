package com.litmethod.android.ui.Dashboard.AllClassTabScreen.SettingScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.litmethod.android.R
import com.litmethod.android.databinding.FragmentLivescreenBinding
import com.litmethod.android.databinding.FragmentSettingScreensBinding
import com.litmethod.android.models.EditUserRequestNullable.EditUserRequestNullable
import com.litmethod.android.models.GetCustomers.GetCutomerRequest
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.network.FilterActivityRepository
import com.litmethod.android.network.RetrofitService
import com.litmethod.android.network.SettingFragmentRepository
import com.litmethod.android.ui.Dashboard.AccountTabScreen.AccountFragmentScreen.AccountScreenFragment
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.Util.AllClassesDataObject
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.SettingScreen.ViewModel.SettingScreenViewModel
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.SettingScreen.ViewModel.SettingScreenViewModelFactory
import com.litmethod.android.ui.Onboarding.LoginScreen.LoginActivity
import com.litmethod.android.utlis.DataPreferenceObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SettingScreensFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentSettingScreensBinding
    lateinit var viewModel: SettingScreenViewModel
    private val retrofitService = RetrofitService.getInstance()
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

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
//                Log.d("imgbutt","button clicked")
//                val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
//                ft.replace(R.id.container, AccountScreenFragment(), "NewFragmentTag")
//                ft.commit()
            }
            R.id.cv_signout ->{
                viewModel.checkgetLogOut(AllClassesDataObject.accessToken, LogOutRequest("logOut"))

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