package com.litmethod.android.models.GetAllAccessFilter

data class GetAllAccessFilterRequest(
    val action: String,
    val all_acess_category: String,
    val isExerciseGlossary: Boolean,
    val typeEquipment: String,
    val typeGoal: String,
    val typeInstructor: String,
    val typeMuscleGroup: String,
    val typeduration: String,
    val typelevel: String
)