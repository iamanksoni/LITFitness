package com.litmethod.android.models.SignIn

data class UserAlias(
    val EquipmentOwnershipLSM: Boolean,
    val EquipmentOwnershipLSMaccessories: Boolean,
    val EquipmentOwnershipnon: Boolean,
    val EquipmentOwnershipnonLSM: Boolean,
    val email: String,
    val name: String,
    val subscriptionStatus: String
)