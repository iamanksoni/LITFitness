package com.litmethod.android.ui.root.AllClassTabScreen.FilterScreen

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import carbon.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityFilterBinding
import com.litmethod.android.models.FilterList.Filter
import com.litmethod.android.models.FilterList.FilterListRequest
import com.litmethod.android.models.FilterList.Parameter
import com.litmethod.android.network.FilterActivityRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.FilterScreen.ViewModel.FilterActivityViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.FilterScreen.ViewModel.FilterActivityViewModelFactory
import com.litmethod.android.utlis.MarginItemDecoration

class FilterActivity : BaseActivity(), View.OnClickListener,FilterChildAdapter.FilterChildAdapterListener {
    lateinit var binding: ActivityFilterBinding
    private var cvSaveBoolean = false
    private var cvTakenByMeBoolean = false
    private var filterHeaderAdapter:FilterHeaderAdapter?= null
    val dataList: ArrayList<FilterActivityData> = ArrayList<FilterActivityData>()
    lateinit var viewModel: FilterActivityViewModel
    private val retrofitService = RetrofitDataSourceService.getInstance()
    var dataList2: ArrayList<Filter> = ArrayList<Filter>()
    var level: ArrayList<String> = ArrayList<String>()
    var duration: ArrayList<String> = ArrayList<String>()
    var equipMent: ArrayList<String> = ArrayList<String>()
    var accessorries: ArrayList<String> = ArrayList<String>()
    var instrurtor: ArrayList<String> = ArrayList<String>()
    var muscleGroup: ArrayList<String> = ArrayList<String>()
    var codeId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        viewModelSetup()
        setArryData()
        setUpUi()

        clickListener()
        viewModel.checkGetFilterList(BaseResponseDataObject.accessToken, FilterListRequest("filterList"))


    }

    private fun  setArryData(){
        if (BaseResponseDataObject.isFilter==true){
            level = BaseResponseDataObject.level
            duration = BaseResponseDataObject.duration
            equipMent = BaseResponseDataObject.equipMent
            accessorries = BaseResponseDataObject.accessorries
            instrurtor = BaseResponseDataObject.instrurtor
            muscleGroup = BaseResponseDataObject.muscleGroup
        }
    }
    private fun setUpUi() {
       codeId = intent.getIntExtra("intVariableName", 0).toString();
      Log.d("theintegercodeis","the id is $codeId")

    }

    private fun clickListener() {
        binding.ibBackButton.setOnClickListener(this)
        binding.cvSave.setOnClickListener(this)
        binding.cvTakenByMe.setOnClickListener(this)
        binding.btnApplyFilters.setOnClickListener(this)
        binding.ibReset.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val typeFacebold = Typeface.createFromAsset(assets, "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(assets, "futura_std_condensed.otf")
        when (p0!!.id) {
            R.id.ib_back_button -> {

                finish()
            }
            R.id.cv_save ->{
              if(cvSaveBoolean){
                  cvSaveBoolean = false
                  var colorInt =  resources.getColor(R.color.mono_grey_5)
                  binding.cvSave.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
                  var colorInt1 =  resources.getColor(R.color.mono_grey_60)
                  binding.ivSaved.imageTintList = ColorStateList.valueOf(colorInt1)
                  binding.tvSave.setTextColor(ColorStateList.valueOf(colorInt1))
                  binding.tvSave.typeface = typeFace
              }else{
                  cvSaveBoolean = true
                  var colorInt =  resources.getColor(R.color.yellow)
                  binding.cvSave.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
                  var colorInt1 =  resources.getColor(R.color.black)
                  binding.ivSaved.imageTintList = ColorStateList.valueOf(colorInt1)
                  binding.tvSave.setTextColor(ColorStateList.valueOf(colorInt1))
                  binding.tvSave.typeface = typeFacebold
              }
            }
            R.id.btn_apply_filters ->{
                BaseResponseDataObject.filterToClaassFragment = true
                BaseResponseDataObject.level = level
                BaseResponseDataObject.equipMent = equipMent
                BaseResponseDataObject.instrurtor = instrurtor
                BaseResponseDataObject.duration = duration
                BaseResponseDataObject.accessorries = accessorries
                BaseResponseDataObject.muscleGroup = muscleGroup
                //BaseResponseDataObject.termId = codeId
                BaseResponseDataObject.isFilter = true
                finish()
            }
            R.id.cv_taken_by_me ->{
                if(cvTakenByMeBoolean){
                    cvTakenByMeBoolean = false
                    var colorInt =  resources.getColor(R.color.mono_grey_5)
                    binding.cvTakenByMe.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
                    var colorInt1 =  resources.getColor(R.color.mono_grey_60)
                    binding.ivTakeByMe.imageTintList = ColorStateList.valueOf(colorInt1)
                    binding.tvTakenByMe.setTextColor(ColorStateList.valueOf(colorInt1))
                    binding.tvTakenByMe.typeface = typeFace
                }else{
                    cvTakenByMeBoolean = true
                    var colorInt =  resources.getColor(R.color.red)
                    binding.cvTakenByMe.setCardBackgroundColor(ColorStateList.valueOf(colorInt))
                    var colorInt1 =  resources.getColor(R.color.black)
                    binding.ivTakeByMe.imageTintList = ColorStateList.valueOf(colorInt1)
                    binding.tvTakenByMe.setTextColor(ColorStateList.valueOf(colorInt1))
                    binding.tvTakenByMe.typeface = typeFacebold
                }
            }
            R.id.ib_reset ->{
                BaseResponseDataObject.filterToClaassFragment = true
                BaseResponseDataObject.level.clear()
                BaseResponseDataObject.equipMent.clear()
                BaseResponseDataObject.instrurtor.clear()
                BaseResponseDataObject.duration.clear()
                BaseResponseDataObject.accessorries.clear()
                BaseResponseDataObject.muscleGroup.clear()
                //BaseResponseDataObject.termId = codeId
                BaseResponseDataObject.isFilter = false
                finish()
            }


        }
    }

    private fun setUpAdapter(){
        val dataListChild: ArrayList<ChildData> = ArrayList<ChildData>()
        val dataListChilda: ArrayList<ChildData> = ArrayList<ChildData>()
        val dataListChildaaa: ArrayList<ChildData> = ArrayList<ChildData>()
        val dataListChildaa: ArrayList<ChildData> = ArrayList<ChildData>()
        val dataListChildaaaa: ArrayList<ChildData> = ArrayList<ChildData>()
        val typeAccessories: ArrayList<ChildData> = ArrayList<ChildData>()
        dataList.clear()

       Log.d("thedataLis","the data list is $dataList2")
        dataList2.map {
            when(it.key){
            "typelevel" ->{
//                val typeLevelArray = it.parameter
//                val parameterCount = typeLevelArray.size
//
//                for (i in 0 until parameterCount) {
//                    dataListChild.add(ChildData("Beginner",
//                        false,
//                        Parameter(attributeCode=typeLevelArray[i].attributeCode,
//                            instructorImage=typeLevelArray[i].instructorImage,
//                            name=typeLevelArray[i].name,
//                            image=typeLevelArray[i].image)))
//                }
//
//                for (i in 0 until dataListChild.size) {
//                    for (j in 0 until BaseResponseDataObject.level.size) {
//                        if (dataListChild[i].childData.attributeCode == BaseResponseDataObject.level[j]) {
//                            dataListChild[i].isSelected = true
//                        }
//                    }
//                }

//                dataList.add(FilterActivityData("${it.name}",dataListChild,false))
                it.parameter.map {
                    var isAddData = false
                    if (BaseResponseDataObject.isFilter == true){
                       for (i in 0..BaseResponseDataObject.level.size-1){
                           var data = BaseResponseDataObject.level[i]
                           if (it.attributeCode==data){
                               isAddData = true
                               dataListChild.add(ChildData("Beginner",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                           }
                       }

                       if (isAddData == false){
                           dataListChild.add(ChildData("Beginner",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                       } else{

                       }
                   } else{
                       dataListChild.add(ChildData("Beginner",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))
                   }


                }
                dataList.add(FilterActivityData("${it.name}",dataListChild,false))
            }
                "typeduration" ->{

                    it.parameter.map {
                        var isAddData = false
                        if (BaseResponseDataObject.isFilter == true){
                            for (i in 0..BaseResponseDataObject.duration.size-1){
                                var data = BaseResponseDataObject.duration[i]
                                if (it.attributeCode==data){
                                    isAddData = true
                                    dataListChilda.add(ChildData("10+ min",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                                }
                            }

                            if (isAddData == false){
                                dataListChilda.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                            } else{

                            }
                        } else{
                            dataListChilda.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                        }


                    }
                    dataList.add(FilterActivityData("${it.name}",dataListChilda,false))
                }
                "typeMuscleGroup" ->{
                    it.parameter.map {

                        var isAddData = false
                        if (BaseResponseDataObject.isFilter == true){
                            for (i in 0..BaseResponseDataObject.muscleGroup.size-1){
                                var data = BaseResponseDataObject.muscleGroup[i]
                                if (it.attributeCode==data){
                                    isAddData = true
                                    dataListChildaaa.add(ChildData("Total Body",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                                }
                            }

                            if (isAddData == false){
                                dataListChildaaa.add(ChildData("Total Body",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                            } else{

                            }
                        } else{
                            dataListChildaaa.add(ChildData("Total Body",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                        }


                    }
                    dataList.add(FilterActivityData("${it.name}",dataListChildaaa,false))
                }
                "typeEquipment" ->{
                    it.parameter.map {

                        var isAddData = false
                        if (BaseResponseDataObject.isFilter == true){
                            for (i in 0..BaseResponseDataObject.equipMent.size-1){
                                var data = BaseResponseDataObject.equipMent[i]
                                if (it.attributeCode==data){
                                    isAddData = true
                                    dataListChildaa.add(ChildData("10+ min",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                                }
                            }

                            if (isAddData == false){
                                dataListChildaa.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                            } else{

                            }
                        } else{
                            dataListChildaa.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))
                        }
                    }
                    dataList.add(FilterActivityData("${it.name}",dataListChildaa,false))
                }
                "typeAccessories" ->{
                    it.parameter.map {

                        var isAddData = false
                        if (BaseResponseDataObject.isFilter == true){
                            for (i in 0..BaseResponseDataObject.accessorries.size-1){
                                var data = BaseResponseDataObject.accessorries[i]
                                if (it.attributeCode==data){
                                    isAddData = true
                                    typeAccessories.add(ChildData("10+ min",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                                }
                            }

                            if (isAddData == false){
                                typeAccessories.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                            } else{

                            }
                        } else{
                            typeAccessories.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                        }


                    }
                    dataList.add(FilterActivityData("${it.name}",typeAccessories,false))
                }
                "instructor"->{
                      it.parameter.map {

                          var isAddData = false
                          if (BaseResponseDataObject.isFilter == true){
                              for (i in 0..BaseResponseDataObject.instrurtor.size-1){
                                  var data = BaseResponseDataObject.instrurtor[i]
                                  if (it.attributeCode==data){
                                      isAddData = true
                                      dataListChildaaaa.add(ChildData("10+ min",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                                  }
                              }

                              if (isAddData == false){
                                  dataListChildaaaa.add(ChildData("10+ min",false,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))

                              } else{

                              }
                          } else{
                              dataListChildaaaa.add(ChildData("10+ min",true,Parameter(attributeCode=it.attributeCode,instructorImage=it.instructorImage,name=it.name,image=it.image)))
                              instrurtor.add(it.attributeCode.toString())

                          }
                      }

                    dataList.add(FilterActivityData("${it.name}",dataListChildaaaa,false))
                  }

                else -> {

            }
            }

        }









        binding.rvFilter.layoutManager = RecyclerView.LinearLayoutManager(this@FilterActivity)
        filterHeaderAdapter = FilterHeaderAdapter(this@FilterActivity,dataList,binding.rvFilter)

        binding.rvFilter.adapter = filterHeaderAdapter
        binding.rvFilter.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_10)))
        FilterChildAdapter.setAdapterListener(this)
    }

    override fun onItemClick(position: Int, title: String,codeAttrib:String) {

        for (i in 0 until dataList.size){
            if(dataList[i].title.equals(title)){
                if(dataList[i].subList[position].isSelected){
                    dataList[i].subList[position].isSelected = false
                    Log.d("theTtribCode","the attrib code is deselected")
                    when (title){
                        "Level" -> {
                            level.remove(codeAttrib)
                        }
                        "Muscle Group" ->{
                            muscleGroup.remove(codeAttrib)
                        }
                        "Durations" ->{
                            duration.remove(codeAttrib)
                        }
                        "Equipment" ->{
                            equipMent.remove(codeAttrib)
                        }
                        "Accessories" ->{
                            accessorries.remove(codeAttrib)
                        }
                        "Instructor" ->{
                            instrurtor.remove(codeAttrib)
                        }
                    }
                      Log.d("theArray","level : $level muscle $muscleGroup duration $duration equipment $equipMent " +
                              "accssrory $accessorries instructor $instrurtor")
                }else{
                    dataList[i].subList[position].isSelected = true
                    Log.d("theTtribCode","the attrib code is selected and title $title")
                    when (title){
                        "Level" -> {
                            level.add(codeAttrib)

                        }
                        "Muscle Group" ->{
                            muscleGroup.add(codeAttrib)
                        }
                        "Durations" ->{
                            duration.add(codeAttrib)
                        }
                        "Equipment" ->{
                            equipMent.add(codeAttrib)
                        }
                        "Accessories" ->{
                            accessorries.add(codeAttrib)
                        }
                        "Instructor" ->{
                            instrurtor.add(codeAttrib)
                        }

                    }
                    Log.d("theArray","level : $level muscle $muscleGroup duration $duration equipment $equipMent " +
                            "accssrory $accessorries instructor $instrurtor")
                }
            }
        }
    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, FilterActivityViewModelFactory(
                FilterActivityRepository(retrofitService),this)
            ).get(
                FilterActivityViewModel::class.java
            )
        loginResponse()
    }


    private  fun loginResponse(){

        viewModel.getFilterListResponse.observe(this, Observer {
            Log.d("theSucccessData","the succes data is ${it.result}")
              dataList2 = it.result.data.filter as ArrayList<Filter>
            setUpAdapter()
        })
        viewModel.errorMessage.observe(this, Observer {

        })
    }
}