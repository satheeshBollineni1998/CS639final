package com.example.myapplication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FoodItemAdapter : RecyclerView.Adapter<FoodItemAdapter.ViewHolder>() {

    private var foodItems: List<FoodItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.bind(foodItem)
    }

    override fun getItemCount(): Int {
        return foodItems.size
    }

    fun setFoodItems(foodItems: List<FoodItem>) {
        this.foodItems = foodItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val caloriesTextView: TextView = itemView.findViewById(R.id.caloriesTextView)
        private val proteinTextView: TextView = itemView.findViewById(R.id.proteinTextView)
        private val carbsTextView: TextView = itemView.findViewById(R.id.carbsTextView)
        private val fatTextView: TextView = itemView.findViewById(R.id.fatTextView)

        fun bind(foodItem: FoodItem) {
            nameTextView.text = foodItem.name
            caloriesTextView.text = "Calories: ${foodItem.calories}"
            foodItem.macros?.let { macros ->
                proteinTextView.text = "Protein: ${macros.protein}"
                carbsTextView.text = "Carbs: ${macros.carbs}"
                fatTextView.text = "Fat: ${macros.fat}"
            }
            Picasso.get().load(foodItem.imageUrl).placeholder(R.drawable.placeholder_image).into(imageView)

        }
    }
}
