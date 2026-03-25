package com.example.echo.ui.history

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.data.local.entity.WellnessEntry
import com.example.echo.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onContextMenuClick: (WellnessEntry, Int) -> Unit
) : ListAdapter<WellnessEntry, HistoryAdapter.HistoryViewHolder>(EntryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        
        init {
            binding.root.setOnCreateContextMenuListener(this)
        }

        fun bind(entry: WellnessEntry) {
            binding.tvMood.text = entry.mood
            binding.tvStress.text = "Stress: ${entry.stressLevel}/10"
            binding.tvActivities.text = entry.activities
            binding.tvScore.text = entry.wellnessScore.toString()
            
            val sdf = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date(entry.timestamp))
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.setHeaderTitle("Options")
            // Context Menu: 1=Edit, 2=Delete
            val editItem = menu?.add(0, 1, 0, "Edit")
            val deleteItem = menu?.add(0, 2, 0, "Delete")
            
            val entry = getItem(adapterPosition)
            editItem?.setOnMenuItemClickListener {
                onContextMenuClick(entry, 1)
                true
            }
            deleteItem?.setOnMenuItemClickListener {
                onContextMenuClick(entry, 2)
                true
            }
        }
    }

    class EntryDiffCallback : DiffUtil.ItemCallback<WellnessEntry>() {
        override fun areItemsTheSame(oldItem: WellnessEntry, newItem: WellnessEntry): Boolean {
            return oldItem.entryId == newItem.entryId
        }

        override fun areContentsTheSame(oldItem: WellnessEntry, newItem: WellnessEntry): Boolean {
            return oldItem == newItem
        }
    }
}
