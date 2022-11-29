package com.onopry.lifehackstudiotesttask.domain.model

import com.squareup.moshi.JsonClass

/*По-хорошему это ДТО-шка и не нуждно ее размещать в домене,
* но поскольку приложение небольшое и,
* чтобы не плодить сущности в каждом слое, сделал так */
@JsonClass(generateAdapter = true)
data class CompanyItem(
    val id: String,
    val img: String,
    val name: String
)