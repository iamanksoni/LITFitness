package com.litmethod.android.ui.Onboarding.LevelScreen

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
import com.litmethod.android.databinding.ActivityLevelBinding
import com.litmethod.android.models.GetLevel.Data
import com.litmethod.android.network.GetInterestRepository
import com.litmethod.android.network.GetLevelRepository
import com.litmethod.android.network.RetrofitService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Dashboard.AllClassTabScreen.ClassesFragmentScreen.Util.AllClassesDataObject
import com.litmethod.android.ui.Onboarding.LevelScreen.ViewModel.GetLevelFactory
import com.litmethod.android.ui.Onboarding.LevelScreen.ViewModel.GetLevelViewModel
import com.litmethod.android.ui.Onboarding.YourGoalsScreen.GoalsData
import com.litmethod.android.ui.Onboarding.YourGoalsScreen.YourGoalsActivity
import com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel.GetInterestViewModel
import com.litmethod.android.ui.Onboarding.YourInterestScreen.ViewModel.GetInterestViewModelFactory
import com.litmethod.android.utlis.MarginItemDecoration
import com.litmethod.android.utlis.UiDataObject

class LevelActivity : BaseActivity(),LevelAdapter.LevelAdapterListener,View.OnClickListener{
    lateinit var binding: ActivityLevelBinding
    val dataList: ArrayList<LevelData> = ArrayList<LevelData>()
    private var levelAdapter: LevelAdapter? = null
    lateinit var viewModel: GetLevelViewModel
    private val retrofitService = RetrofitService.getInstance()
    var levelList: List<Data> = ArrayList<Data>()
    val levelListResponse: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_level)
        setUpUi()
//        setUpAdapter()
        viewModelSetup()
        clickListener()
        viewModel.checkgetLevel(AllClassesDataObject.accessToken)
    }

    private fun setUpUi() {

    }

    private fun setUpAdapter() {
      binding.spLoading.visibility = View.GONE
        dataList.clear()


        levelList.map {
            dataList.add(LevelData(false,"#ED2124",false))
        }
        binding.rvLevelType.layoutManager = RecyclerView.LinearLayoutManager(this@LevelActivity)
        levelAdapter = LevelAdapter(dataList, this@LevelActivity,levelList)
        binding.rvLevelType.adapter = levelAdapter
        binding.rvLevelType.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        levelAdapter!!.setAdapterListener(this)
    }

    override fun onItemClick(position: Int,data:String) {
        levelListResponse.clear()
        levelListResponse.add(data)
        Log.d("data","add data arry is $levelListResponse")
        for (i in 0 until dataList.size){
            if(position == i){
                dataList[position].selectedItem = !dataList[position].selectedItem
            }else{
                dataList[i].selectedItem = false
            }
        }
        var  dataListFilter: List<LevelData> = dataList.filter { s -> s.selectedItem }
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
        levelAdapter!!.notifyDataSetChanged()
    }

    private fun pageValueChecking(){
        var  dataListFilter: List<LevelData> = dataList.filter { s -> s.selectedItem }
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
                nextScreen()
            }
        }
    }

    fun nextScreen(){

        UiDataObject.level = levelListResponse
        Log.d("theInterEsdata","level data $levelListResponse")
        val intent = Intent(this@LevelActivity, YourGoalsActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }
    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, GetLevelFactory(GetLevelRepository(retrofitService),this)).get(
                GetLevelViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.getLevelData.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            Log.d("getData","the data is ${it.result.data}")
            it.result.data.map {
                dataList.add(LevelData(false,"#52CFC5",false))
            }
            levelList = it.result.data
            setUpAdapter()

        })

        viewModel.errorMessage.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }
}