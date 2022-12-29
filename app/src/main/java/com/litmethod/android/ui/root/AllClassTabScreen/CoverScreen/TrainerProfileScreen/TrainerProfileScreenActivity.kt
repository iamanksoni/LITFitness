package com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.TrainerProfileScreen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.widget.RecyclerView
import com.bumptech.glide.Glide
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityTrainerProfileScreenBinding
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.models.GetInstructorInfo.Video
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.network.TrainerProfileScreenRepository
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.TrainerProfileScreen.ViewModel.TrainerProfileViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.TrainerProfileScreen.ViewModel.TrainerProfileViewModelFactory
import com.litmethod.android.utlis.MarginItemDecoration

class TrainerProfileScreenActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: ActivityTrainerProfileScreenBinding
    private var classesCoverDeviceVideoAdapter: TraineerProfileAdapter? = null
    private var pageNo = 1
    var getInstruutorVideo: ArrayList<Video> = ArrayList<Video>()
    lateinit var viewModel: TrainerProfileViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    private var isresponseSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trainer_profile_screen)
        viewModelSetup()
        setUpUi()

        setUpAdapter()

        clickListener()
    }

    private fun setUpUi() {
        getInstruutorVideo = BaseResponseDataObject.getInstructorInfoResponse.videos as ArrayList<Video>
        Glide.with(this)
            .load(BaseResponseDataObject.getInstructorInfoResponse.instructor_image)
            .into(binding.ivInstructor);
       binding.tvName.text = BaseResponseDataObject.getInstructorInfoResponse.name
        if (BaseResponseDataObject.getInstructorInfoResponse.instructor_type==""){
            binding.tvStatus.visibility = View.GONE
        }else{
            binding.tvStatus.visibility = View.VISIBLE
            binding.tvStatus.text = BaseResponseDataObject.getInstructorInfoResponse.instructor_type
        }

        binding.tvTrainerDesc.text = BaseResponseDataObject.getInstructorInfoResponse.description

    }

    private fun setUpAdapter() {

        binding.rvTrainerVideo.layoutManager =
            RecyclerView.LinearLayoutManager(this@TrainerProfileScreenActivity)
        classesCoverDeviceVideoAdapter =
            TraineerProfileAdapter(getInstruutorVideo, this@TrainerProfileScreenActivity)
        binding.rvTrainerVideo.adapter = classesCoverDeviceVideoAdapter
        binding.rvTrainerVideo.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )


        binding.rvTrainerVideo.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy);

                val linearLayoutManager =binding.rvTrainerVideo.layoutManager as LinearLayoutManager?

//            Log.d("theItem"," the lis is $getClassCatagoryByIdResponseList")
                val visibleItemCount = linearLayoutManager?.childCount
                val totalItemCount = linearLayoutManager?.itemCount
                val firstVisibleItemPosition = linearLayoutManager?.findFirstVisibleItemPosition()



                if (firstVisibleItemPosition == getInstruutorVideo.size - 3) {
                    if (isresponseSuccess==false) {
                        isresponseSuccess = true
                        pageNo++



                        viewModel.checkGetInstructorInfoRequest("${BaseResponseDataObject.accessToken}",
                            GetInstructorInfoRequest("10","getInstructorinfo","${BaseResponseDataObject.getInstructorInfoResponse.id}","$pageNo")
                        )
                        Log.d("theItem", " the network is called")
                    }
                }


                Log.d("getItem","the  visible item count $visibleItemCount and Past visible item count $firstVisibleItemPosition total $totalItemCount and list is ${getInstruutorVideo.size} ")
            }
    })
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

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, TrainerProfileViewModelFactory(
                TrainerProfileScreenRepository(retrofitService),this)
            ).get(
                TrainerProfileViewModel::class.java
            )
        loginResponse()
    }


    private  fun loginResponse(){

        viewModel.getInstructorInfoResponse.observe(this, Observer {
            Log.d("theSucccessData","the succes data is ${it.result}")
           isresponseSuccess = false
            getInstruutorVideo.addAll(it.result.pagenation.data.videos)
            binding.rvTrainerVideo.adapter?.notifyDataSetChanged()

        })
        viewModel.errorMessage.observe(this, Observer {

        })
    }

}