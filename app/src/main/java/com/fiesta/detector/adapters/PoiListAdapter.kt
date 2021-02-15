package com.fiesta.detector.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fiesta.detector.data.Poi
import com.fiesta.detector.databinding.ListItemBinding

class PoiListAdapter(
    private val detailsListener: OnDetailsClickListener,
    private val zoomLocationListener: OnZoomLocationClickListener
) : ListAdapter<Poi, PoiListAdapter.PoiViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PoiViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(poi: Poi) {
            binding.apply {
                if (poi.uri != "") {
                    itemImage.setImageURI(Uri.parse(poi.uri))
                }
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val poi = getItem(position)
                    itemName.text = poi.name
                    itemShowDetails.text = "szczegoly"
                    itemShowDetails.setOnClickListener(View.OnClickListener {
                        detailsListener.onDetailsItemClick(poi)
                    }
                    )
                    itemShowOnMap.text = "wyszukaj"
                    itemShowOnMap.setOnClickListener(View.OnClickListener {
                        zoomLocationListener.onZoomLocationItemClick(poi)
                    })
                }
            }
        }
    }

    interface OnDetailsClickListener {
        fun onDetailsItemClick(poi: Poi)
    }

    interface OnZoomLocationClickListener {
        fun onZoomLocationItemClick(poi: Poi)
    }

    class DiffCallback : DiffUtil.ItemCallback<Poi>() {
        override fun areItemsTheSame(oldItem: Poi, newItem: Poi) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Poi, newItem: Poi) =
            oldItem == newItem
    }

}