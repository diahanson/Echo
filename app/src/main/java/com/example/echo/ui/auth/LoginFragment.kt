package com.example.echo.ui.auth

import android.os.Bundle
import android.util.Patterns
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
import com.example.echo.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            var isValid = true

            if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))) {
                binding.tilEmail.error = "Invalid email format"
                isValid = false
            } else {
                binding.tilEmail.error = null
            }

            if (password.length < 8 || !password.any { it.isDigit() }) {
                binding.tilPassword.error = "Min 8 chars, at least 1 digit"
                isValid = false
            } else {
                binding.tilPassword.error = null
            }

            if (isValid) {
                viewModel.login(email, password)
            }
        }

        binding.tvRegisterPrompt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collect { state ->
                binding.progressBar.visibility = if (state is AuthState.Loading) View.VISIBLE else View.GONE
                when (state) {
                    is AuthState.Success -> {
                        viewModel.resetState()
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
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
