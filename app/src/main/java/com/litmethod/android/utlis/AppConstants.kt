package com.litmethod.android.utlis

interface AppConstants {
    companion object {
        var recyclerview_horizantolspace: Int = 0
        const val baseUrlOld: String = "https://api.litmethod.com/app/"
        const val baseUrl: String = "https://dev.api.litmethod.com/app/"
        const val OSType: String = "Android"
        const val actionTypeSignIn: String = "signIn"
        const val actionTypeSignUp: String = "signUp"
        const val actionTypegetCatagoryLIst: String = "getCategory"
        const val actionTypegetClassCatagoryById: String = "getClassBycategoryId"
        const val actionTypegetPrograms: String = "recommendProgram"
        const val actionTypegetAllAccessCategory: String = "getAllAccessCategory"
        const val actionTypegetProgramsById: String = "getProgramById"
        const val actionTypegetClassDetails: String = "classDetails"
        const val actionTypegetAllAccessFilterRequest: String = "getAllAccessfilter"
        const val ALLTIME = "all_time"
        const val PASTMONTH = "past_month"
        const val THREEMONTH = "three_month"
        const val SIXMONTH = "six_month"
        const val PASTWEEK = "last_week"
        const val TODAY = "today"
        const val heart_rate = "AVG. HEART RATE"
        const val calories = "TOTAL KCAL"
        const val distance = "DISTANCE (M)"
        const val total_force = "TOTAL FORCE (LBS)"
        const val total_reps = "TOTAL REPS"
        const val time_under_tension = "TIME UNDER TENSION"
        const val total_time = "TOTAL TIME"
        const val getCustomer = "getCustomer"
        const val beginner = "beginner"
        const val intermediate = "intermediate"
        const val advanced = "advanced"
        const val AUTH_TOKEN = "AUTHTOKEN"
        const val APP_HEADER = "Content-Type: application/json"
        const val API_END_POINT = "/app/action.php"
        const val beginner_value = 0
        const val intermediate_value = 1
        const val advanced_value = 2
        const val MULTIPART_FORM_DATA = "multipart/form-data"
        const val IMAGE = "image"
        const val TEXT_PLAIN = "text/plain"
        const val AVATAR_IMAGE = "avtarImage"
        const val ACTION_GET_CALENDAR_TRACK = "getcalanderTrack"
        const val ACTION_FORGOT_PASSWORD: String = "forgetPassword"
        const val ACTION_BOOK_MARK_CLASS: String = "bookmarkClass"
        const val ACTION_GET_CLASS_STATISTICS: String = "getClassStatistics"
        const val ACTION_GET_ACHIEVEMENTS_CLASS: String = "getAchievementsClass"
        const val ACTION_GET_ACHIEVEMENTS_DAYILY_STREAK: String = "getAchievementsdayStreak"
        const val ACTION_GET_ACHIEVEMENTS_WEEKLY_STREAK: String = "getAchievementsweeklyStreak"
        const val ACTION_GET_CALORIES: String = "getCalories"
        const val ACTION_GET_WEIGHT: String = "getLbs"
        const val USER_TOKEN: String = "userToken"

        const val DEVICE_LIT_AXIS="lit_axis"
        const val DEVICE_HEART_RATE="heart_rate"
        const val DEVICE_STRENGTH_MACHINE="strength_machine"
        const val DEVICE_NAME="device_name"
        const val LIT_AXIS_DEVICE_ID="cl8etkso120560i5p08mc6rq08"
        const val LIT_STRENGTH_DEVICE_ID="cl8etjfrr20509i5p09s7975ox"

        const val WEB_URL="web_url"
        const val URL_TERMS_CONDITION="https://litmethod.com/policies/terms-of-service"
        const val URL_PRIVACY_POLICY="https://litmethod.com/policies/privacy-policy"
        const val URL_SUPPORT="https://litmethod.com/pages/faq"

    }
}