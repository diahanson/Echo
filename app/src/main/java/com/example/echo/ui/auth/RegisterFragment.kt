package com.example.echo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.echo.EchoApplication
import com.example.echo.R
import com.example.echo.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val app = requireActivity().application as EchoApplication
        val factory = AuthViewModelFactory(app.authRepository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            var isValid = true

            if (name.isEmpty()) {
                binding.tilName.error = "Name required"
                isValid = false
            } else {
                binding.tilName.error = null
            }

            if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))) {
                binding.tilEmail.error = "Invalid email format"
                isValid = false
            } else {
                binding.tilEmail.error = null
            }

            if (phone.length != 10 || !phone.all { it.isDigit() }) {
                binding.tilPhone.error = "Must be 10 digits"
                isValid = false
            } else {
                binding.tilPhone.error = null
            }

            if (password.length < 8 || !password.any { it.isDigit() }) {
                binding.tilPassword.error = "Min 8 chars, at least 1 digit"
                isValid = false
            } else {
                binding.tilPassword.error = null
            }

            if (password != confirm) {
                binding.tilConfirmPassword.error = "Passwords do not match"
                isValid = false
            } else {
                binding.tilConfirmPassword.error = null
            }

            if (isValid) {
                viewModel.register(name, email, phone, password)
            }
        }

        binding.tvLoginPrompt.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collect { state ->
                binding.progressBar.visibility = if (state is AuthState.Loading) View.VISIBLE else View.GONE
                when (state) {
                    is AuthState.RegisterSuccess -> {
                        viewModel.resetState()
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
