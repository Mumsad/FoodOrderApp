package com.example.foodorder

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodorder.Fragment.CongratsBottomSheetFragment
import com.example.foodorder.databinding.ActivityPayoutBinding
import com.example.foodorder.models.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener;
import org.json.JSONObject


class PayoutActivity : AppCompatActivity(), PaymentResultWithDataListener {

    private lateinit var binding:ActivityPayoutBinding
    private lateinit var name :String
    private lateinit var address:String
    private lateinit var phoneNumber:String
    private lateinit var totalAmount:String

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId :String

    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Checkout.preload(applicationContext)
        val co = Checkout()

        co.setKeyID("rzp_test_2VAdMcH9KEwkOV")

        binding  = ActivityPayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initiaz Firebase and USer Details
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        setUserData()

        // Get user details form Firebase
        foodItemName = intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("foodItemPrice") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("foodItemDescription") as ArrayList<String>
        foodItemIngredient = intent.getStringArrayListExtra("foodItemIngredient") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("foodItemImage") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("foodItemQuantities") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString() + "$"

        binding.payoutTotalAmount.isEnabled = false
        binding.payoutTotalAmount.text = totalAmount

        binding.placeMyOrderButton.setOnClickListener {

            // get data from Edittext
            name = binding.payoutName.text.toString().trim()
            address = binding.payoutAddress.text.toString().trim()
            phoneNumber = binding.payoutPhoneNumber.text.toString().trim()

            if (name.isBlank() || address.isBlank() || phoneNumber.isBlank()){
                Toast.makeText(this,"Please Enter all the Details",Toast.LENGTH_SHORT).show()
            }else {
                placeTheOrder()
                initiateRazorpayPayment()

            }

//            initiateRazorpayPayment()

//            val bottomSheetDialog = CongratsBottomSheetFragment()
//            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
        binding.payoutBackButton.setOnClickListener {
            finish()
        }
    }

    private fun initiateRazorpayPayment() {

        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Food Order")
            options.put("description", "Demoing Charges")
            options.put("image", "android.resource://${activity.packageName}/${R.drawable.logo}")
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc")

            options.put("currency", "INR")

            // Dynamically set the total amount using calculateTotalAmount()
            val totalAmountPaisa = (calculateTotalAmount() * 100).toString() // Convert to paisa
            options.put("amount", totalAmountPaisa)

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", "gaurav.kumar@example.com")
            prefill.put("contact", "9324286645")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

//        val activity:Activity = this
//        val co = Checkout()
//
//
//        try {
//            val options = JSONObject()
//            options.put("name","Food Order")
//            options.put("description","Demoing Charges")
//            //You can omit the image option to fetch the image from the dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
//            options.put("theme.color", "#3399cc");
//            options.put("currency","INR");
////            options.put("order_id", "order_DBJOWzybf0sJbb");
//            options.put("amount","50000")//pass amount in currency subunits
//
//            val retryObj = JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            val prefill = JSONObject()
//            prefill.put("email","gaurav.kumar@example.com")
//            prefill.put("contact","9324286645")
//
//            options.put("prefill",prefill)
//            co.open(activity,options)
//        }catch (e: Exception){
//            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
//            e.printStackTrace()
//        }

    }

    private fun placeTheOrder() {
        userId = auth.currentUser?.uid?:""

        val time = System.currentTimeMillis()
        val itemPushKey  =databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId, name, foodItemName,foodItemPrice,foodItemImage,foodItemQuantities,
            address,totalAmount,phoneNumber,time,itemPushKey, false, false)

        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails)
            .addOnSuccessListener {
                val bottomSheetDialog = CongratsBottomSheetFragment()
                bottomSheetDialog.show(supportFragmentManager,"Test")
                removeItemFromCart()
                addOrderToHistory(orderDetails)

            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to Order 😒",Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeItemFromCart() {
        val cartItemReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemReference.removeValue()
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '$') {
                price.dropLast(1).toInt()
            }else {
                price.toInt()
            }
            var quantity = foodItemQuantities[i]
            totalAmount += priceIntValue *quantity
        }

        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null){
            val userId = user.uid
            val userReferencer = databaseReference.child("user").child(userId)

            userReferencer.addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        val name = snapshot.child("name").getValue(String::class.java)?:""
                        val address = snapshot.child("address").getValue(String::class.java)?:""
                        val phoneNumber = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            payoutName.setText(name)
                            payoutAddress.setText(address)
                            payoutPhoneNumber.setText(phoneNumber)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
//        // Handle payment success
//        // Show Congrats bottom sheet or navigate to success screen
        val intent = Intent(this, CongratsBottomSheetFragment::class.java)
        startActivity(intent)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        finish()
        Toast.makeText(this,"Failed to Payment 😒${p1}",Toast.LENGTH_SHORT).show()

    }

//    override fun onPaymentSuccess(p0: String?) {
//        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
//        // Handle payment success
//        // Show Congrats bottom sheet or navigate to success screen
//        val intent = Intent(this, CongratsBottomSheetFragment::class.java)
//        startActivity(intent)
//    }
//
//    override fun onPaymentError(p0: Int, p1: String?) {
//        Toast.makeText(this,"Failed to Payment 😒",Toast.LENGTH_SHORT).show()
////        Toast.makeText(this, "Payment failed: $response", Toast.LENGTH_SHORT).show()
//        // Handle payment failure
//        // Optionally, you can retry payment or show an error message to the user
//    }

}