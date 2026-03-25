package com.example.echo.ui.reflection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.echo.EchoApplication
import com.example.echo.data.local.entity.Reflection
import com.example.echo.databinding.FragmentReflectionBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReflectionFragment : Fragment() {

    private var _binding: FragmentReflectionBinding? = null
    private val binding get() = _binding!!

    private val prompts = listOf(
        "What made you happy today?",
        "What stressed you today?",
        "What are you grateful for?"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReflectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prompt = prompts.random()
        binding.tvPrompt.text = "Prompt: $prompt"

        binding.btnSave.setOnClickListener {
            val text = binding.etJournal.text.toString()
            if (text.isNotBlank()) {
                saveReflection("General", text)
            } else {
                Toast.makeText(requireContext(), "Please write something.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveReflection(tag: String, text: String) {
        val app = requireActivity().application as EchoApplication
        val repo = app.wellnessRepository
        val session = app.sessionManager

        viewLifecycleOwner.lifecycleScope.launch {
            val userId = session.userIdFlow.first()
            if (userId != -1) {
                repo.addReflection(
                    Reflection(
                        userId = userId,
                        emotionTag = tag,
                        reflectionText = text,
                        timestamp = System.currentTimeMillis()
                    )
                )
                Toast.makeText(requireContext(), "Reflection saved", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
