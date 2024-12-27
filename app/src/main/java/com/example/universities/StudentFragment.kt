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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterStudent
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
        var view =  inflater.inflate(R.layout.fragment_student, container, false)
        GlobalScope.launch(Dispatchers.IO) {
            val data = database.studentDao().getAllStudents()
            withContext(Dispatchers.Main) {
                adapter.submitList(data)
            }
        }
        addbutton = view.findViewById(R.id.addButoon)
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterStudent()
        recyclerView.adapter = adapter
        adapter.setOnDeleteClickListener { student ->
            GlobalScope.launch(Dispatchers.IO) {
                database.studentDao().deleteStudent(student)
                val students = database.studentDao().getAllStudents()
                withContext(Dispatchers.Main) {
                    adapter.submitList(students)
                }
            }
        }
        adapter.setOnEditClickListener { student ->
            EditDialog(student)
        }
        addbutton.setOnClickListener{
            val intent = Intent(requireContext(), AddStudent::class.java)
            startActivity(intent)
        }
        return view
    }
    fun EditDialog(student: Student) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val universityNames = universityDao.getAllUniversityNames()
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        var name = student.name
        var univer = student.university
        var speciality = student.specialty
        var birthday = student.birthDate
        var budget = student.scholarship
        alertDialogBuilder.setTitle("Редактор данных")
        val editText1 = EditText(requireContext())
        val spinner = Spinner(requireContext())
        val editText2 = EditText(requireContext())
        val editText3 = EditText(requireContext())
        val checkbox = CheckBox(requireContext())
        editText3.inputType = InputType.TYPE_DATETIME_VARIATION_DATE
        checkbox.text = "Бюджет"
        editText1.hint = "Введите ФИО студента"
        val universityAdapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, universityNames
        )
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = universityAdapter
        editText2.hint = "Введите специальность"
        editText3.hint = "Введите дату рождения"
        editText1.setText("${name}")
        editText2.setText("${speciality}")
        editText3.setText("${birthday}")

        layout.addView(editText1)
        layout.addView(spinner)
        layout.addView(editText2)
        layout.addView(editText3)
        layout.addView(checkbox)
        alertDialogBuilder.setView(layout)
        alertDialogBuilder.setPositiveButton("Сохранить") { _, _ ->
            val newinfo1 = editText1.text.toString()
            val spinerinfo = spinner.selectedItem
            val newinfo2 = editText2.text.toString()
            var newinfo3 = editText3.text.toString()
            var budgetinfo = checkbox.isChecked

            if (newinfo1.isNotEmpty() && newinfo1 != null && newinfo2.isNotEmpty() && newinfo2 != null && newinfo3.isNotEmpty() && newinfo3 != null) {
                val update = student.copy(name = newinfo1, university = spinerinfo.toString(), specialty = newinfo2, birthDate = newinfo3, scholarship = budgetinfo)
                GlobalScope.launch(Dispatchers.IO) {
                    database.studentDao().updateTeacher(update)
                    val students = database.studentDao().getAllStudents()
                    withContext(Dispatchers.Main) {
                        adapter.submitList(students)
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
         * @return A new instance of fragment StudentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}