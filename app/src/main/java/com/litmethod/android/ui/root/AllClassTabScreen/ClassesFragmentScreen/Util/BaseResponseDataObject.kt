package com.litmethod.android.ui.root.AllClassTabScreen.ClassesFragmentScreen.Util

import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.VideoType
import com.litmethod.android.models.ClassDetails.Data6
import com.litmethod.android.models.GetAllAccessFilter.Data7
import com.litmethod.android.models.GetCustomers.ProfileDetails
import com.litmethod.android.models.GetInstructorInfo.Data8
import com.litmethod.android.models.GetProgramById.Data5
import com.litmethod.android.models.VideoXXX

object BaseResponseDataObject {
    var hasSubscription: Boolean = false
    lateinit var getProgramByIdResponse: Data5
    lateinit var getProgramsByIdToNextScreen: GetProgramsByIdToNextScreen
    lateinit var getClassDetailsResponse: Data6
    lateinit var getAllAccessFilterResponse: List<Data7>
    lateinit var getInstructorInfoResponse: Data8
    var accessToken = ""
    var filterToClaassFragment:Boolean= false
    var level: ArrayList<String> = ArrayList<String>()
    var duration: ArrayList<String> = ArrayList<String>()
    var equipMent: ArrayList<String> = ArrayList<String>()
    var accessorries: ArrayList<String> = ArrayList<String>()
    var instrurtor: ArrayList<String> = ArrayList<String>()
    var muscleGroup: ArrayList<String> = ArrayList<String>()
    var termId =""
    var onResumeViewModel = false
    var checkActivityData = "filterActivity"
    var token = ""
    var isFilter =false
    lateinit var profilePageData:ProfileDetails
    var getClassStatisticsList: MutableList<VideoType> = ArrayList<VideoType>()
    var getClassStatisticsListAll: MutableList<VideoType> = ArrayList<VideoType>()
    var getClassStatisticsListNew: MutableList<VideoType> = ArrayList<VideoType>()
    var getVideosForShowAllInHome: MutableList<VideoXXX> = ArrayList<VideoXXX>()
}