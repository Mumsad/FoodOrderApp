// PhoneNumberActivity.kt
package com.example.foodorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.example.foodorder.databinding.ActivityPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.TimeUnit

class PhoneNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumberBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.sendotpbutton.setOnClickListener {
            number = binding.phonetext.text.trim().toString()
            if (number.isNotEmpty()) {
                if (number.length == 10) {
                    number = "+91$number"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(this, "Please Enter correct Number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                } else {
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
                // Remove the next line to avoid ProgressBar visibility changes
                // binding.phoneProgressBar.visibility = View.INVISIBLE
            }
    }


    private fun init() {
        auth = FirebaseAuth.getInstance()
    }

    private fun sendToOtpActivity(verificationId: String) {
        val intent = Intent(this@PhoneNumberActivity, PhoneOtpActivity::class.java)
        intent.putExtra("verificationId", verificationId)
        intent.putExtra("phoneNumber", number)
        startActivity(intent)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }
            // Remove the next line to avoid ProgressBar visibility changes
            // binding.phoneProgressBar.visibility = View.VISIBLE
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            val intent = Intent(this@PhoneNumberActivity, PhoneOtpActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phoneNumber", number)
            startActivity(intent)
            // Remove the next line to avoid ProgressBar visibility changes
            // binding.phoneProgressBar.visibility = View.INVISIBLE
        }
    }
}


//work code

//package com.example.foodorder
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.FirebaseException
//import com.google.firebase.FirebaseTooManyRequestsException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthProvider
//import com.example.foodorder.databinding.ActivityPhoneNumberBinding
//import com.example.foodorder.databinding.ActivitySignUpBinding
//import com.google.firebase.auth.PhoneAuthOptions
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
//        binding.sendotpbutton.setOnClickListener {
//            number = binding.phonetext.text.trim().toString()
//            if (number.isNotEmpty()) {
//                if (number.length == 10) {
//                    number = "+91$number"
//                    // Remove the next line to avoid ProgressBar visibility changes
//                    // binding.phoneProgressBar.visibility = View.VISIBLE
//                    val options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(number)
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(this)
//                        .setCallbacks(callbacks)
//                        .build()
//                    PhoneAuthProvider.verifyPhoneNumber(options)
//                } else {
//                    Toast.makeText(this, "Please Enter correct Number", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Please Enter Number", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun init() {
//        auth = FirebaseAuth.getInstance()
//        // Remove the next line to avoid ProgressBar initialization
//        // binding.phoneProgressBar.visibility = View.INVISIBLE
//    }
//
//    private fun sendToMain() {
//        startActivity(Intent(this, MainActivity::class.java))
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
//                    sendToMain()
//                } else {
//                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                }
//                // Remove the next line to avoid ProgressBar visibility changes
//                // binding.phoneProgressBar.visibility = View.INVISIBLE
//            }
//    }

//    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
//            // Remove the next line to avoid ProgressBar visibility changes
//            // binding.phoneProgressBar.visibility = View.VISIBLE
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
//            // Remove the next line to avoid ProgressBar visibility changes
//            // binding.phoneProgressBar.visibility = View.INVISIBLE
//        }
//    }
//}
//chek code

//package com.example.foodorder
//
//import android.content.ContentValues.TAG
//import android.content.Intent
//import android.nfc.Tag
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import com.example.foodorder.databinding.ActivityLoginBinding
//import com.example.foodorder.databinding.ActivityMainBinding
//import com.example.foodorder.databinding.ActivityPhoneNumberBinding
//import com.example.foodorder.databinding.ActivityPhoneOtpBinding
//import com.google.firebase.FirebaseException
//import com.google.firebase.FirebaseTooManyRequestsException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
//import java.util.concurrent.TimeUnit
//
//class PhoneNumberActivity : AppCompatActivity() {
//
//    private val binding: ActivityPhoneNumberBinding by lazy {
//        ActivityPhoneNumberBinding.inflate(layoutInflater)
//    }
//
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//
//
//        binding.sendotpbutton.setOnClickListener {
//            val phoneverify = binding.sendotpbutton
//
//           var phoneNumber = phoneverify.text.trim().toString()
//            if (phoneNumber.isNotEmpty()){
//                if(phoneNumber.length==10){
//                    phoneNumber="+91$phoneNumber"
//
//                    val options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(phoneNumber) // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this) // Activity (for callback binding)
//                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
//                        .build()
//                    PhoneAuthProvider.verifyPhoneNumber(options)
//
//                }else{
//                    Toast.makeText(this,"Please Enter Correct Number ",Toast.LENGTH_SHORT).show()
//                }
//            }else{
//                    Toast.makeText(this,"Please Enter Number ",Toast.LENGTH_SHORT).show()
//
//                }
//
//                }
//
//            val intent = Intent(this, PhoneOtpActivity::class.java)
//            startActivity(intent)
//        }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                    // Update UI
//                }
//            }
//    }
//    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            // This callback will be invoked in two situations:
//            // 1 - Instant verification. In some cases the phone number can be instantly
//            //     verified without needing to send or enter a verification code.
//            // 2 - Auto-retrieval. On some devices Google Play services can automatically
//            //     detect the incoming verification SMS and perform verification without
//            //     user action.
//            signInWithPhoneAuthCredential(credential)
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            // This callback is invoked in an invalid request for verification is made,
//            // for instance if the the phone number format is not valid.
//
//
//            if (e is FirebaseAuthInvalidCredentialsException) {
//
//                // Invalid request
//                Log.d(TAG,"OnVerificatonFailed: ${e.toString()}")
//            } else if (e is FirebaseTooManyRequestsException) {
//                // The SMS quota for the project has been exceeded
//
//                Log.d(TAG,"OnVerificatonFailed: ${e.toString()}")
//            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                // reCAPTCHA verification attempted with null Activity
//            }
//
//            // Show a message and update the UI
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken,
//        ) {
//            // The SMS verification code has been sent to the provided phone number, we
//            // now need to ask the user to enter the code and then construct a credential
//            // by combining the code with a verification ID.
//
//            // Save verification ID and resending token so we can use them later
//            val intent = Intent(this,ActivityPhoneOtpBinding::class.java)
//            intent.putExtra("OTP",verificationId)
//            Intent.putExtra("resendToken",token)
//            startActivity(intent)
//        }
//    }
//
//    override fun onStart(){
//        super.onStart()
//        if (auth.currentUser!=null){
//            startActivity(Intent(this,ActivityMainBinding::class.java))
//        }
//    }
//    }
