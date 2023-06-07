package com.codegeniuses.estetikin.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleResponse(
    val data: List<ModuleItem?>,
    val error: Boolean,
    val status: String
) : Parcelable