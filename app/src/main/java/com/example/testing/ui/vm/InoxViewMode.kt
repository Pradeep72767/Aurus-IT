package com.example.testing.ui.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testing.data.model.CartItem
import com.example.testing.data.model.FnbItem
import com.example.testing.data.model.RootModel
import com.example.testing.data.repository.InoxRepo
import kotlin.math.truncate

class InoxViewMode(private val repository: InoxRepo) :ViewModel() {
    private val _fnbData = MutableLiveData<RootModel>()
    val fnbData: LiveData<RootModel> get() = _fnbData

    private val _cart = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cart: LiveData<MutableList<CartItem>> = _cart

    private val _filters = MutableLiveData<Set<String>>(emptySet())
    val filters: LiveData<Set<String>> = _filters

    private val _allItems = MutableLiveData<MutableList<FnbItem>>(mutableListOf())
    val allItems: LiveData<MutableList<FnbItem>> = _allItems

    private val _filteredItems = MutableLiveData<MutableList<FnbItem>>()
    val filteredItems: LiveData<MutableList<FnbItem>> = _filteredItems



    fun fetchData() {
        _fnbData.value = repository.getFoodDetails()
    }


    fun addToCart(item: FnbItem) {
        val list = _cart.value ?: mutableListOf()
        val found = list.find { it.itemId == item.itemId }
        if (found != null) {
            found.quantity++
        } else {
            list.add(CartItem(item.itemId, item, 1))
        }
        _cart.value = list.toMutableList()
        Log.d("Cart", "Added: ${item.itemName}, Cart: $list")
    }

    fun removeFromCart(item: FnbItem) {
        val list = _cart.value ?: mutableListOf()
        val found = list.find { it.itemId == item.itemId }
        if (found != null) {
            if (found.quantity > 1) {
                found.quantity--
            } else {
                list.remove(found)
            }
        }
        _cart.value = list.toMutableList()
    }


    fun setFilterSet(filters: Set<String>) {
        _filters.value = filters
        applyFilters()
    }


    private fun applyFilters() {
        val items = _fnbData.value
        val fnbData  = items?.listOfFnbItems
        val filters = _filters.value.orEmpty()
        if (filters.isEmpty()) {
            _filteredItems.value = fnbData!!.toMutableList()
            return
        }
        val filtered = fnbData?.filter { item ->
            filters.all { filter ->
                when (filter.uppercase()) {
                    "VEG" -> item.foodType.equals("Veg", true)
                    "NON-VEG" -> item.foodType.equals("NonVeg", true)
                    "BEST SELLERS" -> item.isPopuplarItem
                    "SNACKS" ->item.itemCategory.equals(filter, true)
                    "COMBOS" ->item.itemCategory.equals(filter, true)
                    "COLD BEVERAGES" ->item.itemCategory.equals(filter, true)
                    "HOT BEVERAGES" ->item.itemCategory.equals(filter, true)
                    else -> true
                }
            }
        }
        _filteredItems.value = filtered?.toMutableList()
    }



}