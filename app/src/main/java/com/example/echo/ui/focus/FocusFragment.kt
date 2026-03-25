package com.example.echo.ui.focus

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.echo.databinding.FragmentFocusBinding
import java.util.Locale

class FocusFragment : Fragment() {

    private var _binding: FragmentFocusBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var breathingAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFocusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isTimerRunning) {
                // Ignore back press during session
            } else {
                findNavController().navigateUp()
            }
        }

        binding.rgModes.setOnCheckedChangeListener { _, checkedId ->
            if (!isTimerRunning) {
                when (checkedId) {
                    binding.rb5Min.id -> setTimerText(5 * 60 * 1000L)
                    binding.rb15Min.id -> setTimerText(15 * 60 * 1000L)
                    binding.rb30Min.id -> setTimerText(30 * 60 * 1000L)
                }
            }
        }

        binding.btnStart.setOnClickListener {
            if (isTimerRunning) {
                stopSession()
            } else {
                val timeMs = when (binding.rgModes.checkedRadioButtonId) {
                    binding.rb15Min.id -> 15 * 60 * 1000L
                    binding.rb30Min.id -> 30 * 60 * 1000L
                    else -> 5 * 60 * 1000L
                }
                startSession(timeMs)
            }
        }
    }

    private fun startSession(timeMs: Long) {
        isTimerRunning = true
        binding.btnStart.text = "End Session Early"
        binding.rgModes.visibility = View.INVISIBLE

        startBreathingAnimation()

        countDownTimer = object : CountDownTimer(timeMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                setTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                stopSession()
                setTimerText(0)
            }
        }.start()
    }

    private fun stopSession() {
        isTimerRunning = false
        countDownTimer?.cancel()
        breathingAnimator?.cancel()
        
        binding.vBreathingCircle.scaleX = 1f
        binding.vBreathingCircle.scaleY = 1f

        binding.btnStart.text = "Start Focus Session"
        binding.rgModes.visibility = View.VISIBLE
        
        val defaultMs = when (binding.rgModes.checkedRadioButtonId) {
            binding.rb15Min.id -> 15 * 60 * 1000L
            binding.rb30Min.id -> 30 * 60 * 1000L
            else -> 5 * 60 * 1000L
        }
        setTimerText(defaultMs)
    }

    private fun setTimerText(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        binding.tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun startBreathingAnimation() {
        val scaleDown = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f)
        val scaleUp = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f)
        breathingAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.vBreathingCircle, scaleDown, scaleUp).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        breathingAnimator?.cancel()
        _binding = null
    }
}
