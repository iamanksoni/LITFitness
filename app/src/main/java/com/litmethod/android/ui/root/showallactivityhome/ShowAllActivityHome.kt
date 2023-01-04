package com.litmethod.android.ui.root.showallactivityhome

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityShowAllBinding
import com.litmethod.android.databinding.ActivityShowAllHomeBinding
import com.litmethod.android.models.GetAllAccessFilter.Data7
import com.litmethod.android.models.VideoXXX
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.ShowAllScreen.ShowAllVideoAdapterHome
import com.litmethod.android.utlis.MarginItemDecoration

class ShowAllActivityHome : BaseActivity(), View.OnClickListener{
    lateinit var binding: ActivityShowAllHomeBinding
    private var showAllVideoAdapter: ShowAllVideoAdapterHome? = null
    var dataList2: ArrayList<VideoXXX> = ArrayList<VideoXXX>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_all_home)
        setUpUi()
        setUpAdapter()
        clickListener()
    }

    private fun setUpUi(){
        dataList2 =  BaseResponseDataObject.getVideosForShowAllInHome as ArrayList<VideoXXX>
        if(intent.extras != null){
            binding.tvHeader.text = intent.extras!!.getString("header")
        }
    }
    private fun setUpAdapter() {
        binding.rvAllVideo.layoutManager = RecyclerView.LinearLayoutManager(this@ShowAllActivityHome)
        showAllVideoAdapter = ShowAllVideoAdapterHome(dataList2, this@ShowAllActivityHome)
        binding.rvAllVideo.adapter = showAllVideoAdapter
        binding.rvAllVideo.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
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
}