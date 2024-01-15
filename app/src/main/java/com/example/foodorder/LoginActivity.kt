package com.example.foodorder

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorder.databinding.ActivityLoginBinding
import com.example.foodorder.databinding.ActivityPhoneNumberBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.donthavebutton.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        binding.loginbutton.setOnClickListener {
            val email = binding.loginemail.text.toString().trim()
            val password = binding.loginpass.text.toString().trim()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.loginphone.setOnClickListener {
            var intent=Intent(this,PhoneNumberActivity::class.java)
            startActivity(intent)
        }

        binding.googleSignInButton.setOnClickListener {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this) { connectionResult ->
                    Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result != null) {
                if (if (result.isSuccess) {
                        true
                    } else {
                        false
                    }
                ) {
                    val account = result?.signInAccount
                    firebaseAuthWithGoogle(account)
                } else {
                    Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Google Sign In failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


}

//package com.example.foodorder
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.foodorder.databinding.ActivityLoginBinding
//import com.example.foodorder.databinding.ActivityMainBinding
//
//class LoginActivity : AppCompatActivity() {
//    private val binding:ActivityLoginBinding by lazy {
//        ActivityLoginBinding.inflate(layoutInflater)
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        binding.donthavebutton.setOnClickListener {
//            val intent = Intent(this,SignUpActivity::class.java)
//            startActivity(intent)
//        }
//        binding.loginbutton.setOnClickListener {
//            val intent = Intent(this,SignUpActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}

//package com.example.foodorder
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.foodorder.databinding.ActivityLoginBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//class LoginActivity : AppCompatActivity() {
//    private val binding: ActivityLoginBinding by lazy {
//        ActivityLoginBinding.inflate(layoutInflater)
//    }
//
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        binding.donthavebutton.setOnClickListener {
//            val intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.loginbutton.setOnClickListener {
//            // Extract email and password from EditText fields
//            val email = binding.loginemail.text.toString().trim()
//            val password = binding.loginpass.text.toString().trim()
//
//            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Sign in user with email and password
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI or navigate to another activity
//                        val user: FirebaseUser? = auth.currentUser
//                        // Example: You can navigate to the main activity
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        // You can handle specific error cases here
//                        // For simplicity, a generic message is displayed
//                        // You might want to display more specific error messages based on task.exception
//                        // task.exception?.message
//                        // Example: Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show()
//                        Toast.makeText(this, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//            if (auth.currentUser != null) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//
//    }
//}
