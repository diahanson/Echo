package com.example.echo.ui.support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.echo.databinding.FragmentSupportBinding

class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSms.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim().replace(" ", "")
            if (phoneNumber.isEmpty()) {
                binding.tilPhone.error = "Please enter a phone number"
                return@setOnClickListener
            }
            binding.tilPhone.error = null

            // Lab 3: Implicit Intent (SMS)
            val message = "Hello Echo Support,\nI need help with my mental wellness tracker."
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", message)
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "No SMS app found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEmail.setOnClickListener {
            val emailAddress = binding.etEmail.text.toString().trim()
            if (emailAddress.isEmpty()) {
                binding.tilEmail.error = "Please enter an email address"
                return@setOnClickListener
            }
            binding.tilEmail.error = null

            // Lab 3: Implicit Intent (Email)
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
                putExtra(Intent.EXTRA_SUBJECT, "Echo App Support Request")
                putExtra(Intent.EXTRA_TEXT, "Hello Echo Support,\nI need help with my mental wellness tracker.")
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "No Email app found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
