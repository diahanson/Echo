package com.example.echo.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.databinding.FragmentTipsBinding

class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding!!

    private val tips = listOf(
        "Drink a glass of water right now.",
        "Take a short walk out in the fresh air.",
        "Practice 4-7-8 breathing technique.",
        "Stretch your body, reach for the sky.",
        "Write down three things you are grateful for.",
        "Disconnect from screens for 30 minutes.",
        "Call a friend or family member just to chat.",
        "Read a chapter of a physical book.",
        "Try a 5-minute guided meditation.",
        "Write down what is stressing you to get it out."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = TipsAdapter(tips)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TipsAdapter(private val items: List<String>) : RecyclerView.Adapter<TipsAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTip: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvTip.text = "💡 ${items[position]}"
        }

        override fun getItemCount() = items.size
    }
}
