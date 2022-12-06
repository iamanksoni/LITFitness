package com.litmethod.android.utlis

interface AppConstants {
    companion object {
        var recyclerview_horizantolspace: Int = 0
        var baseUrlOld: String = "https://litmdev.wpengine.com/app/"
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
        var beginner_value = 0
        var intermediate_value = 1
        var advanced_value = 2
        var forgetPassword: String = "forgetPassword"
    }
}