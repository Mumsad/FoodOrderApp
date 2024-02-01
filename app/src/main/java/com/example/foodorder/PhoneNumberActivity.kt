// PhoneNumberActivity.kt
package com.example.foodorder

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.example.foodorder.databinding.ActivityPhoneNumberBinding
import com.example.foodorder.databinding.ActivityPhoneOtpBinding
import com.google.firebase.FirebaseException

class PhoneNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumberBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.sendOTPBtn.setOnClickListener {
            val phoneNumber = "+91" + binding.phoneEditTextNumber.text.toString().trim()

            // Show progress bar while sending OTP
            binding.phoneProgressBar.visibility = View.VISIBLE

            // Send OTP to the provided phone number
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60, // Timeout duration
                java.util.concurrent.TimeUnit.SECONDS,
                this,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Automatically sign in the user if the verification is successful
                        signInWithPhoneAuthCredential(credential)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        // Handle verification failure
                        binding.phoneProgressBar.visibility = View.GONE
                        // You can display an error message to the user
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // Save the verification ID and show the user that they should enter the code
                        this@PhoneNumberActivity.verificationId = verificationId
                        binding.phoneProgressBar.visibility = View.GONE

                        // Navigate to OTPVerificationActivity
                        val intent = Intent(this@PhoneNumberActivity, PhoneOtpActivity::class.java)
                        intent.putExtra("verificationId", verificationId)
                        startActivity(intent)
                    }
                }
            )
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI or navigate to the next activity
                    // For example: startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // If sign-in fails, display an error message to the user
                    // You can also handle specific error cases here
                }
            }
    }
}


//
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import com.example.foodorder.databinding.ActivityPhoneNumberBinding
////import com.example.myapplication.databinding.ActivityPhoneNumberBinding
//import com.google.firebase.FirebaseException
//import com.google.firebase.FirebaseTooManyRequestsException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
//import java.util.concurrent.TimeUnit
//
//class PhoneNumberActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityPhoneNumberBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var number: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        init()
//
//        binding.sendOTPBtn.setOnClickListener {
//            number = binding.phoneEditTextNumber.text.trim().toString()
//            if (number.isNotEmpty()) {
//                if (number.length == 10) {
//                    number = "+91$number"
//                    binding.phoneProgressBar.visibility = View.VISIBLE
//                    val options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(number)
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(this)
//                        .setCallbacks(callbacks)
//                        .build()
//                    PhoneAuthProvider.verifyPhoneNumber(options)
//                } else {
//                    showToast("Please Enter correct Number")
//                }
//            } else {
//                showToast("Please Enter Number")
//            }
//        }
//    }
//
//    private fun init() {
//        binding.phoneProgressBar.visibility = View.INVISIBLE
//        auth = FirebaseAuth.getInstance()
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    showToast("Authenticate Successfully")
//                    sendToMain()
//                } else {
//                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                }
//                binding.phoneProgressBar.visibility = View.INVISIBLE
//            }
//    }
//
//    private fun sendToMain() {
//        startActivity(Intent(this, MainActivity::class.java))
//    }
//
//    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            signInWithPhoneAuthCredential(credential)
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            if (e is FirebaseAuthInvalidCredentialsException) {
//                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
//            } else if (e is FirebaseTooManyRequestsException) {
//                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
//            }
//            binding.phoneProgressBar.visibility = View.VISIBLE
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken
//        ) {
//            val intent = Intent(this@PhoneNumberActivity, PhoneOtpActivity::class.java)
//            intent.putExtra("OTP", verificationId)
//            intent.putExtra("resendToken", token)
//            intent.putExtra("phoneNumber", number)
//            startActivity(intent)
//            binding.phoneProgressBar.visibility = View.INVISIBLE
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (auth.currentUser != null) {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}