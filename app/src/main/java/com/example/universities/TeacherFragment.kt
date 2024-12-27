package com.example.universities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterTeacher
    private lateinit var database: UniversityDatabase
    private lateinit var addbutton: Button
    private val universityDao: UniversityDao by lazy {

        val database = Room.databaseBuilder(
            requireContext(),
            UniversityDatabase::class.java,
            "database6"
        ).allowMainThreadQueries().build()
        database.universityDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = Room.databaseBuilder(
            requireContext(),
            UniversityDatabase::class.java, "database6"
        )
            .fallbackToDestructiveMigration()
            .build()
        var view = inflater.inflate(R.layout.fragment_teacher, container, false)
        GlobalScope.launch(Dispatchers.IO) {
            val data = database.teacherDao().getAllTeachers()
            withContext(Dispatchers.Main) {
                adapter.submitList(data)
            }
        }
        addbutton = view.findViewById(R.id.addButoon)
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterTeacher()
        recyclerView.adapter = adapter
        adapter.setOnDeleteClickListener { teacher ->
            GlobalScope.launch(Dispatchers.IO) {
                database.teacherDao().deleteTeacher(teacher)
                val teachers = database.teacherDao().getAllTeachers()
                withContext(Dispatchers.Main) {
                    adapter.submitList(teachers)
                }
            }
        }
        adapter.setOnEditClickListener { teacher ->
            EditDialog1(teacher)
        }
        addbutton.setOnClickListener{
            val intent = Intent(requireContext(), AddTeacher::class.java)
            startActivity(intent)
        }

        return view
    }

    fun EditDialog1(teacher: Teacher) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val universityNames = universityDao.getAllUniversityNames()
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        var name = teacher.name
        var univer = teacher.university
        var speciality = teacher.specialty
        var salary = teacher.salary
        var hours = teacher.teachingHours
        alertDialogBuilder.setTitle("Редактор данных")
        val editText1 = EditText(requireContext())
        val spinner = Spinner(requireContext())
        val editText2 = EditText(requireContext())
        val editText3 = EditText(requireContext())
        val editText4 = EditText(requireContext())
        editText3.inputType = InputType.TYPE_CLASS_NUMBER
        editText4.inputType = InputType.TYPE_CLASS_NUMBER
        editText1.hint = "Введите ФИО преподавателя"
        val universityAdapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, universityNames
        )
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = universityAdapter
        editText2.hint = "Введите специальность"
        editText3.hint = "Введите зарплату"
        editText4.hint = "Введите кол-во часов"
        editText1.setText("${name}")
        editText2.setText("${speciality}")
        editText3.setText("${salary}")
        editText4.setText("${hours}")
        layout.addView(editText1)
        layout.addView(spinner)
        layout.addView(editText2)
        layout.addView(editText3)
        layout.addView(editText4)
        alertDialogBuilder.setView(layout)
        alertDialogBuilder.setPositiveButton("Сохранить") { _, _ ->
            val newinfo1 = editText1.text.toString()
            val spinerinfo = spinner.selectedItem
            val newinfo2 = editText2.text.toString()
            var newinfo3 = editText3.text.toString()
            var newinfo4 = editText4.text.toString()

            if (newinfo1.isNotEmpty() && newinfo1 != null && newinfo2.isNotEmpty() && newinfo2 != null && newinfo3.isNotEmpty() && newinfo3 != null && newinfo4.isNotEmpty() && newinfo4 != null) {
                if (newinfo4.toInt()>1440)
                    newinfo3 = (newinfo3.toDouble() + ((newinfo4.toDouble()-1440)*300)).toString()
                val update = teacher.copy(name = newinfo1, university = spinerinfo.toString(), specialty = newinfo2, salary = newinfo3.toDouble(), teachingHours = newinfo4.toInt())
                GlobalScope.launch(Dispatchers.IO) {
                    database.teacherDao().updateTeacher(update)
                    val teacher = database.teacherDao().getAllTeachers()
                    withContext(Dispatchers.Main) {
                        adapter.submitList(teacher)
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Введите все необходимые данные", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TeacherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}