package com.example.testing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.model.FnbItem
import com.example.testing.utils.ImageUtil

class InoxFnbAdapter(
    private var items: MutableList<FnbItem>,
    private val onAddClicked: (FnbItem) -> Unit,
    private val onQuantityChange: (itemId: String, delta: Int) -> Unit
): RecyclerView.Adapter<ViewHolderFnbMain>() {

    private val quantities = mutableMapOf<String, Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFnbMain {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repeat_items_layout, parent, false)
        return ViewHolderFnbMain(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderFnbMain, position: Int) {
        val item = items[position]
        val qty = quantities[item.itemId] ?: 0

        holder.productName.text = item.itemName
        holder.price.text = "₹${item.itemRate.toInt()}"
        holder.foodType.setImageResource(
            if (item.foodType.equals("Veg", true))
                R.drawable.veg_icon
            else
                R.drawable.nonveg_icon
        )

        holder.btnAdd.text = "Add"
        holder.btnAdd.setOnClickListener {
            val newQty = 1
            quantities[item.itemId] = newQty
            holder.btnAdd.visibility = View.GONE
            holder.layoutQty.visibility = View.VISIBLE
            holder.tvQuantity.text = newQty.toString()

            onAddClicked(item)  // add to cart first time
            onQuantityChange(item.itemId, newQty)
        }

        if (qty == 0) {

            holder.btnAdd.visibility = View.VISIBLE
            holder.layoutQty.visibility = View.GONE
        } else {
            holder.btnAdd.visibility = View.GONE
            holder.layoutQty.visibility = View.VISIBLE
            holder.tvQuantity.text = qty.toString()
        }

        holder.btnPlus.setOnClickListener {
            val newQty = (quantities[item.itemName] ?: 0) + 1
            quantities[item.itemName] = newQty
            holder.tvQuantity.text = newQty.toString()
            onQuantityChange(item.itemId, newQty)

        }

        holder.btnMinus.setOnClickListener {
            val newQty = (quantities[item.itemName] ?: 0) - 1
            if (newQty > 0) {
                quantities[item.itemName] = newQty
                holder.tvQuantity.text = newQty.toString()
                onQuantityChange(item.itemId, newQty)
            } else {
                quantities[item.itemName] = 0
                holder.btnAdd.visibility = View.VISIBLE
                holder.layoutQty.visibility = View.GONE
                onQuantityChange(item.itemId, 0)
            }


        }

        val bmp = ImageUtil.decodeBase64ToBitmap(item.itemImageURL)
        if (bmp != null) {
            holder.foodImage.setImageBitmap(bmp)
        } else {

        }

    }
}

class ViewHolderFnbMain(view:View): RecyclerView.ViewHolder(view)  {

    val foodImage: ImageView = view.findViewById(R.id.ivItem)
    val productName: TextView = view.findViewById(R.id.tvName)
    val price: TextView = view.findViewById(R.id.tvPrice)
    val foodType: ImageView = view.findViewById(R.id.ivFoodType)
    val btnAdd: Button = view.findViewById(R.id.btnAdd)

    val layoutQty: LinearLayout = view.findViewById(R.id.layoutQty)
    val btnMinus: TextView = view.findViewById(R.id.btnMinus)
    val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
    val btnPlus: TextView = view.findViewById(R.id.btnPlus)
}
