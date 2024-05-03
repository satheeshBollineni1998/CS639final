package com.example.myapplication2
import com.example.myapplication2.R
import com.example.myapplication2.FoodItem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SecondFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodItemAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FoodItemAdapter()
        recyclerView.adapter = adapter

        // Fetch data from Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("food_items")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val foodItems = mutableListOf<FoodItem>()

                for (snapshot in dataSnapshot.children) {
                    val foodItem = snapshot.getValue(FoodItem::class.java)
                    foodItem?.let { foodItems.add(it) }
                }

                adapter.setFoodItems(foodItems)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException())
            }
        })

        return view
    }

    companion object {
        private const val TAG = "SecondFragment"

        // Optional: Create a newInstance() method if you need to pass arguments to the fragment
        fun newInstance(): SecondFragment {
            return SecondFragment()
        }
    }
}
