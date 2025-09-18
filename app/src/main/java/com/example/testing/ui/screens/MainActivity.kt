package com.example.testing.ui.screens

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.testing.R
import com.example.testing.data.model.ChipItem
import com.example.testing.data.model.FnbItem
import com.example.testing.data.model.RootModel
import com.example.testing.data.repository.InoxRepo
import com.example.testing.databinding.ActivityMainBinding
import com.example.testing.databinding.BottomSheetCartBinding
import com.example.testing.ui.adapter.AllProductAdapter
import com.example.testing.ui.adapter.CartAdapter
import com.example.testing.ui.adapter.InoxFnbAdapter
import com.example.testing.ui.vm.InoxViewMode
import com.example.testing.utils.GenericViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: InoxViewMode
    private lateinit var pager: ViewPager2
    private lateinit var recommndedPager: ViewPager2

    private lateinit var mainAdapter: InoxFnbAdapter
    private lateinit var allProductAdapter: AllProductAdapter

    private lateinit var mainAdapterRecommnded: InoxFnbAdapter
    private lateinit var offers: ChipGroup
    private lateinit var filterChipGroup: ChipGroup
    private lateinit var rvAllProduct: RecyclerView
    private lateinit var fabCart: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        viewModel.fnbData.observe(this, fnbObserver)
        viewModel.fetchData()

        initView()


    }

    private fun initViewModel() {

        val objectRepository = InoxRepo(this@MainActivity)
        val viewModelFactory = GenericViewModelFactory { InoxViewMode(objectRepository) }
        viewModel = ViewModelProvider(this, viewModelFactory)[InoxViewMode::class.java]
    }

    private fun initView() {
        pager = binding.repeaterRecyclerview
        offers = binding.chipGroup
        recommndedPager = binding.recomndedPager
        filterChipGroup = binding.filterChipGroup
        rvAllProduct = binding.allProduct
        fabCart = binding.fabCart

        setUpOfferChip()
        setupChip()

        fabCart.setOnClickListener {
            showCartBottomSheet()
        }

       /* viewModel.filteredItems.observe(this) { filteredList ->
            allProductAdapter.updateData(filteredList)
        }*/


    }

    private val fnbObserver =
        Observer<RootModel> { result ->
            Log.d("TAG", "pradeep: $result")

            val fnbItemShow = mutableListOf<FnbItem>()
            val fnbItemShowPopular = mutableListOf<FnbItem>()
            val allProdust = mutableListOf<FnbItem>()
            for (item in result.listOfFnbItems) {
                if (item.isRepeat) {
                    fnbItemShow.add(item)
                } else if (item.isPopuplarItem) {
                    fnbItemShowPopular.add(item)
                }
                allProdust.add(item)
            }


            mainAdapter =
                InoxFnbAdapter(
                    fnbItemShow,
                    onAddClicked = { item -> viewModel.addToCart(item) },
                    onQuantityChange = { itemId, delta ->
                        val item = fnbItemShow.find { it.itemId == itemId }
                        item?.let {
                            if (delta > 0) {
                                viewModel.addToCart(it)
                            } else {
                                viewModel.removeFromCart(it)
                            }
                        }
                    }

                )
            pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            pager.adapter = mainAdapter

            mainAdapterRecommnded = InoxFnbAdapter(
                fnbItemShowPopular,
                onAddClicked = { item -> viewModel.addToCart(item) },
                onQuantityChange = { itemId, delta ->
                    val item = fnbItemShow.find { it.itemId == itemId }
                    item?.let {
                        if (delta > 0) {
                            viewModel.addToCart(it)
                        } else {
                            viewModel.removeFromCart(it)
                        }
                    }
                }
            )
            recommndedPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            recommndedPager.adapter = mainAdapterRecommnded

            allProductAdapter =
                AllProductAdapter(
                    allProdust,
                    onAddClicked = { item -> viewModel.addToCart(item) },
                    onQuantityChange = { itemId, delta ->
                        val item = fnbItemShow.find { it.itemId == itemId }
                        item?.let {
                            if (delta > 0) {
                                viewModel.addToCart(it)
                            } else {
                                viewModel.removeFromCart(it)
                            }
                        }
                    }
                )
            rvAllProduct.layoutManager = LinearLayoutManager(this)
            rvAllProduct.adapter = allProductAdapter

        }


    private fun setupChip() {
        val stringList = listOf(
            "Veg",
            "Non-Veg",
            "Best Sellers",
            "SNACKS",
            "POPCORN",
            "COMBOS",
            "COLD BEVERAGES",
            "HOT BEVERAGES"
        )

        for (text in stringList) {
            val chip = Chip(this).apply {
                this.text = text
                isCheckable = true
                isClickable = true

                setChipBackgroundColorResource(R.color.white)
                ContextCompat.getColorStateList(context, R.color.bg)
                setTextColor(Color.BLACK)
            }
            filterChipGroup.addView(chip)


            chip.setOnCheckedChangeListener { _, _ ->
                val selected = mutableSetOf<String>()

                filterChipGroup.forEach { v ->
                    val c = v as? com.google.android.material.chip.Chip
                    if (c?.isChecked == true) selected.add(c.text.toString())
                }
                viewModel.setFilterSet(selected)
            }
        }
    }

    private fun setUpOfferChip() {
        val chipItems = listOf(
            ChipItem("Get 50% Off up to  140", "Use PVR00001"),
            ChipItem("Get 40% Off up to  110", "Use PVR00002"),
            ChipItem("Get 20% Off up to  50", "Use PVR00003")
        )
        chipItems.forEach { item ->
            val chipView = LayoutInflater.from(this).inflate(R.layout.custom_chip, offers, false)

            val title = chipView.findViewById<TextView>(R.id.tv_offer)
            val subtitle = chipView.findViewById<TextView>(R.id.tv_coupan)

            title.text = item.offer
            subtitle.text = item.coupan

            offers.addView(chipView)
        }
    }

    private fun showCartBottomSheet() {
        val bottomSheet = BottomSheetDialog(this@MainActivity)
        val binding = BottomSheetCartBinding.inflate(layoutInflater)
        bottomSheet.setContentView(binding.root)

        viewModel.cart.observe(this) { cartList ->
            val adapter = CartAdapter(
                cartList,
                onAdd = { item -> viewModel.addToCart(item) },
                onRemove = { item -> viewModel.removeFromCart(item) }
            )
            binding.rvCartItems.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rvCartItems.adapter = adapter

            val total = cartList.sumOf { it.item.itemRate.toInt() * it.quantity }
            binding.tvTotal.text = "₹$total"
        }

        bottomSheet.show()
    }
}