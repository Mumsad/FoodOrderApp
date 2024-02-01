package com.example.foodorder.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorder.R
import com.example.foodorder.adapter.NotificationAdapter
import com.example.foodorder.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomBinding.inflate(inflater, container, false)

        val notifications = listOf(
            "Your order has been Canceled Successfully",
            "Order has been taken by the driver", "Congrats Your Order Placed",
            "Your order has been Canceled Successfully",
            "Order has been taken by the driver", "Congrats Your Order Placed"
        )
        val notificationImages = listOf(
            R.drawable.sademoji,
            R.drawable.buss,
            R.drawable.congratulations,
            R.drawable.arrow_left,
            R.drawable.lock_01, //lock
            R.drawable.congratulations
        )
        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {

    }
}