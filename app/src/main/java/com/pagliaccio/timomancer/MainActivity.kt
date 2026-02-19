package com.pagliaccio.timomancer

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.datastore.preferences.preferencesDataStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

data class Task(
    val id: Int = System.currentTimeMillis().toInt(), // simple unique ID
    val title: String
//    var isCompleted: Boolean = false
    //add tasktheme or tasktype or the nessecity
)
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Context.dataStore by preferencesDataStore("tasks")

class MainActivity : AppCompatActivity() {
    val TASKS_KEY = stringPreferencesKey("tasks_list")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        loadTasks()
        initDataStoreListener()
        val addButton = findViewById<ImageButton>(R.id.addButton)

        addButton.setOnClickListener {
            val editText = EditText(this)

            AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setMessage("Enter task name:")
                .setView(editText)
                .setPositiveButton("Add") { dialog, which ->
                    var task = Task(title = editText.text.toString())

                    lifecycleScope.launch {
                        dataStore.edit { preferences ->
                            val jsonString = preferences[TASKS_KEY] ?: "[]"
                            val existingList: MutableList<Task> = Gson().fromJson(
                                jsonString,
                                object : TypeToken<MutableList<Task>>() {}.type
                            ) ?: mutableListOf()

                            existingList.add(task)

                            preferences[TASKS_KEY] = Gson().toJson(existingList)
                        }
                    }
//                    loadTasks()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addTaskTV(task: Task) {
        val task_tv = TextView(this)
        task_tv.text = task.title
        task_tv.id = task.id
        task_tv.setPadding(15.dp)
        task_tv.setTextAppearance(R.style.TodoListItem)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.bottomMargin = 15.dp
        task_tv.background = AppCompatResources.getDrawable(this,R.drawable.shape_round_corners)
        task_tv.layoutParams = params

        findViewById<LinearLayout>(R.id.taskLinearLayout).addView(task_tv)
    }

//    private fun loadTasks(){
//        findViewById<LinearLayout>(R.id.taskLinearLayout).removeAllViews()
//
//        lifecycleScope.launch {
//            dataStore.data.collect { preferences ->
//                val jsonString = preferences[TASKS_KEY] ?: "[]"
//                val taskList: List<Task> = Gson().fromJson(
//                    jsonString,
//                    object : TypeToken<List<Task>>() {}.type
//                )
//
//                taskList.forEach { task ->
//                    addTaskTV(task)
//                }
//            }
//        }
//    }

    fun initDataStoreListener() {
        lifecycleScope.launch {
            dataStore.data.collect { preferences ->
                findViewById<LinearLayout>(R.id.taskLinearLayout).removeAllViews()
                val jsonString = preferences[TASKS_KEY] ?: "[]"
                val taskList: List<Task> = Gson().fromJson(
                    jsonString,
                    object : TypeToken<List<Task>>() {}.type
                )
                taskList.forEach { task ->
                    addTaskTV(task)
                }
            }
        }
    }
}