package com.litmethod.android.utlis

interface AppConstants {
    companion object {
        var recyclerview_horizantolspace: Int = 0
        var baseUrlOld: String = "https://api.litmethod.com/app/"
        var baseUrl: String = "https://dev.api.litmethod.com/app/"
        var OSType: String = "Android"
        var actionTypeSignIn: String = "signIn"
        var actionTypeSignUp: String = "signUp"
        var actionTypegetCatagoryLIst: String = "getCategory"
        var actionTypegetClassCatagoryById: String = "getClassBycategoryId"
        var actionTypegetPrograms: String = "recommendProgram"
        var actionTypegetAllAccessCategory: String = "getAllAccessCategory"
        var actionTypegetProgramsById: String = "getProgramById"
        var actionTypegetClassDetails: String = "classDetails"
        var actionTypegetAllAccessFilterRequest: String = "getAllAccessfilter"
        var ALLTIME = "all_time"
        var PASTMONTH = "past_month"
        var PASTWEEK = "last_week"
        var TODAY = "today"
        var heart_rate = "AVG. HEART RATE"
        var calories = "TOTAL KCAL"
        var distance = "DISTANCE (M)"
        var total_force = "TOTAL FORCE (LBS)"
        var total_reps = "TOTAL REPS"
        var time_under_tension = "TIME UNDER TENSION"
        var total_time = "TOTAL TIME"
        var getCustomer = "getCustomer"
        var beginner = "beginner"
        var intermediate = "intermediate"
        var advanced = "advanced"
        const val AUTH_TOKEN = "AUTHTOKEN"
        const val APP_HEADER = "Content-Type: application/json"
        const val API_END_POINT = "/app/action.php"
        var beginner_value = 0
        var intermediate_value = 1
        var advanced_value = 2


        var MULTIPART_FORM_DATA = "multipart/form-data"
        var IMAGE = "image"
        var TEXT_PLAIN = "text/plain"
        var AVATAR_IMAGE = "avtarImage"
        var ACTION_GET_CALENDAR_TRACK = "getcalanderTrack"
        var ACTION_FORGOT_PASSWORD: String = "forgetPassword"
        var ACTION_BOOK_MARK_CLASS: String = "bookmarkClass"
        var ACTION_GET_CLASS_STATISTICS: String = "getClassStatistics"
        var ACTION_GET_ACHIEVEMENTS_CLASS: String = "getAchievementsClass"
        var ACTION_GET_ACHIEVEMENTS_DAYILY_STREAK: String = "getAchievementsdayStreak"
        var ACTION_GET_ACHIEVEMENTS_WEEKLY_STREAK: String = "getAchievementsweeklyStreak"
        var ACTION_GET_CALORIES: String = "getCalories"
        var ACTION_GET_WEIGHT: String = "getLbs"

        const val DEVICE_LIT_AXIS="lit_axis"
        const val DEVICE_HEART_RATE="heart_rate"
        const val DEVICE_STRENGTH_MACHINE="strength_machine"
        const val DEVICE_NAME="device_name"
        const val LIT_AXIS_DEVICE_ID="cl8etkso120560i5p08mc6rq08"
        const val LIT_STRENGTH_DEVICE_ID="cl8etjfrr20509i5p09s7975ox"

    }
}