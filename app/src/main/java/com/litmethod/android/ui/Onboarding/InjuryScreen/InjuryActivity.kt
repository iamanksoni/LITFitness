package com.litmethod.android.ui.Onboarding.InjuryScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityInjuryBinding
import com.litmethod.android.models.InjuryResponse.Data
import com.litmethod.android.network.InjuryRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel.InjuryViewModel
import com.litmethod.android.ui.Onboarding.InjuryScreen.ViewModel.InjuryViewModelFactory
import com.litmethod.android.ui.Onboarding.LevelScreen.LevelActivity
import com.litmethod.android.utlis.MarginItemDecoration
import com.litmethod.android.utlis.UiDataObject


class InjuryActivity : BaseActivity(),InjuryAdapter.InjuryAdapterListener, View.OnClickListener {
    lateinit var binding: ActivityInjuryBinding
    private var injuryAdapter: InjuryAdapter? = null
    lateinit var viewModel: InjuryViewModel

    private val retrofitService = RetrofitDataSourceService.getInstance()
    val dataList: ArrayList<InjuryData> = ArrayList<InjuryData>()
    var injuryList: List<Data> = ArrayList<Data>()
    val injuryLevel: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_injury)
        setUpUi()


        viewModelSetup()
        clickListener()
        viewModel.checkgetInjury(BaseResponseDataObject.accessToken)
        Log.d("textChnged","metr is ${UiDataObject.Weight}")
    }


    private fun setUpUi() {
//        binding.spLoading.visibility = View.VISIBLE
        val typeFacebold = Typeface.createFromAsset(assets, "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(assets, "futura_std_condensed.otf")
        binding.rgQuestion.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_yes -> {
                        binding.rbYes.typeface = typeFacebold
                        binding.rbNo.typeface = typeFace
                        setClickable()
                        pageValueChecking()
                    }
                    R.id.rb_no -> {
                        binding.rbYes.typeface = typeFace
                        binding.rbNo.typeface = typeFacebold
                        setClickable()
                        pageValueChecking()
                    }
                }
            }
        }
    }

    private fun setUpAdapter(){
        injuryLevel.clear()
        dataList.clear()
//        dataList.add(InjuryData(clickableItem = false, selectedItem = false))
//        dataList.add(InjuryData(clickableItem = false, selectedItem = false))
//        dataList.add(InjuryData(clickableItem = false, selectedItem = false))
//        dataList.add(InjuryData(clickableItem = false, selectedItem = false))
//        dataList.add(InjuryData(clickableItem = false, selectedItem = false))
//        dataList.add(InjuryData(clickableItem = false, selectedItem = false))

        injuryList.map {
            dataList.add(InjuryData(clickableItem = false, selectedItem = false))
        }
        binding.rbNo.isEnabled = true
        binding.rbYes.isEnabled = true
//        binding.spLoading.visibility = View.GONE
        binding.rvInjuryType.layoutManager = GridLayoutManager(this@InjuryActivity, 3)
        injuryAdapter = InjuryAdapter(dataList, this@InjuryActivity,injuryList)
        binding.rvInjuryType.adapter = injuryAdapter
        binding.rvInjuryType.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        injuryAdapter!!.setAdapterListener(this)
    }

    private fun setClickable(){
        if(binding.rbYes.isChecked){
            for (i in 0 until dataList.size){
                dataList[i].clickableItem = true
            }
            injuryAdapter!!.notifyDataSetChanged()
        }else if(binding.rbNo.isChecked){
            for (i in 0 until dataList.size){
                dataList[i].clickableItem = false
                dataList[i].selectedItem = false
            }
            injuryAdapter!!.notifyDataSetChanged()
        }else{
            for (i in 0 until dataList.size){
                dataList[i].clickableItem = false
                dataList[i].selectedItem = false
            }
            injuryAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onItemClick(position: Int,data:String) {
        if (dataList[position].selectedItem){
            dataList[position].selectedItem = false

            injuryLevel.remove(data)
            Log.d("data","remove data arry is $injuryLevel")
        }else{
            dataList[position].selectedItem = true
            injuryLevel.add(data)
            Log.d("data","add data arry is $injuryLevel")
        }
        injuryAdapter!!.notifyDataSetChanged()
        pageValueChecking()
    }

    private fun pageValueChecking(){
       if(binding.rbYes.isChecked){
           var  dataListFilter: List<InjuryData> = dataList.filter { s -> s.selectedItem }
           if(dataListFilter.isNotEmpty()){
               nextButtonactive()
           }else{
               nextButtonInactive()
           }
       }else if(binding.rbNo.isChecked){
           nextButtonactive()
       }else{
           nextButtonInactive()
       }
    }

    private fun nextButtonInactive() {
        binding.btnNext.backgroundTintList = null
        binding.btnNext.isEnabled = false
        binding.btnNext.isClickable = false
    }

    private fun nextButtonactive() {
        val colorInt = resources.getColor(R.color.red)
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(colorInt)
        binding.btnNext.isEnabled = true
        binding.btnNext.isClickable = true
    }

    private fun clickListener(){
        binding.ibBackButton.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_back_button -> {
                finish()
            }
            R.id.btn_next ->{


                nextScreen()
            }
        }
    }


    fun nextScreen(){
        if (binding.rbNo.isChecked){
            UiDataObject.injuryLevel = ArrayList<String>()
            UiDataObject.has_injury = false
            Log.d("theInterEsdata","injury data ${UiDataObject.injuryLevel} status ${UiDataObject.has_injury}")
            val intent = Intent(this@InjuryActivity, LevelActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else{
            UiDataObject.injuryLevel = injuryLevel
            UiDataObject.has_injury = true
            Log.d("theInterEsdata","injury data ${UiDataObject.injuryLevel} status ${UiDataObject.has_injury}")
            val intent = Intent(this@InjuryActivity, LevelActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, InjuryViewModelFactory(InjuryRepository(retrofitService),this)).get(
                InjuryViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.injuryData.observe(this, Observer {
            Log.d("getData","the data is ${it.result.data}")
            it.result.data.map {
                dataList.add(InjuryData(clickableItem = false, selectedItem = false))
            }
            injuryList = it.result.data
            setUpAdapter()

        })

        viewModel.errorMessage.observe(this, Observer {
//            binding.spLoading.visibility = View.GONE
            toastMessageShow(it.toString())
        })
    }

}