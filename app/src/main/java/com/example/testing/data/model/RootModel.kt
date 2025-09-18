package com.example.testing.data.model

data class RootModel(
    val status: Int,
    val msg: String,
    val isPopularAvailable: Boolean,
    val isRepeatAvailable: Boolean,
    val listOfFnbItems: List<FnbItem>,
    val cinemaDetails: List<CinemaDetail>
)
