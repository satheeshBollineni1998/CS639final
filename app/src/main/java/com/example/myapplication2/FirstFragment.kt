package com.example.myapplication2
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication2.databinding.FragmentFirstBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class FirstFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseApp.initializeApp(requireContext())

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("nutrition_tracker")

        val caloriesTextView = binding.caloriesTextView
        val macrosTextView = binding.macrosTextView

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val calories = dataSnapshot.child("calories").getValue(String::class.java)
                val macros = dataSnapshot.child("macros").getValue(String::class.java)


                caloriesTextView.text = "Calories: $calories"
                macrosTextView.text = "Macros: $macros"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to retrieve data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnAddFood.setOnClickListener {
            showAddFoodDialog()
        }

        binding.btnMealPlan.setOnClickListener {
            navigateToSecondFragment()
        }
    }

    private fun showAddFoodDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Food")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which ->
            val foodItemName = input.text.toString()
            if (foodItemName.isNotEmpty()) {
                addFoodItem(foodItemName)
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun addFoodItem(foodItemName: String) {
        val foodItemRef = databaseReference.child("foodItems").push()
        foodItemRef.setValue(foodItemName)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Food item added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add food item: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToSecondFragment() {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}