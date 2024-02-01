package com.example.foodorder.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorder.R
import com.example.foodorder.adapter.MenuAdapter
import com.example.foodorder.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.backbutton.setOnClickListener {
            dismiss()
        }
/*
         val menuFoodName =
             listOf(
                 "Burger",
                 "Sandwich",
                 "momo",
                 "Herbal Pancake",
                 "Mixing",
                 "Burger",
                 "Burger",
                 "Sandwich",
                 "momo",
                 "Herbal Pancake",
                 "Mixing",
                 "Burger"
             )
         val menuPrice =
             listOf("$10", "$8", "$15", "$99", "$50", "$12", "$10", "$8", "$15", "$99", "$50", "$12")

         val menuImages = listOf(
             R.drawable.menu1,
             R.drawable.menu2,
             R.drawable.menu3,
             R.drawable.menu4,
             R.drawable.menu6
         )

         val adapter =
             MenuAdapter(
                 ArrayList(menuFoodName),
                 ArrayList(menuPrice),
                 ArrayList(menuImages),
                 requireContext()
             )
         binding.menuBottomSheetRecyclerView.layoutManager = LinearLayoutManager(requireContext())
         binding.menuBottomSheetRecyclerView.adapter = adapter */

//        retrieveMenuItems()
        return binding.root
    }

//    private fun retrieveMenuItems() {
//        database = FirebaseDatabase.getInstance()
//        val foodRef : DatabaseReference = database.reference.child("menu")
//        menuItems = mutableListOf()
//
//        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (foodSnapshot in snapshot.children){
//                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
//                    menuItem?.let {
//                        menuItems.add(it)
//                    }
//                }
//                Log.d("ITEMS", "onDataChange : Data Received")
//                // once data receive set to adapter
//                setAdapter()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(),"Data Not Fetching",Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

//    private fun setAdapter() {
//        if (menuItems.isNotEmpty()){
//            val adapter = MenuAdapter(menuItems, requireContext())
//            binding.menuBottomSheetRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//            binding.menuBottomSheetRecyclerView.adapter = adapter
//            Log.d("ITEMS", "setAdapter : Data set")
//        }
//        else{
//            Log.d("ITEMS", "setAdapter : Data Not set")
//        }
//    }

    companion object {

    }
}