package com.example.foodorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.foodorder.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {
    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupcreateacc.setOnClickListener {
            // Extract email and password from EditText fields
            val email = binding.signupemail.text.toString().trim()
            val password = binding.signuppass.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Stop further execution if email is empty
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener  // Stop further execution if password is empty
            }
//            if(TextUtils.isEmpty(email)){
//                Toast.makeText(this,"Enter Email ",Toast.LENGTH_SHORT).show()
//
//            }
//            if(TextUtils.isEmpty(password)){
//                Toast.makeText(this,"Enter Email ",Toast.LENGTH_SHORT).show()
//
//            }

            // Create a new user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI or navigate to another activity
                        val user: FirebaseUser? = auth.currentUser

                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                        // Example: You can navigate to LoginActivity
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign up fails, display a message to the user.
                        // You can handle specific error cases here
                        // For simplicity, a generic message is displayed
                        // You might want to display more specific error messages based on task.exception
                        // task.exception?.message
                        Toast.makeText(this, "Sign up failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }




    }
}