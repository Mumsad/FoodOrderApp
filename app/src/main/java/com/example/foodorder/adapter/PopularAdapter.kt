package com.example.foodorder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorder.DetailsActivity
import com.example.foodorder.databinding.PopularItemsBinding
import com.example.foodorder.models.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class PopularAdapter(
    private val items: List<String>,
    private val price: List<String>,
    private val image: List<Int>,
    private val requireContext: Context
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularAdapter.PopularViewHolder {
        return PopularViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularAdapter.PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item, price, images)
        holder.itemView.setOnClickListener {
            // set On Click Listener to Open Details
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", item)
            intent.putExtra("MenuItemImage", images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(private val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imageViewPopularItems
        fun bind(item: String, price: String, images: Int) {
            binding.foodNamePopularItems.text = item
            binding.PricePopularItems.text = price
            imageView.setImageResource(images)
        }

    }


}