package com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen
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
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.litmethod.android.R
import com.litmethod.android.databinding.CalendarDayBinding
import com.litmethod.android.databinding.FragmentAccountBinding
import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.BookmarkClassRequest
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserLevel.EditUserLevelRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.ClassMilestone
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.GetAchieveMentRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.CalendarDataModel
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalenderRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesRequest
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.GetClassStatisticsRequest
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.VideoType
import com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsRequest
import com.litmethod.android.models.CalendarEvent
import com.litmethod.android.models.GetCustomers.Goal
import com.litmethod.android.models.GetCustomers.Interest
import com.litmethod.android.models.calendarItemsModels
import com.litmethod.android.network.AcountScreenFragmentRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseFragment
import com.litmethod.android.ui.Onboarding.LevelScreen.LevelAdapter
import com.litmethod.android.ui.Onboarding.LevelScreen.LevelData
import com.litmethod.android.ui.Onboarding.MeasureScreen.Util.UntiConvert
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentActivity
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentAdapter
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentData
import com.litmethod.android.ui.Onboarding.YourGoalsScreen.GoalsData
import com.litmethod.android.ui.Onboarding.YourGoalsScreen.YourGoalsActivity
import com.litmethod.android.ui.Onboarding.YourInterestScreen.YourInterestActivity
import com.litmethod.android.ui.Onboarding.YourInterestScreen.YourInterestAdapter
import com.litmethod.android.ui.Onboarding.YourInterestScreen.YourInterestData
import com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.Adapter.GoalsAdapterAccountFragment
import com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.Adapter.YourInterestAdaperAccountFragment
import com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.ViewModel.AccountScreenViewModel
import com.litmethod.android.ui.root.AccountTabScreen.AccountFragmentScreen.ViewModel.AccountScrrenViewModelFactory
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.EditProfile.EditProfileFragment
import com.litmethod.android.ui.root.HomeTabScreen.HomeTabFragmentScreen.HomePageVideosModel
import com.litmethod.android.ui.root.WorkOut.WorkoutActivity
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_BOOK_MARK_CLASS
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_ACHIEVEMENTS_CLASS
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_ACHIEVEMENTS_DAYILY_STREAK
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_ACHIEVEMENTS_WEEKLY_STREAK
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_CALENDAR_TRACK
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_CALORIES
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_CLASS_STATISTICS
import com.litmethod.android.utlis.AppConstants.Companion.ACTION_GET_WEIGHT
import com.litmethod.android.utlis.MarginItemDecoration
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


class AccountScreenFragment : BaseFragment(), AllTabItemAdapter.AllTabItemAdapterListener,
    LevelAdapter.LevelAdapterListener, YourInterestAdapter.LevelAdapterListener,
    YourEquipmentAdapter.YourEquipmentAdapterListener,WorkoutHistoryAdapter.WorkoutHistoryAdapterListener,SaveClassAdapter.SaveClassAdapterListener {
    lateinit var binding: FragmentAccountBinding
    private val dataListAllTabItem: ArrayList<HomePageVideosModel> =
        ArrayList<HomePageVideosModel>()
    private var layoutManagerAllTabItem: RecyclerView.LayoutManager? = null
    private var allTabItemAdapter: AllTabItemAdapter? = null
    lateinit var viewModel: AccountScreenViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    val dataList: ArrayList<GoalsData> = ArrayList<GoalsData>()
    private var layoutManagerGoals: RecyclerView.LayoutManager? = null
    private var goalsAdapter: GoalsAdapterAccountFragment? = null
    var getGoalList: ArrayList<Goal> = ArrayList<Goal>()
    val convert=  UntiConvert()

    val dataListClassMilestone: ArrayList<AllClassMilestoneReponse> =
        ArrayList<AllClassMilestoneReponse>()
    private var layoutManagerClassMilestone: RecyclerView.LayoutManager? = null
//    private var allClassMilestoneHeaderAdapter: AllClassMilestoneHeaderAdapter? = null

    private var allClassMilestoneChildAdapter: AllClassMilestoneChildAdapter? = null
    private var allClassMilestoneChildAdapter2: AllClassMilestoneChildAdapter? = null
    private var allClassMilestoneChildAdapter3: AllClassMilestoneChildAdapter? = null
    private var allClassMilestoneChildAdapter4: AllClassMilestoneChildAdapter? = null
    private var allClassMilestoneChildAdapter5: AllClassMilestoneChildAdapter? = null

    private val dataListlevel: ArrayList<LevelData> = ArrayList<LevelData>()
    private var levelAdapter: LevelAdapter? = null
    var levelList: ArrayList<com.litmethod.android.models.GetLevel.Data> =
        ArrayList<com.litmethod.android.models.GetLevel.Data>()
    var levelList2: List<com.litmethod.android.models.GetLevel.Data> =
        ArrayList<com.litmethod.android.models.GetLevel.Data>()
    private val dataListInterest: ArrayList<YourInterestData> = ArrayList<YourInterestData>()
    private var yourInterestAdapter: YourInterestAdaperAccountFragment? = null
    var interestList: ArrayList<Interest> =
        ArrayList<Interest>()
    private val dataListEquipment: ArrayList<YourEquipmentData> = ArrayList<YourEquipmentData>()
    private var yourEquipmentAdapter: YourEquipmentAdapter? = null
    private var equipmentList: ArrayList<com.litmethod.android.models.GetEquipment.Data> =
        ArrayList<com.litmethod.android.models.GetEquipment.Data>()

    private var workoutHistoryAdapter: WorkoutHistoryAdapter? = null
    private var layoutManagerWorkoutHistory: RecyclerView.LayoutManager? = null

    private var saveClassAdapter: SaveClassAdapter? = null
    private var layoutManagerSaveClass: RecyclerView.LayoutManager? = null

    private var getBookmarkClassRespomseList: MutableList<com.litmethod.android.models.AcountScreenFragment.BookmarkClass.Data> =
        ArrayList<com.litmethod.android.models.AcountScreenFragment.BookmarkClass.Data>()
    var getClassStatisticsList: MutableList<VideoType> = ArrayList<VideoType>()
    var getClassAchievementList: MutableList<ClassMilestone> = ArrayList<ClassMilestone>()
    private var getAchievementsDayStreakList: MutableList<ClassMilestone> =
        ArrayList<ClassMilestone>()
    private var getAchievementsWeeklyStreakList: MutableList<ClassMilestone> =
        ArrayList<ClassMilestone>()
    var getCaloriesList: MutableList<ClassMilestone> = ArrayList<ClassMilestone>()
    var getLbsList: MutableList<ClassMilestone> = ArrayList<ClassMilestone>()
    private val goalLevelForEditUser: ArrayList<String> = ArrayList<String>()

    val calendarItems: java.util.ArrayList<calendarItemsModels> =
        java.util.ArrayList<calendarItemsModels>()
    private val cleanedUpCalendarItemsOnlyDateAndDifficulty: ArrayList<CalendarEvent> =
        ArrayList<CalendarEvent>()
    private var spinnerWorking: Boolean = false
    private lateinit var thisYearMonth: YearMonth
    private var calenderAlreadySet: Boolean = false
    private var calenderFirstTimeSet: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        viewModelSetup()
        setClickListner()
        setUiDataFromNetwork()
        setUpAdapter()
        getCurrentYearAndMonth()
        calenderFirstTimeSet = true
    }



    override fun onResume() {
        super.onResume()
        goalTabAdapter()
        preferenceTabAdapter()

    }

    private fun   setUiDataFromNetwork() {
        binding.tvHeader2.text =
            "${BaseResponseDataObject.profilePageData.firstName} ${BaseResponseDataObject.profilePageData.lastName}"
        binding.tvGender.text = BaseResponseDataObject.profilePageData.gender
        context?.let {
            Glide
                .with(it)
                .load(BaseResponseDataObject.profilePageData.profileImage)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivUserImage)
        };

        if (BaseResponseDataObject.profilePageData.weightUnit == "kgs") {
            val kgs =
                convert.converlbsTokg(BaseResponseDataObject.profilePageData.weight.toString())
            val delim = "."
            val decimalPartInweignt = kgs.split(delim)
            val finalkg = decimalPartInweignt[0]
            binding.tvWeight.text = "${finalkg}"
            binding.tvWeightUnit.text = "KGS"

        } else {
            binding.tvWeight.text = "${BaseResponseDataObject.profilePageData.weight.toInt()}"
            binding.tvWeightUnit.text = "LBS"
        }

        if (BaseResponseDataObject.profilePageData.heightUnit == "Feet") {
            binding.tvHeightFeet.text = "${BaseResponseDataObject.profilePageData.heightValueFeet}"
            binding.tvHeightFeetUnit.text = "Ft"
            binding.tvHeightInch.text =
                "${BaseResponseDataObject.profilePageData.heightValueInches}"
            binding.tvHeightInchUnit.text = "in"
            binding.tvWeightUnit.visibility = View.VISIBLE
            binding.tvHeightInchUnit.visibility = View.VISIBLE

        } else {
            val height = convert.convertFeetToMeters(
                BaseResponseDataObject.profilePageData.heightValueFeet.toString(),
                BaseResponseDataObject.profilePageData.heightValueInches.toString()
            )
            val delim = "."
            val decimalPartInweignt = height.split(delim)
            val finaheight = decimalPartInweignt[0]
            binding.tvHeightFeet.text = "${finaheight}CM"
            binding.tvWeightUnit.visibility = View.GONE
            binding.tvHeightInchUnit.visibility = View.GONE

        }


        viewModel.checkgetCalanderTrack(
            BaseResponseDataObject.accessToken,
            GetCalenderRequest(ACTION_GET_CALENDAR_TRACK, (Calendar.getInstance().get(Calendar.MONTH)+1).toString(), Year.now().value.toString())
        )
        viewModel.checkgetLevel(BaseResponseDataObject.accessToken)
        viewModel.checkgetBookmarkClass(
            BaseResponseDataObject.accessToken,
            BookmarkClassRequest(ACTION_BOOK_MARK_CLASS)
        )
        viewModel.checkgetClassStatistics(
            BaseResponseDataObject.accessToken,
            GetClassStatisticsRequest(ACTION_GET_CLASS_STATISTICS)
        )
        viewModel.checkgetAchievementsClass(
            BaseResponseDataObject.accessToken,
            GetAchieveMentRequest(ACTION_GET_ACHIEVEMENTS_CLASS)
        )
        viewModel.checkgetAchievementsdayStreak(
            BaseResponseDataObject.accessToken,
            GetAchievementsdayStreakRequest(ACTION_GET_ACHIEVEMENTS_DAYILY_STREAK)
        )
        viewModel.checkgetAchievementsweeklyStreak(
            BaseResponseDataObject.accessToken,
            GetAchievementsweeklyStreakRequest(ACTION_GET_ACHIEVEMENTS_WEEKLY_STREAK)
        )
        viewModel.checkgetCalories(
            BaseResponseDataObject.accessToken,
            GetCaloriesRequest(ACTION_GET_CALORIES)
        )
        viewModel.checkgetLbs(BaseResponseDataObject.accessToken, GetLbsRequest(ACTION_GET_WEIGHT))
    }
    private fun setClickListner(){
        binding.tvEdit.setOnClickListener {
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            ft.replace(R.id.container, EditProfileFragment(), "NewFragmentTag")
            ft.commit()
        }
        binding.goalTab1.btnChangeGoal.setOnClickListener {
            val intent = Intent(context,YourGoalsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("accountFragment","AccountFrag")
            startActivity(intent)
        }
        binding.preferenceTab1.btnChangeIntesert.setOnClickListener {
            val intent = Intent(context,YourInterestActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("accountFragment","AccountFrag")
            startActivity(intent)
        }

        binding.preferenceTab1.btnChangeEquipment.setOnClickListener {
            val intent = Intent(context,YourEquipmentActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("accountFragment","AccountFrag")
            startActivity(intent)
        }
        binding.workoutTab1.tvShowAll.setOnClickListener {
            val newList = getClassStatisticsList.filter {
                it.id == "68"
            }
            BaseResponseDataObject.getClassStatisticsList = newList as MutableList<VideoType>
            BaseResponseDataObject.getClassStatisticsListAll.addAll(getClassStatisticsList)
            val intent = Intent(context, WorkoutActivity::class.java)
            startActivity(intent)

        }
    }

    private fun setupUi() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
        if(BaseResponseDataObject.hasSubscription){
            binding.llProMember.visibility = View.VISIBLE
        }
        else{
            binding.llProMember.visibility = View.GONE
        }
    }

    private fun setUpAdapter() {
        dataListAllTabItem.clear()
        dataListAllTabItem.add(HomePageVideosModel("Goals", true))
        dataListAllTabItem.add(HomePageVideosModel("Preferences", false))
        dataListAllTabItem.add(HomePageVideosModel("Workouts", false))
        dataListAllTabItem.add(HomePageVideosModel("Saved classes", false))
        binding.rvTabItem.apply {
            layoutManagerAllTabItem =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerAllTabItem
            allTabItemAdapter =
                AllTabItemAdapter(dataListAllTabItem, requireContext(), binding.rvTabItem)
            this.adapter = allTabItemAdapter

        }
        allTabItemAdapter!!.setAdapterListener(this)

        //first time when page open
        goalTabAdapter()
        preferenceTabAdapter()
//        workoutsTabAdapter()
//        saveClassesAdapter()
        binding.goalTab1.root.visibility = View.VISIBLE
        binding.preferenceTab1.root.visibility = View.GONE
        binding.workoutTab1.root.visibility = View.GONE
        binding.saveclassTab1.root.visibility = View.GONE
    }

    override fun onItemClickTime(position: Int) {
        for (i in 0 until dataListAllTabItem.size) {
            if (i == position) {
                if (dataListAllTabItem[i].selected) {
                    dataListAllTabItem[i].selected = false
                } else {
                    dataListAllTabItem[i].selected = true
                }
            } else {
                dataListAllTabItem[i].selected = false
            }
        }
        when (position) {
            0 -> {
                binding.goalTab1.root.visibility = View.VISIBLE
                binding.preferenceTab1.root.visibility = View.GONE
                binding.workoutTab1.root.visibility = View.GONE
                binding.saveclassTab1.root.visibility = View.GONE
            }
            1 -> {
                binding.goalTab1.root.visibility = View.GONE
                binding.preferenceTab1.root.visibility = View.VISIBLE
                binding.workoutTab1.root.visibility = View.GONE
                binding.saveclassTab1.root.visibility = View.GONE
            }
            2 -> {
                binding.goalTab1.root.visibility = View.GONE
                binding.preferenceTab1.root.visibility = View.GONE
                binding.workoutTab1.root.visibility = View.VISIBLE
                binding.saveclassTab1.root.visibility = View.GONE
            }
            3 -> {
                binding.goalTab1.root.visibility = View.GONE
                binding.preferenceTab1.root.visibility = View.GONE
                binding.workoutTab1.root.visibility = View.GONE
                binding.saveclassTab1.root.visibility = View.VISIBLE
            }
        }
    }




    private fun goalTabAdapter() {
        dataList.clear()
        getGoalList.clear()

        BaseResponseDataObject.profilePageData.goal.map {
            getGoalList.add(
                Goal(
                    id = it.id,
                    description = it.description,
                    image = it.image,
                    title = it.title
                )
            )
        }
        getGoalList.map {
            dataList.add(GoalsData(true, "#F9B801", true))
        }
        binding.goalTab1.rvGoal.apply {
            layoutManagerGoals =
                carbon.widget.RecyclerView.LinearLayoutManager(requireContext())
            this.layoutManager = layoutManagerGoals
            goalsAdapter =
                GoalsAdapterAccountFragment(dataList, requireContext(), getGoalList)
            this.adapter = goalsAdapter

        }


//        dataListClassMilestone.clear()
//        val ClassMilestone: ArrayList<String> = ArrayList<String>()
//        ClassMilestone.clear()
//        ClassMilestone.add("")
//        ClassMilestone.add("")
//        ClassMilestone.add("")
//        ClassMilestone.add("")
//        dataListClassMilestone.add(AllClassMilestoneReponse("Class Milestones", ClassMilestone))
//        dataListClassMilestone.add(AllClassMilestoneReponse("Class Milestones", ClassMilestone))
//        dataListClassMilestone.add(AllClassMilestoneReponse("Class Milestones", ClassMilestone))
//        binding.goalTab1.rvClassMilestone.apply {
//            layoutManagerClassMilestone =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            this.layoutManager = layoutManagerClassMilestone
//            allClassMilestoneHeaderAdapter =
//                AllClassMilestoneHeaderAdapter(requireContext(), dataListClassMilestone)
//            this.adapter = allClassMilestoneHeaderAdapter
//        }


    }


    private fun achievementAdapter(){

//        ClassMilestone.add("")
        var getDecimalHexcode: MutableList<String> = ArrayList<String>()
//        getClassAcievementList.map {
//            getDecimalHexcode.add("")
//        }

        val newCountArray = getClassAchievementList.filter { it.isComplete }
//        Log.d("newArrayis","the new count Arry is $newCountArray")
        for (i in 0..newCountArray.size-1){

            var alpha:Double = i.toDouble() / (newCountArray.size.toDouble()-1.0)
            Log.d("fractiobdata","$i abd array ${newCountArray.size-1} alpha $alpha demo ")
            getDecimalHexcode.add(alpha.toString())
//            getDecimalHexcode[i] = alpha.toString()
        }
        Log.d("theFilterDataArray", "the filter data is $getClassAchievementList")
        binding.goalTab1.rvHomeWorkoutChild.apply {
            layoutManagerClassMilestone =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerClassMilestone
            allClassMilestoneChildAdapter =
                AllClassMilestoneChildAdapter(
                    getClassAchievementList,
                    requireContext(),
                    "class",
                    getDecimalHexcode
                )
            this.adapter = allClassMilestoneChildAdapter

        }


    }


    private fun dayStreakAdpater(){

        var getDecimalHexcode: MutableList<String> = ArrayList<String>()
//        getAchievementsdayStreakList.map {
//            getDecimalHexcode.add("")
//        }

        val newCountArray = getAchievementsDayStreakList.filter { it.isComplete }
//        Log.d("newArrayis","the new count Arry is $newCountArray")
        for (i in 0..newCountArray.size-1){
            var alpha:Double = i.toDouble() / (newCountArray.size.toDouble()-1.0)
            getDecimalHexcode.add(alpha.toString())
        }
        binding.goalTab1.rvHomeWorkoutChild2.apply {
            val  layoutManagerClassMilestone2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerClassMilestone2
            allClassMilestoneChildAdapter2 =
                AllClassMilestoneChildAdapter(
                    getAchievementsDayStreakList,
                    requireContext(),
                    "day",
                    getDecimalHexcode
                )
            this.adapter = allClassMilestoneChildAdapter2

        }
    }

    private fun weeklyStreakAdapter(){
        var getDecimalHexcode: MutableList<String> = ArrayList<String>()


        val newCountArray = getAchievementsWeeklyStreakList.filter { it.isComplete }
//        Log.d("newArrayis","the new count Arry is $newCountArray")
        for (i in 0..newCountArray.size-1){
            var alpha:Double = i.toDouble() / (newCountArray.size.toDouble()-1.0)
            getDecimalHexcode.add(alpha.toString())
        }
        binding.goalTab1.rvHomeWorkoutChild3.apply {
            val  layoutManagerClassMilestone2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerClassMilestone2
            allClassMilestoneChildAdapter3 =
                AllClassMilestoneChildAdapter(
                    getAchievementsWeeklyStreakList,
                    requireContext(),
                    "week",
                    getDecimalHexcode
                )
            this.adapter = allClassMilestoneChildAdapter3

        }
    }

    private fun getCaloriesAdapter(){
        var getDecimalHexcode: MutableList<String> = ArrayList<String>()

        val newCountArray=  getCaloriesList.filter { it.isComplete }
//        Log.d("newArrayis","the new count Arry is $newCountArray")
        for (i in 0..newCountArray.size-1){
            var alpha:Double = i.toDouble() / (newCountArray.size.toDouble()-1.0)
            getDecimalHexcode.add(alpha.toString())
        }
        binding.goalTab1.rvHomeWorkoutChild4.apply {
            val  layoutManagerClassMilestone2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerClassMilestone2
            allClassMilestoneChildAdapter4 =
                AllClassMilestoneChildAdapter( getCaloriesList,requireContext(),"calories",getDecimalHexcode)
            this.adapter = allClassMilestoneChildAdapter4

        }
    }

    private fun getLbsAdapter(){
        var getDecimalHexcode: MutableList<String> = ArrayList<String>()

        val newCountArray=  getLbsList.filter { it.isComplete }
//        Log.d("newArrayis","the new count Arry is $newCountArray")
        for (i in 0..newCountArray.size-1){
            var alpha:Double = i.toDouble() / (newCountArray.size.toDouble()-1.0)
            getDecimalHexcode.add(alpha.toString())
        }
                binding.goalTab1.rvHomeWorkoutChild5.apply {
            val  layoutManagerClassMilestone2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagerClassMilestone2
            allClassMilestoneChildAdapter5 =
                AllClassMilestoneChildAdapter( getLbsList,requireContext(),"lbs",getDecimalHexcode)
            this.adapter = allClassMilestoneChildAdapter5

        }
    }

    private fun levelListAdapter(){
        levelList.clear()
        dataListlevel.clear()

        levelList2.map {
            levelList.add(
                com.litmethod.android.models.GetLevel.Data(
                    description = it.description,
                    hexcode = it.hexcode, id = it.id, image = it.image, title = it.title
                )
            )
        }

        levelList2.map {
            val userid=it.id
            var isAddDAta= false
            BaseResponseDataObject.profilePageData.level.map {
                if (it.id == userid) {
                    isAddDAta = true
                    goalLevelForEditUser.add(it.id)
                    dataListlevel.add(LevelData(true, "#ED2124", true))
                }
            }
            if (isAddDAta==false){
                dataListlevel.add(LevelData(false, "#ED2124", false))
            }
        }

        binding.preferenceTab1.rvFitness.layoutManager =
            carbon.widget.RecyclerView.LinearLayoutManager(requireContext())
        levelAdapter = LevelAdapter(dataListlevel, requireContext(), levelList)
        binding.preferenceTab1.rvFitness.adapter = levelAdapter
        binding.preferenceTab1.rvFitness.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
        levelAdapter!!.setAdapterListener(this)
    }


    private fun preferenceTabAdapter() {


        interestList.clear()
        dataListInterest.clear()
        BaseResponseDataObject.profilePageData.interest.map {
            interestList.add(
                Interest(
                    description = it.description,
                    id = it.id,
                    image = it.image,
                    title = it.title
                )
            )
        }

        interestList.map {
            dataListInterest.add(YourInterestData(true, "#ffffff", true))
        }
        binding.preferenceTab1.rvInterest.layoutManager =
            carbon.widget.RecyclerView.LinearLayoutManager(requireContext())
        yourInterestAdapter = YourInterestAdaperAccountFragment(dataListInterest, requireContext(), interestList)
        binding.preferenceTab1.rvInterest.adapter = yourInterestAdapter
        binding.preferenceTab1.rvInterest.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )

        equipmentList.clear()
        dataListEquipment.clear()
        BaseResponseDataObject.profilePageData.equipment.map {
            equipmentList.add(
                com.litmethod.android.models.GetEquipment.Data(
                    description = it.description,
                    id = it.id,
                    image = it.image,
                    title = it.title,
                    hexcode = ""
                )
            )
        }

        equipmentList.map {
            dataListEquipment.add(YourEquipmentData(true))
        }
        binding.preferenceTab1.rvEquipment.layoutManager =
            carbon.widget.RecyclerView.LinearLayoutManager(requireContext())
        yourEquipmentAdapter =
            YourEquipmentAdapter(dataListEquipment, requireContext(), equipmentList)
        binding.preferenceTab1.rvEquipment.adapter = yourEquipmentAdapter
        binding.preferenceTab1.rvEquipment.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
        yourEquipmentAdapter!!.setAdapterListener(this)

    }


    private fun workoutsTabAdapter(){
        binding.workoutTab1.rvHistory.apply {
            layoutManagerWorkoutHistory =
                carbon.widget.RecyclerView.LinearLayoutManager(requireContext())
            this.layoutManager = layoutManagerWorkoutHistory
            workoutHistoryAdapter =
                WorkoutHistoryAdapter(
                    getClassStatisticsList as ArrayList<VideoType>,
                    requireContext()
                )
            this.adapter = workoutHistoryAdapter
        }
        workoutHistoryAdapter?.setAdapterListener(this)
    }

    private fun saveClassesAdapter(){
        binding.saveclassTab1.rvSaveCllass.apply {
            layoutManagerSaveClass =
                carbon.widget.RecyclerView.LinearLayoutManager(requireContext())
            this.layoutManager = layoutManagerSaveClass
            saveClassAdapter =
                SaveClassAdapter(getBookmarkClassRespomseList, requireContext())
            this.adapter = saveClassAdapter
        }
        saveClassAdapter?.setAdapterListener(this)
    }

    override fun onItemClick(position: Int, code: String) {
       val isLevelChnaged= goalLevelForEditUser.contains(code)
        if(!isLevelChnaged){
            goalLevelForEditUser.clear()
            goalLevelForEditUser.add(code)
            viewModel.checkeditUserForLevel(
                BaseResponseDataObject.accessToken,
                EditUserLevelRequest("editUser", goalLevelForEditUser)
            )
            Log.d("theitemClicked", "the item network call")
        }

        for (i in 0 until dataListlevel.size){
            if(position == i){
                dataListlevel[position].selectedItem = !dataListlevel[position].selectedItem
            }else{
                dataListlevel[i].selectedItem = false
            }
        }
        var  dataListFilter: List<LevelData> = dataListlevel.filter { s -> s.selectedItem }
        if(dataListFilter.isNotEmpty()){
            for (i in 0 until dataListlevel.size){
                dataListlevel[i].oneItemSelected = true


            }
        }else{
            for (i in 0 until dataListlevel.size){
                dataListlevel[i].oneItemSelected = false
            }
        }

        levelAdapter!!.notifyDataSetChanged()
    }




    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, AccountScrrenViewModelFactory(AcountScreenFragmentRepository(retrofitService),requireContext())).get(
                AccountScreenViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.getLevelData.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            Log.d("getData","the data is ${it.result.data}")
//            it.result.data.map {
//                dataList.add(LevelData(false,"#52CFC5",false))
//            }
            levelList2 = it.result.data
            levelListAdapter()

        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getBookmarkClassResponse.observe(viewLifecycleOwner, Observer {
            binding.spLoading.visibility = View.GONE
            getBookmarkClassRespomseList= it.result.data as MutableList<com.litmethod.android.models.AcountScreenFragment.BookmarkClass.Data>
//            binding.saveclassTab1.rvSaveCllass.adapter?.notifyDataSetChanged()
            saveClassesAdapter()

        })

        viewModel.errorMessage2.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getClassStatisticsResponse.observe(viewLifecycleOwner, Observer {
       Log.d("GetClassStatisticsResponse", "the data resp $it")
            getClassStatisticsList = it.result.data.videoType as MutableList<VideoType>
            BaseResponseDataObject.getClassStatisticsList.addAll(it.result.data.videoType)
            BaseResponseDataObject.getClassStatisticsListNew.addAll(it.result.data.videoType)
                // as MutableList<VideoType>
////            getClassStatistics= it.result
            workoutsTabAdapter()

        })

        viewModel.errorMessage3.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getAchievementResponse.observe(viewLifecycleOwner, Observer {
            getClassAchievementList = it.result.data.classMilestone as MutableList<ClassMilestone>

          achievementAdapter()

        })

        viewModel.errorMessage4.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getAchievementsdayStreakResponse.observe(viewLifecycleOwner, Observer {
            getAchievementsDayStreakList = it.result.data.dayStreak as MutableList<ClassMilestone>

            dayStreakAdpater()

        })

        viewModel.errorMessage5.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getAchievementsweeklyStreakResponse.observe(viewLifecycleOwner, Observer {
            getAchievementsWeeklyStreakList =
                it.result.data.weeklyStreak as MutableList<ClassMilestone>

            weeklyStreakAdapter()

        })

        viewModel.errorMessage6.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getCaloriesResponse.observe(viewLifecycleOwner, Observer {
            getCaloriesList= it.result.data.caloriesachievements as MutableList<ClassMilestone>

           getCaloriesAdapter()

        })

        viewModel.errorMessage7.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })


        viewModel.GetLbsResponse.observe(viewLifecycleOwner, Observer {
            getLbsList= it.result.data.lbsachievements as MutableList<ClassMilestone>

           getLbsAdapter()

        })

        viewModel.errorMessage8.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.getCustomerResponse.observe(viewLifecycleOwner, Observer {

            BaseResponseDataObject.profilePageData = it.result.profileDetails
       Log.d("theDataChnged","the data is $it")

        })

        viewModel.errorMessage9.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

        viewModel.classBookmarkResponse.observe(viewLifecycleOwner, Observer {
//            binding.spLoading.visibility = View.GONE
            Log.d("getData255", "message is ${it.serverResponse.message}")
            viewModel.checkgetBookmarkClass(
                BaseResponseDataObject.accessToken,
                BookmarkClassRequest("bookmarkClass")
            )
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

        })


        viewModel.getCalanderResponse.observe(viewLifecycleOwner, Observer {
            binding.spLoading.visibility = View.GONE
            binding.workoutTab1.calendarBottomView.tvActiveNumber.text = it.result.dailyStreaks.toString()
            binding.workoutTab1.calendarBottomView.tvClassNumber.text = it.result.monthlyCount.toString()
            addCalederNewData(it.result.data)
        })
        viewModel.errorMessage11.observe(viewLifecycleOwner, Observer {
            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })

    }

    override fun onItemWorkoutHistoryClick(position: Int, code: String) {
        val newList = getClassStatisticsList.filter {
            it.id == code
        }
        BaseResponseDataObject.getClassStatisticsList = newList as MutableList<VideoType>
        BaseResponseDataObject.getClassStatisticsListAll.addAll(getClassStatisticsList)
        val intent = Intent(context, WorkoutActivity::class.java)
        intent.putExtra("isComingFromSingle", true)
        intent.putExtra("code", code)
        startActivity(intent)
    }

    override fun onItemRatingBarClick(position: Int, code: String) {
        binding.spLoading.visibility = View.VISIBLE
        viewModel.checkgetClassBookmark(
            BaseResponseDataObject.accessToken,
            ClassBookmarkRequest("classBookmark", code, "unsave")
        )
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

    private fun addCalederNewData(data: java.util.ArrayList<CalendarDataModel>) {
        var dataListWithOutBlank = data.filter { it.completeTime != "" }
        if (dataListWithOutBlank.isNotEmpty()) {
            val calendarItemsOnlyDateAndDifficulty: java.util.ArrayList<CalendarEvent> =
                java.util.ArrayList<CalendarEvent>()
            calendarItemsOnlyDateAndDifficulty.clear()
            dataListWithOutBlank.filter { it.completeTime != "" }.forEach {
                val oldFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
                var value: Date? = null
                var dueDateAsNormal = ""
                var difficultyLevel = 0
                var difficulty: java.util.ArrayList<Int> = java.util.ArrayList<Int>()
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
            cleanedUpCalendarItemsOnlyDateAndDifficulty.clear()
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
                var difficultyInt: java.util.ArrayList<Int> = java.util.ArrayList<Int>()
                difficultyInt.clear()
                difficultyInt.addAll(difficultyset)
                cleanedUpCalendarItemsOnlyDateAndDifficulty.add(CalendarEvent(ik, difficultyInt))
                difficultyset.clear()
            }
            Log.d("datacalender", cleanedUpCalendarItemsOnlyDateAndDifficulty.toString())
            setCalender()
        } else {
            setCalender()
        }
        if (spinnerWorking) {
            spinnerWorking = false
            binding.workoutTab1.calenderFullView.exOneCalendar.smoothScrollToMonth(thisYearMonth)
            Handler(Looper.getMainLooper()).postDelayed({
                calenderAlreadySet = true
            }, 1000)
        }
    }

    fun convertToLocalDateTimeViaInstant(dateToConvert: Date): LocalDateTime {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    private fun calendarApiCallOnScroll() {
        if (calenderAlreadySet) {
            calenderAlreadySet = false
            calendarApiCall(
                binding.workoutTab1.calenderFullView.exOneYearText.text.toString(),
                getMonthNumber(binding.workoutTab1.calenderFullView.exOneMonthText.text.toString()).toString()
            )
        }
        Handler(Looper.getMainLooper()).postDelayed({
            calenderAlreadySet = true
        }, 1000)
    }

    private fun setCalender() {
        val selectedDates = mutableSetOf<LocalDate>()
        val daysOfWeek = daysOfWeekFromLocale()
        val today = LocalDate.now()
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        val CalendarItems =
            cleanedUpCalendarItemsOnlyDateAndDifficulty.groupBy { it.date.toLocalDate() }

        binding.workoutTab1.calenderFullView.legendLayout.root.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    .toUpperCase(Locale.ENGLISH)

                if (daysOfWeek[index].name == LocalDate.now().getDayOfWeek().name) {
                    setTextColorRes(R.color.white)
                } else {
                    setTextColorRes(R.color.mono_grey_60)
                }
            }
        }

        if (calenderFirstTimeSet) {
            calenderFirstTimeSet = false
            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(20)
            val endMonth = currentMonth.plusMonths(20)
            binding.workoutTab1.calenderFullView.exOneCalendar.setup(
                startMonth,
                endMonth,
                daysOfWeek.first()
            )
            binding.workoutTab1.calenderFullView.exOneCalendar.scrollToMonth(currentMonth)
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
//                        if (selectedDates.contains(day.date)) {
//                            selectedDates.remove(day.date)
//                        } else {
//                            selectedDates.add(day.date)
//                        }
//                        binding.workoutTab1.calenderFullView.exOneCalendar.notifyDayChanged(day)
                    }
                }
            }
        }

        binding.workoutTab1.calenderFullView.exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
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

        binding.workoutTab1.calenderFullView.exOneCalendar.monthScrollListener = {
            if (binding.workoutTab1.calenderFullView.exOneCalendar.maxRowCount == 6) {
                binding.workoutTab1.calenderFullView.exOneYearText.text = it.yearMonth.year.toString()
                binding.workoutTab1.calenderFullView.exOneMonthText.text =
                    monthTitleFormatter.format(it.yearMonth)
                calendarApiCallOnScroll()
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.workoutTab1.calenderFullView.exOneYearText.text =
                        firstDate.yearMonth.year.toString()
                    binding.workoutTab1.calenderFullView.exOneMonthText.text =
                        monthTitleFormatter.format(firstDate)
                } else {
                    binding.workoutTab1.calenderFullView.exOneMonthText.text =
                        "${monthTitleFormatter.format(firstDate)} - ${
                            monthTitleFormatter.format(
                                lastDate
                            )
                        }"
                    if (firstDate.year == lastDate.year) {
                        binding.workoutTab1.calenderFullView.exOneYearText.text =
                            firstDate.yearMonth.year.toString()
                    } else {
                        binding.workoutTab1.calenderFullView.exOneYearText.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }
        yearSpinner()
        monthSpinner()
    }

    private fun yearSpinner() {
        var currentYear = Year.now().value
        var lastYear: Int = currentYear - 1
        val year = mutableListOf<String>()
        year.add("Select a year")
        for (i in lastYear..currentYear) {
            year.add(i.toString())
        }
        if (binding.workoutTab1.calenderFullView.spinnerYr != null) {
            val adapter = ArrayAdapter(
                requireActivity(),
                R.layout.spinner_text, year
            )
            binding.workoutTab1.calenderFullView.spinnerYr.adapter = adapter

            binding.workoutTab1.calenderFullView.spinnerYr.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (year[position].toString().equals("Select a year")) {
                    } else {
                        binding.workoutTab1.calenderFullView.exOneYearText.text = year[position].toString()
                        val monthNumber =
                            getMonthNumber(binding.workoutTab1.calenderFullView.exOneMonthText.text.toString())
                        thisYearMonth = YearMonth.of(
                            year[position].toInt(), monthNumber
                        )
                        calenderAlreadySet = false
                        spinnerWorking = true
                        calendarApiCall(year[position].toInt().toString(), monthNumber.toString())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        binding.workoutTab1.calenderFullView.exOneYearText.setOnClickListener {
            binding.workoutTab1.calenderFullView.spinnerYr.performClick()
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

        if (binding.workoutTab1.calenderFullView.spinnerMonth != null) {
            val adapter = ArrayAdapter(
                requireActivity(),
                R.layout.spinner_text, months
            )
            binding.workoutTab1.calenderFullView.spinnerMonth.adapter = adapter

            binding.workoutTab1.calenderFullView.spinnerMonth.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (months[position].toString().equals("Select a month")) {
                    } else {
                        binding.workoutTab1.calenderFullView.exOneMonthText.text = months[position].toString()
                        val monthNumber =
                            getMonthNumber(binding.workoutTab1.calenderFullView.exOneMonthText.text.toString())
                        thisYearMonth = YearMonth.of(
                            binding.workoutTab1.calenderFullView.exOneYearText.text.toString()
                                .toInt(),
                            monthNumber
                        )
                        calenderAlreadySet = false
                        spinnerWorking = true
                        calendarApiCall(
                            binding.workoutTab1.calenderFullView.exOneYearText.text.toString(),
                            monthNumber.toString()
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
        binding.workoutTab1.calenderFullView.exOneMonthText.setOnClickListener {
            binding.workoutTab1.calenderFullView.spinnerMonth.performClick()
        }
    }

    private fun getMonthNumber(monthName: String): Int {
        return Month.valueOf(monthName.uppercase(Locale.getDefault())).getValue()
    }

    override fun onItemEquipClick(position: Int, data: String) {

    }

}