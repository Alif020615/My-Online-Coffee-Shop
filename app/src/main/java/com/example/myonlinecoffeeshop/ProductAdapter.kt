package com.example.myonlinecoffeeshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val productList: List<Product>,  // List of products to display
    private val onBuyClick: (Product) -> Unit  // Callback for adding to the cart
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.name
        holder.productPrice.text = "RM ${product.price}"
        holder.productImage.setImageResource(product.imageResId)
        holder.productQuantity.text = "Quantity: ${product.quantity}"

        // Set the onClickListener for the Add button
        holder.buyButton.setOnClickListener {
            product.quantity++  // Increase the quantity of the product
            notifyItemChanged(position)  // Notify the adapter to update the specific item
            onBuyClick(product)  // Trigger the callback to add the product to the cart or another action
        }
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)  // Quantity TextView
        val buyButton: Button = itemView.findViewById(R.id.buy_button)  // Add button
    }
}