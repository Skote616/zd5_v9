package com.example.universities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AdapterTeacher : ListAdapter<Teacher, AdapterTeacher.TeacherViewHolder>(TeacherDiffCallback()) {

    private var onDeleteClickListener: ((Teacher) -> Unit)? = null
    private var onEditClickListener: ((Teacher) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = layoutInflater.inflate(R.layout.teacher_item, parent, false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = getItem(position)
        holder.bind(teacher, onDeleteClickListener, onEditClickListener)
    }
    fun setOnDeleteClickListener(listener: (Teacher) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: (Teacher) -> Unit) {
        onEditClickListener = listener
    }

    class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fio: TextView = itemView.findViewById(R.id.info1)
        private val univer: TextView = itemView.findViewById(R.id.info2)
        private val speciality: TextView = itemView.findViewById(R.id.info3)
        private val salary: TextView = itemView.findViewById(R.id.info4)
        private val hours: TextView = itemView.findViewById(R.id.info5)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete)
        private val editButton: ImageButton = itemView.findViewById(R.id.edit)

        fun bind(teacher: Teacher, onDeleteClickListener: ((Teacher) -> Unit)?, onEditClickListener: ((Teacher) -> Unit)?) {
            fio.text = "Преподаватель: ${teacher.name}"
            univer.text = "Университет: ${teacher.university}"
            speciality.text = "Специальность: ${teacher.specialty}"
            salary.text = "Зарплата: ${teacher.salary.toString()}"
            hours.text = "Количество часов: ${teacher.teachingHours.toString()}"
            deleteButton.setOnClickListener {
                onDeleteClickListener?.invoke(teacher)
            }
            editButton.setOnClickListener {
                onEditClickListener?.invoke(teacher)
            }

        }
    }

    class TeacherDiffCallback : DiffUtil.ItemCallback<Teacher>() {
        override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
            return oldItem == newItem
        }
    }
}