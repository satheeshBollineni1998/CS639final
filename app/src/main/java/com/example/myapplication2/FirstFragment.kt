package com.example.myapplication2

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication2.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    val foodItems = mutableListOf<Array<String>>()

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        binding.btnAddFood.setOnClickListener {
            showAddFoodDialog()
        }

        binding.btnMealPlan.setOnClickListener {
            navigateToSecondFragment()
        }
//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment)
//            showToast("Going to Contact ...")
//
//        }

    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        // Add the food item to the list of food items
        foodItems.add(arrayOf(foodItemName));

        // Notify the adapter that the data has changed

    }
    fun navigateToSecondFragment() {
        // Navigate to the existing FirstFragment using NavHostFragment
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

}