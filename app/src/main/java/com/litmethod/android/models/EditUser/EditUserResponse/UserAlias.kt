package com.litmethod.android.models.EditUser.EditUserResponse

data class UserAlias(
    val EquipmentOwnershipLSM: Boolean,
    val EquipmentOwnershipLSMaccessories: Boolean,
    val EquipmentOwnershipnon: Boolean,
    val EquipmentOwnershipnonLSM: Boolean,
    val email: Any,
    val name: String,
    val subscriptionStatus: String
)