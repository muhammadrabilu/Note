package com.rabilu.note.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rabilu.note.R
import com.rabilu.note.databinding.FragmentRegistrationBinding
import com.rabilu.note.model.User
import com.rabilu.note.util.Constance
import com.rabilu.note.util.getText
import com.rabilu.note.util.isEditTextEmpty
import com.rabilu.note.util.isValidPassword


class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private lateinit var binding: FragmentRegistrationBinding
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)
        progressBar = binding.progressBar

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignUp.setOnClickListener {
            if (binding.fullName.isEditTextEmpty()){
                binding.fullName.error = "Your full name is required"
            }else if (binding.email.isEditTextEmpty()){
                binding.email.error = "Please type in your email"
            } else if (!isValidPassword(binding.password)){
                binding.password.error = "Please make sure your password \nlength is not less than six char"
            }else {
                progressBar.visibility = View.VISIBLE
                val user = User(binding.fullName.getText(), binding.email.getText())
                auth.createUserWithEmailAndPassword(binding.email.getText(), binding.password.getText()).addOnSuccessListener {
                    db.collection(Constance.USERS.name).document(it.user!!.uid).set(user).addOnSuccessListener {
                        progressBar.visibility = View.GONE
                        findNavController().popBackStack(findNavController().currentDestination!!.id, true)
                        findNavController().navigate(R.id.homeFragment)
                        Snackbar.make(requireView(), "Registration successfully", Snackbar.LENGTH_LONG).show()

                    }.addOnFailureListener{ exception ->
                        progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), exception.localizedMessage!!.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), it.localizedMessage!!.toString(), Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }

}