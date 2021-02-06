package com.fiesta.detector.adapters
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fiesta.detector.R
import com.fiesta.detector.data.Poi
import com.fiesta.detector.databinding.ListItemBinding

class PoiListAdapter(private val listener: OnItemClickListener) : ListAdapter<Poi, PoiListAdapter.PoiViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PoiViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val poi = getItem(position)
                        listener.onItemClick(poi)
                    }
                }
            }
        }

        fun bind(poi : Poi) {
            binding.apply {
                if (poi.uri != "") {
                    itemImage.setImageURI(Uri.parse(poi.uri))
                }
                itemName.text = poi.name
                itemDescription.text = poi.description
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(poi: Poi)
    }

    class DiffCallback : DiffUtil.ItemCallback<Poi>() {
        override fun areItemsTheSame(oldItem: Poi, newItem: Poi) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Poi, newItem: Poi) =
            oldItem == newItem
    }

}