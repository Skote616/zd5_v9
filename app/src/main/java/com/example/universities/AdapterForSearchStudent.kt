package com.example.universities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AdapterForSearchStudent : ListAdapter<Student, AdapterForSearchStudent.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = layoutInflater.inflate(R.layout.student_for_search_item, parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.bind(student)
    }


    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val info1: TextView = itemView.findViewById(R.id.info1)
        private val info2: TextView = itemView.findViewById(R.id.info2)
        private val info3: TextView = itemView.findViewById(R.id.info3)
        private val info4: TextView = itemView.findViewById(R.id.info4)
        private val info5: TextView = itemView.findViewById(R.id.info5)

        fun bind(student: Student) {
            info1.text = "ФИО студента: ${student.name}"
            info2.text = "Университет: ${student.university}"
            info3.text = "Специальность: ${student.specialty}"
            info4.text = "Дата рождения: ${student.birthDate}"
            if (student.scholarship)
                info5.text = "Бюджет"
            else
                info5.text = "Внебюджет"

        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }
    }
}