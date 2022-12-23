package com.litmethod.android.ui.root.WorkOut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityWorkoutBinding
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.VideoType
import com.litmethod.android.models.ClassHistoryList.ClassHistoryListRequest
import com.litmethod.android.models.ClassHistoryList.Data
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.WorkOutActivityRepository
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.WorkOut.Adapter.WorkOutHistoryListParentAdapter
import com.litmethod.android.ui.root.WorkOut.Adapter.WorkoutDropDownAdapter
import com.litmethod.android.ui.root.WorkOut.ViewModel.WorkOutActivityViewModel
import com.litmethod.android.ui.root.WorkOut.ViewModel.WorkOutActivityViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkoutActivity : AppCompatActivity(), WorkoutDropDownAdapter.WorkoutDropDownAdapterListener, View.OnClickListener {
    private var layoutManagerworkoutHistory: RecyclerView.LayoutManager? = null
    private var workoutHistoryAdapter:WorkoutDropDownAdapter? = null
    private var classHistoryAdapter:WorkOutHistoryListParentAdapter? = null
    lateinit var viewModel: WorkOutActivityViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var binding: ActivityWorkoutBinding
    var isRvExtended =false
    var getClassStatisticsList: MutableList<VideoType> = ArrayList<VideoType>()
    var classHistoryList: MutableList<Data> = ArrayList<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_workout)
        getClassStatisticsList.addAll(BaseResponseDataObject.getNewStaticsticVideoClass)
        workoutsTabAdapter()
        monthSpinner()
        yearSpinner()
        viewModelSetup()
        binding.ibBackButton.setOnClickListener(this)
        viewModel.checkgetClassHistoryList(BaseResponseDataObject.accessToken, ClassHistoryListRequest(10,"classHistorylistV2","cl8esljwz8048i5p0yfyjw5q2",1,"10","2022"))
    }

    private fun workoutsTabAdapter(){
        binding.rvWorkoutDropdown.apply {

            setLayoutManager( LinearLayoutManager(this@WorkoutActivity))
            workoutHistoryAdapter =
                WorkoutDropDownAdapter(getClassStatisticsList as ArrayList<VideoType>, this@WorkoutActivity)
            this.adapter = workoutHistoryAdapter

        }
        workoutHistoryAdapter?.setAdapterListener(this)
    }

    private fun historyListAdapter(){
        binding.rvWorkOutParent.apply {

            setLayoutManager( LinearLayoutManager(this@WorkoutActivity))
            classHistoryAdapter =
                WorkOutHistoryListParentAdapter(classHistoryList , this@WorkoutActivity)
            this.adapter = classHistoryAdapter

        }
//        workoutHistoryAdapter?.setAdapterListener(this)
    }

    override fun onItemClick(position: Int, code: String) {
        Log.d("Workout","position is $ and $position and $code")
       if (position==0){
           if (isRvExtended){
               Log.d("valuechecking","true called")


               getClassStatisticsList.clear()
               getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsList)
               Log.d("valuechecking","value is ${BaseResponseDataObject.getClassStatisticsList}")
               workoutHistoryAdapter?.notifyDataSetChanged()
//               workoutsTabAdapter()

               isRvExtended=false
           }else{
               Log.d("valuechecking","false called")
               getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsListAll)
//               workoutHistoryAdapter?.notifyDataSetChanged()
               Log.d("valuechecking","value is ${BaseResponseDataObject.getClassStatisticsList}")

               workoutsTabAdapter()
               isRvExtended=true
           }

       } else{
         val newFilteredArray =  BaseResponseDataObject.getClassStatisticsListAll.filter {
               it.id==code
           }
           BaseResponseDataObject.getClassStatisticsList.clear()
           BaseResponseDataObject.getClassStatisticsList.addAll(newFilteredArray)
           getClassStatisticsList.clear()
           getClassStatisticsList.addAll(newFilteredArray)
           isRvExtended=false
//           getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsListAll)
           Log.d("valuechecking10","value is ${getClassStatisticsList}")
           workoutHistoryAdapter?.notifyDataSetChanged()

       }

    }

    private fun yearSpinner() {
        val year = arrayOf("Select a year", "2021", "2022")
        if (binding.spinnerYr != null) {
            val adapter = ArrayAdapter(
               this,
                R.layout.spinner_text, year
            )
            binding.spinnerYr.adapter = adapter

            binding.spinnerYr.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (year[position].toString().equals("Select a year")) {
                    } else {
                        binding.exOneYearText.text = year[position].toString()

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        binding.exOneYearText.setOnClickListener {
            binding.spinnerYr.performClick()
        }
    }

    private fun monthSpinner() {
        val months = java.util.ArrayList<String>()
        months.clear()
        months.add("Select a month")
        for (i in 0..11) {
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("MMMM")
            cal[Calendar.MONTH] = i
            val month_name: String = month_date.format(cal.time)
            months.add(month_name)
        }

        if (binding.spinnerMonth != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_text, months
            )
            binding.spinnerMonth.adapter = adapter

            binding.spinnerMonth.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (months[position].toString().equals("Select a month")) {
                    } else {
                        binding.exOneMonthText.text = months[position].toString()

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        binding.exOneMonthText.setOnClickListener {
            binding.spinnerMonth.performClick()
        }
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, WorkOutActivityViewModelFactory(WorkOutActivityRepository(retrofitService),this)).get(
                WorkOutActivityViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.classHistoryListResponse.observe(this, androidx.lifecycle.Observer {

            classHistoryList= it.result.data as MutableList<Data>
            historyListAdapter()
            Log.d("workOutActivityresponse","workout activity response is ${it.result}")
        })

        viewModel.errorMessage.observe(this, androidx.lifecycle.Observer {


        })

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ib_back_button -> {
                finish()
            }
        }
    }


}