package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import carbon.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityProgramsCoverBinding
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.GetProgramById.WeekVideo
import com.litmethod.android.network.ProgramsCoverActivityRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ClassesCoverActivity
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ViewModel.ProgramsCoverAcivityViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ViewModel.ProgramsCoverActivityViewModelFactory
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.MarginItemDecoration

class ProgramsCoverActivity : BaseActivity(), View.OnClickListener,
    ProgramChildAdapter.ProgramChildAdapterListener{
    lateinit var binding: ActivityProgramsCoverBinding
    lateinit var viewModel: ProgramsCoverAcivityViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    val dataList: ArrayList<ProgramsCoverData> = ArrayList<ProgramsCoverData>()
    var dataList2: ArrayList<WeekVideo> = ArrayList<WeekVideo>()
    private var programsCoverHeaderAdapter:ProgramsCoverHeaderAdapter?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_programs_cover)
        setUpUi()
        setUpAdapter()
        viewModelSetup()
        clickListener()
    }

    private fun setUpUi() {
        binding.tvCatagory.text = BaseResponseDataObject.getProgramByIdResponse.category
        Glide.with(this)
            .load(BaseResponseDataObject.getProgramByIdResponse.image)
//            .placeholder(R.drawable.piwo_48)
//            .transform(new CircleTransform(context))
            .into(binding.expandedImage)
        binding.tvVideoDesc.text = BaseResponseDataObject.getProgramsByIdToNextScreen.description
        binding.tvVideoTime.text = BaseResponseDataObject.getProgramsByIdToNextScreen.week
        binding.tvIntermediate.text = BaseResponseDataObject.getProgramByIdResponse.level

        when (BaseResponseDataObject.getProgramByIdResponse.level){
            "Intermediate" -> {
                binding.tvIntermediate.setTextColor(Color.parseColor("#f95a01"))
            }
            "Beginner" -> {
                binding.tvIntermediate.setTextColor(Color.parseColor("#52cfc5"))
            }
            "Advanced" -> {
                binding.tvIntermediate.setTextColor(Color.parseColor("#ed2124"))
            }

        }
        val firstWord = "Resistance row\n"
        val secondWord = "with ${BaseResponseDataObject.getProgramByIdResponse.instructorName}"
        val spannable: Spannable = SpannableString(firstWord + secondWord)
        spannable.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.white)),
            0,
            firstWord!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.mono_grey_60)),
            firstWord!!.length,
            firstWord!!.length + secondWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvVideoTitleWithName.text = spannable
    }

    private fun setUpAdapter(){
        dataList.clear()
        dataList2 = BaseResponseDataObject.getProgramByIdResponse.weekVideos as ArrayList<WeekVideo>
//        val dataListChild: ArrayList<ChildDataProgram> = ArrayList<ChildDataProgram>()
//        dataListChild.add(ChildDataProgram("Beginner",false))
//        dataListChild.add(ChildDataProgram("Intermediate",false))
//        dataListChild.add(ChildDataProgram("Advanced",false))
//        dataList.add(ProgramsCoverData("Level",dataListChild))
//
//        val dataListChilda: ArrayList<ChildDataProgram> = ArrayList<ChildDataProgram>()
//        dataListChilda.add(ChildDataProgram("10+ min",false))
//        dataListChilda.add(ChildDataProgram("20+ min",false))
//        dataListChilda.add(ChildDataProgram("30+ min",false))
//        dataListChilda.add(ChildDataProgram("40+ min",false))
//        dataList.add(ProgramsCoverData("Duration",dataListChilda))
//
//        val dataListChildaaa: ArrayList<ChildDataProgram> = ArrayList<ChildDataProgram>()
//        dataListChildaaa.add(ChildDataProgram("Total Body",false))
//        dataListChildaaa.add(ChildDataProgram("Total Body",false))
//        dataListChildaaa.add(ChildDataProgram("Total Body",false))
//        dataListChildaaa.add(ChildDataProgram("Total Body",false))
//        dataList.add(ProgramsCoverData("Muscle Groups",dataListChildaaa))
//
//        val dataListChildaa: ArrayList<ChildDataProgram> = ArrayList<ChildDataProgram>()
//        dataListChildaa.add(ChildDataProgram("10+ min",false))
//        dataListChildaa.add(ChildDataProgram("20+ min",false))
//        dataListChildaa.add(ChildDataProgram("30+ min",false))
//        dataListChildaa.add(ChildDataProgram("40+ min",false))
//        dataList.add(ProgramsCoverData("Equipment & Accessories",dataListChildaa))

        binding.rvTrainnerWeekVideo.layoutManager = RecyclerView.LinearLayoutManager(this@ProgramsCoverActivity)
        programsCoverHeaderAdapter = ProgramsCoverHeaderAdapter(this@ProgramsCoverActivity,dataList2)
        binding.rvTrainnerWeekVideo.adapter = programsCoverHeaderAdapter
        binding.rvTrainnerWeekVideo.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        ProgramChildAdapter.setAdapterListener(this)
    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
        }
    }

    override fun onItemClick(position: Int,code:String) {
        Log.d("theitemid","the item id is $code")
        viewModel.checkgetClassDetails(BaseResponseDataObject.accessToken, ClassDetailsRequest(AppConstants.actionTypegetClassDetails,code))

//        intentActivity(this, ClassesCoverActivity::class.java,"ProgramsCoverActivity")
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, ProgramsCoverActivityViewModelFactory(
                ProgramsCoverActivityRepository(retrofitService),this)).get(
                ProgramsCoverAcivityViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse() {
        viewModel.classDetailsResponse.observe(this, Observer {

            BaseResponseDataObject.getClassDetailsResponse=it.result.data
            val intent =  Intent(this, ClassesCoverActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        })

        viewModel.errorMessage6.observe(this, Observer {

        })
    }
    }

