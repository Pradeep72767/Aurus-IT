package com.example.testing.data.model

data class FnbItem(
    val itemId: String,
    val itemName: String,
    val itemImageURL: String,
    val itemRate: Double,
    val itemOfferRate: Double,
    val foodType: String,
    val isComboAvailable: Boolean,
    val comboListItems: List<ComboList>,
    val isAddOnAvailable: Boolean,
    val addOnItems: List<Any>,
    val itemCategory: String,
    val isPopuplarItem: Boolean,
    val isRepeat: Boolean,
    val calories: String,
    val itemWeight: String

)
