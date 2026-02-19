package com.pagliaccio.timomancer

import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding

data class Task(
    val id: Int = System.currentTimeMillis().toInt(), // simple unique ID
    val title: String
//    var isCompleted: Boolean = false
    //add tasktheme or tasktype or the nessecity
)
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val addButton = findViewById<ImageButton>(R.id.addButton)

        addButton.setOnClickListener {
            val editText = EditText(this)

            AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setMessage("Enter task name:")
                .setView(editText)
                .setPositiveButton("Add") { dialog, which ->
                    var task = Task(title = editText.text.toString())
                    addTaskTV(task)
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
}