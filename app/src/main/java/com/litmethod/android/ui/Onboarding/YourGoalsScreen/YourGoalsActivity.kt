package com.litmethod.android.ui.Onboarding.YourGoalsScreen

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
import com.litmethod.android.databinding.ActivityYourGoalsBinding
import com.litmethod.android.models.AcountScreenFragment.EditUserGoal.EditUserGoalRequest
import com.litmethod.android.models.GetYourGoalResponse.Data
import com.litmethod.android.network.GetGoalRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.Onboarding.YourGoalsScreen.ViewModel.GetGoalViewModel
import com.litmethod.android.ui.Onboarding.YourGoalsScreen.ViewModel.GetGoalViewModelFactory
import com.litmethod.android.ui.Onboarding.YourInterestScreen.YourInterestActivity
import com.litmethod.android.utlis.MarginItemDecoration
import com.litmethod.android.utlis.UiDataObject


class YourGoalsActivity : BaseActivity(),GoalsAdapter.GoalsAdapterListener,View.OnClickListener {
    lateinit var binding: ActivityYourGoalsBinding
    val dataList: ArrayList<GoalsData> = ArrayList<GoalsData>()
    lateinit var viewModel: GetGoalViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    var getGoalList: List<Data> = ArrayList<Data>()
    val goalLevel: ArrayList<String> = ArrayList<String>()
    var checkintentData:String? =null
    var switchActivity = ""
    private var goalsAdapter: GoalsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_your_goals)
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
        viewModel.checkgetGoal(BaseResponseDataObject.accessToken)
    }




    private fun setUpAdapter() {
        binding.spLoading.visibility = View.GONE
        dataList.clear()
        goalLevel.clear()
       if (switchActivity=="AccountFrag"){
         var count=0
        getGoalList.map {
            val userid=it.id
            var isAddDAta= false
         BaseResponseDataObject.profilePageData.goal.map {
             if (it.id==userid){
                 isAddDAta = true
                 count++
                 goalLevel.add(it.id)
                 dataList.add(GoalsData(true, "#ffffff", true))
             }
         }
            if (isAddDAta==false){
                dataList.add(GoalsData(false, "#ffffff", false))
            }
        }
           if (count==getGoalList.size){
               dataList.add(GoalsData(true,"#ffffff",true))
           }else{
               dataList.add(GoalsData(false,"#ffffff",false))
           }

       } else{
           getGoalList.map {
               dataList.add(GoalsData(false, "#ffffff", false))
           }
           dataList.add(GoalsData(false,"#ffffff",false))
       }

        binding.rvGoalType.layoutManager = RecyclerView.LinearLayoutManager(this@YourGoalsActivity)
        goalsAdapter = GoalsAdapter(dataList, this@YourGoalsActivity,getGoalList)
        binding.rvGoalType.adapter = goalsAdapter
        binding.rvGoalType.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        goalsAdapter!!.setAdapterListenerNew(this)
    }

    override fun onItemClick(position: Int,data:String) {
        if (data=="999"){
            goalLevel.clear()
            for (i in 0..dataList.size-1){
                dataList[i].selectedItem = true
                dataList[i].oneItemSelected = true

            }
            getGoalList.map {
                    goalLevel.add(it.id)
            }
            Log.d("data","last item clifked and list $goalLevel")
            pageValueChecking()
            goalsAdapter!!.notifyDataSetChanged()

        }else{

            if(dataList[position].selectedItem){
                dataList[position].selectedItem = false
               var  size = dataList.size-1
                dataList[size].selectedItem = false
                goalLevel.remove(data)
                Log.d("data","remove data arry is $goalLevel")
            }else{
                dataList[position].selectedItem = true
                goalLevel.add(data)
                Log.d("data","add data arry is $goalLevel")
            }
            var  dataListFilter: List<GoalsData> = dataList.filter { s -> s.selectedItem }
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
            goalsAdapter!!.notifyDataSetChanged()
        }

    }

    private fun pageValueChecking(){
        var  dataListFilter: List<GoalsData> = dataList.filter { s -> s.selectedItem }
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
                   viewModel.checkEditUserForGoal(BaseResponseDataObject.accessToken,
                       EditUserGoalRequest("editUser",goalLevel)
                   )

                }else{
                    nextScreen()
                }

            }
        }
    }

    fun nextScreen(){
        UiDataObject.goalLevel = goalLevel
        val intent = Intent(this@YourGoalsActivity, YourInterestActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, GetGoalViewModelFactory(GetGoalRepository(retrofitService),this)).get(
                GetGoalViewModel::class.java
            )
        loginResponse()
    }
    private fun loginResponse(){
        viewModel.getGoalData.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            Log.d("getData","the data is ${it.result.data}")
            getGoalList = it.result.data
            setUpAdapter()

        })

        viewModel.errorMessage.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

            viewModel.editUserForGoalResponse.observe(this, Observer {
           BaseResponseDataObject.profilePageData = it.result.profileDetails
                finish()

            })

            viewModel.errorMessage2.observe(this, Observer {

            })


    }
}