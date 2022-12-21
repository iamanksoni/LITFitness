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
import com.litmethod.android.ui.VideoPlayer.VideoPlayerActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.GetProgramsByIdToNextScreen
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ClassesCoverActivity
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ProgramsCoverActivity
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModel
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModelFactory
import com.litmethod.android.ui.root.HomeTabScreen.PerformanceDetailsScreen.PerformanceDetailsActivity
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DataPreferenceObject
import com.litmethod.android.utlis.PeekingLinearLayoutManager
import com.litmethod.android.utlis.SpacesItemDecoration
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
    private lateinit var videoGetStartedAdapter: VideoGetStartedAdapter

    val dataListProgram: ArrayList<VideoX> = ArrayList<VideoX>()
    private var layoutManagernewProgram: RecyclerView.LayoutManager? = null
    private lateinit var programMadeForYouAdapter: ProgramMadeForYouAdapter

    val dataListWorkOut: ArrayList<VideoXX> = ArrayList<VideoXX>()
    private var layoutManagerWorkOut: RecyclerView.LayoutManager? = null
    private var workoutGoalHeaderAdapter: WorkoutGoalHeaderAdapter? = null

    val dataListAllTime: ArrayList<HomePageVideosModel> = ArrayList<HomePageVideosModel>()
    private var layoutManagerAllTime: RecyclerView.LayoutManager? = null
    private var allTimeAdapter: AllTimeAdapter? = null

    val dataListRateKacl: ArrayList<ResultUserAnalytics> = ArrayList<ResultUserAnalytics>()
    private var layoutManagerRateKacl: RecyclerView.LayoutManager? = null
    private lateinit var rateKaclAdapter: RateKaclAdapter

    val dataListAchievement: ArrayList<AchivementsViewModel> = ArrayList<AchivementsViewModel>()
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
        rateKaclAdapter = RateKaclAdapter(dataListRateKacl, requireContext())
        videoGetStartedAdapter = VideoGetStartedAdapter(dataListVideo, requireContext())
        programMadeForYouAdapter = ProgramMadeForYouAdapter(dataListProgram, requireContext())
        workoutGoalHeaderAdapter = WorkoutGoalHeaderAdapter(requireContext(), dataListWorkOut)

    }

    private fun setUpAdapter() {
        binding.rvVideoGetStarted.apply {
            layoutManagernewVideo =
                PeekingLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagernewVideo
            videoGetStartedAdapter = VideoGetStartedAdapter(dataListVideo, requireContext())
            this.adapter = videoGetStartedAdapter
            videoGetStartedAdapter!!.setAdapterListener(this@HomeScreenFragment)
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
        dataListAllTime.add(HomePageVideosModel("Past week", true))
        dataListAllTime.add(HomePageVideosModel("3 months", false))
        dataListAllTime.add(HomePageVideosModel("6 months", false))
        dataListAllTime.add(HomePageVideosModel("All time", false))
        binding.rvAllTime.apply {
            layoutManagerAllTime =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerAllTime
            allTimeAdapter = AllTimeAdapter(dataListAllTime, requireContext(), binding.rvAllTime)
            this.adapter = allTimeAdapter

        }
        allTimeAdapter!!.setAdapterListener(this)

        rateKaclAdapter = RateKaclAdapter(dataListRateKacl, requireContext())

        binding.rvRateKacl.apply {
            layoutManagerRateKacl = GridLayoutManager(requireActivity(), 2)
            this.layoutManager = layoutManagerRateKacl
            this.adapter = rateKaclAdapter
            addItemDecoration(SpacesItemDecoration(19))
        }
        rateKaclAdapter!!.setAdapterListener(this)

        dataListAchievement.clear()
        dataListAchievement.add(AchivementsViewModel("Pro Lit", "Member", false))
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
        viewModel.getUserAnalyticsCheck(token, AppConstants.PASTWEEK)
        viewModel.getAchievementsCheck(token)
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
                if (dataListAllTime[i].title.equals("Past week")) {
                    viewModel.getUserAnalyticsCheck(token, AppConstants.PASTWEEK)
                } else if (dataListAllTime[i].title.equals("3 months")) {
                    viewModel.getUserAnalyticsCheck(token, AppConstants.THREEMONTH)
                } else if (dataListAllTime[i].title.equals("6 months")) {
                    viewModel.getUserAnalyticsCheck(token, AppConstants.SIXMONTH)
                } else if (dataListAllTime[i].title.equals("All time")) {
                    viewModel.getUserAnalyticsCheck(token, AppConstants.ALLTIME)
                }
            }
        }

    }

    override fun onItemClickAchievements(position: Int) {
    }

    override fun onItemClickRateKacl(position: Int) {
        val intent = Intent(requireActivity(), PerformanceDetailsActivity::class.java)
        intent.putParcelableArrayListExtra("dataListRateKacl", dataListRateKacl)
        intent.putParcelableArrayListExtra("dataListAllTime", dataListAllTime)
        intent.putExtra("position", position)
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

        try {
            getHomeApiResponse()
            getUserAnalyticsResponse()
            getAchievementsListResponse()
            getAllResposnse()
        } catch (e: Exception) {

        }
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
            rateKaclAdapter?.notifyDataSetChanged()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

    }

    private fun getAchievementsListResponse() {
        viewModel.getAchievementsClassData.observe(requireActivity(), Observer {
            getAchievementsClassData.clear()
            getAchievementsClassData.addAll(it.data.classMilestone)
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
                if (dataListAchievement.size > 1) {
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
                if (dataListAchievement.size > 2) {
                    dataListAchievement[2] = AchivementsViewModel("$idDefult Day", "Streak", true)
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
                if(dataListAchievement.size>3){
                dataListAchievement[3] = AchivementsViewModel("$idDefult Week", "Streak", true)
            }
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
                if(dataListAchievement.size>4){
                dataListAchievement[4] = AchivementsViewModel("$idDefult", "KCAL", true)
            }
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
                if(dataListAchievement.size>5){
                dataListAchievement[5] = AchivementsViewModel("$idDefult", "LBS LIFTED", true)
            }
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
        val selectedDates = mutableSetOf<LocalDate>()
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
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        binding.calenderFullView.exOneCalendar.notifyDayChanged(day)
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
                    when {
                        selectedDates.contains(day.date) -> {
                            textView.setTextColorRes(R.color.white)
                            flDaySelected.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        today == day.date -> {
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