package com.litmethod.android.ui.Onboarding.LitMembershipScreen

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityLitMembershipBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.utlis.MarginItemDecoration

class LitMembershipActivity : BaseActivity(), LitMembershipAdapter.LevelAdapterListener, View.OnClickListener {
    lateinit var binding: ActivityLitMembershipBinding
    val dataList: ArrayList<LitMembershipData> = ArrayList<LitMembershipData>()
    private var litMembershipAdapter: LitMembershipAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lit_membership)

        setUpUi()

        setUpAdapter()

        clickListener()
    }

    private fun setUpUi() {

    }

    private fun setUpAdapter() {
        dataList.clear()
        dataList.add(LitMembershipData(false,false))
        dataList.add(LitMembershipData(false,false))
        dataList.add(LitMembershipData(false,false))
        dataList.add(LitMembershipData(false,false))
        binding.rvMembershipType.layoutManager =
            RecyclerView.LinearLayoutManager(this@LitMembershipActivity)
        litMembershipAdapter = LitMembershipAdapter(dataList, this@LitMembershipActivity)
        binding.rvMembershipType.adapter = litMembershipAdapter
        binding.rvMembershipType.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.dp_10
                )
            )
        )
        litMembershipAdapter!!.setAdapterListener(this)
    }

    override fun onItemClick(position: Int) {
        for (i in 0 until dataList.size){
            if(position == i){
                dataList[position].selectedItem = !dataList[position].selectedItem
            }else{
                dataList[i].selectedItem = false
            }
        }
        var  dataListFilter: List<LitMembershipData> = dataList.filter { s -> s.selectedItem }
        if(dataListFilter.isNotEmpty()){
            for (i in 0 until dataList.size){
                dataList[i].oneItemSelected = true
            }
        }else{
            for (i in 0 until dataList.size){
                dataList[i].oneItemSelected = false
            }
        }
        pageValueChecking()
        litMembershipAdapter!!.notifyDataSetChanged()
    }

    private fun pageValueChecking() {
        var dataListFilter: List<LitMembershipData> = dataList.filter { s -> s.selectedItem }
        if (dataListFilter.isNotEmpty()) {
            nextButtonactive()
        } else {
            nextButtonInactive()
        }
    }

    private fun nextButtonInactive() {
        binding.btnSubscribe.backgroundTintList = null
        binding.btnSubscribe.isEnabled = false
        binding.btnSubscribe.isClickable = false
    }

    private fun nextButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnSubscribe.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnSubscribe.isEnabled = true
        binding.btnSubscribe.isClickable = true
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