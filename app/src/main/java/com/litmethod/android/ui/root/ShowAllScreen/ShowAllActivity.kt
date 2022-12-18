package com.litmethod.android.ui.root.ShowAllScreen

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityShowAllBinding
import com.litmethod.android.models.GetAllAccessFilter.Data7
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.utlis.MarginItemDecoration

class ShowAllActivity : BaseActivity(), View.OnClickListener{
    lateinit var binding: ActivityShowAllBinding
    private var showAllVideoAdapter: ShowAllVideoAdapter? = null
    var dataList2: ArrayList<Data7> = ArrayList<Data7>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_all)
        setUpUi()
        setUpAdapter()

        clickListener()
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