package com.rabilu.note.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Login(modifier = Modifier.padding(16.dp))
            }
        }
    }

    @Composable
    fun Login(modifier: Modifier = Modifier) {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var messsage by remember {
            mutableStateOf("")
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        ConstraintLayout(modifier = modifier) {
            val (progressBar, container, pageHeading, errorMessage) = createRefs()
            Text(
                modifier = Modifier.constrainAs(errorMessage) {},
                text = messsage
            )
            Text(
                modifier = Modifier.constrainAs(pageHeading) {
                    bottom.linkTo(container.top)
                    start.linkTo(container.start)
                    end.linkTo(container.end)
                },
                text = "Login",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp
                )
            )
            Card(
                modifier = modifier.constrainAs(container) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = email.value,
                        onValueChange = { email.value = it },
                        placeholder = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        })
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password.value,
                        onValueChange = { password.value = it },
                        placeholder = { Text(text = "Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    passwordVisible = !passwordVisible
                                },
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        })
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(modifier = Modifier.fillMaxWidth(), onClick = { }) {
                        Text(text = "Login")
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You don't have account? ",
                            style = TextStyle(
                                color = Color(0xFF808080),
                                fontSize = 11.sp
                            )
                        )
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Sign Up")
                        }
                    }
                }
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progressBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                )
            }

        }

    }

    @Preview
    @Composable
    fun LoginView() {
        Login()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)


//        binding = FragmentLoginBinding.bind(view)
//
//
//        binding.btnSignUp.setOnClickListener {
//            findNavController().navigate(R.id.registrationFragment)
//        }
//
//        binding.btnLogin.setOnClickListener {
//            if (binding.email.isEditTextEmpty()) {
//                binding.email.error = "Email can't be empty"
//            } else if (binding.password.isEditTextEmpty()) {
//                binding.password.error = "Password is empty"
//            } else {
//                binding.progressBar3.visibility = View.VISIBLE
//                Firebase.auth.signInWithEmailAndPassword(
//                    binding.email.getText(),
//                    binding.password.getText()
//                ).addOnSuccessListener {
//                    binding.progressBar3.visibility = View.GONE
//                    findNavController().popBackStack(
//                        findNavController().currentDestination!!.id,
//                        true
//                    )
//                    findNavController().navigate(R.id.homeFragment)
//                    Snackbar.make(requireView(), "Login Successful", Snackbar.LENGTH_LONG).show()
//                }.addOnFailureListener {
//                    binding.progressBar3.visibility = View.GONE
//                    Snackbar.make(requireView(), it.localizedMessage!!.toString(), Snackbar.LENGTH_LONG).show()
//                }
//            }
//
//        }
//    }
}