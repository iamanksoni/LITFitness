package com.litmethod.android.ui.Onboarding.YourEquipmentScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityYourEquipmentBinding
import com.litmethod.android.models.AcountScreenFragment.EditUserEquipment.EditUserEquipmentRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserInterest.EditUserInterestRequest
import com.litmethod.android.models.GetEquipment.Data
import com.litmethod.android.network.GetEquipmentRepository
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.network.RetrofitService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.Util.AllClassesDataObject
import com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel.InjuryViewModel
import com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel.InjuryViewModelFactory
import com.litmethod.android.ui.Onboarding.LitMembershipScreen.LitMembershipActivity
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.ViewModel.GetEquipmentViewModel
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.ViewModel.GetEquipmentViewModelFactory
import com.litmethod.android.ui.Onboarding.YourInterestScreen.YourInterestData
import com.litmethod.android.utlis.DataPreferenceObject
import com.litmethod.android.utlis.MarginItemDecoration
import com.litmethod.android.utlis.UiDataObject

class YourEquipmentActivity :  BaseActivity(),YourEquipmentAdapter.YourEquipmentAdapterListener,View.OnClickListener {
    lateinit var binding: ActivityYourEquipmentBinding
    val dataList: ArrayList<YourEquipmentData> = ArrayList<YourEquipmentData>()
    lateinit var viewModel: GetEquipmentViewModel
    private val retrofitService = RetrofitService.getInstance()
    var equipmentList: List<Data> = ArrayList<Data>()
    private var yourEquipmentAdapter:YourEquipmentAdapter? = null
    val eqipLevel: ArrayList<String> = ArrayList<String>()
    lateinit var dataPreferenceObject: DataPreferenceObject
    var acceessToken = ""
    var checkintentData:String? =null
    var switchActivity = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_your_equipment)
        checkintentData =intent.getStringExtra("accountFragment")
        if (checkintentData!=null){
            if (checkintentData=="AccountFrag"){
                switchActivity="AccountFrag"
                binding.btnNext.text = "Done"
            }else{
                switchActivity = "GoalsActivity"
            }
        }
        dataPreferenceObject = DataPreferenceObject(this)
        getTheAccessToken()
//        setUpAdapter()
          viewModelSetup()
        clickListener()
        viewModel.checkgetEquipment(AllClassesDataObject.accessToken)
    }

    private fun  getTheAccessToken(){
        dataPreferenceObject.getTheData("userToken").asLiveData().observe(this){
            acceessToken = it.toString()
        }
    }

    private fun setUpAdapter() {
        binding.spLoading.visibility = View.GONE
        dataList.clear()
        eqipLevel.clear()


        if (switchActivity=="AccountFrag"){
            equipmentList.map {
                val userid=it.id
                var isAddDAta= false
                AllClassesDataObject.profilePageData.equipment.map {
                    if (it.id==userid){
                        isAddDAta = true
                        eqipLevel.add(it.id)
                        dataList.add(YourEquipmentData(true))
                    }
                }
                if (isAddDAta==false){
                    dataList.add(YourEquipmentData(false))

                }
            }



        } else{
            equipmentList.map {
                dataList.add(YourEquipmentData(false))
            }
        }


//        equipmentList.map {
//            dataList.add(YourEquipmentData(false))
//        }
        binding.rvEquipmentType.layoutManager = RecyclerView.LinearLayoutManager(this@YourEquipmentActivity)
        yourEquipmentAdapter = YourEquipmentAdapter(dataList, this@YourEquipmentActivity,equipmentList)
        binding.rvEquipmentType.adapter = yourEquipmentAdapter
        binding.rvEquipmentType.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        yourEquipmentAdapter!!.setAdapterListener(this)
    }

    override fun onItemEquipClick(position: Int,data:String) {
        if(dataList[position].selectedItem){
            dataList[position].selectedItem = false
            eqipLevel.remove(data)

            Log.d("data","remove data arry is $eqipLevel")
        }else{
            dataList[position].selectedItem = true

            eqipLevel.add(data)
            Log.d("data","add data arry is $eqipLevel")
        }
        pageValueChecking()
        yourEquipmentAdapter!!.notifyDataSetChanged()
    }

    private fun pageValueChecking(){
        var  dataListFilter: List<YourEquipmentData> = dataList.filter { s -> s.selectedItem }
        if(dataListFilter.isNotEmpty()){
            nextButtonactive()
        }else{
            nextButtonInactive()
        }
    }

    private fun nextButtonInactive() {
        binding.btnNext.backgroundTintList = null
        binding.btnNext.isEnabled = false
        binding.btnNext.isClickable = false
    }

    private fun nextButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnNext.isEnabled = true
        binding.btnNext.isClickable = true
    }

    private fun clickListener(){
        binding.ibBackButton.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.btn_next ->{


                if (switchActivity=="AccountFrag"){
                    viewModel.checkEditUserForEquipment(AllClassesDataObject.accessToken,
                        EditUserEquipmentRequest("editUser",eqipLevel)
                    )

                }else{

                    Log.d("theuser","equip ${UiDataObject.eqipLevel} interest ${UiDataObject.interestData} goal ${UiDataObject.goalLevel} the token ${AllClassesDataObject.accessToken}, username ${UiDataObject.username} "+
                          "injuryy level ${UiDataObject.injuryLevel} has injury ${UiDataObject.has_injury} first name ${UiDataObject.firstName} and dob ${UiDataObject.etDob} and last name ${UiDataObject.lastName}" )
                    Log.d("theInterEsdata","equiptdata $eqipLevel")
//                    viewModel.checkSetImage(acceessToken,UiDataObject.body,UiDataObject.action)

                            viewModel.checkgetEditUser(AllClassesDataObject.accessToken,UiDataObject.etDob,UiDataObject.username,eqipLevel,UiDataObject.firstName,UiDataObject.gender,UiDataObject.goalLevel,UiDataObject.unitHeight,UiDataObject.HightFt,UiDataObject.HightIn.toFloat().toInt(),
                                UiDataObject.interestData,UiDataObject.lastName,UiDataObject.unitWeight,UiDataObject.Weight.toFloat().toInt(),
                              UiDataObject.injuryLevel,
                                UiDataObject.has_injury,UiDataObject.level,)


                }


            }
        }
    }


    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, GetEquipmentViewModelFactory(GetEquipmentRepository(retrofitService),this)).get(
                GetEquipmentViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.equipmentData.observe(this, Observer {

//            binding.spLoading.visibility = View.GONE
            Log.d("getData","the data is ${it}")
            equipmentList = it.result.data
            setUpAdapter()
//            toastMessageShow(it.serverResponse.message)

        })

        viewModel.errorMessage.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.editUsertData.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            Log.d("theuser","the userresp ${it}")
            if (it.serverResponse.statusCode==200){
                intentActivity(this@YourEquipmentActivity, LitMembershipActivity::class.java,"")
            }
//
        })

        viewModel.errorMessage2.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.setImageData.observe(this, Observer {
            try {
                Log.d("toastMessageShow",it.toString())
            }catch (e:Exception){
                e.printStackTrace()
            }
        })
        viewModel.editUserForEquipmenttResponse.observe(this, Observer {
            AllClassesDataObject.profilePageData = it.result.profileDetails
            finish()
        })
        viewModel.errorMessage4.observe(this, Observer {

        })
    }


}