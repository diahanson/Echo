package com.example.echo.ui.tracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.echo.EchoApplication
import com.example.echo.R
import com.example.echo.databinding.FragmentTrackerBinding

class TrackerFragment : Fragment() {

    private var _binding: FragmentTrackerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TrackerViewModel
    private val args: TrackerFragmentArgs by navArgs()

    private var editingEntryId: Int = -1

    // Lab 5: ArrayList for temporary storage
    private val selectedActivities = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logLifecycle("onCreateView")
        _binding = FragmentTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifecycle("onViewCreated")

        val app = requireActivity().application as EchoApplication
        val factory = TrackerViewModelFactory(app.wellnessRepository, app.sessionManager)
        viewModel = ViewModelProvider(this, factory)[TrackerViewModel::class.java]

        setupUI()
        setupListeners()
        
        editingEntryId = args.entryId
        if (editingEntryId != -1) {
            binding.tvTitle.text = "Edit Wellness Entry"
            binding.btnSubmit.text = "Update Entry"
            loadExistingEntry()
        }
    }

    private fun setupUI() {
        // Setup Mood Dropdown (AutoCompleteTextView)
        val moods = arrayOf("Happy", "Calm", "Relaxed", "Neutral", "Okay", "Sad", "Anxious", "Angry")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, moods)
        (binding.spinnerMood as? AutoCompleteTextView)?.setAdapter(adapter)

        // Setup SeekBar
        binding.seekBarStress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvStressValue.text = (progress + 1).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupListeners() {
        binding.cbMeditation.setOnCheckedChangeListener { _, isChecked -> updateActivities("Meditation", isChecked) }
        binding.cbExercise.setOnCheckedChangeListener { _, isChecked -> updateActivities("Exercise", isChecked) }
        binding.cbSocializing.setOnCheckedChangeListener { _, isChecked -> updateActivities("Socializing", isChecked) }
        binding.cbReading.setOnCheckedChangeListener { _, isChecked -> updateActivities("Reading", isChecked) }
        binding.cbSleep.setOnCheckedChangeListener { _, isChecked -> updateActivities("Quality Sleep", isChecked) }

        binding.btnSubmit.setOnClickListener {
            val mood = binding.spinnerMood.text.toString()
            val stress = binding.seekBarStress.progress + 1

            if (editingEntryId != -1) {
                viewModel.updateExistingEntry(editingEntryId, mood, stress, selectedActivities) { score ->
                    showResultDialog(score)
                }
            } else {
                viewModel.calculateAndSaveScore(mood, stress, selectedActivities) { score ->
                    showResultDialog(score)
                }
            }
        }
    }

    private fun updateActivities(activity: String, isAdded: Boolean) {
        if (isAdded && !selectedActivities.contains(activity)) {
            selectedActivities.add(activity)
        } else if (!isAdded) {
            selectedActivities.remove(activity)
        }
    }

    private fun loadExistingEntry() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val entry = viewModel.getEntryById(editingEntryId)
            entry?.let {
                // Set mood
                val moods = arrayOf("Happy", "Calm", "Relaxed", "Neutral", "Okay", "Sad", "Anxious", "Angry")
                val moodIndex = moods.indexOf(it.mood)
                if (moodIndex >= 0) {
                    binding.spinnerMood.setText(it.mood, false)
                }
                
                // Set stress
                binding.seekBarStress.progress = it.stressLevel - 1
                binding.tvStressValue.text = it.stressLevel.toString()
                
                // Set activities
                val activitiesList = it.activities.split(", ")
                if (activitiesList.contains("Meditation")) binding.cbMeditation.isChecked = true
                if (activitiesList.contains("Exercise")) binding.cbExercise.isChecked = true
                if (activitiesList.contains("Socializing")) binding.cbSocializing.isChecked = true
                if (activitiesList.contains("Reading")) binding.cbReading.isChecked = true
                if (activitiesList.contains("Quality Sleep")) binding.cbSleep.isChecked = true
            }
        }
    }

    // Lab 4: AlertDialog for score results
    private fun showResultDialog(score: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Wellness Score")
            .setMessage("Your calculated wellness score is: $score\n\nBased on your entries.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    override fun onPause() {
        super.onPause()
        logLifecycle("onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logLifecycle("onDestroyView")
        _binding = null
    }

    // Lab 7: Fragment lifecycle tracking
    private fun logLifecycle(event: String) {
        Log.d("TrackerLifecycle", "Lifecycle Event: $event")
        Toast.makeText(context, "Tracker: $event", Toast.LENGTH_SHORT).show()
    }
}
