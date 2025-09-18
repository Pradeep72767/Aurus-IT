package com.example.testing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.model.CartItem
import com.example.testing.data.model.FnbItem
import com.example.testing.utils.ImageUtil

class CartAdapter(
    private var items: MutableList<CartItem>,
    private val onAdd: (item: FnbItem) -> Unit,
    private val onRemove: (item: FnbItem) -> Unit
) : RecyclerView.Adapter<CartViewHolder>() {



  /*  fun submitList(list: List<CartItem>) {
        cartItems = list.toMutableList()
        notifyDataSetChanged()
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]
        val item = cartItem.item

        holder.tvName.text = item.itemName
        holder.tvQuantity.text = cartItem.quantity.toString()
        holder.ivFood.setImageBitmap(ImageUtil.decodeBase64ToBitmap(item.itemImageURL))
        holder.ivFoodType.setImageResource(
            if (item.foodType.equals("Veg", true)) R.drawable.veg_icon
            else R.drawable.nonveg_icon
        )

        holder.btnPlus.setOnClickListener {
            onAdd(item)
        }

        holder.btnMinus.setOnClickListener {
            onRemove(item)
        }
    }
}

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivFood: ImageView = itemView.findViewById(R.id.ivFood)
    val tvName: TextView = itemView.findViewById(R.id.tvName)
    val ivFoodType: ImageView = itemView.findViewById(R.id.ivFoodType)
    val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
    val btnPlus: TextView = itemView.findViewById(R.id.btnPlus)
    val btnMinus: TextView = itemView.findViewById(R.id.btnMinus)
}
