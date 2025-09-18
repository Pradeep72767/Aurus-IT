package com.example.testing.data.model

data class CartItem(
    val itemId: String,
    val item: FnbItem,
    var quantity: Int = 1
)

data class ChipItem(
    val offer :String,
    val coupan :String
)
