package com.example.testing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.R
import com.example.testing.data.model.ComboList

class ComboAdapter(private val combos: List<ComboList>) :
    RecyclerView.Adapter<ComboViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_combo, parent, false)
        return ComboViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComboViewHolder, position: Int) {
        val comboItem = combos[position]
        holder.tvComboName.text =  comboItem.itemName
        holder.tvComboPrice.text =  comboItem.itemRate.toString()

    }

    override fun getItemCount(): Int = combos.size


}

class ComboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvComboName: TextView = itemView.findViewById(R.id.tvComboName)
    val tvComboPrice: TextView = itemView.findViewById(R.id.tvComboPrice)

    val btnAdd :Button = itemView.findViewById(R.id.btnAdd)

}