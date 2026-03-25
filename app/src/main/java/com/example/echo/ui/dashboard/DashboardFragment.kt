package com.example.echo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.echo.EchoApplication
import com.example.echo.R
import com.example.echo.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val app = requireActivity().application as EchoApplication
        val factory = DashboardViewModelFactory(app.wellnessRepository, app.sessionManager)
        viewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]

        setupGreeting()
        setupListeners()
        setupObservers()
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 0..11 -> "Good Morning, Echo"
            in 12..16 -> "Good Afternoon, Echo"
            in 17..20 -> "Good Evening, Echo"
            else -> "Good Night, Echo"
        }
        binding.tvGreeting.text = greeting
    }

    private fun setupListeners() {
        binding.cardTracker.setOnClickListener {
            findNavController().navigate(R.id.trackerFragment)
        }
        binding.cardJournal.setOnClickListener {
            findNavController().navigate(R.id.reflectionFragment)
        }
        binding.cardHistory.setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
        binding.cardFocus.setOnClickListener {
            findNavController().navigate(R.id.focusFragment)
        }

        binding.tvGreeting.setOnClickListener { view ->
            val popup = android.widget.PopupMenu(requireContext(), view)
            popup.menu.add("💡 Need water? Drink a glass now!")
            popup.menu.add("🌬️ Take a deep breath.")
            popup.show()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.latestEntry.collect { entry ->
                binding.tvWeatherBadge.text = getEmotionWeather(entry?.wellnessScore ?: 50)
            }
        }
    }

    private fun getEmotionWeather(score: Int): String {
        return when {
            score >= 80 -> "☀️ Sunny Mind - Calm & Productive"
            score >= 50 -> "🌥️ Cloudy Mind - Slight stress"
            score >= 30 -> "🌧️ Rainy Mind - Noticeable tension"
            else -> "⛈️ Stormy Mind - High Anxiety"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
