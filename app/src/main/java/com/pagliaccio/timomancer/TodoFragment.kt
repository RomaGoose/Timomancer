package com.pagliaccio.timomancer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class TodoFragment : Fragment() {
    private var tabType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabType = it.getString("TAB_TITLE") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            (requireContext() as MainActivity).dataStore.data.collect { preferences ->
                val container = view.findViewById<LinearLayout>(R.id.taskLinearLayout)
                container.removeAllViews()

                val jsonString = preferences[getTasksKey()] ?: "[]"
                val taskList: List<Task> = Gson().fromJson(
                    jsonString,
                    object : TypeToken<List<Task>>() {}.type
                )
                taskList.forEach { task ->
                    addTaskTV(container, task)
                }
            }
        }
    }

    companion object {
        fun newInstance(title: String): com.pagliaccio.timomancer.TodoFragment {
            val todoFragment = com.pagliaccio.timomancer.TodoFragment()
            val args = Bundle()
            args.putString("TAB_TITLE", title)
            todoFragment.arguments = args
            return todoFragment
        }
    }
    private fun addTaskTV(container: LinearLayout, task: Task) {
        val task_tv = TextView(requireContext())
        task_tv.text = task.title
        task_tv.id = task.id
        task_tv.setPadding(15.dp)
        task_tv.setTextAppearance(R.style.TodoListItem)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.bottomMargin = 15.dp
        task_tv.background = AppCompatResources.getDrawable(requireContext(),R.drawable.shape_round_corners)
        task_tv.layoutParams = params

        container.addView(task_tv)
    }

    private fun getTasksKey(): Preferences.Key<String> {
        return stringPreferencesKey("tasks_${tabType}")
    }
}