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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val tabTitles = mutableListOf("All", "Personal", "Work", "School")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Create adapter with the list
        val adapter = ViewPagerAdapter(this, tabTitles)
        viewPager.adapter = adapter

        // Connect tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        val addButton = findViewById<ImageButton>(R.id.addButton)
        val clearButton = findViewById<ImageButton>(R.id.clearButton)

//        addButton.setOnClickListener {
//            val editText = EditText(this)
//
//            AlertDialog.Builder(this)
//                .setTitle("Add Task")
//                .setMessage("Enter task name:")
//                .setView(editText)
//                .setPositiveButton("Add") { dialog, which ->
//                    var task = Task(title = editText.text.toString())
//
//                    lifecycleScope.launch {
//                        dataStore.edit { preferences ->
//                            val jsonString = preferences[TASKS_KEY] ?: "[]"
//                            val existingList: MutableList<Task> = Gson().fromJson(
//                                jsonString,
//                                object : TypeToken<MutableList<Task>>() {}.type
//                            ) ?: mutableListOf()
//
//                            existingList.add(task)
//
//                            preferences[TASKS_KEY] = Gson().toJson(existingList)
//                        }
//                    }
////                    loadTasks()
//                }
//                .setNegativeButton("Cancel", null)
//                .show()
//        }

//        clearButton.setOnClickListener {
//            lifecycleScope.launch {
//                dataStore.edit { preferences ->
//                    preferences.remove(TASKS_KEY)
//                }
//            }
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}