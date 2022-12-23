package com.litmethod.android.ui.root.HomeTabScreen.PerformanceDetailsScreen

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.style.Circle
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityPerformanceDetailsBinding
import com.litmethod.android.models.HomePageModels.GetUserAnalycticsDetilesRequest
import com.litmethod.android.models.HomePageModels.GrapData
import com.litmethod.android.models.HomePageModels.VideoGetUserAnalycticsDetiles
import com.litmethod.android.models.ResultUserAnalytics
import com.litmethod.android.network.HomeTabFragmentRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen.HomePageVideosModel
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModel
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModelFactory
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DataPreferenceObject
import java.util.*
import kotlin.collections.ArrayList


class PerformanceDetailsActivity : BaseActivity(),AllTimeDetailsAdapter.AllTimeAdapterListener{
    lateinit var binding:ActivityPerformanceDetailsBinding
    var dataListRateKacl: ArrayList<ResultUserAnalytics> = ArrayList<ResultUserAnalytics>()
    var selectedPosition:Int = 0
    var colournew:Int = 0

    var dataListAllTime: ArrayList<HomePageVideosModel> = ArrayList<HomePageVideosModel>()
    private var layoutManagerAllTime: RecyclerView.LayoutManager? = null
    private var allTimeDetailsAdapter: AllTimeDetailsAdapter? = null

    val dataListGraph: ArrayList<GrapData> = ArrayList<GrapData>()
    val dataListGoal: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()
    private var layoutManagerGoal: RecyclerView.LayoutManager? = null
    private var performanceDetailsGoalAdapter: PerformanceDetailsGoalAdapter? = null

    var dataListMetrics: ArrayList<ResultUserAnalytics> = ArrayList<ResultUserAnalytics>()
    private var layoutManagerMetrics: RecyclerView.LayoutManager? = null
    private var performanceMetricsDetailsAdapter: PerformanceMetricsDetailsAdapter? = null

    lateinit var viewModel: HomeViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var dataPreferenceObject: DataPreferenceObject
    var acceessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformanceDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        getTheAccessToken()

        viewModelSetup()

        setUpChart()
    }

    private fun setUpUi(color: Int) {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            window.statusBarColor = color
        }

        binding.ibBackButton.setOnClickListener {
            finish()
        }

    }

    private fun setUpDataFromPrevious(){
        if(intent.extras != null){
            dataListRateKacl.clear()
            dataListRateKacl = intent.getParcelableArrayListExtra<ResultUserAnalytics>("dataListRateKacl")!!
            dataListAllTime.clear()
            dataListAllTime = intent.getParcelableArrayListExtra<HomePageVideosModel>("dataListAllTime")!!
            dataListMetrics.clear()
            dataListMetrics = intent.getParcelableArrayListExtra<ResultUserAnalytics>("dataListRateKacl")!!
            selectedPosition = intent.getIntExtra("position",0)
        }

        binding.tvDateTime.visibility = View.INVISIBLE

        if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {
            colournew = resources.getColor(R.color.red)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_heart_rate_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.heart_rate
            setUpUi(colournew)
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for heart rate"

        }else if (dataListRateKacl[selectedPosition].key.equals("calories")) {
            colournew = resources.getColor(R.color.yellow)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_kcal_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.calories
            setUpUi(colournew)
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for calorie burn"

        }else if (dataListRateKacl[selectedPosition].key.equals("distance")) {
            colournew = resources.getColor(R.color.blue)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_location_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.distance
            setUpUi(colournew)
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for rowing"

        }else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
            colournew = resources.getColor(R.color.purple_new_100)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_total_force_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.total_force
            setUpUi(resources.getColor(R.color.purple_new_100))
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for pushing pounds"

        }else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
            colournew = resources.getColor(R.color.green_100)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_reps_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.total_reps
            setUpUi(resources.getColor(R.color.green_100))
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for reps"

        }else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
            colournew = resources.getColor(R.color.blue)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_tension_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.time_under_tension
            setUpUi(resources.getColor(R.color.blue))
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for time under tension"

        }else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {
            colournew = resources.getColor(R.color.mono_grey_100)
            var  doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(resources.getColor(R.color.mono_grey_100))
            binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            Glide.with(this)
                .load(getDrawable(R.drawable.ic_time_fill))
                .into(binding.ivRatePic)
            val colorInt2 = resources.getColor(R.color.black)
            binding.ivRatePic.imageTintList = ColorStateList.valueOf(colorInt2)
            binding.tvAvgRating.text = AppConstants.total_time
            setUpUi(resources.getColor(R.color.mono_grey_100))
            binding.tvPerformance.setTextColor(colournew)
            binding.tvHeartHeader.setTextColor(colournew)
            binding.tvYourMetrics.setTextColor(colournew)
            binding.tvHeartHeader.text = "workouts for stamina"
        }

    }

    private fun setUpAdapter() {

        if(dataListAllTime.size>0) {
            binding.rvAllTime.apply {
                layoutManagerAllTime = LinearLayoutManager(
                    this@PerformanceDetailsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                this.layoutManager = layoutManagerAllTime
                allTimeDetailsAdapter = AllTimeDetailsAdapter(
                    dataListAllTime,
                    this@PerformanceDetailsActivity,
                    binding.rvAllTime,
                    colournew
                )
                this.adapter = allTimeDetailsAdapter

            }
            allTimeDetailsAdapter!!.setAdapterListener(this)
        }

        binding.rvGoalList.apply {
            layoutManagerGoal = LinearLayoutManager(this@PerformanceDetailsActivity, LinearLayoutManager.VERTICAL, false)
            this.layoutManager = layoutManagerGoal
            performanceDetailsGoalAdapter = PerformanceDetailsGoalAdapter(dataListGoal,this@PerformanceDetailsActivity,selectedPosition)
            this.adapter = performanceDetailsGoalAdapter
        }


        binding.rvMetricsList.apply {
            layoutManagerMetrics = GridLayoutManager(this@PerformanceDetailsActivity,2)
            this.layoutManager = layoutManagerMetrics
            performanceMetricsDetailsAdapter = PerformanceMetricsDetailsAdapter(dataListMetrics,this@PerformanceDetailsActivity,selectedPosition)
            this.adapter = performanceMetricsDetailsAdapter
        }


        //api call
        var filterKey = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                if (dataListAllTime[i].title.equals("All time")) {
                    filterKey = AppConstants.ALLTIME
                } else if (dataListAllTime[i].title.equals("past month")) {
                    filterKey = AppConstants.PASTMONTH
                } else if (dataListAllTime[i].title.equals("Past week")) {
                    filterKey = AppConstants.PASTWEEK
                }else if (dataListAllTime[i].title.equals("Today")) {
                    filterKey = AppConstants.TODAY
                }
            }
        }
        val tz: TimeZone = TimeZone.getDefault()
        binding.spLoading.visibility = View.VISIBLE
        viewModel.getUserAnalycticsDetilesCheck(acceessToken, GetUserAnalycticsDetilesRequest(
            "getUserAnalycticsDetiles",
            filterKey,
            dataListRateKacl[selectedPosition].key,
            dataListRateKacl[selectedPosition].subKey,
            tz.getID(),
        ))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private fun setUpChart(){
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setBorderColor(R.color.red)
        binding.lineChart.setBorderWidth(2.0f)
        binding.lineChart.setTouchEnabled(true)
        val colors = IntArray(2)
        colors[0] = ColorUtils.setAlphaComponent(R.color.red , 2)
        colors[1] = ColorUtils.setAlphaComponent(R.color.red , 10)
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, colors
        )
        gd.shape = GradientDrawable.RECTANGLE
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
        gd.gradientRadius = 90f
        gd.cornerRadius = 0f
        binding.lineChart.setBackgroundDrawable(gd)
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this,
                HomeViewModelFactory(HomeTabFragmentRepository(retrofitService), this)
            ).get(
                HomeViewModel::class.java
            )

        getUserAnalycticsDetilesResponse()
        getUserAnalyticsResponse()
    }

    private fun getUserAnalycticsDetilesResponse(){
        viewModel.getUserAnalycticsDetilesData.observe(this, Observer {
            dataListGoal.clear()
            dataListGoal.addAll(it.videos)
            performanceDetailsGoalAdapter!!.notifyDataSetChanged()
            dataListGraph.clear()
            dataListGraph.addAll(it.grapData)
            binding.spLoading.visibility = View.GONE
        })
    }

    private fun getUserAnalyticsResponse() {
        viewModel.getUserAnalyticsData.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            dataListMetrics.clear()
            dataListMetrics.addAll(it)
            performanceMetricsDetailsAdapter!!.notifyDataSetChanged()
        })

        viewModel.errorMessage.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

    }

    override fun onItemClickTime(position: Int) {
        for (i in 0 until dataListAllTime.size) {
            if (i == position) {
                if (dataListAllTime[i].selected) {
                    dataListAllTime[i].selected = false
                } else {
                    dataListAllTime[i].selected = true
                }
            } else {
                dataListAllTime[i].selected = false
            }
        }

        for (i in 0 until dataListAllTime.size) {
            binding.spLoading.visibility = View.VISIBLE
            if (dataListAllTime[i].selected) {
                if (dataListAllTime[i].title.equals("All time")) {
                    detailsApiCall(AppConstants.ALLTIME)
                    viewModel.getUserAnalyticsCheck(acceessToken, AppConstants.ALLTIME)
                } else if (dataListAllTime[i].title.equals("past month")) {
                    detailsApiCall(AppConstants.PASTMONTH)
                    viewModel.getUserAnalyticsCheck(acceessToken, AppConstants.PASTMONTH)
                } else if (dataListAllTime[i].title.equals("Past week")) {
                    detailsApiCall(AppConstants.PASTWEEK)
                    viewModel.getUserAnalyticsCheck(acceessToken, AppConstants.PASTWEEK)
                } else if (dataListAllTime[i].title.equals("Today")) {
                    detailsApiCall(AppConstants.TODAY)
                    viewModel.getUserAnalyticsCheck(acceessToken, AppConstants.TODAY)
                }
            }
        }
    }

    private fun detailsApiCall(filterKey:String){
        val tz: TimeZone = TimeZone.getDefault()
        binding.spLoading.visibility = View.VISIBLE
        viewModel.getUserAnalycticsDetilesCheck(acceessToken, GetUserAnalycticsDetilesRequest(
            "getUserAnalycticsDetiles",
            filterKey,
            dataListRateKacl[selectedPosition].key,
            dataListRateKacl[selectedPosition].subKey,
            tz.getID(),
        ))
    }

    private fun  getTheAccessToken(){
        dataPreferenceObject = DataPreferenceObject(this)
        dataPreferenceObject.getTheData("userToken").asLiveData().observe(this){
            acceessToken = it.toString()
            setUpDataFromPrevious()
            setUpAdapter()
        }
    }
}