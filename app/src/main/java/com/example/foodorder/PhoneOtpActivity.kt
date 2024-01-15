
package com.example.foodorder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.example.foodorder.databinding.ActivityPhoneOtpBinding

class PhoneOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        verificationId = intent.getStringExtra("verificationId") ?: ""

        binding.verifyotpbutton.setOnClickListener {
            val otp = binding.otpenter.text.trim().toString()
            if (otp.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
    }

    private fun sendToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Close this activity after moving to MainActivity
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMainActivity()
                } else {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
