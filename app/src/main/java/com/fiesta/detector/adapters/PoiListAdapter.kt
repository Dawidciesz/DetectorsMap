package com.fiesta.detector.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fiesta.detector.data.Poi
import com.fiesta.detector.databinding.ListItemBinding

class PoiListAdapter : ListAdapter<Poi, PoiListAdapter.PoiViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PoiViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(poi : Poi) {
            binding.apply {
                itemImage
                itemName.text = poi.name
                itemDescription.text = poi.description
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Poi>() {
        override fun areItemsTheSame(oldItem: Poi, newItem: Poi) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Poi, newItem: Poi) =
            oldItem == newItem
    }

}