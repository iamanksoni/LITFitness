package com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.litmethod.android.R
import com.litmethod.android.databinding.CalendarDayBinding
import com.litmethod.android.databinding.FragmentHomeBinding
import com.litmethod.android.models.*
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.CalendarDataModel
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalenderRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.Data6
import com.litmethod.android.models.GetProgramById.Data5
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest
import com.litmethod.android.models.HomePageModels.*
import com.litmethod.android.network.HomeTabFragmentRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseFragment
import com.litmethod.android.ui.Dashboard.HomeTabScreen.PerformanceDetailsScreen.PerformanceDetailsActivity
import com.litmethod.android.ui.VideoPlayer.VideoPlayerActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.GetProgramsByIdToNextScreen
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ClassesCoverActivity
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ProgramsCoverActivity
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModel
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModelFactory
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DataPreferenceObject
import com.litmethod.android.utlis.PeekingLinearLayoutManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class HomeScreenFragment : BaseFragment(), AllTimeAdapter.AllTimeAdapterListener,
    AchievementsAdapter.AchievementsAdapterListener, RateKaclAdapter.RateKaclAdapterListener,
    ProgramMadeForYouAdapter.ProgramMadeForYouAdapterrListener,
    WorkoutGoalChildAdapter.WorkoutGoalChildAdapterListener,
    VideoGetStartedAdapter.VideoGetStartedAdapterListener {
    lateinit var binding: FragmentHomeBinding

    val dataListVideo: ArrayList<Video> = ArrayList<Video>()
    private var layoutManagernewVideo: RecyclerView.LayoutManager? = null
    private var videoGetStartedAdapter: VideoGetStartedAdapter? = null

    val dataListProgram: ArrayList<VideoX> = ArrayList<VideoX>()
    private var layoutManagernewProgram: RecyclerView.LayoutManager? = null
    private var programMadeForYouAdapter: ProgramMadeForYouAdapter? = null

    val dataListWorkOut: ArrayList<VideoXX> = ArrayList<VideoXX>()
    private var layoutManagerWorkOut: RecyclerView.LayoutManager? = null
    private var workoutGoalHeaderAdapter: WorkoutGoalHeaderAdapter? = null

    val dataListAllTime: ArrayList<HomePageVideosModel> = ArrayList<HomePageVideosModel>()
    private var layoutManagerAllTime: RecyclerView.LayoutManager? = null
    private var allTimeAdapter: AllTimeAdapter? = null

    val dataListRateKacl: ArrayList<ResultUserAnalytics> = ArrayList<ResultUserAnalytics>()
    private var layoutManagerRateKacl: RecyclerView.LayoutManager? = null
    private var rateKaclAdapter: RateKaclAdapter? = null

    var dataListAchievement: ArrayList<AchivementsViewModel> = ArrayList<AchivementsViewModel>()
    private var layoutManagerAchievement: RecyclerView.LayoutManager? = null
    private var achievementsAdapter: AchievementsAdapter? = null

    val getAchievementsClassData: ArrayList<ClassMilestone> = ArrayList<ClassMilestone>()
    val getAchievementsDayStreakData: ArrayList<DayStreak> = ArrayList<DayStreak>()
    val getAchievementsweeklyStreakData: ArrayList<WeeklyStreak> = ArrayList<WeeklyStreak>()
    val getAchievementscaloriesachievementsData: ArrayList<Caloriesachievement> =
        ArrayList<Caloriesachievement>()
    val getAchievementsLbsData: ArrayList<Lbsachievement> = ArrayList<Lbsachievement>()
    val cleanedUpcalendarItemsOnlyDateAndDifficulty: ArrayList<CalendarEvent> =
        ArrayList<CalendarEvent>()

    lateinit var viewModel: HomeViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    lateinit var dataPreferenceObject: DataPreferenceObject
    private var token = ""
    private var spinnerWorking: Boolean = false
    private var CalenderAlreadySet: Boolean = false
    private var CalenderFirstTimeSet: Boolean = false
    private lateinit var thisYearMonth: YearMonth
    lateinit var getProgramByIdResponse: Data5
    lateinit var getClassDetailsList: Data6
    var currentKey: String = ""
    var currentPosition = 0
    var totalPosition = 0
    val heart_rateData: ArrayList<GrapData> = ArrayList<GrapData>()
    var heart_rateDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var heart_rateDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    val caloriesData: ArrayList<GrapData> = ArrayList<GrapData>()
    var caloriesDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var caloriesDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    val distanceData: ArrayList<GrapData> = ArrayList<GrapData>()
    var distanceDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var distanceDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    val total_forceData: ArrayList<GrapData> = ArrayList<GrapData>()
    var total_forceDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_forceDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    val total_repsData: ArrayList<GrapData> = ArrayList<GrapData>()
    var total_repsDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_repsDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    val time_under_tensionData: ArrayList<GrapData> = ArrayList<GrapData>()
    var time_under_tensionDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var time_under_tensionDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    val total_timeData: ArrayList<GrapData> = ArrayList<GrapData>()
    var total_timeDataAsc: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
    var total_timeDataVideo: ArrayList<VideoGetUserAnalycticsDetiles> = ArrayList<VideoGetUserAnalycticsDetiles>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        dataPreferenceObject = DataPreferenceObject(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        getTheAccessToken()
        setClickListner()
        viewModelSetup()
        getCurrentYearAndMonth()
        CalenderFirstTimeSet = true
    }

    private fun setClickListner() {

    }

    private fun setupUi() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
        binding.ibSetting.visibility = View.GONE

    }

    private fun setUpAdapter() {
        videoGetStartedAdapter = VideoGetStartedAdapter(dataListVideo, requireContext())
        binding.rvVideoGetStarted.apply {
            layoutManagernewVideo =
                PeekingLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagernewVideo
            this.adapter = videoGetStartedAdapter
            videoGetStartedAdapter?.setAdapterListener(this@HomeScreenFragment)
        }

        binding.rvProgramsMadeForYou.apply {
            layoutManagernewProgram =
                PeekingLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagernewProgram
            programMadeForYouAdapter = ProgramMadeForYouAdapter(dataListProgram, requireContext())
            this.adapter = programMadeForYouAdapter
            programMadeForYouAdapter!!.setAdapterListener(this@HomeScreenFragment)
        }

        binding.rvWorkOut.apply {
            layoutManagerWorkOut = carbon.widget.RecyclerView.LinearLayoutManager(requireActivity())
            this.layoutManager = layoutManagerWorkOut
            workoutGoalHeaderAdapter = WorkoutGoalHeaderAdapter(requireContext(), dataListWorkOut)
            this.adapter = workoutGoalHeaderAdapter
            WorkoutGoalChildAdapter.setAdapterListener(this@HomeScreenFragment)
        }

        dataListAllTime.clear()
        dataListAllTime.add(HomePageVideosModel("Past week", false))
        dataListAllTime.add(HomePageVideosModel("3 Months", false))
        dataListAllTime.add(HomePageVideosModel("6 Months", false))
        dataListAllTime.add(HomePageVideosModel("All time", true))
        binding.rvAllTime.apply {
            layoutManagerAllTime =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerAllTime
            allTimeAdapter = AllTimeAdapter(dataListAllTime, requireContext(), binding.rvAllTime)
            this.adapter = allTimeAdapter

        }
        allTimeAdapter!!.setAdapterListener(this)


        binding.rvRateKacl.apply {
            layoutManagerRateKacl = GridLayoutManager(requireActivity(), 2)
            this.layoutManager = layoutManagerRateKacl
            rateKaclAdapter = RateKaclAdapter(dataListRateKacl, requireContext())
            this.adapter = rateKaclAdapter
//            addItemDecoration(SpacesItemDecoration(19))
        }
        rateKaclAdapter!!.setAdapterListener(this)

        dataListAchievement.clear()
        if(BaseResponseDataObject.hasSubscription) {
            dataListAchievement.add(AchivementsViewModel("Pro Lit", "Member", true))
        }else{
            dataListAchievement.add(AchivementsViewModel("Pro Lit", "Member", false))
        }
        dataListAchievement.add(AchivementsViewModel("0", "Classes", false))
        dataListAchievement.add(AchivementsViewModel("0 Day", "streak", false))
        dataListAchievement.add(AchivementsViewModel("0 Week", "streak", false))
        dataListAchievement.add(AchivementsViewModel("0", "KCAL", false))
        dataListAchievement.add(AchivementsViewModel("0", "LBS LIFTED", false))
        binding.rvAchievements.apply {
            layoutManagerAchievement = GridLayoutManager(requireContext(), 4)
            this.layoutManager = layoutManagerAchievement
            achievementsAdapter =
                AchievementsAdapter(dataListAchievement, requireContext(), binding.rvAchievements)
            this.adapter = achievementsAdapter
        }
        achievementsAdapter!!.setAdapterListener(this)

        binding.spLoading.visibility = View.VISIBLE
        viewModel.getHomeCheck(token)
        viewModel.getUserAnalyticsCheck(token, AppConstants.ALLTIME)
        viewModel.getAchievementsCheck(token)
    }

    override fun onItemClickTime(position: Int) {
        for (i in 0 until dataListAllTime.size) {
            if (i == position) {
                if (dataListAllTime[i].selected) {
//                    dataListAllTime[i].selected = false
                } else {
                    dataListAllTime[i].selected = true
                }
            } else {
                dataListAllTime[i].selected = false
            }
        }
        binding.spLoadingNew.visibility = View.VISIBLE
        setDatatoHomepagechart()
    }

    override fun onItemClickAchievements(position: Int) {
    }

    override fun onItemClickRateKacl(position: Int) {
        val intent = Intent(requireActivity(), PerformanceDetailsActivity::class.java)
        intent.putParcelableArrayListExtra("dataListRateKacl", dataListRateKacl)
        intent.putParcelableArrayListExtra("dataListAllTime", dataListAllTime)
        intent.putParcelableArrayListExtra("heart_rateDataAsc", heart_rateDataAsc)
        intent.putParcelableArrayListExtra("caloriesDataAsc", caloriesDataAsc)
        intent.putParcelableArrayListExtra("distanceDataAsc", distanceDataAsc)
        intent.putParcelableArrayListExtra("total_forceDataAsc", total_forceDataAsc)
        intent.putParcelableArrayListExtra("total_repsDataAsc", total_repsDataAsc)
        intent.putParcelableArrayListExtra("time_under_tensionDataAsc",time_under_tensionDataAsc)
        intent.putParcelableArrayListExtra("total_timeDataAsc", total_timeDataAsc)
        intent.putExtra("position", position)
        if (dataListRateKacl[position].key.equals("heart_rate")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", heart_rateDataVideo!!)
        } else if (dataListRateKacl[position].key.equals("calories")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", caloriesDataVideo!!)
        } else if (dataListRateKacl[position].key.equals("distance")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", distanceDataVideo!!)
        } else if (dataListRateKacl[position].key.equals("total_force")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", total_forceDataVideo!!)
        } else if (dataListRateKacl[position].key.equals("total_reps")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", total_repsDataVideo!!)
        } else if (dataListRateKacl[position].key.equals("time_under_tension")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", time_under_tensionDataVideo!!)
        } else if (dataListRateKacl[position].key.equals("total_time")) {
            intent.putParcelableArrayListExtra("heart_rateDataVideo", total_timeDataVideo!!)
        }
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.push_up_in,
            R.anim.push_up_out
        )
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this,
                HomeViewModelFactory(HomeTabFragmentRepository(retrofitService), requireActivity())
            ).get(
                HomeViewModel::class.java
            )
        getHomeApiResponse()
        getUserAnalyticsResponse()
        getAchievementsListResponse()
        getAllResposnse()
        getUserAnalycticsDetiles()
    }

    private fun getCurrentYearAndMonth() {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH] + 1
        calendarApiCall(year.toString(), month.toString())
    }

    private fun calendarApiCall(year: String, month: String) {
        if (!binding.spLoading.isVisible) {
            binding.spLoading.visibility = View.VISIBLE
        }
        viewModel.checkgetCalanderTrack(
            BaseResponseDataObject.accessToken,
            GetCalenderRequest("getcalanderTrack", month, year)
        )
    }

    private fun getAllResposnse() {
        viewModel.getProgramByIdResponse.observe(viewLifecycleOwner, Observer {

            if (it.serverResponse.statusCode == 200) {
                getProgramByIdResponse = it.result!!.data
                BaseResponseDataObject.getProgramByIdResponse = getProgramByIdResponse

                binding.spLoading.visibility = View.GONE
                val intent = Intent(requireActivity(), ProgramsCoverActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left
                )

            } else {
                binding.spLoading.visibility = View.GONE

            }

        })

        viewModel.classDetailsResponse.observe(viewLifecycleOwner, Observer {
            Log.d("getData255", "the data size is ${it.result.data.isSave}")
            getClassDetailsList = it.result!!.data
            BaseResponseDataObject.getClassDetailsResponse = getClassDetailsList
            binding.spLoading.visibility = View.GONE
            val intent = Intent(requireActivity(), ClassesCoverActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            requireActivity().overridePendingTransition(
                R.anim.slide_from_right,
                R.anim.slide_to_left
            )
        })

    }

    private fun getHomeApiResponse() {
        viewModel.getHomeData.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            binding.tvVideoHeader.text = it.gettingstarted.headerTitle
            binding.tvProgramHeader.text = "PROGRAMS MADE FOR YOU"
//            it.programsmadeforyou.headerTitle
            dataListVideo.clear()
            dataListVideo.addAll(it.gettingstarted.videos)
            videoGetStartedAdapter?.notifyDataSetChanged()
            dataListProgram.clear()
            dataListProgram.addAll(it.programsmadeforyou.videos)
            programMadeForYouAdapter?.notifyDataSetChanged()
            dataListWorkOut.clear()
            if (!it.videos.isNullOrEmpty()) {
                dataListWorkOut.addAll(it.videos)
                workoutGoalHeaderAdapter?.notifyDataSetChanged()
            }

        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getCalanderResponse.observe(viewLifecycleOwner, Observer {
            binding.spLoading.visibility = View.GONE
            binding.calendarBottomView.tvActiveNumber.text = it.result.dailyStreaks.toString()
            binding.calendarBottomView.tvClassNumber.text = it.result.monthlyCount.toString()
            addCalederNewData(it.result.data)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

    private fun getUserAnalyticsResponse() {
        viewModel.getUserAnalyticsData.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            dataListRateKacl.clear()
            dataListRateKacl.addAll(it)
            totalPosition = dataListRateKacl.size
            getUserAnalycticsDetilesCheckNew()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

    }

    private fun getUserAnalycticsDetilesCheckNew() {
        val tz: TimeZone = TimeZone.getDefault()
        currentKey = ""
        if(currentPosition<totalPosition) {
            binding.spLoadingNew.visibility = View.VISIBLE
            currentKey = dataListRateKacl[currentPosition].key
            viewModel.getUserAnalycticsDetilesCheck(
                token, GetUserAnalycticsDetilesRequest(
                    "getUserAnalycticsDetiles",
                    "",
                    dataListRateKacl[currentPosition].key,
                    dataListRateKacl[currentPosition].subKey,
                    tz.getID(),
                )
            )
        }

    }

    private fun getUserAnalycticsDetiles() {
        viewModel.getUserAnalycticsDetilesData.observe(requireActivity(), Observer {
            if (currentKey.equals("heart_rate")) {
                heart_rateData.clear()
                heart_rateDataVideo.clear()
                heart_rateData.addAll(it.grapData)
                heart_rateDataVideo.addAll(it.videos)
            } else if (currentKey.equals("calories")) {
                caloriesData.clear()
                caloriesDataVideo.clear()
                caloriesData.addAll(it.grapData)
                caloriesDataVideo.addAll(it.videos)
            } else if (currentKey.equals("distance")) {
                distanceData.clear()
                distanceDataVideo.clear()
                distanceData.addAll(it.grapData)
                distanceDataVideo.addAll(it.videos)
            } else if (currentKey.equals("total_force")) {
                total_forceData.clear()
                total_forceDataVideo.clear()
                total_forceData.addAll(it.grapData)
                total_forceDataVideo.addAll(it.videos)
            } else if (currentKey.equals("total_reps")) {
                total_repsData.clear()
                total_repsDataVideo.clear()
                total_repsData.addAll(it.grapData)
                total_repsDataVideo.addAll(it.videos)
            } else if (currentKey.equals("time_under_tension")) {
                time_under_tensionData.clear()
                time_under_tensionDataVideo.clear()
                time_under_tensionData.addAll(it.grapData)
                time_under_tensionDataVideo.addAll(it.videos)
            } else if (currentKey.equals("total_time")) {
                total_timeData.clear()
                total_timeDataVideo.clear()
                total_timeData.addAll(it.grapData)
                total_timeDataVideo.addAll(it.videos)
            }
            currentPosition += 1
            if (currentPosition < totalPosition) {
                getUserAnalycticsDetilesCheckNew()
            } else {
                Log.d("ghghfghfhgfhf", heart_rateData.toString())
                DataCheckingAndChange()
            }
        })

    }

    private fun DataCheckingAndChange() {
        if (heart_rateData.size > 0) {
            heart_rateData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until heart_rateData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == heart_rateData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + heart_rateData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == heart_rateData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        heart_rateData[i].date,
                                        heart_rateData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            heart_rateData[i].date,
                            heart_rateData[i].totalData
                        )
                    )
                }
                heart_rateDataAsc.clear()
                heart_rateDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        if (caloriesData.size > 0) {
            caloriesData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until caloriesData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == caloriesData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + caloriesData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == caloriesData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        caloriesData[i].date,
                                        caloriesData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            caloriesData[i].date,
                            caloriesData[i].totalData
                        )
                    )
                }
                caloriesDataAsc.clear()
                caloriesDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        if (distanceData.size > 0) {
            distanceData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until distanceData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == distanceData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + distanceData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == distanceData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        distanceData[i].date,
                                        distanceData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            distanceData[i].date,
                            distanceData[i].totalData
                        )
                    )
                }
                distanceDataAsc.clear()
                distanceDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        if (total_forceData.size > 0) {
            total_forceData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until total_forceData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == total_forceData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + total_forceData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == total_forceData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        total_forceData[i].date,
                                        total_forceData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            total_forceData[i].date,
                            total_forceData[i].totalData
                        )
                    )
                }
                total_forceDataAsc.clear()
                total_forceDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        if (total_repsData.size > 0) {
            total_repsData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until total_repsData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == total_repsData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + total_repsData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == total_repsData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        total_repsData[i].date,
                                        total_repsData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            total_repsData[i].date,
                            total_repsData[i].totalData
                        )
                    )
                }
                total_repsDataAsc.clear()
                total_repsDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        if (time_under_tensionData.size > 0) {
            time_under_tensionData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until time_under_tensionData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == time_under_tensionData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + time_under_tensionData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == time_under_tensionData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        time_under_tensionData[i].date,
                                        time_under_tensionData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            time_under_tensionData[i].date,
                            time_under_tensionData[i].totalData
                        )
                    )
                }
                time_under_tensionDataAsc.clear()
                time_under_tensionDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        if (total_timeData.size > 0) {
            total_timeData.forEach {
                it.date = it.date.split("T")[0]
            }
            var difficultyset: ArrayList<GrapDataShort> = ArrayList<GrapDataShort>()
            difficultyset.clear()
            for (i in 0 until total_timeData.size) {
                if (difficultyset.size > 0) {
                    for (j in 0 until difficultyset.size) {
                        if (difficultyset[j].date == total_timeData[i].date) {
                            difficultyset[j].totalData =
                                difficultyset[j].totalData + total_timeData[i].totalData
                        } else {
                            var verify = false
                            for (k in 0 until difficultyset.size) {
                                if (difficultyset[k].date == total_timeData[i].date) {
                                    verify = true
                                }
                            }
                            if (!verify) {
                                difficultyset.add(
                                    GrapDataShort(
                                        total_timeData[i].date,
                                        total_timeData[i].totalData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    difficultyset.add(
                        GrapDataShort(
                            total_timeData[i].date,
                            total_timeData[i].totalData
                        )
                    )
                }
                total_timeDataAsc.clear()
                total_timeDataAsc.addAll(difficultyset.sortedBy { it.date.toDate() })
            }
        }
        setDatatoHomepagechart()
    }

    private fun setDatatoHomepagechart() {
        var selectedItem: String = ""
        for (i in 0 until dataListAllTime.size) {
            if (dataListAllTime[i].selected) {
                selectedItem = dataListAllTime[i].title.toString()
            }
        }
        when (selectedItem) {
            "All time" -> {
                dataListRateKacl.forEach {
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
                dataListRateKacl.forEach {
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
            "3 Months" -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val sevenDayAgoDateFormat = getDaysAgo(90)

                dataListRateKacl.forEach {
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
                dataListRateKacl.forEach {
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
        rateKaclAdapter!!.notifyDataSetChanged()
        binding.spLoadingNew.visibility = View.GONE
    }

    private fun getAchievementsListResponse() {
        viewModel.getAchievementsClassData.observe(requireActivity(), Observer {
            getAchievementsClassData.clear()
            getAchievementsClassData.addAll(it.data.classMilestone)
            Log.d("getAchievementsClassDataa1",getAchievementsClassData.toString())
            getAchievementsdayStreakListResponse()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

    private fun getAchievementsdayStreakListResponse() {
        viewModel.getAchievementsdayStreakData.observe(requireActivity(), Observer {
            getAchievementsDayStreakData.clear()
            getAchievementsDayStreakData.addAll(it.data.dayStreak)
            Log.d("getAchievementsClassDataa2",getAchievementsDayStreakData.toString())
            getAchievementsweeklyStreakListResponse()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

    private fun getAchievementsweeklyStreakListResponse() {
        viewModel.getAchievementsweeklyData.observe(requireActivity(), Observer {
            getAchievementsweeklyStreakData.clear()
            getAchievementsweeklyStreakData.addAll(it.data.weeklyStreak)
            Log.d("getAchievementsClassDataa3",getAchievementsweeklyStreakData.toString())
            getAchievementscaloriesachievementsListResponse()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

    private fun getAchievementscaloriesachievementsListResponse() {
        viewModel.getCaloriesData.observe(requireActivity(), Observer {
            getAchievementscaloriesachievementsData.clear()
            getAchievementscaloriesachievementsData.addAll(it.data.caloriesachievements)
            Log.d("getAchievementsClassDataa4",getAchievementscaloriesachievementsData.toString())
            getAchievementsLbsListResponse()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

    private fun getAchievementsLbsListResponse() {
        viewModel.getLbsData.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            getAchievementsLbsData.clear()
            getAchievementsLbsData.addAll(it.data.lbsachievements)
            Log.d("getAchievementsClassDataa5",getAchievementsLbsData.toString())
            finalAchievemntsResult()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })


    }

    private fun finalAchievemntsResult() {
        binding.spLoading.visibility = View.VISIBLE
        var trueSize = 0
        for (i in 0 until getAchievementsClassData.size) {
            if (getAchievementsClassData[i].isComplete) {
                trueSize += 1
            }
        }
        if (trueSize > 0) {
            var idDefult = getAchievementsClassData[trueSize - 1].number
            if (idDefult.toInt() == 1) {
                dataListAchievement[1] = AchivementsViewModel(idDefult, "Class", true)
            } else {
                if (idDefult.toInt() < 10) {
                    idDefult = "0$idDefult"
                }
                if (dataListAchievement.size > 0) {
                    dataListAchievement[1] = AchivementsViewModel(idDefult, "Classes", true)
                }
            }
        } else {
            dataListAchievement[1] = AchivementsViewModel("0", "Class", false)
        }

        trueSize = 0
        for (i in 0 until getAchievementsDayStreakData.size) {
            if (getAchievementsDayStreakData[i].isComplete) {
                trueSize += 1
            }
        }
        if (trueSize > 0) {
            var idDefult = getAchievementsDayStreakData[trueSize - 1].number
            if (idDefult.toInt() == 1) {
                dataListAchievement[2] = AchivementsViewModel("$idDefult Day", "Streak", true)
            } else {
                if (idDefult.toInt() < 10) {
                    idDefult = "0$idDefult"
                }
                if (dataListAchievement.size > 0) {
                    dataListAchievement.add(
                        2,
                        AchivementsViewModel("$idDefult Day", "Streak", true)
                    )

                }
            }
        } else {
            dataListAchievement[2] = AchivementsViewModel("0 Day", "Streak", false)
        }

        trueSize = 0
        for (i in 0 until getAchievementsweeklyStreakData.size) {
            if (getAchievementsweeklyStreakData[i].isComplete) {
                trueSize += 1
            }
        }
        if (trueSize > 0) {
            var idDefult = getAchievementsweeklyStreakData[trueSize - 1].number
            if (idDefult.toInt() == 1) {
                dataListAchievement[3] = AchivementsViewModel("$idDefult Week", "Streak", true)
            } else {
                if (idDefult.toInt() < 10) {
                    idDefult = "0$idDefult"
                }
                if(dataListAchievement.size>0)
                    dataListAchievement[3] = AchivementsViewModel("$idDefult Week", "Streak", true)
            }
        } else {
            dataListAchievement[3] = AchivementsViewModel("0 Week", "streak", false)
        }


        trueSize = 0
        for (i in 0 until getAchievementscaloriesachievementsData.size) {
            if (getAchievementscaloriesachievementsData[i].isComplete) {
                trueSize += 1
            }
        }
        if (trueSize > 0) {
            var idDefult = getAchievementscaloriesachievementsData[trueSize - 1].number
            if (idDefult.toInt() == 1) {
                dataListAchievement[4] = AchivementsViewModel("$idDefult", "KCAL", true)
            } else {
                if (idDefult.toInt() < 10) {
                    idDefult = "0$idDefult"
                }
                if(dataListAchievement.size>0)
                dataListAchievement[4] = AchivementsViewModel("$idDefult", "KCAL", true)
            }
        } else {
            dataListAchievement[4] = AchivementsViewModel("0", "KCAL", false)
        }

        trueSize = 0
        for (i in 0 until getAchievementsLbsData.size) {
            if (getAchievementsLbsData[i].isComplete) {
                trueSize += 1
            }
        }
        if (trueSize > 0) {
            var idDefult = getAchievementsLbsData[trueSize - 1].number
            if (idDefult.toInt() == 1) {
                dataListAchievement[5] = AchivementsViewModel("$idDefult", "LBS LIFTED", true)
            } else {
                if (idDefult.toInt() < 10) {
                    idDefult = "0$idDefult"
                }
                if(dataListAchievement.size>0)
                    dataListAchievement[5] = AchivementsViewModel("$idDefult", "LBS LIFTED", true)
            }
        } else {
            dataListAchievement[5] = AchivementsViewModel("0", "LBS LIFTED", false)
        }
        achievementsAdapter?.notifyDataSetChanged()
        binding.spLoading.visibility = View.GONE
    }

    private fun getTheAccessToken() {
        dataPreferenceObject.getTheData("userToken").asLiveData().observe(viewLifecycleOwner) {
            token = it.toString()
            setUpAdapter()
        }
        context?.let {
            Glide
                .with(it)
                .load(BaseResponseDataObject.profilePageData.profileImage)
                .centerCrop()
                .into(binding.ivUserImage)
        }
        binding.tvHeader1.text =
            "Welcome ${BaseResponseDataObject.profilePageData.firstName} ${BaseResponseDataObject.profilePageData.lastName}"
    }

    private fun setCalender() {
        var selectedDate: LocalDate? = null
        val daysOfWeek = daysOfWeekFromLocale()
        val today = LocalDate.now()
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        val CalendarItems =
            cleanedUpcalendarItemsOnlyDateAndDifficulty.groupBy { it.date.toLocalDate() }

        binding.calenderFullView.legendLayout.root.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    .toUpperCase(Locale.ENGLISH)
                setTextColorRes(R.color.white)
            }
        }

        if (CalenderFirstTimeSet) {
            CalenderFirstTimeSet = false
            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(20)
            val endMonth = currentMonth.plusMonths(20)
            binding.calenderFullView.exOneCalendar.setup(startMonth, endMonth, daysOfWeek.first())
            binding.calenderFullView.exOneCalendar.scrollToMonth(currentMonth)
        }
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayBinding.bind(view).exOneDayText
            val flDaySelected = CalendarDayBinding.bind(view).flDaySelected
            val exFiveDayLayout = CalendarDayBinding.bind(view).exFiveDayLayout
            val firstLine = CalendarDayBinding.bind(view).firstLine
            val secondLine = CalendarDayBinding.bind(view).secondLine
            val thirdLine = CalendarDayBinding.bind(view).thirdLine

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            binding.calenderFullView.exOneCalendar.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.calenderFullView.exOneCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.calenderFullView.exOneCalendar.notifyDateChanged(oldDate) }
                        }
                    }
                }
            }
        }

        binding.calenderFullView.exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val layout = container.exFiveDayLayout
                val flDaySelected = container.flDaySelected
                textView.text = day.date.dayOfMonth.toString()

                container.firstLine.visibility = View.GONE
                container.secondLine.visibility = View.GONE
                container.thirdLine.visibility = View.GONE
                if (day.owner == DayOwner.THIS_MONTH) {
                    when(day.date) {
                        selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            flDaySelected.background = null
                        }
                        today -> {
                            textView.setTextColorRes(R.color.white)
                            flDaySelected.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.white)
                            flDaySelected.background = null
                        }
                    }
                    val calendarItem = CalendarItems[day.date]
                    if (calendarItem != null) {
                        for (item in calendarItem.indices) {
                            calendarItem[item].difficulty.forEach {
                                when (it) {
                                    0 -> {
                                        container.firstLine.visibility = View.VISIBLE
                                    }
                                    1 -> {
                                        container.secondLine.visibility = View.VISIBLE
                                    }
                                    2 -> {
                                        container.thirdLine.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.mono_grey_60)
                    flDaySelected.background = null
                    container.firstLine.visibility = View.GONE
                    container.secondLine.visibility = View.GONE
                    container.thirdLine.visibility = View.GONE
                }
            }
        }

        binding.calenderFullView.exOneCalendar.monthScrollListener = {
            if (binding.calenderFullView.exOneCalendar.maxRowCount == 6) {
                binding.calenderFullView.exOneYearText.text = it.yearMonth.year.toString()
                binding.calenderFullView.exOneMonthText.text =
                    monthTitleFormatter.format(it.yearMonth)
                calendarApiCallOnScroll()
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.calenderFullView.exOneYearText.text =
                        firstDate.yearMonth.year.toString()
                    binding.calenderFullView.exOneMonthText.text =
                        monthTitleFormatter.format(firstDate)
                } else {
                    binding.calenderFullView.exOneMonthText.text =
                        "${monthTitleFormatter.format(firstDate)} - ${
                            monthTitleFormatter.format(
                                lastDate
                            )
                        }"
                    if (firstDate.year == lastDate.year) {
                        binding.calenderFullView.exOneYearText.text =
                            firstDate.yearMonth.year.toString()
                    } else {
                        binding.calenderFullView.exOneYearText.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }
        yearSpinner()
        monthSpinner()
    }

    private fun yearSpinner() {
        val year = arrayOf("Select a year", "2021", "2022")
        if (binding.calenderFullView.spinnerYr != null) {
            val adapter = ArrayAdapter(
                requireActivity(),
                R.layout.spinner_text, year
            )
            binding.calenderFullView.spinnerYr.adapter = adapter

            binding.calenderFullView.spinnerYr.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (year[position].toString().equals("Select a year")) {
                    } else {
                        binding.calenderFullView.exOneYearText.text = year[position].toString()
                        val monthNumber =
                            getMonthNumber(binding.calenderFullView.exOneMonthText.text.toString())
                        thisYearMonth = YearMonth.of(
                            year[position].toInt(), monthNumber
                        )
                        CalenderAlreadySet = false
                        spinnerWorking = true
                        calendarApiCall(year[position].toInt().toString(), monthNumber.toString())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        binding.calenderFullView.exOneYearText.setOnClickListener {
            binding.calenderFullView.spinnerYr.performClick()
        }
    }

    private fun monthSpinner() {
        val months = ArrayList<String>()
        months.clear()
        months.add("Select a month")
        for (i in 0..11) {
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("MMMM")
            cal[Calendar.MONTH] = i
            val month_name: String = month_date.format(cal.time)
            months.add(month_name)
        }

        if (binding.calenderFullView.spinnerMonth != null) {
            val adapter = ArrayAdapter(
                requireActivity(),
                R.layout.spinner_text, months
            )
            binding.calenderFullView.spinnerMonth.adapter = adapter

            binding.calenderFullView.spinnerMonth.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (months[position].toString().equals("Select a month")) {
                    } else {
                        binding.calenderFullView.exOneMonthText.text = months[position].toString()
                        val monthNumber =
                            getMonthNumber(binding.calenderFullView.exOneMonthText.text.toString())
                        thisYearMonth = YearMonth.of(
                            binding.calenderFullView.exOneYearText.text.toString().toInt(),
                            monthNumber
                        )
                        CalenderAlreadySet = false
                        spinnerWorking = true
                        calendarApiCall(
                            binding.calenderFullView.exOneYearText.text.toString(),
                            monthNumber.toString()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        binding.calenderFullView.exOneMonthText.setOnClickListener {
            binding.calenderFullView.spinnerMonth.performClick()
        }
    }

    private fun getMonthNumber(monthName: String): Int {
        return Month.valueOf(monthName.uppercase(Locale.getDefault())).getValue()
    }

    private fun addCalederNewData(data: ArrayList<CalendarDataModel>) {
        var dataListWithOutBlank = data.filter { it.completeTime != "" }
        if (dataListWithOutBlank.isNotEmpty()) {
            val calendarItemsOnlyDateAndDifficulty: ArrayList<CalendarEvent> =
                ArrayList<CalendarEvent>()
            calendarItemsOnlyDateAndDifficulty.clear()
            dataListWithOutBlank.filter { it.completeTime != "" }.forEach {
                val oldFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
                var value: Date? = null
                var dueDateAsNormal = ""
                var difficultyLevel = 0
                var difficulty: ArrayList<Int> = ArrayList<Int>()
                try {
                    value = oldFormatter.parse(it.completeTime)
                    val newFormatter = SimpleDateFormat("yyyy-MM-dd")
                    newFormatter.timeZone = TimeZone.getDefault()
                    dueDateAsNormal = newFormatter.format(value)

                    if (it.getLevelName.lowercase().equals(AppConstants.beginner)) {
                        difficultyLevel = AppConstants.beginner_value
                    } else if (it.getLevelName.lowercase().equals(AppConstants.intermediate)) {
                        difficultyLevel = AppConstants.intermediate_value
                    } else if (it.getLevelName.lowercase().equals(AppConstants.advanced)) {
                        difficultyLevel = AppConstants.advanced_value
                    }
                    difficulty.clear()
                    difficulty.add(difficultyLevel)
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val date = format.parse(dueDateAsNormal)
                    calendarItemsOnlyDateAndDifficulty.add(
                        CalendarEvent(
                            convertToLocalDateTimeViaInstant(date),
                            difficulty
                        )
                    )
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            calendarItemsOnlyDateAndDifficulty.sortBy { it.date }
            cleanedUpcalendarItemsOnlyDateAndDifficulty.clear()
            var hashSetDate = HashSet<LocalDateTime>()
            hashSetDate.clear()
            calendarItemsOnlyDateAndDifficulty.forEach {
                hashSetDate.add(it.date)
            }
            var difficultyset = TreeSet<Int>()
            hashSetDate.forEach { ik ->
                calendarItemsOnlyDateAndDifficulty.forEach {
                    if (ik == it.date) {
                        difficultyset.add(it.difficulty[0])
                    }
                }
                var difficultyInt: ArrayList<Int> = ArrayList<Int>()
                difficultyInt.clear()
                difficultyInt.addAll(difficultyset)
                cleanedUpcalendarItemsOnlyDateAndDifficulty.add(CalendarEvent(ik, difficultyInt))
                difficultyset.clear()
            }
            Log.d("datacalender", cleanedUpcalendarItemsOnlyDateAndDifficulty.toString())
            setCalender()
        } else {
            setCalender()
        }
        if (spinnerWorking) {
            spinnerWorking = false
            binding.calenderFullView.exOneCalendar.smoothScrollToMonth(thisYearMonth)
            Handler(Looper.getMainLooper()).postDelayed({
                CalenderAlreadySet = true
            }, 1000)
        }
    }

    fun convertToLocalDateTimeViaInstant(dateToConvert: Date): LocalDateTime {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    private fun calendarApiCallOnScroll() {
        if (CalenderAlreadySet) {
            CalenderAlreadySet = false
            calendarApiCall(
                binding.calenderFullView.exOneYearText.text.toString(),
                getMonthNumber(binding.calenderFullView.exOneMonthText.text.toString()).toString()
            )
        }
        Handler(Looper.getMainLooper()).postDelayed({
            CalenderAlreadySet = true
        }, 1000)
    }

    override fun onItemClick(position: Int, data: String) {
        val item = dataListProgram[position]
        BaseResponseDataObject.getProgramsByIdToNextScreen =
            GetProgramsByIdToNextScreen(item.description, item.week)
        viewModel.checkgetProgramsById(
            BaseResponseDataObject.accessToken,
            GetProgramByIdRequest("getProgramById", data)
        )
    }

    override fun onItemWorkOutClick(position: Int, attribCode: String) {
        viewModel.checkgetClassDetails(
            BaseResponseDataObject.accessToken,
            ClassDetailsRequest("classDetails", attribCode)
        )
    }

    override fun onVideoFileItemClick(position: Int, videoUrl: String) {
        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java)
        intent.putExtra("videoUrl", videoUrl)
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            R.anim.slide_from_right,
            R.anim.slide_to_left
        )
    }
}