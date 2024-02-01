package com.example.foodorder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.foodorder.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locationList =
            arrayOf("Mumbai", "Maharashtra", "Goa", "Delhi", "UttarPradesh", "Gujarat")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.btnGoToMainScreenButton.setOnClickListener {
            val intent = Intent(this@ChooseLocationActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}