package com.litmethod.android.ui.root.ShowAllScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityShowAllBinding
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.Data6
import com.litmethod.android.models.GetAllAccessFilter.Data7
import com.litmethod.android.network.HomeTabFragmentRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ClassesCoverActivity
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModel
import com.litmethod.android.ui.root.HomeTabScreen.HomeViewModelFactory
import com.litmethod.android.utlis.MarginItemDecoration

class ShowAllActivity : BaseActivity(), View.OnClickListener, ShowAllVideoAdapter.ShowAllClickListener{
    lateinit var binding: ActivityShowAllBinding
    private var showAllVideoAdapter: ShowAllVideoAdapter? = null
    lateinit var viewModel: HomeViewModel
    lateinit var getClassDetailsList: Data6
    private val retrofitService = RetrofitDataSourceService.getInstance()
    var dataList2: ArrayList<Data7> = ArrayList<Data7>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_all)
        setUpUi()
        setUpAdapter()
        viewModelSetup()
        clickListener()
        getResponse()
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(
                this,
                HomeViewModelFactory(HomeTabFragmentRepository(retrofitService), this)
            ).get(
                HomeViewModel::class.java
            )
    }

    private fun setUpUi(){
        dataList2 = BaseResponseDataObject.getAllAccessFilterResponse as ArrayList<Data7>
        binding.tvHeader.text = "ExerCise Glossary"
    }
    private fun setUpAdapter() {
        binding.rvAllVideo.layoutManager = RecyclerView.LinearLayoutManager(this@ShowAllActivity)
        showAllVideoAdapter = ShowAllVideoAdapter(dataList2, this@ShowAllActivity)
        binding.rvAllVideo.adapter = showAllVideoAdapter
        binding.rvAllVideo.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
        ShowAllVideoAdapter?.setAdapterListener(this)
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

    private fun getResponse(){
        viewModel.classDetailsResponse.observe(this, Observer {
            getClassDetailsList = it.result!!.data
            BaseResponseDataObject.getClassDetailsResponse = getClassDetailsList
            //TODO :: Working here
            val intent =  Intent(this, ClassesCoverActivity::class.java)
            intent.putExtra("videoUrl", it.result!!.data.videoUrl)
            intent.putExtra("muscleUrl", it.result!!.data.muscle_image)
            intent.putExtra("videoTitle", it.result!!.data.title)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            this.overridePendingTransition(
                R.anim.slide_from_right,
                R.anim.slide_to_left
            )
        })

    }


    override fun onItemClickListener(position: Int, attribCode: String) {
        viewModel.checkgetClassDetails(
            BaseResponseDataObject.accessToken,
            ClassDetailsRequest("classDetails", attribCode)
        )
    }
}