package com.litmethod.android.models.GetCustomers

import com.litmethod.android.models.SignIn.AccessToken

data class ProfileDetails(
    val accessToken: AccessToken,
    val billing_city: String,
    val billing_country: String,
    val billing_state: String,
    val countryCode: String,
    val dialCode: String,
    val dob: String,
    val email: String,
    val equipment: List<Equipment>,
    val firstName: String,
    val gender: String,
    val goal: List<Goal>,
    val has_injury: String,
    val has_subscription: Boolean,
    val heightUnit: String,
    val heightValueFeet: Int,
    val heightValueInches: Int,
    val injury: List<Injury>,
    val interest: List<Interest>,
    val is_app_purchase: Boolean,
    val lastName: String,
    val level: List<Level>,
    val musclegroup: List<Any>,
    val next_payment_date: String,
    val onbordingStatus: Boolean,
    val originalTransactionId: String,
    val profileImage: String,
    val registered_date: String,
    val trialDate: String,
    val userAlias: UserAlias,
    val user_copun_code: String,
    val user_id: String,
    val username: String,
    val uuid: String,
    val weight: Int,
    val weightUnit: String
)