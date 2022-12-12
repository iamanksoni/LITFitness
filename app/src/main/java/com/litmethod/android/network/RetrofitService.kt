package com.litmethod.android.network

import com.litmethod.android.models.*
import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.BookmarkClassRequest
import com.litmethod.android.models.AcountScreenFragment.BookmarkClass.BookmarkClassResponse
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkRequest
import com.litmethod.android.models.AcountScreenFragment.ClassBookmark.ClassBookmarkResponse
import com.litmethod.android.models.AcountScreenFragment.EditUserEquipment.EditUserEquipmentRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserGoal.EditUserGoalRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserInterest.EditUserInterestRequest
import com.litmethod.android.models.AcountScreenFragment.EditUserLevel.EditUserLevelRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakRequest
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.GetAchieveMentRequest
import com.litmethod.android.models.AcountScreenFragment.GetAcieveMentClass.GetAchievementResponse
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalanderRequest
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalanderResponse
import com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesRequest
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.GetClassStatisticsRequest
import com.litmethod.android.models.AcountScreenFragment.GetClassStatistics.GetClassStatisticsResponse
import com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsRequest
import com.litmethod.android.models.ClassDetails.ClassDetailsResponse
import com.litmethod.android.models.ClassHistoryList.ClassHistoryListRequest
import com.litmethod.android.models.ClassHistoryList.ClassHistoryListResponse
import com.litmethod.android.models.EditUser.EdiitUserRequest.EditUserRequest
import com.litmethod.android.models.EditUserRequestNullable.EditUserRequestNullable
import com.litmethod.android.models.FilterList.FilterListRequest
import com.litmethod.android.models.FilterList.FilterListResponse
import com.litmethod.android.models.ForgetPasswordModel.ForgetPasswordRequest
import com.litmethod.android.models.ForgetPasswordModel.ForgetPasswordResponse
import com.litmethod.android.models.GetAllAccessCatagory.GetAllAccessCatagoryRequest
import com.litmethod.android.models.GetAllAccessCatagory.GetAllCatagoryResponse
import com.litmethod.android.models.GetAllAccessFilter.GetAllAccessFilterRequest
import com.litmethod.android.models.GetAllAccessFilter.GetAllAccessFilterResponse
import com.litmethod.android.models.GetCatagory.GetCatagoryRequest
import com.litmethod.android.models.GetCatagory.GetCatagoryResponse
import com.litmethod.android.models.GetClassCatagoryById.GetClassCatagoryByIDRequest
import com.litmethod.android.models.GetClassCatagoryById.GetClassCatagoryByIdResponse
import com.litmethod.android.models.GetCustomers.GetCustomerResponse
import com.litmethod.android.models.GetCustomers.GetCutomerRequest
import com.litmethod.android.models.GetEquipment.GetEquipMentResponse
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoRequest
import com.litmethod.android.models.GetInstructorInfo.GetInstructorInfoResponse
import com.litmethod.android.models.GetInterest.GetInterestResponse
import com.litmethod.android.models.GetLevel.GetLevelResponse
import com.litmethod.android.models.GetProgram.GetProgramsRequest
import com.litmethod.android.models.GetProgram.GetProgramsResponse
import com.litmethod.android.models.GetProgramById.GetProgramByIdRequest
import com.litmethod.android.models.GetProgramById.GetProgramByIdResponse
import com.litmethod.android.models.GetYourGoalResponse.GetYourGoalResponse
import com.litmethod.android.models.HomePageModels.*
import com.litmethod.android.models.InjuryResponse.InjuryResponse
import com.litmethod.android.models.Liveclass.LiveClassResponse
import com.litmethod.android.models.LogOut.LogOutRequest
import com.litmethod.android.models.LogOut.LogOutResponse
import com.litmethod.android.models.SetImageResponse.SetImageResponse
import com.litmethod.android.models.SignUpResponse.SignUpResponse
import com.litmethod.android.utlis.AppConstants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun signInUser(@Body signInRequest: SignInRequest): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun signUpUser(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getInjury(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<InjuryResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getYourGoals(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetYourGoalResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getInterest(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetInterestResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getEquipment(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetEquipMentResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getLevel(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetLevelResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun editUser(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body editUserRequest: EditUserRequest
    ): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @Multipart
    @POST("/me/setProfileImage")
    fun setImage(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Part file: MultipartBody.Part,
        @Part("action") action: RequestBody
    ): Call<SetImageResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getCategory(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getCatagoryRequest: GetCatagoryRequest
    ): Call<GetCatagoryResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getClassCategoryById(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getClassCatagoryByIDRequest: GetClassCatagoryByIDRequest
    ): Call<GetClassCatagoryByIdResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getPrograms(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getProgramsRequest: GetProgramsRequest
    ): Call<GetProgramsResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAllAccessCatagory(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getAllAccessCatagoryRequest: GetAllAccessCatagoryRequest
    ): Call<GetAllCatagoryResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getProgramsById(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getProgramByIdRequest: GetProgramByIdRequest
    ): Call<GetProgramByIdResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getClassDetails(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body classDetailsRequest: ClassDetailsRequest
    ): Call<ClassDetailsResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAllAccessFilter(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getAllAccessFilterRequest: GetAllAccessFilterRequest
    ): Call<GetAllAccessFilterResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getHome(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetHomeResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getUserAnalytics(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body userAnalyticsRequest: UserAnalyticsRequest
    ): Call<UserAnalyticsResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAchievementsClass(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetAchievementsClassResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAchievementsdayStreak(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetAchievementsdayStreakResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAchievementsweeklyStreak(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetAchievementsweeklyStreakResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getCalories(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetCaloriesResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getLbs(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetLbsResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getUserAnalycticsDetiles(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getUserAnalycticsDetilesRequest: GetUserAnalycticsDetilesRequest
    ): Call<GetUserAnalycticsDetilesResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getCustomer(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getCutomerRequest: GetCutomerRequest
    ): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getInstructorInfo(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getInstructorInfoRequest: GetInstructorInfoRequest): Call<GetInstructorInfoResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getFilterList(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body filterListRequest: FilterListRequest): Call<FilterListResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun editUserNullable(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body editUserRequestNullable: EditUserRequestNullable
    ): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun editUserForGoal(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body editUserGoalRequest: EditUserGoalRequest): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun editUserForInterest(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body editUserInterestRequest: EditUserInterestRequest): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun editUserForEquipment(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body editUserInterestRequest: EditUserEquipmentRequest): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun editUserForLevel(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body editUserLevelRequest: EditUserLevelRequest): Call<GetCustomerResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getBookmarkClass(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body bookmarkClassRequest: BookmarkClassRequest): Call<BookmarkClassResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getClassStatitstics(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getClassStatisticsRequest: GetClassStatisticsRequest): Call<GetClassStatisticsResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAchievementsClass(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getAchieveMentRequest: GetAchieveMentRequest): Call<GetAchievementResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAchievementsdayStreak(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getAchievementsdayStreakRequest: GetAchievementsdayStreakRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getAchievementsweeklyStreak(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getAchievementsweeklyStreakRequest: GetAchievementsweeklyStreakRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getCalories(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getCaloriesRequest: GetCaloriesRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getLbs(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body getLbsRequest: GetLbsRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getClassBookmark(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body classBookmarkRequest: ClassBookmarkRequest): Call<ClassBookmarkResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getCalanderTrack(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body classBookmarkRequest: GetCalanderRequest): Call<GetCalanderResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun logOut(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body logOutRequest: LogOutRequest): Call<LogOutResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getLiveClass(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body logOutRequest: LogOutRequest): Call<LiveClassResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun getClassHistoryList(
        @Header("AUTHTOKEN") authorizationKey: String,
        @Body classHistoryListRequest: ClassHistoryListRequest): Call<ClassHistoryListResponse>

    @Headers("Content-Type: application/json")
    @POST("/app/action.php")
    fun forgetPasswordRequest(
        @Body forgetPasswordRequest: ForgetPasswordRequest
    ): Call<ForgetPasswordResponse>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(AppConstants.baseUrlOld)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(ApiWorker.client)
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}
//"https://api.litmethod.com"