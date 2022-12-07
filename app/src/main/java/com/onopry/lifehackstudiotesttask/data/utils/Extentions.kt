package com.onopry.lifehackstudiotesttask.data.utils

fun coordinatesIsNotBlank(lat: Double?, lon: Double?) =
    lat != null && lon != null && lat != 0.0 && lon != 0.0