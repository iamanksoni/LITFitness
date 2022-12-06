package com.litmethod.android.models.GetClassCatagoryById

data class GetClassCatagoryByIDRequest(
    val action: String,
    val pageNo: Int,
    val termid: String,
    val typeEquipment: List<String>,
    val typeInstructor: List<String>,
    val typeMuscleGroup: List<String>,
    val typeduration: List<String>,
    val typelevel: List<String>,
    val typeAccessories:List<String>,
    val bookmark: String,
    val takenbyme: String

)