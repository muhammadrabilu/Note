package com.rabilu.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragmentContainerView)
        if (auth.currentUser != null) {
            navController.popBackStack(R.id.homeFragment, true)
            navController.navigate(R.id.homeFragment)
        } else {
            navController.popBackStack(navController.currentDestination!!.id, true)
            navController.navigate(R.id.loginFragment)
        }
    }
}