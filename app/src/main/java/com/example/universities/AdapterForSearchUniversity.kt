package com.example.universities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterForSearchUniversity : ListAdapter<University, AdapterForSearchUniversity.UniversityViewHolder>(UniversityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = layoutInflater.inflate(R.layout.university_for_search_item, parent, false)
        return UniversityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val university = getItem(position)
        holder.bind(university)
        Picasso.get().load(university.photo).into(holder.image)
    }



    class UniversityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val info1: TextView = itemView.findViewById(R.id.info1)
        private val info2: TextView = itemView.findViewById(R.id.info2)
        val image: ImageView = itemView.findViewById(R.id.photo)

        fun bind(university: University) {
            info1.text = "Университет: ${university.university}"
            info2.text = "Сайт:\n${university.web}"
            Picasso.get()
                .load(university.photo)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(image)

        }
    }

    class UniversityDiffCallback : DiffUtil.ItemCallback<University>() {
        override fun areItemsTheSame(oldItem: University, newItem: University): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: University, newItem: University): Boolean {
            return oldItem == newItem
        }
    }
}