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
import com.litmethod.android.models.AcountScreenFragment.GetCalanderTrack.GetCalenderRequest
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
import com.litmethod.android.utlis.AppConstants.Companion.API_END_POINT
import com.litmethod.android.utlis.AppConstants.Companion.APP_HEADER
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitDataSourceService {

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun signInUser(@Body signInRequest: SignInRequest): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun signUpUser(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getInjury(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<InjuryResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getYourGoals(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetYourGoalResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getInterest(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetInterestResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getEquipment(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetEquipMentResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getLevel(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest): Call<GetLevelResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun editUser(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body editUserRequest: EditUserRequest
    ): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @Multipart
    @POST("/me/setProfileImage")
    fun setImage(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Part file: MultipartBody.Part,
        @Part action: RequestBody
    ): Call<SetImageResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getCategory(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getCatagoryRequest: GetCatagoryRequest
    ): Call<GetCatagoryResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getClassCategoryById(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getClassCatagoryByIDRequest: GetClassCatagoryByIDRequest
    ): Call<GetClassCatagoryByIdResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getPrograms(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getProgramsRequest: GetProgramsRequest
    ): Call<GetProgramsResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAllAccessCatagory(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getAllAccessCatagoryRequest: GetAllAccessCatagoryRequest
    ): Call<GetAllCatagoryResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getProgramsById(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getProgramByIdRequest: GetProgramByIdRequest
    ): Call<GetProgramByIdResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getClassDetails(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body classDetailsRequest: ClassDetailsRequest
    ): Call<ClassDetailsResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAllAccessFilter(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getAllAccessFilterRequest: GetAllAccessFilterRequest
    ): Call<GetAllAccessFilterResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getHome(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetHomeResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getUserAnalytics(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body userAnalyticsRequest: UserAnalyticsRequest
    ): Call<UserAnalyticsResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAchievementsClass(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetAchievementsClassResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAchievementsdayStreak(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetAchievementsdayStreakResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAchievementsweeklyStreak(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetAchievementsweeklyStreakResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getCalories(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetCaloriesResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getLbs(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body injuryRequest: InjuryRequest
    ): Call<GetLbsResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getUserAnalycticsDetiles(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getUserAnalycticsDetilesRequest: GetUserAnalycticsDetilesRequest
    ): Call<GetUserAnalycticsDetilesResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getCustomer(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getCutomerRequest: GetCutomerRequest
    ): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getInstructorInfo(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getInstructorInfoRequest: GetInstructorInfoRequest): Call<GetInstructorInfoResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getFilterList(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body filterListRequest: FilterListRequest): Call<FilterListResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun editUserNullable(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body editUserRequestNullable: EditUserRequestNullable
    ): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun editUserForGoal(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body editUserGoalRequest: EditUserGoalRequest): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun editUserForInterest(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body editUserInterestRequest: EditUserInterestRequest): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun editUserForEquipment(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body editUserInterestRequest: EditUserEquipmentRequest): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun editUserForLevel(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body editUserLevelRequest: EditUserLevelRequest): Call<GetCustomerResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getBookmarkClass(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body bookmarkClassRequest: BookmarkClassRequest): Call<BookmarkClassResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getClassStatitstics(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getClassStatisticsRequest: GetClassStatisticsRequest): Call<GetClassStatisticsResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAchievementsClass(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getAchieveMentRequest: GetAchieveMentRequest): Call<GetAchievementResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAchievementsdayStreak(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getAchievementsdayStreakRequest: GetAchievementsdayStreakRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetAchievementsdayStreak.GetAchievementsdayStreakResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getAchievementsweeklyStreak(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getAchievementsweeklyStreakRequest: GetAchievementsweeklyStreakRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetAchievementsweeklyStreak.GetAchievementsweeklyStreakResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getCalories(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getCaloriesRequest: GetCaloriesRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetCalories.GetCaloriesResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getLbs(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body getLbsRequest: GetLbsRequest): Call<com.litmethod.android.models.AcountScreenFragment.GetLbs.GetLbsResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getClassBookmark(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body classBookmarkRequest: ClassBookmarkRequest): Call<ClassBookmarkResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getCalenderTrack(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body classBookmarkRequest: GetCalenderRequest): Call<GetCalanderResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun logOut(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body logOutRequest: LogOutRequest): Call<LogOutResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getLiveClass(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body logOutRequest: LogOutRequest): Call<LiveClassResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun getClassHistoryList(
        @Header(AppConstants.AUTH_TOKEN) authorizationKey: String,
        @Body classHistoryListRequest: ClassHistoryListRequest): Call<ClassHistoryListResponse>

    @Headers(APP_HEADER)
    @POST(API_END_POINT)
    fun forgetPasswordRequest(
        @Body forgetPasswordRequest: ForgetPasswordRequest
    ): Call<ForgetPasswordResponse>

    companion object {
        var retrofitService: RetrofitDataSourceService? = null
        fun getInstance(): RetrofitDataSourceService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(AppConstants.baseUrlOld)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(ApiWorker.client)
                    .build()
                retrofitService = retrofit.create(RetrofitDataSourceService::class.java)
            }
            return retrofitService!!
        }
    }
}
