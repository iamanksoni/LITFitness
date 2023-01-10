package com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.FragmentLivescreenBinding
import com.litmethod.android.models.Liveclass.Data
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.network.LiveClassFragmentRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseFragment
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen.ViewModel.LiveScreenFragmentViewModel
import com.litmethod.android.ui.root.LiveClassTabScreen.LiveClassFragmentScreen.ViewModel.LiveScreenFragmentViewModelfactory
import com.litmethod.android.ui.root.LiveClassTabScreen.TimeUtil.LiveClassSeparatedByDate
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class LiveScreenFragment : BaseFragment() {
    lateinit var binding: FragmentLivescreenBinding
    val dataListVideo: ArrayList<Boolean> = ArrayList<Boolean>()
    private var layoutManagernewVideo: RecyclerView.LayoutManager? = null
    private var liveScreenVideoAdapter: LiveScreenVideoAdapterParentAdapter? = null
    lateinit var viewModel: LiveScreenFragmentViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    var liveClassList: ArrayList<Data> = ArrayList<Data>()
    var liveClassesSeparatedByDate = ArrayList<LiveClassSeparatedByDate>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLivescreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        viewModelSetup()
        hitApiOnSwipeRefresh()
        binding.swipeRefreshLayoutForLiveClass.setOnRefreshListener {
            hitApiOnSwipeRefresh()
        }
    }

    private fun hitApiOnSwipeRefresh() {
        viewModel.checkGetLiveClass(BaseResponseDataObject.accessToken, LogOutRequest("liveClass"))
    }

    private fun setupUi() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
    }

    private fun setUpAdapter() {
        dataListVideo.clear()
        dataListVideo.add(false)
        dataListVideo.add(false)
        dataListVideo.add(false)
        dataListVideo.add(false)
        binding.rvLiveClass.apply {
            layoutManagernewVideo =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            this.layoutManager = layoutManagernewVideo
            liveScreenVideoAdapter =
                LiveScreenVideoAdapterParentAdapter(liveClassesSeparatedByDate, requireContext())
            this.adapter = liveScreenVideoAdapter
        }
        Log.d("theChnagedData", "the chnage data is $liveClassesSeparatedByDate")
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this,
                LiveScreenFragmentViewModelfactory(
                    LiveClassFragmentRepository(retrofitService),
                    requireContext()
                )
            ).get(
                LiveScreenFragmentViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse() {
        viewModel.getLiveClassResponse.observe(viewLifecycleOwner, Observer {

            if (binding.swipeRefreshLayoutForLiveClass.isRefreshing) {
                binding.swipeRefreshLayoutForLiveClass.isRefreshing = false
            }

            if (it.result != null) {
                liveClassList = it.result.data as ArrayList<Data>
                mapTheResponseData()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {


        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mapTheResponseData() {
        val liveClassScheduledDates = liveClassList.map {
            it.class_time_show
        }.map {
            getLocalDateFrom(it)
        }.toSet().toList().map {
            convertDateStringToLocalDate(it)
        }.sorted()




        for (localDate in liveClassScheduledDates) {
            val filteredClasses = liveClassList.filter {
                val webResponseDateString = it.class_time_show
                val webResponseLocalDate = getLocalDateFrom(webResponseDateString)
                val webResponseDate = convertDateStringToLocalDate(webResponseLocalDate)

                webResponseDate == localDate
            }

            val sortedClasses = filteredClasses.sortedBy {
                val dateString = it.class_time_show
                val localDate = getLocalDateFrom(dateString)
                val date = convertDateStringToLocalDate(localDate)

                date
            }

            val separatedClass = LiveClassSeparatedByDate(localDate, sortedClasses)
            liveClassesSeparatedByDate.add(separatedClass)
        }
        setUpAdapter()
        Log.d("separtedClass", "${liveClassesSeparatedByDate.first().videoList}")
    }


    fun getLocalDateFrom(utcDateString: String): String {
        val oldFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val value = oldFormatter.parse(utcDateString)
        val newFormatter = SimpleDateFormat("yyyy-MM-dd")
        newFormatter.timeZone = TimeZone.getDefault()
        return newFormatter.format(value)
    }

    fun convertDateStringToLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(dateString, formatter)
    }
}