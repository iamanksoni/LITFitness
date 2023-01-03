package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
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
import com.foxlabz.statisticvideoplayer.LitVideoPlayerSDK
import com.foxlabz.statisticvideoplayer.MainActivity
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityClassesCoverBinding
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.ClassDetails.EquipmentVideo
import com.litmethod.android.models.ClassDetails.InstructorInfo
import com.litmethod.android.models.GetEquipment.Data
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.network.ClassesCoverActivityRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentAdapter
import com.litmethod.android.ui.Onboarding.YourEquipmentScreen.YourEquipmentData
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassCoverActvityViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ViewModel.ClassesCoverActivityViewModelFactory
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.TrainerProfileScreen.TrainerProfileScreenActivity
import com.litmethod.android.utlis.MarginItemDecoration
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ClassesCoverActivity : BaseActivity(), YourEquipmentAdapter.YourEquipmentAdapterListener,
    View.OnClickListener{
    lateinit var binding: ActivityClassesCoverBinding
    val dataList: ArrayList<YourEquipmentData> = ArrayList<YourEquipmentData>()
    private var yourEquipmentAdapter: YourEquipmentAdapter? = null
    val eqipLevel: ArrayList<Int> = ArrayList<Int>()
    val dataListDeviceVideo: ArrayList<String> = ArrayList<String>()
    private var classesCoverDeviceVideoAdapter: ClassesCoverDeviceVideoAdapter? = null
    var equipmentList: MutableList<Data> = ArrayList<Data>()
    var equipMentVideoList: ArrayList<EquipmentVideo> = ArrayList<EquipmentVideo>()
    var instructorInfoist: ArrayList<InstructorInfo> = ArrayList<InstructorInfo>()
    lateinit var viewModel: ClassCoverActvityViewModel
    lateinit var item:InstructorInfo
    var isVideoSave = false
    private val retrofitService = RetrofitDataSourceService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_classes_cover)



        viewModelSetup()
        setUpUi()

        setUpAdapter()
        setDeviceAdapter()
        progressBarAnimation()
        clickListener()
    }

    private fun setUpUi() {
        equipMentVideoList = BaseResponseDataObject.getClassDetailsResponse.equipment_video as ArrayList<EquipmentVideo>
        instructorInfoist = BaseResponseDataObject.getClassDetailsResponse.instructor_info as ArrayList<InstructorInfo>
         item = instructorInfoist.get(0)
        binding.trainnerProfileSub.tvTrainerName.text = item.instructor_name
        binding.trainnerProfileSub.tvTrainerVideoCount.text = "${item.video_count} classes"
        binding.trainnerProfileSub.tvTrainerVideoDesc.text = item.instructor_details

        if(BaseResponseDataObject.getClassDetailsResponse.isSave){
            isVideoSave = true
            binding.imageView.setImageResource(R.drawable.ic_star_active)
        } else {
            isVideoSave = false
            binding.imageView.setImageResource(R.drawable.ic_like)
        }

        if (BaseResponseDataObject.getClassDetailsResponse.isViewed== false){
            binding.ivNew.visibility = View.VISIBLE
        } else{
            binding.ivNew.visibility = View.GONE
        }

        val originalFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd")
        val targetFormat: DateFormat = SimpleDateFormat("dd MMMM, yyyy")
        val date: Date = originalFormat.parse(BaseResponseDataObject.getClassDetailsResponse.date)
        val formattedDate: String = targetFormat.format(date)
        binding.tvDate.text = "Aired On $formattedDate"

        Glide.with(this)
            .load(BaseResponseDataObject.getClassDetailsResponse.thumbnail)
            .into(binding.expandedImage)
        Glide.with(this)
            .load(item.instructor_image)
            .into(binding.trainnerProfileSub.ivInstructorImage)

        val firstWord = "${BaseResponseDataObject.getClassDetailsResponse.title}"
        val secondWord = " with ${BaseResponseDataObject.getClassDetailsResponse.getInstructor}"
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

        val firstArray = BaseResponseDataObject.getClassDetailsResponse.equipment.map {
            it.name
        }
        val secondArray = BaseResponseDataObject.getClassDetailsResponse.accessories.map {
            it.name
        }
        val combinedEquipments = (firstArray + secondArray).joinToString()
         binding.tvCombinedEquip.text = combinedEquipments
        val firstWordtime = "${BaseResponseDataObject.getClassDetailsResponse.getDurations}"
        val secondWordtime = "  •  "
        val ThirdWordtime = "${BaseResponseDataObject.getClassDetailsResponse.getLevelName}"
        val FourthWordtime = "  •  "
        val  FifthWordtime= "${BaseResponseDataObject.getClassDetailsResponse.class_type}"

        val spannabletime: Spannable =
            SpannableString(firstWordtime + secondWordtime + ThirdWordtime + FourthWordtime + FifthWordtime)
        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.white)),
            0,
            firstWordtime!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.mono_grey_60)),
            firstWordtime!!.length,
            firstWordtime!!.length + secondWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        when (BaseResponseDataObject.getClassDetailsResponse.getLevelName){
            "Intermediate" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(getResources().getColor(R.color.intermediate)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Beginner" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(getResources().getColor(R.color.beginner)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "Advanced" -> {
                spannabletime.setSpan(
                    ForegroundColorSpan(getResources().getColor(R.color.Advanced)),
                    firstWordtime!!.length + secondWordtime.length,
                    firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        }



        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.mono_grey_60)),
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length,
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length + FourthWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannabletime.setSpan(
            ForegroundColorSpan(getResources().getColor(R.color.white)),
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length + FourthWordtime.length,
            firstWordtime!!.length + secondWordtime.length + ThirdWordtime.length + FourthWordtime.length + FifthWordtime.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvVideoTime.text = spannabletime

        if (intent.extras != null) {
            val pagename = intent.extras!!.getString("pagename")
            if (pagename == "ProgramsCoverActivity") {

//                binding.trainnerProgressSub.trpRating2.progressBackgroundColor = resources.getColor(R.color.blue_light)
//                binding.trainnerProgressSub.trpRating2.progressColor = resources.getColor(R.color.blue)
            }
        }
        binding.btnStartWorkout.setOnClickListener {

            LitVideoPlayerSDK.streamingUrl =
                "https://d1p2c1ey61b4dk.cloudfront.net/f1f2bd39-07b9-4e78-91b7-38e439b15151/hls/TIFFLsmSpdBndCirTra40Min1013-22.m3u8"

            startActivity(Intent(this@ClassesCoverActivity, MainActivity::class.java))
        }

    }

    private fun setUpAdapter() {
        dataList.clear()
        eqipLevel.clear()
        binding.rvEquipmentType.layoutManager =
            RecyclerView.LinearLayoutManager(this@ClassesCoverActivity)
        BaseResponseDataObject.getClassDetailsResponse.devices.map {
            equipmentList.add(Data(it.uuid, it.name, it.uuid, it.imgUrl, it.name))
            dataList.add(YourEquipmentData(false))

        }
        yourEquipmentAdapter =
            YourEquipmentAdapter(dataList, this@ClassesCoverActivity, equipmentList)
        binding.rvEquipmentType.adapter = yourEquipmentAdapter
        binding.rvEquipmentType.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
        yourEquipmentAdapter!!.setAdapterListener(this)


    }

    private fun setDeviceAdapter(){
        dataListDeviceVideo.clear()
        binding.rvDeviceVideo.layoutManager =
            RecyclerView.LinearLayoutManager(this@ClassesCoverActivity)
        classesCoverDeviceVideoAdapter =
            ClassesCoverDeviceVideoAdapter(equipMentVideoList, this@ClassesCoverActivity)
        binding.rvDeviceVideo.adapter = classesCoverDeviceVideoAdapter
        binding.rvDeviceVideo.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
    }

    override fun onItemEquipClick(position: Int,data:String) {
        if (dataList[position].selectedItem) {
            dataList[position].selectedItem = false
        } else {
            dataList[position].selectedItem = true
        }
        yourEquipmentAdapter!!.notifyDataSetChanged()
    }

    private fun progressBarAnimation() {
        val timer = object : CountDownTimer(1000, 500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
              val progressLikePercent=  BaseResponseDataObject.getClassDetailsResponse.classRatting.totalLikepersentage.toFloat()
                val totalRating =BaseResponseDataObject.getClassDetailsResponse.classRatting.totalCount
                 val difficultyRating = BaseResponseDataObject.getClassDetailsResponse.difficultyRating
             val percent =  (difficultyRating.toDouble()/5)*100
                Log.d("thepercentis","the percent $percent and rating $difficultyRating")
                binding.trainnerProgressSub.progressbar1.progress = progressLikePercent
                binding.trainnerProgressSub.progressbar2.progress = percent.toFloat()
                binding.trainnerProgressSub.progressbar1.labelText ="$totalRating RATINGS  (${progressLikePercent.toInt()}%)"
                binding.trainnerProgressSub.progressbar2.labelText = "DIFFICULTY ${difficultyRating}/5"
            //                binding.trainnerProgressSub.trpRating.progress = 70f
//                binding.trainnerProgressSub.trpRating2.progress = 70f
            }
        }
        timer.start()


    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.trainnerProfileSub.llTopLayout.setOnClickListener(this)
        binding.imageView.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.ll_top_layout -> {
                binding.spLoading.visibility = View.VISIBLE
                  viewModel.checkGetInstructorInfoRequest("${BaseResponseDataObject.accessToken}",
                  GetInstructorInfoRequest("10","getInstructorinfo",item.instructor_id,"1")
                  )
                 Log.d("theINstrust","the instructor id is ${item.instructor_id}")
            }
            R.id.imageView -> {
                binding.spLoading.visibility = View.VISIBLE
                if(isVideoSave){
                    viewModel.checkgetClassBookmark(BaseResponseDataObject.accessToken,
                        ClassBookmarkRequest("classBookmark","${BaseResponseDataObject.getClassDetailsResponse.id}","unsave")
                    )
                    isVideoSave=false
                    binding.imageView.setImageResource(R.drawable.ic_like)
                }else{
                    viewModel.checkgetClassBookmark(BaseResponseDataObject.accessToken,
                        ClassBookmarkRequest("classBookmark","${BaseResponseDataObject.getClassDetailsResponse.id}","save")
                    )
                    isVideoSave=true
                    binding.imageView.setImageResource(R.drawable.ic_star_active)
                }

            }

        }
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, ClassesCoverActivityViewModelFactory(
                ClassesCoverActivityRepository(retrofitService),this)).get(
                ClassCoverActvityViewModel::class.java
            )
        loginResponse()
    }


    private  fun loginResponse(){

        viewModel.getInstructorInfoResponse.observe(this, Observer {
            Log.d("theSucccessData","the succes data is ${it.result}")
            binding.spLoading.visibility = View.GONE
            BaseResponseDataObject.getInstructorInfoResponse = it.result.pagenation.data
            intentActivity(this@ClassesCoverActivity, TrainerProfileScreenActivity::class.java,"")
        })
        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.classBookmarkResponse.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
            Log.d("getData255","message is ${it.serverResponse.message}")
               })
        viewModel.errorMessage.observe(this, Observer {
            binding.spLoading.visibility = View.GONE
        })
    }


}


