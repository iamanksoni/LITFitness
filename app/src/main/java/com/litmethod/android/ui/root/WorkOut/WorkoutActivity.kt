package com.litmethod.android.ui.root.WorkOut

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityWorkoutBinding
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.VideoType
import com.litmethod.android.models.ClassHistoryList.ClassHistoryListRequest
import com.litmethod.android.models.ClassHistoryList.Data
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.WorkOutActivityRepository
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.LiveClassTabScreen.TimeUtil.SetThePostFixinDate
import com.litmethod.android.ui.root.WorkOut.Adapter.WorkOutHistoryListParentAdapter
import com.litmethod.android.ui.root.WorkOut.Adapter.WorkoutDropDownAdapter
import com.litmethod.android.ui.root.WorkOut.ViewModel.WorkOutActivityViewModel
import com.litmethod.android.ui.root.WorkOut.ViewModel.WorkOutActivityViewModelFactory
import com.litmethod.android.utlis.CustomTypefaceSpan
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkoutActivity : AppCompatActivity(), WorkoutDropDownAdapter.WorkoutDropDownAdapterListener,
    View.OnClickListener {
    private var layoutManagerworkoutHistory: RecyclerView.LayoutManager? = null
    private var workoutHistoryAdapter: WorkoutDropDownAdapter? = null
    private var classHistoryAdapter: WorkOutHistoryListParentAdapter? = null
    lateinit var viewModel: WorkOutActivityViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var binding: ActivityWorkoutBinding
    var isRvExtended = false
    private var selectedMonth: String = ""
    private var selectedYear: String = ""
    var getClassStatisticsList: MutableList<VideoType> = ArrayList<VideoType>()
    var classHistoryList: MutableList<Data> = ArrayList<Data>()
    private var isComingFromSingle: Boolean = false
    private var code: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_workout)
        if(intent.extras != null){
            isComingFromSingle = intent.extras!!.getBoolean("isComingFromSingle")
            code = intent.extras!!.getString("code")!!
        }
        getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsListNew)
        checkThePosition()
        monthSpinner()
        yearSpinner()
        historyListAdapter()
        viewModelSetup()
        binding.ibBackButton.setOnClickListener(this)
        binding.headerItem.setOnClickListener(this)
        if(isComingFromSingle){
            hitApiOfClassHistory(code)
        }else {
            hitApiOfClassHistory(getClassStatisticsList[getClassStatisticsList.size - 1].id)
        }
    }

    private fun checkThePosition(){
        if(isComingFromSingle){
            for(i in 0 until getClassStatisticsList.size){
                if(code == getClassStatisticsList[i].id){
                    workoutsTabAdapter(i)
                    break
                }
            }
        }else{
            workoutsTabAdapter(getClassStatisticsList.size-1)
        }
    }


    private fun hitApiOfClassHistory(id: String) {
        binding.spLoading.visibility = View.VISIBLE
        viewModel.checkgetClassHistoryList(
            BaseResponseDataObject.accessToken,
            ClassHistoryListRequest(10, "classHistorylistV2", id, 1, selectedMonth, selectedYear)
        )
    }

    private fun workoutsTabAdapter(position: Int) {

        if (getClassStatisticsList.size != 0) {
            val typeFacebold = Typeface.createFromAsset(this.assets, "futura_std_condensed.otf")
            val typeFace = Typeface.createFromAsset(this.assets, "futura_std_condensed_light.otf")

            val firstWord = "${getClassStatisticsList[position].name} "
            val secondWord = "(${getClassStatisticsList[position].count})"
            val spannable: Spannable = SpannableString(firstWord + secondWord)
            spannable.setSpan(
                CustomTypefaceSpan("sans-serif", typeFacebold),
                0,
                firstWord.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                CustomTypefaceSpan("sans-serif", typeFace),
                firstWord.length,
                firstWord.length + secondWord.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.tvHeaderTitle.text = spannable
            Glide.with(this)
                .load(getClassStatisticsList[position].image)
                .into(binding.ivLevelImage)
        }


        binding.rvWorkoutDropdown.apply {

            setLayoutManager(LinearLayoutManager(this@WorkoutActivity))
            workoutHistoryAdapter =
                WorkoutDropDownAdapter(
                    getClassStatisticsList as ArrayList<VideoType>,
                    this@WorkoutActivity
                )
            this.adapter = workoutHistoryAdapter

        }
        workoutHistoryAdapter?.setAdapterListener(this)
        binding.rvWorkoutDropdown.visibility = View.GONE
    }

    private fun historyListAdapter() {
        binding.rvWorkOutParent.apply {

            setLayoutManager(LinearLayoutManager(this@WorkoutActivity))
            classHistoryAdapter =
                WorkOutHistoryListParentAdapter(classHistoryList, this@WorkoutActivity)
            this.adapter = classHistoryAdapter

        }
//        workoutHistoryAdapter?.setAdapterListener(this)
    }

    override fun onItemClick(position: Int, code: String) {
        Log.d("Workout", "position is $ and $position and $code")
        binding.rvWorkoutDropdown.visibility = View.GONE

        Glide.with(this)
            .load(getClassStatisticsList[position].image)
            .into(binding.ivLevelImage)

        val typeFacebold = Typeface.createFromAsset(this.assets, "futura_std_condensed.otf")
        val typeFace = Typeface.createFromAsset(this.assets, "futura_std_condensed_light.otf")

        val firstWord = "${getClassStatisticsList[position].name} "
        val secondWord = "(${getClassStatisticsList[position].count})"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            CustomTypefaceSpan("sans-serif", typeFacebold),
            0,
            firstWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            CustomTypefaceSpan("sans-serif", typeFace),
            firstWord.length,
            firstWord.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvHeaderTitle.text = spannable

        hitApiOfClassHistory(getClassStatisticsList[position].id)


//       if (position==0){
//           if (isRvExtended){
//               Log.d("valuechecking","true called")
//
//
//               getClassStatisticsList.clear()
//               getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsList)
//               Log.d("valuechecking","value is ${BaseResponseDataObject.getClassStatisticsList}")
//               workoutHistoryAdapter?.notifyDataSetChanged()
////               workoutsTabAdapter()
//
//               isRvExtended=false
//           }else{
//               Log.d("valuechecking","false called")
//               getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsListAll)
////               workoutHistoryAdapter?.notifyDataSetChanged()
//               Log.d("valuechecking","value is ${BaseResponseDataObject.getClassStatisticsList}")
//
//               workoutsTabAdapter()
//               isRvExtended=true
//           }
//
//       } else{
//         val newFilteredArray =  BaseResponseDataObject.getClassStatisticsListAll.filter {
//               it.id==code
//           }
//           BaseResponseDataObject.getClassStatisticsList.clear()
//           BaseResponseDataObject.getClassStatisticsList.addAll(newFilteredArray)
//           getClassStatisticsList.clear()
//           getClassStatisticsList.addAll(newFilteredArray)
//           isRvExtended=false
////           getClassStatisticsList.addAll(BaseResponseDataObject.getClassStatisticsListAll)
//           Log.d("valuechecking10","value is ${getClassStatisticsList}")
//           workoutHistoryAdapter?.notifyDataSetChanged()
//
//       }

    }

    private fun yearSpinner() {
        val year = arrayOf("Select a year", "2021", "2022", "2023")
        if (binding.spinnerYr != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_text, year
            )
            binding.spinnerYr.adapter = adapter

            binding.exOneYearText.text = year.get(2)
            selectedYear = binding.exOneYearText.text.toString()

            binding.spinnerYr.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (year[position].toString().equals("Select a year")) {
                    } else {
                        binding.exOneYearText.text = year[position].toString()
                        selectedYear = binding.exOneYearText.text.toString()
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

    private fun visibleSpinner() {
        if (binding.rvWorkoutDropdown.visibility == View.VISIBLE) {
            binding.rvWorkoutDropdown.visibility = View.GONE
        } else {
            binding.rvWorkoutDropdown.visibility = View.VISIBLE
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

        binding.exOneMonthText.text = months.get(12)
        selectedMonth = months.indexOf(binding.exOneMonthText.text.toString()).toString()

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
                        selectedMonth = (position).toString()
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
            ViewModelProvider(
                this,
                WorkOutActivityViewModelFactory(WorkOutActivityRepository(retrofitService), this)
            ).get(
                WorkOutActivityViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse() {
        viewModel.classHistoryListResponse.observe(this, androidx.lifecycle.Observer {

            binding.spLoading.visibility = View.GONE
            if (it.result.data.isNotEmpty()) {
                binding.tvNoDataError.visibility = View.GONE
                binding.tvDate.visibility = View.VISIBLE
                binding.tvDate.text = getLocalDateFrom(it.result.data[0].date)
                binding.rvWorkOutParent.visibility = View.VISIBLE
                classHistoryList.clear()
                classHistoryList.addAll(it.result.data as MutableList<Data>)
                classHistoryAdapter!!.notifyDataSetChanged()
            } else {
                binding.rvWorkOutParent.visibility = View.GONE
                binding.tvNoDataError.visibility = View.VISIBLE
                binding.tvDate.visibility = View.GONE
            }

        })

        viewModel.errorMessage.observe(this, androidx.lifecycle.Observer {
            binding.spLoading.visibility = View.GONE
        })

    }

    fun getLocalDateFrom(utcDateString: String): String {
        val oldFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = oldFormatter.parse(utcDateString)

        val suffixMonth = SetThePostFixinDate()
        val newFormatterDate = SimpleDateFormat("dd")
        newFormatterDate.timeZone = TimeZone.getDefault()
        val dateNew = newFormatterDate.format(value)
        Log.d("thedateis", "the date is  $dateNew")
        val getLast = suffixMonth.getTheLastNo(dateNew.toString())
        val suffixSet = suffixMonth.setThePostFix(getLast)

        val newFormatterDaysName = SimpleDateFormat("EEEE")
        newFormatterDaysName.timeZone = TimeZone.getDefault()
        val daysName = newFormatterDaysName.format(value)

        val newFormatterMonth = SimpleDateFormat("MMMM")
        newFormatterMonth.timeZone = TimeZone.getDefault()
        val monthName = newFormatterMonth.format(value)


        return "$daysName, ${dateNew}$suffixSet $monthName"
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.headerItem -> {
                visibleSpinner()
            }
        }
    }


}