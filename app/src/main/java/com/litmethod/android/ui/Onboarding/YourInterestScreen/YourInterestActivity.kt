package com.litmethod.android.ui.Onboarding.YourInterestScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityYourInterestBinding
import com.litmethod.android.models.AcountScreenFragment.EditUserInterest.EditUserInterestRequest
import com.litmethod.android.models.GetInterest.Data
import com.litmethod.android.network.GetInterestRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentActivity
import com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel.GetInterestViewModel
import com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel.GetInterestViewModelFactory
import com.litmethod.android.utlis.MarginItemDecoration
import com.litmethod.android.utlis.UiDataObject

class YourInterestActivity : BaseActivity(),YourInterestAdapter.LevelAdapterListener, View.OnClickListener{
    lateinit var binding: ActivityYourInterestBinding
    val dataList: ArrayList<YourInterestData> = ArrayList<YourInterestData>()
    private var yourInterestAdapter: YourInterestAdapter? = null
    lateinit var viewModel: GetInterestViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    var interestList: List<Data> = ArrayList<Data>()
    val interestData: ArrayList<String> = ArrayList<String>()
    var checkintentData:String? =null
    var switchActivity = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_your_interest)
        checkintentData =intent.getStringExtra("accountFragment")
        if (checkintentData!=null){
            if (checkintentData=="AccountFrag"){
                switchActivity="AccountFrag"
                binding.btnNext.text = "Done"
            }else{
                switchActivity = "GoalsActivity"
            }
        }
//        setUpAdapter()
        viewModelSetup()
        clickListener()
        viewModel.checkgetInterest(BaseResponseDataObject.accessToken)
    }


    private fun setUpAdapter() {
        binding.spLoading.visibility = View.GONE
        dataList.clear()
        interestData.clear()

        if (switchActivity=="AccountFrag"){
            var count=0
            interestList.map {
                val userid=it.id
                var isAddDAta= false
                BaseResponseDataObject.profilePageData.interest.map {
                    if (it.id==userid){
                        isAddDAta = true
                        count++
                        interestData.add(it.id)
                        dataList.add(YourInterestData(true, "#ffffff", true))
                    }
                }
                if (isAddDAta==false){
                    dataList.add(YourInterestData(false, "#ffffff", false))

                }
            }
            if (count==interestList.size){
                dataList.add(YourInterestData(true, "#ffffff", true))

            }else{
                dataList.add(YourInterestData(false, "#ffffff", false))

            }

        } else{
            interestList.map {
                dataList.add(YourInterestData(false, "#ffffff", false))
            }
            dataList.add(YourInterestData(false, "#ffffff", false))
        }




        binding.rvInterestType.layoutManager = RecyclerView.LinearLayoutManager(this@YourInterestActivity)
        yourInterestAdapter = YourInterestAdapter(dataList, this@YourInterestActivity,interestList)
        binding.rvInterestType.adapter = yourInterestAdapter
        binding.rvInterestType.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        yourInterestAdapter!!.setAdapterListener(this)
    }

    override fun onItemClick(position: Int,data:String) {


        if (data=="999"){
            interestData.clear()
            for (i in 0..dataList.size-1){
                dataList[i].selectedItem = true
                dataList[i].oneItemSelected = true

            }
            interestList.map {
                interestData.add(it.id)
            }
            Log.d("data","last item clifked and list $interestData")
            pageValueChecking()
            yourInterestAdapter!!.notifyDataSetChanged()

        }else{

            if(dataList[position].selectedItem){
                dataList[position].selectedItem = false
                var  size = dataList.size-1
                dataList[size].selectedItem = false
                interestData.remove(data)
                Log.d("data","remove data arry is $interestData")
            }else{
                dataList[position].selectedItem = true
                interestData.add(data)
                Log.d("data","add data arry is $interestData")
            }
            var  dataListFilter: List<YourInterestData> = dataList.filter { s -> s.selectedItem }
            if(dataListFilter.isNotEmpty()){
                for (i in 0 until dataList.size){
                    dataList[i].oneItemSelected = true
                }
            }else{
                for (i in 0 until dataList.size){
                    dataList[i].oneItemSelected = false
                }
            }
            pageValueChecking()
            yourInterestAdapter!!.notifyDataSetChanged()

        }

    }

    private fun pageValueChecking(){
        var  dataListFilter: List<YourInterestData> = dataList.filter { s -> s.selectedItem }
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
                    viewModel.checkEditUserForInterest(BaseResponseDataObject.accessToken,
                        EditUserInterestRequest("editUser",interestData)
                    )

                }else{
                    nextScreen()
                }

            }
        }
    }

    fun nextScreen(){

        UiDataObject.interestData = interestData
        Log.d("theInterEsdata","interrestdata $interestData")
        val intent = Intent(this@YourInterestActivity, YourEquipmentActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, GetInterestViewModelFactory(GetInterestRepository(retrofitService),this)).get(
                GetInterestViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.interestData.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            Log.d("getData","the data is ${it.result.data}")
            interestList = it.result.data
            setUpAdapter()

        })

        viewModel.errorMessage.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.editUserForInterestResponse.observe(this, Observer {
            BaseResponseDataObject.profilePageData = it.result.profileDetails
            finish()

        })

        viewModel.errorMessage2.observe(this, Observer {

        })
    }
}