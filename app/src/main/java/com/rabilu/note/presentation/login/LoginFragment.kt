package com.rabilu.note.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rabilu.note.R
import com.rabilu.note.databinding.FragmentLoginBinding
import com.rabilu.note.util.getText
import com.rabilu.note.util.isEditTextEmpty

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)


        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.email.isEditTextEmpty()) {
                binding.email.error = "Email can't be empty"
            } else if (binding.password.isEditTextEmpty()) {
                binding.password.error = "Password is empty"
            } else {
                binding.progressBar3.visibility = View.VISIBLE
                Firebase.auth.signInWithEmailAndPassword(
                    binding.email.getText(),
                    binding.password.getText()
                ).addOnSuccessListener {
                    binding.progressBar3.visibility = View.GONE
                    findNavController().popBackStack(
                        findNavController().currentDestination!!.id,
                        true
                    )
                    findNavController().navigate(R.id.homeFragment)
                    Snackbar.make(requireView(), "Login Successful", Snackbar.LENGTH_LONG).show()
                }.addOnFailureListener {
                    binding.progressBar3.visibility = View.GONE
                    Snackbar.make(requireView(), it.localizedMessage!!.toString(), Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }
}