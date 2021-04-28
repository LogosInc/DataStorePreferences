package com.example.datastorepreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.datastorepreferences.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // create a global variable to manager UserManager
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // getting references to userManager class
        userManager = UserManager(applicationContext)


        // save and get the user input
        binding.btnSave.setOnClickListener {
            storeUser()
        }

        /**
         * this function retrieves saved data as soon as
         * they are stored and even after the app is close
         * and registered again
         *
         */

        observeDate()

    }

    private fun observeDate() {

        /**
         *  getting and update the age every time user age changes
         *  it will be observed by useAgeFlow
         *
         */

        userManager.userAgeFlow.asLiveData().observe(this) { age ->
            age?.let {
                binding.tvAge.text = "Age: $age"
            }
        }

        /**
         *  getting and update the name every time user name changes
         *  it will be observed by useNameFlow
         *
         */

        userManager.userNameFlow.asLiveData().observe(this) { name ->
            name?.let {
                binding.tvName.text = "Name: $name"
            }
        }
    }

    /**
     *  this function save the data to preferences,
     *  when we click on save button
     *
     */

    private fun storeUser() {
        val name = binding.etName.text.toString()
        val age = binding.etAge.text.toString().toInt()

        /**
         *  store the values
         *  Our class is a suspend function,
         *  it's run only inside of coroutine scope
         *
         */

        lifecycleScope.launch {
            userManager.storeUserData(age, name)
            Toast.makeText(this@MainActivity, "User Saved", Toast.LENGTH_SHORT).show()
        }

        // after save user data, we need to clear the input data in edit text
        binding.etAge.text.clear()
        binding.etName.text.clear()
    }


}