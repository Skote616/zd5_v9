package com.example.universities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UniversityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class UniversityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterUniversity
    private lateinit var database: UniversityDatabase
    private lateinit var addbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
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

        val view = inflater.inflate(R.layout.fragment_university, container, false)
        //makeApiRequestAndSaveData()
        GlobalScope.launch(Dispatchers.IO) {
            val data = database.universityDao().getAllUniversities()
            withContext(Dispatchers.Main) {
                adapter.submitList(data)
            }
        }
        addbutton = view.findViewById(R.id.addButoon)
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterUniversity()
        recyclerView.adapter = adapter
        adapter.setOnDeleteClickListener { university ->
            GlobalScope.launch(Dispatchers.IO) {
                database.universityDao().deleteUniversity(university)
                val univers = database.universityDao().getAllUniversities()
                withContext(Dispatchers.Main) {
                    adapter.submitList(univers)
                }
            }
        }
        adapter.setOnEditClickListener { university ->
            EditDialog(university)
        }
        addbutton.setOnClickListener{
            val intent = Intent(requireContext(), AddUniversity::class.java)
            startActivity(intent)
        }


        return view
    }

    fun EditDialog(university: University) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        val name = university.university
        val website = university.web
        val photo = university.photo
        alertDialogBuilder.setTitle("Редактор данных")
        val editText1 = EditText(requireContext())
        val editText2 = EditText(requireContext())
        val editText3 = EditText(requireContext())
        editText1.hint = "Введите название университета"
        editText2.hint = "Введите ссылку на сайт университета"
        editText3.hint = "Введите url-ссылку фотографии университета"
        editText1.setText("${name}")
        editText2.setText("${website}")
        editText3.setText("${photo}")
        layout.addView(editText1)
        layout.addView(editText2)
        layout.addView(editText3)
        alertDialogBuilder.setView(layout)
        alertDialogBuilder.setPositiveButton("Сохранить") { _, _ ->
            val newinfo1 = editText1.text.toString()
            val newinfo2 = editText2.text.toString()
            var newinfo3 = editText3.text.toString()
            val imageAvailable = runBlocking{CheckImageAvailability(newinfo3)}
            if (newinfo1.isNotEmpty() && newinfo1 != null) {
                if (ValidUrl(newinfo2)) {
                    if (!imageAvailable)
                    {
                        newinfo3 = "https://cdn-icons-png.flaticon.com/512/4054/4054617.png"
                    }

                    val update = university.copy(university = newinfo1, web = newinfo2, photo = newinfo3)
                    GlobalScope.launch(Dispatchers.IO) {
                        database.universityDao().updateUniversity(update)
                        val university = database.universityDao().getAllUniversities()
                        withContext(Dispatchers.Main) {
                            adapter.submitList(university)
                        }
                    }


                } else {
                    Toast.makeText(
                        requireContext(),
                        "ссылка на сайт введена некоректно",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(requireContext(), "Введите университет", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }
    fun ValidUrl(url: String): Boolean {
        val urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
        return Regex(urlPattern).matches(url)
    }

    suspend fun CheckImageAvailability(imageUrl: String): Boolean {
        val client = HttpClient()
        return try {
            val response: HttpResponse = client.get(imageUrl)
            true
        } catch (e: Exception) {
            false
        } finally {
            client.close()
        }
    }



        fun makeApiRequestAndSaveData() {
            val url = "http://universities.hipolabs.com/search?country=Belarus"
            var queue = Volley.newRequestQueue(requireContext())
            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    val universityList = mutableListOf<University>()
                    for (i in 0 until response.length()) {
                        val universityObj = response.getJSONObject(i)
                        val name = universityObj.getString("name")
                        val website = universityObj.getString("web_pages")
                        var photo = ""
                        when (i) {
                            0 -> photo = "https://www.amia.by/images/headers/amia_logo.png"
                            1 -> photo = "https://baa.by/bitrix/templates/newtemplate/img/logo.png"
                            2 -> photo = "https://www.bsatu.by/sites/default/files/images/logo.gif"
                            3 -> photo = "https://www.bsut.by/templates/bsutby/img/logo-color.png"
                            4 -> photo = "https://bgam.by/wp-content/themes/bgam/images/logo.png"
                            5 -> photo =
                                "https://robofinist.ru/files/113150/filename/%D0%91%D0%9D%D0%A2%D0%A3%202020.png"
                            else -> photo = "https://cdn-icons-png.flaticon.com/512/4054/4054617.png"

                        }

                        val university = University(photo = photo, university = name, web = website)
                        universityList.add(university)
                        /*GlobalScope.launch(Dispatchers.IO){
                            database.universityDao().insertUniversity(university)
                        }*/
                    }
                    GlobalScope.launch(Dispatchers.IO) {
                        universityList.forEach { university ->
                            database.universityDao().insertUniversity(university)
                        }
                    }

                },
                {
                    Log.d("Log", "Volley error: $it")
                }
            )
            queue.add(request)



        }



        companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment UniversityFragment.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                UniversityFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }




}

