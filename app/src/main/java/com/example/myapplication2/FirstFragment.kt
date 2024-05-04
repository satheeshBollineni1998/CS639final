package com.example.myapplication2
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.navigation.fragment.findNavController


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val meals = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and set adapter
        val adapter = FoodDiaryAdapter(meals)
        binding.recyclerViewFoodDiary.adapter = adapter
        binding.recyclerViewFoodDiary.layoutManager = LinearLayoutManager(requireContext())

        // Example: Add some sample meals
        meals.add("Breakfast")
        meals.add("Lunch")
        meals.add("Dinner")

        // Notify adapter about data changes
        adapter.notifyDataSetChanged()

        // Set click listener for RecyclerView items
        adapter.setOnItemClickListener { mealName ->
            showAddMealDialog(mealName)
        }

        // Set click listener for the "Meal History" button
        // Set click listener for the "Meal History" button
        binding.btnMealHistory.setOnClickListener {
            // Navigate to MealHistoryFragment
            findNavController().navigate(R.id.action_FirstFragment_to_MealHistoryFragment)
        }
        binding.btnMealPlan.setOnClickListener {
            // Navigate to MealHistoryFragment
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    private fun showAddMealDialog(mealName: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_food_dialog, null)
        val inputEditText = dialogView.findViewById<EditText>(R.id.editTextMeal)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Register Meal")
            .setView(dialogView)
            .setPositiveButton("Register") { _, _ ->
                val meal = inputEditText.text.toString().trim()
                if (meal.isNotEmpty()) {
                    registerMeal(mealName, meal)
                } else {
                    Toast.makeText(requireContext(), "Please enter the meal", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.setOnShowListener { dialog ->
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positiveButton.setTextColor(resources.getColor(R.color.green)) // Set text color of "Register" button to green
            negativeButton.setTextColor(resources.getColor(R.color.green)) // Set text color of "Cancel" button to green
        }

        dialog.show()
    }

    private fun registerMeal(mealName: String, meal: String) {
        // Register meal to Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val mealRef = FirebaseDatabase.getInstance().getReference("meals").child(userId)
            val mealId = mealRef.push().key ?: ""
            val mealData = mapOf(
                "mealName" to mealName,
                "meal" to meal
                // Add other meal details here
            )
            mealRef.child(mealId).setValue(mealData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Meal registered successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to register meal", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class FoodDiaryAdapter(private val meals: List<String>) :
        RecyclerView.Adapter<FoodDiaryAdapter.ViewHolder>() {

        private var itemClickListener: ((String) -> Unit)? = null

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mealName: TextView = view.findViewById(R.id.textViewMealName)

            init {
                view.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val meal = meals[position]
                        itemClickListener?.invoke(meal)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.food_dairy_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mealName.text = meals[position]
        }

        override fun getItemCount(): Int {
            return meals.size
        }

        fun setOnItemClickListener(listener: (String) -> Unit) {
            itemClickListener = listener
        }
    }
}








