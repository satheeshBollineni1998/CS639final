package com.example.myapplication2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication2.databinding.FragmentMealHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.TextView


class MealHistoryFragment : Fragment() {

    private var _binding: FragmentMealHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        fetchMealHistory()
    }

    private fun fetchMealHistory() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val mealsRef = database.child("meals").child(userId)

            mealsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val meals = mutableListOf<Meal>()
                    for (userSnapshot in snapshot.children) {
                        for (mealSnapshot in userSnapshot.children) {
                            val mealName = mealSnapshot.child("mealName").getValue(String::class.java)
                            mealName?.let {
                                val meal = Meal(it, mealName)
                                meals.add(meal)
                            }
                        }
                    }
                    updateMealHistory(meals)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    private fun updateMealHistory(meals: List<Meal>) {
        val adapter = MealHistoryAdapter(meals)
        binding.recyclerViewMealHistory.adapter = adapter
        binding.recyclerViewMealHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class Meal(
        val meal: String,
        val mealName: String
    )

    inner class MealHistoryAdapter(private val meals: List<Meal>) :
        RecyclerView.Adapter<MealHistoryAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val meal: TextView = view.findViewById(R.id.recyclerViewMealHistory)
            // Add other views as needed
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_meal_history, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val meal = meals[position]
            holder.meal.text = meal.meal

        }


        override fun getItemCount(): Int {
            return meals.size
        }
    }
}
