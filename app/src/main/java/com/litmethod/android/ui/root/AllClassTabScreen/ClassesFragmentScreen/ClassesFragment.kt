package com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.FragmentClassesBinding
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.Data6
import com.litmethod.android.models.GetAllAccessCatagory.Data4
import com.litmethod.android.models.GetAllAccessCatagory.GetAllAccessCatagoryRequest
import com.litmethod.android.models.GetAllAccessFilter.Data7
import com.litmethod.android.models.GetAllAccessFilter.GetAllAccessFilterRequest
import com.litmethod.android.models.GetCatagory.Data
import com.litmethod.android.models.GetClassCatagoryById.GetClassCatagoryByIDRequest
import com.litmethod.android.models.GetProgram.Data3
import com.litmethod.android.models.GetProgram.GetProgramsRequest
import com.litmethod.android.models.GetProgramById.Data5
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest
import com.litmethod.android.network.ClassesFragmentRepository
import com.litmethod.android.network.RetrofitDataSourceService
import com.litmethod.android.shared.BaseFragment
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.AllAccessTabScreen.AllAccessTabAdapter
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ClassesTabScreen.StackClassesAdapter
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ClassesTabScreen.StackClassesData
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ClassesTabScreen.VideoClassesAdapter
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ProgramsTabScreen.ProgramsTabAdapter
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.BaseResponseDataObject
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util.GetProgramsByIdToNextScreen
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ViewModel.ClassesFragementViewModelFactory
import com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.ViewModel.ClassesFragmentViewModel
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ClassesCoverScreen.ClassesCoverActivity
import com.litmethod.android.ui.root.AllClassTabScreen.CoverScreen.ProgramsCoverScreen.ProgramsCoverActivity
import com.litmethod.android.ui.root.AllClassTabScreen.FilterScreen.FilterActivity
import com.litmethod.android.ui.root.ShowAllScreen.ShowAllActivity
import com.litmethod.android.utlis.AppConstants
import com.litmethod.android.utlis.DataPreferenceObject

class ClassesFragment : BaseFragment(), StackClassesAdapter.StackClassesAdapterListener,
    View.OnClickListener,VideoClassesAdapter.VideoClassesAdapterListener,ProgramsTabAdapter.ProgramsTabAdapterListener,AllAccessTabAdapter.AllAccessTabAdapterListener  {
    lateinit var binding: FragmentClassesBinding
    val dataList: ArrayList<StackClassesData> = ArrayList<StackClassesData>()
    private var layoutManagernew: RecyclerView.LayoutManager? = null
    private var stackClassesAdapter: StackClassesAdapter? = null
    lateinit var dataPreferenceObject: DataPreferenceObject

    val dataListVideo: ArrayList<Boolean> = ArrayList<Boolean>()
    val dataListVideoFilter: ArrayList<Boolean> = ArrayList<Boolean>()

    private var layoutManagernewVideo: RecyclerView.LayoutManager? = null
    private var layoutManagernewVideoFilter: RecyclerView.LayoutManager? = null
    private var videoClassesAdapter: VideoClassesAdapter? = null
    private var videoClassesAdapterFilter: VideoClassesAdapter? = null

    val dataListProgram: ArrayList<Boolean> = ArrayList<Boolean>()
    private var layoutManagernewProgram: RecyclerView.LayoutManager? = null
    private var programsTabAdapter: ProgramsTabAdapter? = null

    val dataListAccess: ArrayList<Boolean> = ArrayList<Boolean>()
    private var layoutManagernewAccess: RecyclerView.LayoutManager? = null
    private var allAccessTabAdapter: AllAccessTabAdapter? = null

    private var rbClassesActive = false
    private var rbProgramActive = false
    private var rbAccessActive = false
    private var pageNo = 1
    private var pageNoFilter = 1
    private var isLoading = false
    private var isresponseSuccess = true
    private var isresponseSuccessFilter = false


    private var codeNextRecylerView =""
    lateinit var viewModel: ClassesFragmentViewModel
    private val retrofitDataSourceService = RetrofitDataSourceService.getInstance()
    var getCatagoryList: List<Data> = ArrayList<Data>()
    var getClassCatagoryByIdResponseList: MutableList<com.litmethod.android.models.GetClassCatagoryById.Data> = ArrayList<com.litmethod.android.models.GetClassCatagoryById.Data>()
    var getClassCatagoryByIdResponseListFilter: MutableList<com.litmethod.android.models.GetClassCatagoryById.Data> = ArrayList<com.litmethod.android.models.GetClassCatagoryById.Data>()

    var getProgramsList: List<Data3> = ArrayList<Data3>()
    var getAllAccessCatagoryList: List<Data4> = ArrayList<Data4>()
    lateinit var getProgramByIdResponse: Data5
    lateinit var getClassDetailsList: Data6
    var getAllAccessFilterResponseList: List<Data7> = ArrayList<Data7>()
    var accessToken =""
    var pastVisibleItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    var loading = true
    var switchAdapter = false
    var showErrorTextView:Boolean = false
    var sendCatagoryidToFilter:String="0"
    var firstActivityCreate= true
    var filterActivtyScroll = false
    var activityCreated = true
    var isScrolling = false
    
//    7168_bhHCWmI40Xn69SxqBgYfoeK58tMJydL17GQzpUsZuFiVjrPaOkv3DNE2TRlAcw
//    7861_xCYNRc3Vdkg2WeUGvaqEMpbFOjrsAhl7BZXzPnou15ID6mwTHJ0i98yKtLfSQ4
    companion object {
        fun newInstance(): ClassesFragment {
            return ClassesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        viewModelSetup()

        getToken()
        Log.d("getClassCatagoryData","the onViewCreated is calling")
        Log.d("checkClling","on creaedView Called")
//        setUpAdapter()

        clickListener()

        binding.spLoading.visibility = View.VISIBLE

    }


    override fun onResume() {
        super.onResume()

        if (BaseResponseDataObject.filterToClaassFragment==true){
            BaseResponseDataObject.filterToClaassFragment = false
            if (BaseResponseDataObject.isFilter==true){

                BaseResponseDataObject.onResumeViewModel = true
                pageNoFilter=1
                binding.ibFilter.setImageResource(R.drawable.filter_enable)
                Log.d("checkClling","view model on resume clicked cllaed")
                binding.spLoading.visibility = View.VISIBLE
                isScrolling=false
                viewModel.checkGetClassCatagoryById(
                    accessToken,
                    GetClassCatagoryByIDRequest(
                        action= AppConstants.actionTypegetClassCatagoryById,
                        pageNo=  pageNoFilter,
                        termid=  BaseResponseDataObject.termId,
                        typeEquipment=BaseResponseDataObject.equipMent,
                        typeInstructor= BaseResponseDataObject.instrurtor,
                        typeMuscleGroup=BaseResponseDataObject.muscleGroup,
                        typeduration= BaseResponseDataObject.duration,
                        typelevel= BaseResponseDataObject.level,
                        typeAccessories=  BaseResponseDataObject.accessorries,
                        "",
                        ""
                    )

                )
                Log.d("thefrag","fragmrnt callin method")
            }else{
                binding.ibFilter.setImageResource(R.drawable.filter_disabled)
                pageNoFilter =1
                BaseResponseDataObject.onResumeViewModel = true
                binding.spLoading.visibility = View.VISIBLE
                viewModel.checkGetClassCatagoryById(accessToken,
                    GetClassCatagoryByIDRequest(AppConstants.actionTypegetClassCatagoryById,1,BaseResponseDataObject.termId,
                        listOf(),listOf(),listOf(),listOf(),listOf(), listOf(),"","")

                )

            }



        }

        Log.d("thefrag","fragmrnt intent is ${BaseResponseDataObject.equipMent}")
    }

    private fun  getToken(){
        accessToken = BaseResponseDataObject.token

            if (activityCreated== true){


               activityCreated = false
                viewModel.checkgetInjury(accessToken)
                viewModel.checkgetProgramsList(
                    accessToken,
                    GetProgramsRequest(AppConstants.actionTypegetPrograms)
                )
                viewModel.checkGetAllAccessCatagory(
                    accessToken,
                    GetAllAccessCatagoryRequest(AppConstants.actionTypegetAllAccessCategory)
                )
//                dataPreferenceObject.getTheData("userToken").asLiveData().observe(viewLifecycleOwner) {
////                    accessToken = it.toString()
//                    BaseResponseDataObject.accessToken = it
//                    Log.d("checkClling", "view token item clicked cllaed")
//                    viewModel.checkgetInjury(accessToken)
//                    viewModel.checkgetProgramsList(
//                        accessToken,
//                        GetProgramsRequest(AppConstants.actionTypegetPrograms)
//                    )
//                    viewModel.checkGetAllAccessCatagory(
//                        accessToken,
//                        GetAllAccessCatagoryRequest(AppConstants.actionTypegetAllAccessCategory)
//                    )
//
//                }
            }



    }


       private fun setAdapterForFilterData(){

           dataListVideoFilter.clear()
           getClassCatagoryByIdResponseList.clear()
           getClassCatagoryByIdResponseListFilter.map {
               dataListVideoFilter.add(false)
           }
           binding.rvVideoClass.apply {
               layoutManagernewVideoFilter = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
               this.layoutManager = layoutManagernewVideoFilter
               videoClassesAdapterFilter = VideoClassesAdapter(dataListVideoFilter, requireContext(),getClassCatagoryByIdResponseListFilter)
               this.adapter = videoClassesAdapterFilter
           }

           videoClassesAdapterFilter!!.setAdapterListenerVideoClass(this)

           binding.rvVideoClass.addOnScrollListener(object : RecyclerView.OnScrollListener() {

               override fun onScrolled( recyclerView:RecyclerView,  dx:Int,  dy:Int) {
                   super.onScrolled(recyclerView, dx, dy);

                   val linearLayoutManager =binding.rvVideoClass.layoutManager as LinearLayoutManager?

//            Log.d("theItem"," the lis is $getClassCatagoryByIdResponseList")
                   val visibleItemCount = linearLayoutManager?.childCount
                   val totalItemCount = linearLayoutManager?.itemCount
                   val firstVisibleItemPosition = linearLayoutManager?.findFirstVisibleItemPosition()

//                   BaseResponseDataObject.onResumeViewModel = true
                   if (firstVisibleItemPosition == getClassCatagoryByIdResponseListFilter.size - 3) {
                       if (isresponseSuccessFilter==false) {
                           isresponseSuccessFilter = true
                           pageNoFilter++
//                           isLoading = true
                           isScrolling = true
                           binding.spLoading.visibility = View.VISIBLE
                           Log.d("getAllAcrray", "the array data is page no$pageNoFilter and code rv$codeNextRecylerView")
                           Log.d("viewModelcallawithdata","Flter Data Adapter Adpater pageno is$pageNoFilter code id ${BaseResponseDataObject.termId}")
                           Log.d("viewModelcallawithdata","filter array size is ${getClassCatagoryByIdResponseListFilter.size}")
                           viewModel.checkGetClassCatagoryById(
                               accessToken,
                               GetClassCatagoryByIDRequest(
                                   action= AppConstants.actionTypegetClassCatagoryById,
                                   pageNo=  pageNoFilter,
                                   termid=  BaseResponseDataObject.termId,
                                   typeEquipment=BaseResponseDataObject.equipMent,
                                   typeInstructor= BaseResponseDataObject.instrurtor,
                                   typeMuscleGroup=BaseResponseDataObject.muscleGroup,
                                   typeduration= BaseResponseDataObject.duration,
                                   typelevel= BaseResponseDataObject.level,
                                   typeAccessories=  BaseResponseDataObject.accessorries,
                                   "",
                                   ""
                               )

                           )
                           Log.d("theItem", " the network is called")
                       }
                   }

               }

           })
       }



    private fun setupUi(){
        dataPreferenceObject = context?.let { DataPreferenceObject(it) }!!
        if (Build.VERSION.SDK_INT >= 21) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
        val typeFacebold = Typeface.createFromAsset(requireContext().assets, "futura_std_condensed_bold.otf")
        val typeFace = Typeface.createFromAsset(requireContext().assets, "futura_std_condensed.otf")

        binding.rbClasses.setOnClickListener {
            binding.tvHeader.text = "AXIS STRENGTH"
            binding.ibFilter.visibility = View.VISIBLE

            if (BaseResponseDataObject.isFilter==true){
                binding.ibFilter.setImageResource(R.drawable.filter_enable)
            } else{
                binding.ibFilter.setImageResource(R.drawable.filter_disabled)
            }
            if(rbClassesActive){
                rbClassesActive = false
                binding.spLoading.visibility = View.VISIBLE

            }else{
                rbClassesActive = true
                rbProgramActive = false
                rbAccessActive = false
                binding.spLoading.visibility = View.VISIBLE
            }
            if(rbClassesActive){
                val colorInt = requireContext().resources.getColor(R.color.black)
                binding.ivClasses.imageTintList =  ColorStateList.valueOf(colorInt)
                binding.rbClasses.typeface = typeFacebold
                binding.rbClasses.isChecked = true

                val colorInt2 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivProgram.imageTintList =  ColorStateList.valueOf(colorInt2)
                binding.rbPrograms.typeface = typeFace
                binding.rbPrograms.isChecked = false

                val colorInt3 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivAccess.imageTintList =  ColorStateList.valueOf(colorInt3)
                binding.rbAccess.typeface = typeFace
                binding.rbAccess.isChecked = false
            }else{
                val colorInt = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivClasses.imageTintList =  ColorStateList.valueOf(colorInt)
                binding.rbClasses.typeface = typeFace
                binding.rbClasses.isChecked = false

                val colorInt2 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivProgram.imageTintList =  ColorStateList.valueOf(colorInt2)
                binding.rbPrograms.typeface = typeFace
                binding.rbPrograms.isChecked = false

                val colorInt3 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivAccess.imageTintList =  ColorStateList.valueOf(colorInt3)
                binding.rbAccess.typeface = typeFace
                binding.rbAccess.isChecked = false
            }

            if(rbClassesActive){
                binding.rvVideoClass.visibility = View.VISIBLE
                binding.rvVideoProgram.visibility = View.GONE
                binding.rvVideoAccess.visibility = View.GONE
                binding.llStackView.visibility = View.VISIBLE
                binding.tvAllAccessError.visibility = View.GONE
                binding.spLoading.visibility = View.GONE
            }
        }

        binding.rbPrograms.setOnClickListener {
            binding.tvHeader.text = "PROGRAMS"
            binding.ibFilter.visibility = View.GONE
            if(rbProgramActive){
                rbProgramActive = false
                binding.spLoading.visibility = View.VISIBLE

            }else{
                rbClassesActive = false
                rbProgramActive = true
                rbAccessActive = false
                binding.spLoading.visibility = View.VISIBLE
            }
            if(rbProgramActive){
                val colorInt = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivClasses.imageTintList =  ColorStateList.valueOf(colorInt)
                binding.rbClasses.typeface = typeFace
                binding.rbClasses.isChecked = false

                val colorInt2 = requireContext().resources.getColor(R.color.black)
                binding.ivProgram.imageTintList =  ColorStateList.valueOf(colorInt2)
                binding.rbPrograms.typeface = typeFacebold
                binding.rbPrograms.isChecked = true

                val colorInt3 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivAccess.imageTintList =  ColorStateList.valueOf(colorInt3)
                binding.rbAccess.typeface = typeFace
                binding.rbAccess.isChecked = false

            }else{
                val colorInt = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivClasses.imageTintList =  ColorStateList.valueOf(colorInt)
                binding.rbClasses.typeface = typeFace
                binding.rbClasses.isChecked = false

                val colorInt2 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivProgram.imageTintList =  ColorStateList.valueOf(colorInt2)
                binding.rbPrograms.typeface = typeFace
                binding.rbPrograms.isChecked = false

                val colorInt3 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivAccess.imageTintList =  ColorStateList.valueOf(colorInt3)
                binding.rbAccess.typeface = typeFace
                binding.rbAccess.isChecked = false
            }
            if(rbProgramActive){
                binding.rvVideoClass.visibility = View.GONE
                binding.rvVideoProgram.visibility = View.VISIBLE
                binding.rvVideoAccess.visibility = View.GONE
                binding.tvAllAccessError.visibility = View.GONE
                binding.llStackView.visibility = View.GONE
                binding.spLoading.visibility = View.GONE
            }
        }

        binding.rbAccess.setOnClickListener {
            binding.tvHeader.text = "ALL ACCESS"
            binding.ibFilter.visibility = View.GONE
            if(rbAccessActive){
                rbAccessActive = false
                binding.spLoading.visibility = View.VISIBLE
            }else{
                rbClassesActive = false
                rbProgramActive = false
                rbAccessActive = true
                binding.spLoading.visibility = View.VISIBLE
            }
            if(rbAccessActive){
                val colorInt = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivClasses.imageTintList =  ColorStateList.valueOf(colorInt)
                binding.rbClasses.typeface = typeFace
                binding.rbClasses.isChecked = false

                val colorInt2 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivProgram.imageTintList =  ColorStateList.valueOf(colorInt2)
                binding.rbPrograms.typeface = typeFace
                binding.rbPrograms.isChecked = false

                val colorInt3 = requireContext().resources.getColor(R.color.black)
                binding.ivAccess.imageTintList =  ColorStateList.valueOf(colorInt3)
                binding.rbAccess.typeface = typeFacebold
                binding.rbAccess.isChecked = true

            }else{
                val colorInt = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivClasses.imageTintList =  ColorStateList.valueOf(colorInt)
                binding.rbClasses.typeface = typeFace
                binding.rbClasses.isChecked = false

                val colorInt2 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivProgram.imageTintList =  ColorStateList.valueOf(colorInt2)
                binding.rbPrograms.typeface = typeFace
                binding.rbPrograms.isChecked = false

                val colorInt3 = requireContext().resources.getColor(R.color.mono_grey_60)
                binding.ivAccess.imageTintList =  ColorStateList.valueOf(colorInt3)
                binding.rbAccess.typeface = typeFace
                binding.rbAccess.isChecked = false
            }

            if(rbAccessActive){

                binding.rvVideoClass.visibility = View.GONE
                binding.rvVideoProgram.visibility = View.GONE
                binding.rvVideoAccess.visibility = View.VISIBLE
                binding.llStackView.visibility = View.GONE
                binding.spLoading.visibility = View.GONE
                if (showErrorTextView== true){
                    binding.tvAllAccessError.visibility = View.VISIBLE
                }
            }
        }

        binding.rbClasses.performClick()
    }

    private fun setUpAdapter() {

        dataList.clear()
        for (i in 0..getCatagoryList.size-1){
            dataList.add(StackClassesData(i==0 ))
        }

        binding.rvStackType.apply {
            layoutManagernew = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManagernew
            stackClassesAdapter = StackClassesAdapter(dataList, requireContext(),getCatagoryList)
            this.adapter = stackClassesAdapter
        }
        binding.rvStackType.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager.HORIZONTAL))
        stackClassesAdapter!!.setAdapterListener(this)


    }

    private fun setProgramsAdapter(){

        dataListProgram.clear()
        getProgramsList.map {
            dataListProgram.add(false)
        }



        binding.rvVideoProgram.apply {
            layoutManagernewProgram = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            this.layoutManager = layoutManagernewProgram
            programsTabAdapter = ProgramsTabAdapter(dataListProgram, requireContext(),getProgramsList)
            this.adapter = programsTabAdapter
        }
        programsTabAdapter!!.setAdapterListenerProgramsTabAdapter(this)

    }





    private fun setClassVideoListAdpater(){
        dataListVideo.clear()

        getClassCatagoryByIdResponseList.map {
            dataListVideo.add(false)
        }
        binding.rvVideoClass.apply {
            layoutManagernewVideo = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            this.layoutManager = layoutManagernewVideo
            videoClassesAdapter = VideoClassesAdapter(dataListVideo, requireContext(),getClassCatagoryByIdResponseList)
            this.adapter = videoClassesAdapter

        }
        videoClassesAdapter!!.setAdapterListenerVideoClass(this)

        binding.rvVideoClass.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled( recyclerView:RecyclerView,  dx:Int,  dy:Int) {
                super.onScrolled(recyclerView, dx, dy);

                val linearLayoutManager =binding.rvVideoClass.layoutManager as LinearLayoutManager?

//            Log.d("theItem"," the lis is $getClassCatagoryByIdResponseList")
                val visibleItemCount = linearLayoutManager?.childCount
                val totalItemCount = linearLayoutManager?.itemCount
                val firstVisibleItemPosition = linearLayoutManager?.findFirstVisibleItemPosition()

//                Log.d("getAllAcrray", "rv array and visible item postion and successresp $isresponseSuccess $firstVisibleItemPosition sixe is ${getClassCatagoryByIdResponseList.size }")


                if (firstVisibleItemPosition == getClassCatagoryByIdResponseList.size - 3) {

                    if (BaseResponseDataObject.isFilter == true){

                        if (isresponseSuccess==false) {

                            isresponseSuccess = true
                            pageNo++
                            isLoading = true
                            isScrolling = true
                            binding.spLoading.visibility = View.VISIBLE
                            Log.d("getAllAcrray", "Networ called the array data is page no$pageNo and code rv$codeNextRecylerView")
                            Log.d("viewModelcallawithdata","normal Adpater pageno is$pageNo code id $codeNextRecylerView")
                            viewModel.checkGetClassCatagoryById(
                                accessToken,
                                GetClassCatagoryByIDRequest(
                                    AppConstants.actionTypegetClassCatagoryById,
                                    pageNo,
                                    codeNextRecylerView.toString(),
                                    typeEquipment=BaseResponseDataObject.equipMent,
                                    typeInstructor= BaseResponseDataObject.instrurtor,
                                    typeMuscleGroup=BaseResponseDataObject.muscleGroup,
                                    typeduration= BaseResponseDataObject.duration,
                                    typelevel= BaseResponseDataObject.level,
                                    typeAccessories=  BaseResponseDataObject.accessorries,
                                    "",
                                    ""
                                )

                            )
                            Log.d("theItem", " the network is called")
                        }

                    }else{
                        if (isresponseSuccess==false) {
                            isresponseSuccess = true
                            pageNo++
                            isLoading = true
                            isScrolling = true
                            binding.spLoading.visibility = View.VISIBLE
                            Log.d("getAllAcrray", "Networ called the array data is page no$pageNo and code rv$codeNextRecylerView")
                            Log.d("viewModelcallawithdata","normal Adpater pageno is$pageNo code id $codeNextRecylerView")
                            viewModel.checkGetClassCatagoryById(
                                accessToken,
                                GetClassCatagoryByIDRequest(
                                    AppConstants.actionTypegetClassCatagoryById,
                                    pageNo,
                                    codeNextRecylerView.toString(),
                                    listOf(),
                                    listOf(),
                                    listOf(),
                                    listOf(),
                                    listOf(),
                                    listOf(),
                                    "",
                                    ""
                                )

                            )
                            Log.d("theItem", " the network is called")
                        }
                    }

                }

            }

        })
    }




    private fun getAllAcessCatgoryAdapter(){
        dataListAccess.clear()


        getAllAccessCatagoryList.map {
            dataListAccess.add(false)
        }
        binding.rvVideoAccess.apply {
            layoutManagernewAccess = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            this.layoutManager = layoutManagernewAccess
            allAccessTabAdapter = AllAccessTabAdapter(dataListAccess, requireContext(),getAllAccessCatagoryList)
            this.adapter = allAccessTabAdapter
        }
        allAccessTabAdapter!!.setAdapterListenerAllAccess(this)
    }

    override fun onAllAccessItemClick(position: Int, code: String) {
        binding.spLoading.visibility = View.VISIBLE
        viewModel.checkgetAllAccessFilter(accessToken, GetAllAccessFilterRequest(AppConstants.actionTypegetAllAccessFilterRequest,"$code",
        true,"","","","","",""
            ))

    }
    override fun onItemClick(position: Int,code:String) {
        binding.spLoading.visibility = View.VISIBLE
        sendCatagoryidToFilter = code
        switchAdapter = true
        codeNextRecylerView =code
        BaseResponseDataObject.onResumeViewModel=false
        isScrolling=false
        pageNo =1

        Log.d("checkClling","view model on item clicked cllaed")
        if (BaseResponseDataObject.isFilter ==true){
            Log.d("checkClling","view model on item clicked cllaed")

            viewModel.checkGetClassCatagoryById(accessToken,
                GetClassCatagoryByIDRequest(AppConstants.actionTypegetClassCatagoryById,1,"$code",
                    typeEquipment=BaseResponseDataObject.equipMent,
                    typeInstructor= BaseResponseDataObject.instrurtor,
                    typeMuscleGroup=BaseResponseDataObject.muscleGroup,
                    typeduration= BaseResponseDataObject.duration,
                    typelevel= BaseResponseDataObject.level,
                    typeAccessories=  BaseResponseDataObject.accessorries,
                    "","")

            )

        }else{

            viewModel.checkGetClassCatagoryById(accessToken,
                GetClassCatagoryByIDRequest(AppConstants.actionTypegetClassCatagoryById,1,"$code",
                    listOf(),listOf(),listOf(),listOf(),listOf(), listOf(),"","")

            )

        }

        for (i in 0 until dataList.size){
            if(position == i){
                dataList[position].selectedItem = !dataList[position].selectedItem
            }else{
                dataList[i].selectedItem = false
            }
        }
        stackClassesAdapter!!.notifyDataSetChanged()
    }

    private fun clickListener(){
        binding.ibFilter.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ib_filter -> {
                val intent =  Intent(requireActivity(), FilterActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("intVariableName",sendCatagoryidToFilter );
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left
                )
            }
        }
    }

    override fun onVideoItemClick(position: Int,id:String) {
        binding.spLoading.visibility = View.VISIBLE
        viewModel.checkgetClassDetails(accessToken, ClassDetailsRequest(AppConstants.actionTypegetClassDetails,"$id"))
        Log.d("getData255","item Clicked class id $id")


    }

    override fun onProgramsItemClick(position: Int,programId:String) {
        binding.spLoading.visibility = View.VISIBLE
        val item =getProgramsList[position]
             BaseResponseDataObject.getProgramsByIdToNextScreen = GetProgramsByIdToNextScreen(item.description,item.week)
        Log.d("getProgramByidresponse","item Clicked program id $programId")
        viewModel.checkgetProgramsById(accessToken,
            GetProgramByIdRequest(AppConstants.actionTypegetProgramsById,programId)

        )

    }

    private fun viewModelSetup() {
        viewModel =
            ViewModelProvider(this, ClassesFragementViewModelFactory(ClassesFragmentRepository(retrofitDataSourceService),context!!)).get(
                ClassesFragmentViewModel::class.java
            )
        loginResponse()
    }

    private fun loginResponse(){
        viewModel.getCatagoryListResponse.observe(viewLifecycleOwner, Observer {
            Log.d("classsItemData","the data index api called $it")
           getCatagoryList = it.result.data

                setUpAdapter()
            var firstList=getCatagoryList[0]
            sendCatagoryidToFilter = firstList.id
            codeNextRecylerView = firstList.id
            Log.d("classsItemData","item ids $codeNextRecylerView and firsdt list $firstList")
            viewModel.checkGetClassCatagoryById(accessToken,
                GetClassCatagoryByIDRequest(AppConstants.actionTypegetClassCatagoryById,1,firstList.id,
                    listOf(), listOf(), listOf(), listOf(), listOf(), listOf(),"","")
            )
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {

        })

        viewModel.getClassCatagoryByIdResponse.observe(viewLifecycleOwner, Observer {
            if (it.result.data.isNotEmpty()){
                binding.rvVideoClass.visibility = View.VISIBLE
                Log.d("idResponse","response main filter data $it")
                binding.tvAllAccessError.visibility = View.GONE
                if (switchAdapter==true){
                    binding.spLoading.visibility = View.GONE
                    switchAdapter = false
                    isresponseSuccess = false
                    getClassCatagoryByIdResponseList = it.result.data.toMutableList()
                    setClassVideoListAdpater()
                }else{
                    if (isLoading==true){
                        isresponseSuccess =false
                        getClassCatagoryByIdResponseList.addAll(it.result.data)
                        binding.spLoading.visibility = View.GONE
                        it.result.data.map {
                            dataListVideo.add(false)
                        }
//                getClassCatagoryByIdResponseList.map {
//
//                }
                        binding.rvVideoClass.adapter?.notifyDataSetChanged()

                    } else{

                        binding.spLoading.visibility = View.GONE
                        isresponseSuccess = false
                        getClassCatagoryByIdResponseList = it.result.data.toMutableList()

                        setClassVideoListAdpater()
                    }
                }
            }else{

                if (isScrolling==false){
                    showErrorTextView= true
                    binding.tvAllAccessError.text =  it.serverResponse.message
                    binding.tvAllAccessError.visibility = View.VISIBLE
                    binding.rvVideoClass.visibility = View.GONE
                    getClassCatagoryByIdResponseList.clear()
                    binding.spLoading.visibility = View.GONE
//                    setClassVideoListAdpater()
                } else{
                    binding.spLoading.visibility = View.GONE
                    binding.tvAllAccessError.visibility = View.GONE
                }
                Log.d("theTextView","response main filter error called")

            }



            Log.d("dataChecking","the data is added main response")
            Log.d("getData2","the data is ${it.result.data}")
        })


        //////////////////////////  filter Data
      viewModel.getClassCatagoryByIdResponseFilter.observe(viewLifecycleOwner, Observer {

            if (it.result.data.isNotEmpty()){
                binding.rvVideoClass.visibility = View.VISIBLE
                isresponseSuccessFilter = false
                Log.d("theTextView","response filter called")
                binding.tvAllAccessError.visibility = View.GONE
                if (BaseResponseDataObject.isFilter==true){
                    if (pageNoFilter==1){
                        getClassCatagoryByIdResponseListFilter= it.result.data.toMutableList()
                        binding.spLoading.visibility = View.GONE
                        setAdapterForFilterData()
                    } else{
                        getClassCatagoryByIdResponseListFilter.addAll(it.result.data)
                        binding.spLoading.visibility = View.GONE
                        it.result.data.map {
                            dataListVideoFilter.add(false)
                        }
                        binding.rvVideoClass.adapter?.notifyDataSetChanged()
                    }
                } else{
                    if (pageNoFilter==1){
                        getClassCatagoryByIdResponseListFilter= it.result.data.toMutableList()
                        binding.spLoading.visibility = View.GONE
                        setAdapterForFilterData()
                    }else{
                        getClassCatagoryByIdResponseListFilter.addAll(it.result.data)
                        binding.spLoading.visibility = View.GONE
                        it.result.data.map {
                            dataListVideoFilter.add(false)
                        }
                        binding.rvVideoClass.adapter?.notifyDataSetChanged()
                    }
                }
            } else{
                if (isScrolling==false){

                    Log.d("theTextView","response filter error called")
                    showErrorTextView= true
                    binding.tvAllAccessError.text =  it.serverResponse.message
                    binding.tvAllAccessError.visibility = View.VISIBLE
                    binding.rvVideoClass.visibility = View.GONE
                    getClassCatagoryByIdResponseListFilter.clear()
                    binding.spLoading.visibility = View.GONE
//                    setAdapterForFilterData()
                }else{
                    binding.spLoading.visibility = View.GONE
                    binding.tvAllAccessError.visibility = View.GONE
                }

            }





          Log.d("dataChecking","the data is added filter")
          Log.d("getData2","the data is ${it.result.data} and filterList is $getClassCatagoryByIdResponseListFilter")
      })



        viewModel.errorMessage2.observe(viewLifecycleOwner, Observer {

        })


        viewModel.getProgramsResponseList.observe(viewLifecycleOwner, Observer {

            Log.d("getData2","the data is ${it.result.data}")
            binding.spLoading.visibility = View.GONE
            getProgramsList = it.result.data
            setProgramsAdapter()

        })


        viewModel.errorMessage3.observe(viewLifecycleOwner, Observer {

        })


        viewModel.getAllAccessCatagoryResponse.observe(viewLifecycleOwner, Observer {
            if (it.serverResponse.statusCode==200){
                getAllAccessCatagoryList = it.result!!.data
                getAllAcessCatgoryAdapter()
                binding.tvAllAccessError.visibility = View.INVISIBLE
            } else{
                showErrorTextView= true
                binding.tvAllAccessError.text =  it.serverResponse.message
                binding.tvAllAccessError.visibility = View.VISIBLE
            }
        })

        viewModel.errorMessage4.observe(viewLifecycleOwner, Observer {

        })

        viewModel.getProgramByIdResponse.observe(viewLifecycleOwner, Observer {

            if (it.serverResponse.statusCode==200){
                getProgramByIdResponse = it.result!!.data
                BaseResponseDataObject.getProgramByIdResponse = getProgramByIdResponse
                Log.d("getProgramByidresponse","prgrams id response is $getProgramByIdResponse")
                binding.spLoading.visibility = View.GONE
                val intent =  Intent(requireActivity(), ProgramsCoverActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left
                )
                binding.tvAllAccessError.visibility = View.INVISIBLE
            } else{
                binding.spLoading.visibility = View.GONE
                binding.tvAllAccessError.visibility = View.VISIBLE
            }

        })

        viewModel.errorMessage5.observe(viewLifecycleOwner, Observer {


        })


        viewModel.classDetailsResponse.observe(viewLifecycleOwner, Observer {
            Log.d("getData255","the data size is ${it.result.data.isSave}")

                getClassDetailsList = it.result!!.data
                BaseResponseDataObject.getClassDetailsResponse=getClassDetailsList
                binding.spLoading.visibility = View.GONE
                val intent =  Intent(requireActivity(), ClassesCoverActivity::class.java)
               intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left
                )
                binding.tvAllAccessError.visibility = View.INVISIBLE
        })


        viewModel.errorMessage6.observe(viewLifecycleOwner, Observer {

        })

        viewModel.getAllAccessFilterResponse.observe(viewLifecycleOwner, Observer {
                getAllAccessFilterResponseList = it.result!!.data
                BaseResponseDataObject.getAllAccessFilterResponse=getAllAccessFilterResponseList
                binding.spLoading.visibility = View.GONE
                val intent =  Intent(requireActivity(), ShowAllActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left
                )
        })


        viewModel.errorMessage7.observe(viewLifecycleOwner, Observer {
            binding.spLoading.visibility = View.GONE

        })

    }

}