package com.litmethod.android.ui.Dashboard.HomeTabScreen.PerformanceDetailsScreen

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.ybq.android.spinkit.style.Circle
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityPerformanceDetailsBinding
import com.litmethod.android.models.HomePageModels.GrapDataShort
import com.litmethod.android.models.HomePageModels.VideoGetUserAnalycticsDetiles
import com.litmethod.android.models.ResultUserAnalytics
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen.HomePageVideosModel
import com.litmethod.android.ui.root.HomeTabScreen.PerformanceDetailsScreen.AllTimeDetailsAdapter
import com.litmethod.android.ui.root.HomeTabScreen.PerformanceDetailsScreen.PerformanceDetailsGoalAdapter
import com.litmethod.android.utlis.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class PerformanceDetailsActivity : BaseActivity(), AllTimeDetailsAdapter.AllTimeAdapterListener, PerformanceMetricsDetailsAdapter.RecyclerViewOnClickListener ,PerformanceMetricsDetailsAdapter.ViewChange{
    lateinit var binding: ActivityPerformanceDetailsBinding
    var dataListRateKacl: ArrayList<ResultUserAnalytics> = ArrayList<ResultUserAnalytics>()
    var selectedPosition: Int = 0
    var colournew: Int = 0

    var dataListAllTime: ArrayList<HomePageVideosModel> = ArrayList<HomePageVideosModel>()
    private var layoutManagerAllTime: RecyclerView.LayoutManager? = null
    private var allTimeDetailsAdapter: AllTimeDetailsAdapter? = null

    var dataListGoal: ArrayList<VideoGetUserAnalycticsDetiles> =
        ArrayList<VideoGetUserAnalycticsDetiles>()
    private var layoutManagerGoal: RecyclerView.LayoutManager? = null
    private var performanceDetailsGoalAdapter: PerformanceDetailsGoalAdapter? = null

    var dataListMetrics: ArrayList<ResultUserAnalytics> = ArrayList<ResultUserAnalytics>()
    private var layoutManagerMetrics: RecyclerView.LayoutManager? = null
    private var performanceMetricsDetailsAdapter: PerformanceMetricsDetailsAdapter? = null


    var heart_rateDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var heart_rateDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var caloriesDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var caloriesDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var distanceDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var distanceDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_forceDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_forceDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_repsDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_repsDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var time_under_tensionDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var time_under_tensionDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_timeDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_timeDataAscGrap: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()


    var subtotal_timeDataAsc =arrayListOf<GrapDataShort>()

    var    dayDifference :String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformanceDetailsBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        setUpDataFromPrevious()
        setUpAdapter()

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

    private fun setUpDataFromPrevious() {
        if (intent.extras != null) {
            dataListRateKacl.clear()
            dataListRateKacl =
                intent.getParcelableArrayListExtra<ResultUserAnalytics>("dataListRateKacl")!!
            dataListAllTime.clear()
            dataListAllTime =
                intent.getParcelableArrayListExtra<HomePageVideosModel>("dataListAllTime")!!
            dataListMetrics.clear()
            dataListMetrics =
                intent.getParcelableArrayListExtra<ResultUserAnalytics>("dataListRateKacl")!!
            heart_rateDataAsc.clear()
            heart_rateDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("heart_rateDataAsc")!!
            caloriesDataAsc.clear()
            caloriesDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("caloriesDataAsc")!!
            distanceDataAsc.clear()
            distanceDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("distanceDataAsc")!!
            total_forceDataAsc.clear()
            total_forceDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("total_forceDataAsc")!!
            total_repsDataAsc.clear()
            total_repsDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("total_repsDataAsc")!!
            time_under_tensionDataAsc.clear()
            time_under_tensionDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("time_under_tensionDataAsc")!!
            total_timeDataAsc.clear()
            total_timeDataAsc =
                intent.getParcelableArrayListExtra<GrapDataShort>("total_timeDataAsc")!!
            dataListGoal
            selectedPosition = intent.getIntExtra("position", 0)
            dataListGoal.clear()
            dataListGoal =
                intent.getParcelableArrayListExtra<VideoGetUserAnalycticsDetiles>("heart_rateDataVideo")!!
        }

        binding.tvDateTime.visibility = View.INVISIBLE

        if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {
            colournew = resources.getColor(R.color.red)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())


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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("calories")) {
            colournew = resources.getColor(R.color.yellow)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("distance")) {
            colournew = resources.getColor(R.color.blue)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
            colournew = resources.getColor(R.color.purple_new_100)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
            colournew = resources.getColor(R.color.green_100)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
            colournew = resources.getColor(R.color.blue)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            // binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            var  minutes = (dataListRateKacl[selectedPosition].data.toString().toInt() % 3600) / 60;
            var seconds = dataListRateKacl[selectedPosition].data.toString().toInt() % 60;

            if(minutes==0 && seconds==0){

                binding.tvRating.text = "0$minutes:0$seconds"
            }else binding.tvRating.text = "$minutes:$seconds"
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {
            colournew = resources.getColor(R.color.mono_grey_100)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(resources.getColor(R.color.mono_grey_100))
            //     binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()
            var  minutes = (dataListRateKacl[selectedPosition].data.toString().toInt() % 3600) / 60;
            var seconds = dataListRateKacl[selectedPosition].data.toString().toInt() % 60;

            if(minutes==0 && seconds==0){

                binding.tvRating.text = "0$minutes:0$seconds"
            }else binding.tvRating.text = "$minutes:$seconds"

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
        setUpDataForChart()

    }

    private fun setUpDataFromPrev() {
        binding.tvDateTime.visibility = View.INVISIBLE

        if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {
            colournew = resources.getColor(R.color.red)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text =AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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


        }
        else if (dataListRateKacl[selectedPosition].key.equals("calories")) {
            colournew = resources.getColor(R.color.yellow)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("distance")) {
            colournew = resources.getColor(R.color.blue)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
            colournew = resources.getColor(R.color.purple_new_100)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
            colournew = resources.getColor(R.color.green_100)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            binding.tvRating.text = AppUtils.formatNumber(dataListRateKacl[selectedPosition].data.toString())
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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
            colournew = resources.getColor(R.color.blue)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(colournew)
            //  binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()

            var  minutes = (dataListRateKacl[selectedPosition].data.toString().toInt() % 3600) / 60;
            var seconds = dataListRateKacl[selectedPosition].data.toString().toInt() % 60;

            if(minutes==0 && seconds==0){

                binding.tvRating.text = "0$minutes:0$seconds"
            }else binding.tvRating.text = "$minutes:$seconds"

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

        }
        else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {
            colournew = resources.getColor(R.color.mono_grey_100)
            var doubleBounce = Circle()
            doubleBounce.color = colournew
            binding.spLoading.setIndeterminateDrawable(doubleBounce)
            binding.rlTopLayer.setBackgroundColor(resources.getColor(R.color.mono_grey_100))
            //  binding.tvRating.text = dataListRateKacl[selectedPosition].data.toString()

            var  minutes = (dataListRateKacl[selectedPosition].data.toString().toInt() % 3600) / 60;
            var seconds = dataListRateKacl[selectedPosition].data.toString().toInt() % 60;

            if(minutes==0 && seconds==0){

                binding.tvRating.text = "0$minutes:0$seconds"
            }else binding.tvRating.text = "$minutes:$seconds"

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
        setUpDataForChart()

    }


    private fun setUpDataForChart() {
        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }

        when (selectedItem) {
            "Past week"-> {
                binding.lineChart.visibility=View.GONE
                binding.barChart.visibility=View.VISIBLE
                binding.barChart .notifyDataSetChanged();
                binding.barChart .invalidate();

                total_timeDataAscGrap.clear()
                heart_rateDataAscGrap.clear()
                caloriesDataAscGrap.clear()
                distanceDataAscGrap.clear()
                total_forceDataAscGrap.clear()
                total_repsDataAscGrap.clear()
                time_under_tensionDataAscGrap.clear()

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentdate = sdf.format(Date())
                val startDate = LocalDate.of(
                    currentdate.split("-")[0].toInt(),
                    currentdate.split("-")[1].toInt(),
                    currentdate.split("-")[2].toInt()
                )
                val endDateSevenDay = getDaysAgo(6)
                val endDate = LocalDate.of(
                    endDateSevenDay.split("-")[0].toInt(),
                    endDateSevenDay.split("-")[1].toInt(),
                    endDateSevenDay.split("-")[2].toInt()
                )

                if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {


                    for (date in endDate..startDate) {
                        loop@ for (i in heart_rateDataAsc) {
                            if (i.date == date.toString()) {
                                if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    heart_rateDataAscGrap.add(i)
                                }else{ heart_rateDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("calories")) {

                    for (date in endDate..startDate) {
                        loop@ for (i in caloriesDataAsc) {
                            if (i.date == date.toString()) {
                                if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    caloriesDataAscGrap.add(i)
                                }else{ caloriesDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("distance")) {

                    distanceDataAscGrap.clear()

                    for (date in endDate..startDate) {
                        loop@ for (i in distanceDataAsc) {
                            if (i.date == date.toString()) {
                                if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    distanceDataAscGrap.add(i)
                                }else{ distanceDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
                    total_forceDataAscGrap.clear()

                    for (date in endDate..startDate) {
                        loop@ for (i in total_forceDataAsc) {
                            if (i.date == date.toString()) {
                                if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_forceDataAscGrap.add(i)
                                }else{ total_forceDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
                    total_repsDataAscGrap.clear()

                    for (date in endDate..startDate) {
                        loop@ for (i in total_repsDataAsc) {
                            if (i.date == date.toString()) {
                                if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_repsDataAscGrap.add(i)
                                }else{ total_repsDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
                    time_under_tensionDataAscGrap.clear()

                    for (date in endDate..startDate) {
                        loop@ for (i in time_under_tensionDataAsc) {
                            if (i.date == date.toString()) {
                                if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    time_under_tensionDataAscGrap.add(i)
                                }else{ time_under_tensionDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {

                    for (date in endDate..startDate) {
                        loop@ for (i in total_timeDataAsc) {
                            if (i.date == date.toString()) {
                                if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_timeDataAscGrap.add(i)
                                }else{ total_timeDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                }

                setUpChartForWeek()
            }
            "3 Months" -> {
                binding.lineChart.visibility=View.VISIBLE
                binding.barChart.visibility=View.GONE
                binding.lineChart .notifyDataSetChanged();
                binding.lineChart .invalidate();
                Toast.makeText(this@PerformanceDetailsActivity, selectedItem, Toast.LENGTH_SHORT).show()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentdate = sdf.format(Date())
                val startDate = LocalDate.of(
                    currentdate.split("-")[0].toInt(),
                    currentdate.split("-")[1].toInt(),
                    currentdate.split("-")[2].toInt()
                )
                val endDateSevenDay = getDaysAgo(89)
                val endDate = LocalDate.of(
                    endDateSevenDay.split("-")[0].toInt(),
                    endDateSevenDay.split("-")[1].toInt(),
                    endDateSevenDay.split("-")[2].toInt()
                )

                if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {
                    heart_rateDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in heart_rateDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    heart_rateDataAscGrap.add(i)
                                }else{ heart_rateDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<heart_rateDataAscGrap.size){
                            val subarr = heart_rateDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > heart_rateDataAscGrap.count()-1)   heart_rateDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("calories")) {
                    caloriesDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in caloriesDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    caloriesDataAscGrap.add(i)
                                }else{ caloriesDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }
                        }
                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<caloriesDataAscGrap.size){
                            val subarr = caloriesDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > caloriesDataAscGrap.count()-1)   caloriesDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("distance")) {
                    distanceDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in distanceDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    distanceDataAscGrap.add(i)
                                }else{ distanceDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<distanceDataAscGrap.size){
                            val subarr = distanceDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > distanceDataAscGrap.count()-1)   distanceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
                    total_forceDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in total_forceDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_forceDataAscGrap.add(i)
                                }else{ total_forceDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<total_forceDataAscGrap.size){
                            val subarr = total_forceDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > total_forceDataAscGrap.count()-1)   total_forceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
                    total_repsDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in total_repsDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_repsDataAscGrap.add(i)
                                }else{ total_repsDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<total_repsDataAscGrap.size){
                            val subarr = total_repsDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > total_repsDataAscGrap.count()-1)   total_repsDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
                    time_under_tensionDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in time_under_tensionDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    time_under_tensionDataAscGrap.add(i)
                                }else{ time_under_tensionDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<time_under_tensionDataAscGrap.size){
                            val subarr = time_under_tensionDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > time_under_tensionDataAscGrap.count()-1)   time_under_tensionDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {
                    total_timeDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in total_timeDataAsc) {
                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_timeDataAscGrap.add(i)
                                }else{ total_timeDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<total_timeDataAscGrap.size){
                            val subarr = total_timeDataAscGrap.subList(sdate, eDate+1)
                            val totaldata =
                                subarr.map { it.totalData }.
                                reduce { acc, next -> acc + next }
                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > total_timeDataAscGrap.count()-1)   total_timeDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                    Log.d("totaltime", "setUpDataForChart: "+subtotal_timeDataAsc)
                }
                setUpLineChartForWeek()
            }
            "6 Months" -> {
                binding.lineChart.visibility=View.VISIBLE
                binding.barChart.visibility=View.GONE
                binding.lineChart .notifyDataSetChanged();
                binding.lineChart .invalidate();
                Toast.makeText(this@PerformanceDetailsActivity, selectedItem, Toast.LENGTH_SHORT).show()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentdate = sdf.format(Date())
                val startDate = LocalDate.of(
                    currentdate.split("-")[0].toInt(),
                    currentdate.split("-")[1].toInt(),
                    currentdate.split("-")[2].toInt()
                )
                val endDateSevenDay = getDaysAgo(179)
                val endDate = LocalDate.of(
                    endDateSevenDay.split("-")[0].toInt(),
                    endDateSevenDay.split("-")[1].toInt(),
                    endDateSevenDay.split("-")[2].toInt()
                )

                if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {
                    heart_rateDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in heart_rateDataAsc) {

                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    heart_rateDataAscGrap.add(i)
                                }else{ heart_rateDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<heart_rateDataAscGrap.size){
                            val subarr = heart_rateDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > heart_rateDataAscGrap.count()-1)   heart_rateDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("calories")) {
                    caloriesDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in caloriesDataAsc) {

                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    caloriesDataAscGrap.add(i)
                                }else{ caloriesDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                            }

                        }
                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<caloriesDataAscGrap.size){
                            val subarr = caloriesDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > caloriesDataAscGrap.count()-1)   caloriesDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("distance")) {
                    distanceDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in distanceDataAsc) {

                            if (i.date == date.toString()) {
                                if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    distanceDataAscGrap.add(i)
                                }else{ distanceDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                            }

                        }

                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<distanceDataAscGrap.size){
                            val subarr = distanceDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > distanceDataAscGrap.count()-1)   distanceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
                    total_forceDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in total_forceDataAsc) {

                            if (i.date == date.toString()) {
                                if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_forceDataAscGrap.add(i)
                                }else{ total_forceDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                            }

                        }

                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<total_forceDataAscGrap.size){
                            val subarr = total_forceDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > total_forceDataAscGrap.count()-1)   total_forceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
                    total_repsDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in total_repsDataAsc) {

                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_repsDataAscGrap.add(i)
                                }else{ total_repsDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<total_repsDataAscGrap.size){
                            val subarr = total_repsDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > total_repsDataAscGrap.count()-1)   total_repsDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
                    time_under_tensionDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in time_under_tensionDataAsc) {

                            if (i.date == date.toString()) {
                                if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    time_under_tensionDataAscGrap.add(i)
                                }else{ time_under_tensionDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                            }

                        }
                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<time_under_tensionDataAscGrap.size){
                            val subarr = time_under_tensionDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > time_under_tensionDataAscGrap.count()-1)   time_under_tensionDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {
                    total_timeDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate

                    for (date in dateRange) {
                        loop@ for (i in total_timeDataAsc) {

                            if (i.date == date.toString()) {
                                //  total_timeDataAscGrap.add(i)
                                if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_timeDataAscGrap.add(i)
                                }else{ total_timeDataAscGrap.add(i)}
                                // return
                                break@loop

                            } else  {
                                if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }

                                //   break@loop
                            }

                        }


                    }
                    var sdate=0
                    var eDate=6
                    loop@ for (date in dateRange) {
                        if(sdate<total_timeDataAscGrap.size){
                            val subarr = total_timeDataAscGrap.subList(sdate, eDate+1)

                            val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                            val data = GrapDataShort(
                                subarr[0].date,
                                totaldata,
                                subarr.first().date,
                                subarr.last().date
                            )
                            subtotal_timeDataAsc.add(data)

                            sdate = eDate + 1 //8
                            val appendedDateIndexAfterStatDate = sdate + 6
                            eDate =if(appendedDateIndexAfterStatDate > total_timeDataAscGrap.count()-1)   total_timeDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                        } else {
                            break@loop
                        }

                    }
                    Log.d("totaltime", "setUpDataForChart: "+subtotal_timeDataAsc)
                }

                setUpLineChartForWeek()
            }
            else ->{
                binding.lineChart.visibility=View.VISIBLE
                binding.barChart.visibility=View.GONE
                binding.lineChart .notifyDataSetChanged();
                binding.lineChart .invalidate();
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentdate = sdf.format(Date())
                val startDate = LocalDate.of(
                    currentdate.split("-")[0].toInt(),
                    currentdate.split("-")[1].toInt(),
                    currentdate.split("-")[2].toInt()
                )
                val endDateSevenDay = getDaysAgo(89)
                val endDate = LocalDate.of(
                    endDateSevenDay.split("-")[0].toInt(),
                    endDateSevenDay.split("-")[1].toInt(),
                    endDateSevenDay.split("-")[2].toInt()
                )

                if (dataListRateKacl[selectedPosition].key.equals("heart_rate")) {
                    heart_rateDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in heart_rateDataAsc) {
                            if (i.date == date.toString()) {
                                if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    heart_rateDataAscGrap.add(i)
                                }else{ heart_rateDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${heart_rateDataAsc.get(0).date.split("-")[1]}/${heart_rateDataAsc.get(0).date.split("-")[2]}/${heart_rateDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        heart_rateDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in heart_rateDataAsc) {

                                if (i.date == date.toString()) {

                                    if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        heart_rateDataAscGrap.add(i)
                                    }else{ heart_rateDataAscGrap.add(i)}

                                    break@loop
                                } else  {
                                    if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){

                                        heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))

                                    }
                                }


                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        heart_rateDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in heart_rateDataAsc) {
                                if (i.date == date.toString()) {
                                    if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        heart_rateDataAscGrap.add(i)
                                    }else{ heart_rateDataAscGrap.add(i)}
                                    break@loop

                                } else  {
                                    if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<heart_rateDataAscGrap.size){
                                val subarr = heart_rateDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > heart_rateDataAscGrap.count()-1)   heart_rateDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        heart_rateDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()

                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in heart_rateDataAsc) {

                                if (i.date == date.toString()) {
                                    //  total_timeDataAscGrap.add(i)
                                    if (heart_rateDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        heart_rateDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        heart_rateDataAscGrap.add(i)
                                    }else{ heart_rateDataAscGrap.add(i)}
                                    // return
                                    break@loop

                                } else  {
                                    if (!heart_rateDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        heart_rateDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }

                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<heart_rateDataAscGrap.size){
                                val subarr = heart_rateDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }


                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > heart_rateDataAscGrap.count()-1)   heart_rateDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<heart_rateDataAscGrap.size){
                                val subarr = heart_rateDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > heart_rateDataAscGrap.count()-1)   heart_rateDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("calories")) {
                    caloriesDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in caloriesDataAsc) {
                            if (i.date == date.toString()) {
                                if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    caloriesDataAscGrap.add(i)
                                }else{ caloriesDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${caloriesDataAsc.get(0).date.split("-")[1]}/${caloriesDataAsc.get(0).date.split("-")[2]}/${caloriesDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        caloriesDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in caloriesDataAsc) {

                                if (i.date == date.toString()) {

                                    if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        caloriesDataAscGrap.add(i)
                                    }else{ caloriesDataAscGrap.add(i)}


                                    break@loop
                                } else  {
                                    if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){

                                        caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))

                                    }
                                }
                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        caloriesDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in caloriesDataAsc) {
                                if (i.date == date.toString()) {
                                    if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        caloriesDataAscGrap.add(i)
                                    }else{ caloriesDataAscGrap.add(i)}
                                    break@loop

                                } else  {
                                    if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<caloriesDataAscGrap.size){
                                val subarr = caloriesDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > caloriesDataAscGrap.count()-1)   caloriesDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        caloriesDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in caloriesDataAsc) {

                                if (i.date == date.toString()) {
                                    //  total_timeDataAscGrap.add(i)
                                    if (caloriesDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        caloriesDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        caloriesDataAscGrap.add(i)
                                    }else{ caloriesDataAscGrap.add(i)}
                                    // return
                                    break@loop

                                } else  {
                                    if (!caloriesDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        caloriesDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<caloriesDataAscGrap.size){
                                val subarr = caloriesDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }


                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > caloriesDataAscGrap.count()-1)   caloriesDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }

                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<caloriesDataAscGrap.size){
                                val subarr = caloriesDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > caloriesDataAscGrap.count()-1)   caloriesDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("distance")) {
                    distanceDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in distanceDataAsc) {
                            if (i.date == date.toString()) {
                                if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    distanceDataAscGrap.add(i)
                                }else{ distanceDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${distanceDataAsc.get(0).date.split("-")[1]}/${distanceDataAsc.get(0).date.split("-")[2]}/${distanceDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        distanceDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in distanceDataAsc) {

                                if (i.date == date.toString()) {

                                    if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        distanceDataAscGrap.add(i)
                                    }else{ distanceDataAscGrap.add(i)}



                                    //  }

                                    break@loop
                                } else  {
                                    if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){

                                        distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))

                                    }
                                }


                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        distanceDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in distanceDataAsc) {
                                if (i.date == date.toString()) {
                                    if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        distanceDataAscGrap.add(i)
                                    }else{ distanceDataAscGrap.add(i)}
                                    break@loop

                                } else  {
                                    if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<distanceDataAscGrap.size){
                                val subarr = distanceDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > distanceDataAscGrap.count()-1)   distanceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        distanceDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in distanceDataAsc) {

                                if (i.date == date.toString()) {
                                    if (distanceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        distanceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        distanceDataAscGrap.add(i)
                                    }else{ distanceDataAscGrap.add(i)}
                                    // return
                                    break@loop
                                } else  {
                                    if (!distanceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        distanceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<distanceDataAscGrap.size){
                                val subarr = distanceDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }


                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > distanceDataAscGrap.count()-1)   distanceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<distanceDataAscGrap.size){
                                val subarr = distanceDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > distanceDataAscGrap.count()-1)   distanceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_force")) {
                    total_forceDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in total_forceDataAsc) {
                            if (i.date == date.toString()) {
                                if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_forceDataAscGrap.add(i)
                                }else{ total_forceDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${total_forceDataAsc.get(0).date.split("-")[1]}/${total_forceDataAsc.get(0).date.split("-")[2]}/${total_forceDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        total_forceDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in total_forceDataAsc) {

                                if (i.date == date.toString()) {

                                    if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_forceDataAscGrap.add(i)
                                    }else{ total_forceDataAscGrap.add(i)}



                                    //  }

                                    break@loop
                                } else  {
                                    if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){

                                        total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))

                                    }
                                }


                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        total_forceDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in total_forceDataAsc) {
                                if (i.date == date.toString()) {
                                    if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_forceDataAscGrap.add(i)
                                    }else{ total_forceDataAscGrap.add(i)}
                                    break@loop
                                } else  {
                                    if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<total_forceDataAscGrap.size){
                                val subarr = total_forceDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > total_forceDataAscGrap.count()-1)   total_forceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        total_forceDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()

                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in total_forceDataAsc) {

                                if (i.date == date.toString()) {
                                    //  total_timeDataAscGrap.add(i)
                                    if (total_forceDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_forceDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_forceDataAscGrap.add(i)
                                    }else{ total_forceDataAscGrap.add(i)}
                                    // return
                                    break@loop

                                } else  {
                                    if (!total_forceDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_forceDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }

                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<total_forceDataAscGrap.size){
                                val subarr = total_forceDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }


                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > total_forceDataAscGrap.count()-1)   total_forceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<total_forceDataAscGrap.size){
                                val subarr = total_forceDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > total_forceDataAscGrap.count()-1)   total_forceDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_reps")) {
                    total_repsDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in total_repsDataAsc) {
                            if (i.date == date.toString()) {
                                if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_repsDataAscGrap.add(i)
                                }else{ total_repsDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${total_repsDataAsc.get(0).date.split("-")[1]}/${total_repsDataAsc.get(0).date.split("-")[2]}/${total_repsDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        total_repsDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in total_repsDataAsc) {

                                if (i.date == date.toString()) {

                                    if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_repsDataAscGrap.add(i)
                                    }else{ total_repsDataAscGrap.add(i)}

                                    break@loop
                                } else  {
                                    if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }

                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        total_repsDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in total_repsDataAsc) {
                                if (i.date == date.toString()) {
                                    if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_repsDataAscGrap.add(i)
                                    }else{ total_repsDataAscGrap.add(i)}
                                    break@loop

                                } else  {
                                    if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<total_repsDataAscGrap.size){
                                val subarr = total_repsDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > total_repsDataAscGrap.count()-1)   total_repsDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        total_repsDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()

                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in total_repsDataAsc) {

                                if (i.date == date.toString()) {
                                    //  total_timeDataAscGrap.add(i)
                                    if (total_repsDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_repsDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_repsDataAscGrap.add(i)
                                    }else{ total_repsDataAscGrap.add(i)}
                                    // return
                                    break@loop

                                } else  {
                                    if (!total_repsDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_repsDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }

                                }

                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<total_repsDataAscGrap.size){
                                val subarr = total_repsDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }


                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > total_repsDataAscGrap.count()-1)   total_repsDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<total_repsDataAscGrap.size){
                                val subarr = total_repsDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > total_repsDataAscGrap.count()-1)   total_repsDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("time_under_tension")) {
                    time_under_tensionDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in time_under_tensionDataAsc) {
                            if (i.date == date.toString()) {
                                if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    time_under_tensionDataAscGrap.add(i)
                                }else{ time_under_tensionDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${time_under_tensionDataAsc.get(0).date.split("-")[1]}/${time_under_tensionDataAsc.get(0).date.split("-")[2]}/${time_under_tensionDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        total_repsDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in time_under_tensionDataAsc) {

                                if (i.date == date.toString()) {

                                    if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        time_under_tensionDataAscGrap.add(i)
                                    }else{ time_under_tensionDataAscGrap.add(i)}

                                    break@loop
                                } else  {
                                    if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        time_under_tensionDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in time_under_tensionDataAsc) {
                                if (i.date == date.toString()) {
                                    if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        time_under_tensionDataAscGrap.add(i)
                                    }else{ time_under_tensionDataAscGrap.add(i)}
                                    break@loop

                                } else  {
                                    if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<time_under_tensionDataAscGrap.size){
                                val subarr = time_under_tensionDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > time_under_tensionDataAscGrap.count()-1)   time_under_tensionDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }

                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        time_under_tensionDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in time_under_tensionDataAsc) {

                                if (i.date == date.toString()) {
                                    //  total_timeDataAscGrap.add(i)
                                    if (time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        time_under_tensionDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        time_under_tensionDataAscGrap.add(i)
                                    }else{ time_under_tensionDataAscGrap.add(i)}
                                    // return
                                    break@loop

                                } else  {
                                    if (!time_under_tensionDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        time_under_tensionDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }

                                }
                            }

                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<time_under_tensionDataAscGrap.size){
                                val subarr = time_under_tensionDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }

                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > time_under_tensionDataAscGrap.count()-1)   time_under_tensionDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }

                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<time_under_tensionDataAscGrap.size){
                                val subarr = time_under_tensionDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > time_under_tensionDataAscGrap.count()-1)   time_under_tensionDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                }
                else if (dataListRateKacl[selectedPosition].key.equals("total_time")) {
                    total_timeDataAscGrap.clear()
                    subtotal_timeDataAsc.clear()

                    val dateRange = endDate..startDate
                    for (date in dateRange) {
                        loop@ for (i in total_timeDataAsc) {
                            if (i.date == date.toString()) {
                                if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                    total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                    total_timeDataAscGrap.add(i)
                                }else{ total_timeDataAscGrap.add(i)}
                                break@loop
                            } else  {
                                if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                    total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                }
                            }
                        }
                    }

                    val currentDate: Date
                    val responseStartDate : Date
                    val dates = SimpleDateFormat("MM/dd/yyyy")

                    currentDate = dates.parse("${startDate.toString().split("-")[1]}/${startDate.toString().split("-")[2]}/${startDate.toString().split("-")[0]}")
                    responseStartDate = dates.parse("${total_timeDataAsc.get(0).date.split("-")[1]}/${total_timeDataAsc.get(0).date.split("-")[2]}/${total_timeDataAsc.get(0).date.split("-")[0]}")
                    val difference = Math.abs(currentDate.time - responseStartDate.time)
                    val differenceDates = difference / (24 * 60 * 60 * 1000)
                    dayDifference = java.lang.Long.toString(differenceDates)


                    if (dayDifference.toInt()<=7){
                        total_timeDataAscGrap.clear()
                        for (date in endDate..startDate) {

                            loop@ for (i in total_timeDataAsc) {

                                if (i.date == date.toString()) {

                                    if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_timeDataAscGrap.add(i)
                                    }else{ total_timeDataAscGrap.add(i)}



                                    //  }

                                    break@loop
                                } else  {
                                    if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){

                                        total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))

                                    }
                                }


                            }
                        }
                    }
                    else  if (dayDifference.toInt()<=90){
                        total_timeDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateRange = endDate..startDate
                        for (date in dateRange) {
                            loop@ for (i in total_timeDataAsc) {
                                if (i.date == date.toString()) {
                                    if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_timeDataAscGrap.add(i)
                                    }else{ total_timeDataAscGrap.add(i)}
                                    break@loop

                                } else  {
                                    if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }
                                }
                            }
                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<total_timeDataAscGrap.size){
                                val subarr = total_timeDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }
                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)

                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > total_timeDataAscGrap.count()-1)   total_timeDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


                        }
                    }
                    else  if (dayDifference.toInt()<=180){
                        total_timeDataAscGrap.clear()
                        subtotal_timeDataAsc.clear()
                        val dateStringArray = total_timeDataAsc.mapNotNull { it.date }
                        val dateRange = endDate..startDate

                        for (date in dateRange) {
                            loop@ for (i in total_timeDataAsc) {

                                if (i.date == date.toString()) {
                                    //  total_timeDataAscGrap.add(i)
                                    if (total_timeDataAscGrap.contains(GrapDataShort(date.toString(),0))){
                                        total_timeDataAscGrap.remove(GrapDataShort(date.toString(),0))
                                        total_timeDataAscGrap.add(i)
                                    }else{ total_timeDataAscGrap.add(i)}
                                    // return
                                    break@loop

                                } else  {
                                    if (!total_timeDataAscGrap.contains(GrapDataShort(date.toString(), 0))){
                                        total_timeDataAscGrap.add(GrapDataShort(date.toString(), 0))
                                    }

                                    //   break@loop
                                }

                            }


                        }
                        var sdate=0
                        var eDate=6
                        loop@ for (date in dateRange) {
                            if(sdate<total_timeDataAscGrap.size){
                                val subarr = total_timeDataAscGrap.subList(sdate, eDate+1)

                                val totaldata =
                                    subarr.map { it.totalData }.
                                    reduce { acc, next -> acc + next }


                                val data = GrapDataShort(
                                    subarr[0].date,
                                    totaldata,
                                    subarr.first().date,
                                    subarr.last().date
                                )
                                subtotal_timeDataAsc.add(data)


                                sdate = eDate + 1 //8
                                val appendedDateIndexAfterStatDate = sdate + 6
                                eDate =if(appendedDateIndexAfterStatDate > total_timeDataAscGrap.count()-1)   total_timeDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }


//                        eDate = sdate + 6 // 14

                        }
                    }
                    else{
                        var startmonth=  endDate.month
                        var sdate=0
                        var eDate=(LocalDate.of(endDate.year, endDate.month, 1).lengthOfMonth())-endDate.dayOfMonth
                        loop@ for (date in dateRange) {
                            if(sdate<total_timeDataAscGrap.size){
                                val subarr = total_timeDataAscGrap.subList(sdate, eDate+1)
                                val totaldata = subarr.map { it.totalData }.reduce { acc, next -> acc + next }

                                val data = GrapDataShort(subarr[0].date, totaldata, subarr.first().date, subarr.last().date)
                                subtotal_timeDataAsc.add(data)
                                sdate = eDate + 1 //8
                                startmonth=startmonth+1
                                val appendedDateIndexAfterStatDate = sdate + (LocalDate.of(endDate.year, startmonth, 1).lengthOfMonth())-1
                                eDate =if(appendedDateIndexAfterStatDate > total_timeDataAscGrap.count()-1)   total_timeDataAscGrap.count()-1 else appendedDateIndexAfterStatDate
                            } else {
                                break@loop
                            }
                        }
                    }
                    Log.d("totaltime", "setUpDataForChart: "+subtotal_timeDataAsc)
                }

                setUpLineChartForWeek()
            }
        }

    }

    private fun setUpAdapter() {
        if (dataListAllTime.size > 0) {
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
            layoutManagerGoal = LinearLayoutManager(
                this@PerformanceDetailsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            this.layoutManager = layoutManagerGoal
            performanceDetailsGoalAdapter = PerformanceDetailsGoalAdapter(
                dataListGoal,
                this@PerformanceDetailsActivity,
                selectedPosition
            )
            this.adapter = performanceDetailsGoalAdapter
        }


        binding.rvMetricsList.apply {
            layoutManagerMetrics = GridLayoutManager(this@PerformanceDetailsActivity, 2)
            this.layoutManager = layoutManagerMetrics
            performanceMetricsDetailsAdapter = PerformanceMetricsDetailsAdapter(
                dataListMetrics,
                this@PerformanceDetailsActivity,
                selectedPosition,
                binding.rvMetricsList
            )
            this.adapter = performanceMetricsDetailsAdapter

        }

        performanceMetricsDetailsAdapter!!.setAdapterListener(this)



        setDatatoHomepagechart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    private fun setUpChartForWeek() {
        val values = ArrayList<String>()
        values.clear()
        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }
        var tfLight: Typeface? = null
        tfLight = Typeface.createFromAsset(assets, "futura_std_medium.otf")

        binding.barChart.minOffset = 30f
        binding.barChart.setNoDataText("NO RECORDS AVAILABLE")
        binding.barChart.setNoDataTextColor(ContextCompat.getColor(this, R.color.white))

        val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(binding.barChart)
        val xAxis: XAxis = binding.barChart.xAxis

        when(selectedItem){

            "Past week" ->{

                if(heart_rateDataAscGrap.size>0){


                    for (i in heart_rateDataAscGrap.indices){
                        var str=heart_rateDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==heart_rateDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }



                    }


                }
                if(caloriesDataAscGrap.size>0){


                    for (i in caloriesDataAscGrap.indices){
                        var str=caloriesDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==caloriesDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }



                    }


                }
                if(distanceDataAscGrap.size>0){


                    for (i in distanceDataAscGrap.indices){
                        var str=distanceDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==distanceDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }



                    }


                }
                if(total_forceDataAscGrap.size>0){

                    for (i in total_forceDataAscGrap.indices){
                        var str=total_forceDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==total_forceDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }

                    }

                }
                if(total_repsDataAscGrap.size>0){


                    for (i in total_repsDataAscGrap.indices){
                        var str=total_repsDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==total_repsDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }

                    }


                }
                if(time_under_tensionDataAscGrap.size>0){

                    for (i in time_under_tensionDataAscGrap.indices){
                        var str=time_under_tensionDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==time_under_tensionDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }



                    }


                }
                if(total_timeDataAscGrap.size>0){


                    for (i in total_timeDataAscGrap.indices){
                        var str=total_timeDataAscGrap.get(i).date.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt());
                        if (i==total_timeDataAscGrap.size-1){
                            values.add("today"+"\n")
                        }else{
                            values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                        }



                    }

                }
            }
        }

        xAxis.valueFormatter = IndexAxisValueFormatter(values)
        xAxis.textSize= 14F
        xAxis.position = XAxisPosition.BOTTOM
        binding.barChart.setScaleEnabled(false)
        binding.barChart.setPinchZoom(false)
        binding.barChart.legend.isEnabled = false
        binding.barChart.setDrawMarkers(true)
        binding.barChart.axisRight.isEnabled = false
        binding.barChart.axisLeft.isEnabled = true
        binding.barChart.axisLeft.setDrawGridLines(false)
        binding.barChart.axisLeft.setDrawAxisLine(true)
        binding.barChart.axisLeft.setDrawLabels(true)
        binding.barChart.axisLeft.textColor = ContextCompat.getColor(this, R.color.white)
        binding.barChart.axisLeft.axisMinimum = 0.0f
        binding.barChart.axisLeft.typeface = tfLight

        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1.0f
        xAxis.setDrawLabels(true)
        xAxis.textColor = ContextCompat.getColor(this, R.color.white)
        xAxis.typeface = tfLight


        /* xAxis.setValueFormatter(xAxisFormatter)
         val mv = XYMarkerView(this, xAxisFormatter)
         mv.chartView = binding.barChart
         binding.barChart.setMarker(mv)*/
        setDataChart()
    }

    private fun setUpLineChartForWeek() {
        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }
        var tfLight: Typeface? = null
        tfLight = Typeface.createFromAsset(assets, "futura_std_medium.otf")

        binding.lineChart.minOffset = 30f
        binding.lineChart.setNoDataText("NO RECORDS AVAILABLE")
        binding.lineChart.setNoDataTextColor(ContextCompat.getColor(this, R.color.white))

        // val weekdays =// Your List / array with String Values For X-axis Labels

        // Set the value formatter
        val xAxis: XAxis = binding.lineChart.xAxis

        // xAxis.valueFormatter = IndexAxisValueFormatter(weekdays)
        when(selectedItem){

            "All time"->{
                val values = ArrayList<String>()

                if (dayDifference.toInt()<=7){



                    if(heart_rateDataAscGrap.size>0){


                        for (i in heart_rateDataAscGrap.indices){
                            var str=heart_rateDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==heart_rateDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }



                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }
                    if(caloriesDataAscGrap.size>0){


                        for (i in caloriesDataAscGrap.indices){
                            var str=caloriesDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==caloriesDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }



                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }
                    if(distanceDataAscGrap.size>0){


                        for (i in distanceDataAscGrap.indices){
                            var str=distanceDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==distanceDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }



                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }
                    if(total_forceDataAscGrap.size>0){

                        for (i in total_forceDataAscGrap.indices){
                            var str=total_forceDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==total_forceDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }

                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }
                    if(total_repsDataAscGrap.size>0){


                        for (i in total_repsDataAscGrap.indices){
                            var str=total_repsDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==total_repsDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }

                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }
                    if(time_under_tensionDataAscGrap.size>0){

                        for (i in time_under_tensionDataAscGrap.indices){
                            var str=time_under_tensionDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==time_under_tensionDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }



                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }
                    if(total_timeDataAscGrap.size>0){


                        for (i in total_timeDataAscGrap.indices){
                            var str=total_timeDataAscGrap.get(i).date.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt());
                            if (i==total_timeDataAscGrap.size-1){
                                values.add("today"+"\n")
                            }else{
                                values.add(str[2] +" "+month_date.format(cal.getTime())+"\n")
                            }



                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)
                        xAxis.textSize= 14F

                    }


                }
                else  if (dayDifference.toInt()<=90){

                    if(subtotal_timeDataAsc.size>0){

                        for (i in 0.. subtotal_timeDataAsc.size-1){
                            var str= subtotal_timeDataAsc.get(i).startDate!!.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt()-1);

                            var endDate= subtotal_timeDataAsc.get(i).endDate!!.split("-")
                            val cal1 = Calendar.getInstance()
                            val monthdate = SimpleDateFormat("MMM")
                            cal1.set(Calendar.MONTH,endDate[1].toInt()-1);


                            if(i%2==0){
                                if (i==subtotal_timeDataAsc.size-1) { values.add(str[2] +" "+month_date.format(cal.getTime())+" - "+"\n"+"today")
                                }else {
                                    values.add(
                                        str[2] + " " + month_date.format(cal.getTime()) + " - " + "\n" + endDate[2] + " " + monthdate.format(
                                            cal1.getTime()
                                        )
                                    )
                                }
                            }else{
                                values.add("\n")
                            }
                        }
                        xAxis.valueFormatter = IndexAxisValueFormatter(values)

                    }
                }
                else  if (dayDifference.toInt()<=180){
                    if(subtotal_timeDataAsc.size>0){

                        for (i in subtotal_timeDataAsc.indices step 4){
                            var str= subtotal_timeDataAsc.get(i).startDate!!.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt()-1);

                            var endDate= subtotal_timeDataAsc.get(i).endDate!!.split("-")
                            val cal1 = Calendar.getInstance()
                            val monthdate = SimpleDateFormat("MMM")
                            cal1.set(Calendar.MONTH,endDate[1].toInt()-1);


                            for (j in i..i+3)
                            {
                                if (j==i){
                                    if (i==subtotal_timeDataAsc.size-2) { values.add(str[2] +" "+month_date.format(cal.getTime())+" - "+"\n"+"today")
                                    }else {
                                        values.add(
                                            str[2] + " " + month_date.format(cal.getTime()) + " - " + "\n" + endDate[2] + " " + monthdate.format(
                                                cal1.getTime()
                                            )
                                        )
                                    }
                                }else   values.add("\n")
                            }


                        }

                    }
                }
                else{

                    if(subtotal_timeDataAsc.size>0){

                        for (i in subtotal_timeDataAsc.indices step 4){
                            var str= subtotal_timeDataAsc.get(i).startDate!!.split("-")
                            val cal = Calendar.getInstance()
                            val month_date = SimpleDateFormat("MMM")
                            cal.set(Calendar.MONTH,str[1].toInt()-1);

                            var endDate= subtotal_timeDataAsc.get(i).endDate!!.split("-")
                            val cal1 = Calendar.getInstance()
                            val monthdate = SimpleDateFormat("MMM")
                            cal1.set(Calendar.MONTH,endDate[1].toInt()-1);

                            for (j in i..i+3)
                            {
                                if (j==i){
                                    if (i==subtotal_timeDataAsc.size-2) { values.add(str[2] +" "+month_date.format(cal.getTime())+" - "+"\n"+"today")
                                    }else {
                                        values.add(month_date.format(cal.getTime()) + "\n" + str[0])
                                    }
                                }else   values.add("\n")
                            }


                        }

                    }
                }


            }
            "3 Months"->{
                val values = ArrayList<String>()
                val linevalues = ArrayList<String>()
                if(subtotal_timeDataAsc.size>0){

                    for (i in subtotal_timeDataAsc.indices){
                        var str= subtotal_timeDataAsc.get(i).startDate!!.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt()-1);

                        var endDate= subtotal_timeDataAsc.get(i).endDate!!.split("-")
                        val cal1 = Calendar.getInstance()
                        val monthdate = SimpleDateFormat("MMM")
                        cal1.set(Calendar.MONTH,endDate[1].toInt()-1);


                        if(i%2==0){
                            if (i==subtotal_timeDataAsc.size-1) { values.add(str[2] +" "+month_date.format(cal.getTime())+" - "+"\n"+"today")
                            }else {
                                values.add(str[2] + " " + month_date.format(cal.getTime()) + " - " + "\n" + endDate[2] + " " + monthdate.format(cal1.getTime()))
                            }

                        }else{
                            values.add("\n")
                        }

                    }


                    //  }

                }



                xAxis.valueFormatter = IndexAxisValueFormatter(values)


            }
            "6 Months" ->{
                val values = ArrayList<String>()
                var data= values.size
                val linevalues = ArrayList<String>()
                if(subtotal_timeDataAsc.size>0){

                    for (i in subtotal_timeDataAsc.indices step 4){
                        var str= subtotal_timeDataAsc.get(i).startDate!!.split("-")
                        val cal = Calendar.getInstance()
                        val month_date = SimpleDateFormat("MMM")
                        cal.set(Calendar.MONTH,str[1].toInt()-1);

                        var endDate= subtotal_timeDataAsc.get(i).endDate!!.split("-")
                        val cal1 = Calendar.getInstance()
                        val monthdate = SimpleDateFormat("MMM")
                        cal1.set(Calendar.MONTH,endDate[1].toInt()-1);


                        for (j in i..i+3)
                        {
                            if (j==i){
                                if (i==subtotal_timeDataAsc.size-2) { values.add(str[2] +" "+month_date.format(cal.getTime())+" - "+"\n"+"today")
                                }else {
                                    values.add(str[2] +" "+month_date.format(cal.getTime())+" - "+"\n"+endDate[2]+" "+monthdate.format(cal1.getTime()) )
                                }

                            }else   values.add("\n")
                        }


                    }

                }



                xAxis.valueFormatter = IndexAxisValueFormatter(values)


            }


        }
        binding.lineChart.setXAxisRenderer(
            CustomXAxisRenderer(
                binding. lineChart.getViewPortHandler(),
                binding. lineChart.getXAxis(),
                binding. lineChart.getTransformer(YAxis.AxisDependency.LEFT)
            )
        )
        xAxis.textSize= 14F
        xAxis.position = XAxisPosition.BOTTOM
        binding.lineChart.setScaleEnabled(false)
        binding.lineChart.setPinchZoom(false)
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.setDrawMarkers(true)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.axisLeft.isEnabled = true
        binding.lineChart.axisLeft.setDrawGridLines(false)
        binding.lineChart.axisLeft.setDrawAxisLine(true)
        binding.lineChart.axisLeft.setDrawLabels(true)
        binding.lineChart.axisLeft.textColor = ContextCompat.getColor(this, R.color.white)
        binding.lineChart.axisLeft.axisMinimum = 0.0f
        binding.lineChart.axisLeft.typeface = tfLight

        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1.0f
        xAxis.setDrawLabels(true)
        xAxis.textColor = ContextCompat.getColor(this, R.color.white)
        xAxis.typeface = tfLight


        //xAxis.setValueFormatter(xAxisFormatter)
        /*   val mv = XYMarkerView(this, xAxisFormatter)
           mv.chartView = binding.lineChart*/
        //  binding.lineChart.setMarker(mv)
        setDataLineChart()
    }

    fun setDataLineChart(){
        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }
        val getvalues = ArrayList<Entry>()

        getvalues.clear()
        when(selectedItem) {

            "3 Months"->{


                if(subtotal_timeDataAsc.size>0){
                    subtotal_timeDataAsc.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, Entry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }

            }
            "6 Months" ->{
                if(subtotal_timeDataAsc.size>0){
                    subtotal_timeDataAsc.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, Entry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
            }
            "All time"->{


                if(subtotal_timeDataAsc.size>0){
                    subtotal_timeDataAsc.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, Entry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }

            }
        }

        var set1: LineDataSet
        set1 = LineDataSet(getvalues,"")
        set1.setDrawValues(false)
        set1.setColors(colournew)
        set1.highLightColor = colournew
        set1.formLineWidth = 1.0f
        set1.setDrawCircleHole(false)
        set1.setDrawCircles(false)
        set1.lineWidth= 2F
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        set1.setColors(colournew)
        binding.lineChart.xAxis.labelCount = set1.entryCount
        //binding.barChart.xAxis.valueFormatter = ChartXAxisFormatter(caloriesDataAscGrap)
        val data = LineData(set1)
        binding.lineChart.data = data
    }
    fun setDataChart() {
        val values = ArrayList<BarEntry>()
        values.clear()



        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }
        val getvalues = ArrayList<BarEntry>()

        getvalues.clear()
        when(selectedItem) {
            "Past week" -> {

                if(total_timeDataAscGrap.size>0){
                    total_timeDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
                if(caloriesDataAscGrap.size>0){
                    caloriesDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
                if(heart_rateDataAscGrap.size>0){
                    heart_rateDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
                if(distanceDataAscGrap.size>0){
                    distanceDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
                if(total_forceDataAscGrap.size>0){
                    total_forceDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
                if(total_repsDataAscGrap.size>0){
                    total_repsDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }
                if(time_under_tensionDataAscGrap.size>0){
                    time_under_tensionDataAscGrap.forEachIndexed { index, grapDataShort ->
                        getvalues.add(index, BarEntry(index.toFloat(),grapDataShort.totalData.toFloat()))
                    }
                }

            }
        }
        var set1: BarDataSet
        set1 = BarDataSet(getvalues,"")
        set1.setDrawValues(false)
        set1.setColors(colournew)
        set1.highLightColor = colournew
        set1.formLineWidth = 1.0f

        set1.setDrawValues(false)
        set1.setColors(colournew)
        binding.barChart.xAxis.labelCount = set1.entryCount
        //binding.barChart.xAxis.valueFormatter = ChartXAxisFormatter(caloriesDataAscGrap)
        val data = BarData(set1)
        binding.barChart.data = data


    }



    override fun onItemClickTime(position: Int) {
        for (i in 0 until dataListAllTime.size) {
            if (i == position) {
                if (dataListAllTime[i].selected) {

                    dataListAllTime[i].selected = true

                    setUpDataForChart()
                    performanceMetricsDetailsAdapter!!.setPerformanceValue(this,selectedPosition)


                } else {
                    dataListAllTime[i].selected = true
                    // setUpDate(i)
                    setUpDataForChart()
                    performanceMetricsDetailsAdapter!!.setPerformanceValue(this,selectedPosition)
                }
            } else {
                dataListAllTime[i].selected = false
            }
        }
        setDatatoHomepagechart()
        setUpDataForChart()


    }

    private fun setDatatoHomepagechart() {
        binding.spLoading.visibility = View.VISIBLE
        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }

        when (selectedItem) {
            "All time" -> {
                dataListMetrics.forEach {
                    if (it.key.equals("heart_rate")) {
                        var totalValue: Int = 0
                        heart_rateDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    } else if (it.key.equals("calories")) {
                        var totalValue: Int = 0
                        caloriesDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    } else if (it.key.equals("distance")) {
                        var totalValue: Int = 0
                        distanceDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_force")) {
                        var totalValue: Int = 0
                        total_forceDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_reps")) {
                        var totalValue: Int = 0
                        total_repsDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    } else if (it.key.equals("time_under_tension")) {
                        var totalValue: Int = 0
                        time_under_tensionDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_time")) {
                        var totalValue: Int = 0
                        total_timeDataAsc.forEach {
                            totalValue += it.totalData
                        }
                        it.data = totalValue
                    }
                }
            }
            "Past week" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val sevenDayAgoDateFormat = getDaysAgo(7)
                dataListMetrics.forEach {
                    if (it.key.equals("heart_rate")) {
                        var totalValue: Int = 0
                        heart_rateDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                    else if (it.key.equals("calories")) {
                        var totalValue: Int = 0
                        caloriesDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                    else if (it.key.equals("distance")) {
                        var totalValue: Int = 0
                        distanceDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                    else if (it.key.equals("total_force")) {
                        var totalValue: Int = 0
                        total_forceDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                    else if (it.key.equals("total_reps")) {
                        var totalValue: Int = 0
                        total_repsDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                    else if (it.key.equals("time_under_tension")) {
                        var totalValue: Int = 0
                        time_under_tensionDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                    else if (it.key.equals("total_time")) {
                        var totalValue: Int = 0
                        total_timeDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                }
            }
            "3 Months" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val sevenDayAgoDateFormat = getDaysAgo(90)

                dataListMetrics.forEach {
                    if (it.key.equals("heart_rate")) {
                        var totalValue: Int = 0
                        heart_rateDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("calories")) {
                        var totalValue: Int = 0
                        caloriesDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("distance")) {
                        var totalValue: Int = 0
                        distanceDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_force")) {
                        var totalValue: Int = 0
                        total_forceDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_reps")) {
                        var totalValue: Int = 0
                        total_repsDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("time_under_tension")) {
                        var totalValue: Int = 0
                        time_under_tensionDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_time")) {
                        var totalValue: Int = 0
                        total_timeDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                }
            }
            "6 Months" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val sevenDayAgoDateFormat = getDaysAgo(180)
                dataListMetrics.forEach {
                    if (it.key.equals("heart_rate")) {
                        var totalValue: Int = 0
                        heart_rateDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("calories")) {
                        var totalValue: Int = 0
                        caloriesDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("distance")) {
                        var totalValue: Int = 0
                        distanceDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(),
                                    sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()
                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_force")) {
                        var totalValue: Int = 0
                        total_forceDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(),
                                    sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()
                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_reps")) {
                        var totalValue: Int = 0
                        total_repsDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("time_under_tension")) {
                        var totalValue: Int = 0
                        time_under_tensionDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    } else if (it.key.equals("total_time")) {
                        var totalValue: Int = 0
                        total_timeDataAsc.forEach {
                            if (isWithinRange(
                                    it.date.toDate(), sevenDayAgoDateFormat.toDate(),
                                    currentDate.toDate()

                                )
                            ) {
                                totalValue += it.totalData
                            }
                        }
                        it.data = totalValue
                    }
                }
            }
        }
        performanceMetricsDetailsAdapter!!.notifyDataSetChanged()
        binding.spLoading.visibility = View.GONE
    }

    operator fun LocalDate.rangeTo(other: LocalDate): DateProgression {
        return DateProgression(this, other)
    }

    class CustomXAxisRenderer(
        viewPortHandler: ViewPortHandler?,
        xAxis: XAxis?,
        trans: Transformer?
    ) :
        XAxisRenderer(viewPortHandler, xAxis, trans) {
        override fun drawLabel(
            c: Canvas?,
            formattedLabel: String,
            x: Float,
            y: Float,
            anchor: MPPointF?,
            angleDegrees: Float
        ) {
            val line = formattedLabel.split("\n".toRegex()).toTypedArray()
            Utils.drawXAxisValue(
                c,
                line[0], x, y, mAxisLabelPaint, anchor, angleDegrees
            )
            Utils.drawXAxisValue(
                c,
                line[0],
                x + mAxisLabelPaint.textSize,
                y + mAxisLabelPaint.textSize,
                mAxisLabelPaint,
                anchor,
                angleDegrees
            )
        }
    }

    override fun onItemClick(position: Int) {
        for (i in 0 until dataListMetrics.size) {
            if (i == position) {
                if (dataListMetrics[i].selected) {
                    //  dataListAllTime[i].selected = false
                    dataListMetrics[i].selected = true
                    selectedPosition=i
                    setUpDataFromPrev()

                    setUpAdapter()

                    //  setUpDataForChart()
                } else {
                    dataListMetrics[i].selected = true
                    selectedPosition=i
                    setUpDataFromPrev()
                    setUpAdapter()

                    //  setUpDataForChart()

                }
            } else {
                dataListMetrics[i].selected = false
            }
        }
    }

    override fun setPerfmValue(text: String) {
        binding.tvRating.text=text
    }
}
