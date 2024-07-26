package com.example.taskmanager.presentation.view.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentTaskListBinding
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.presentation.view.addTasks.AddTaskFragment
import com.example.taskmanager.utils.convertObjectToString
import com.example.taskmanager.presentation.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private lateinit var taskListBinding: FragmentTaskListBinding
    private val viewModel: TaskViewModel by viewModels()

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskListBinding = FragmentTaskListBinding.inflate(inflater, container, false);
        taskAdapter = TaskAdapter(
            emptyList(),
            this::onTaskClicked,
            this::onEditTaskClicked,
            this::onDeleteTaskClicked
        )

        taskListBinding.recyclerViewTasks.adapter = taskAdapter
        taskListBinding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        taskListBinding.recyclerViewTasks.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.tasks.onEach { tasks ->
            taskAdapter = TaskAdapter(
                tasks,
                this::onTaskClicked,
                this::onEditTaskClicked,
                this::onDeleteTaskClicked
            )
            taskListBinding.recyclerViewTasks.adapter = taskAdapter
        }.launchIn(lifecycleScope)

        taskListBinding.fabAddTask.setOnClickListener {

            findNavController().navigate(R.id.action_add_task)

        }
        // Inflate the layout for this fragment
        return taskListBinding.root
    }

    private fun onTaskClicked(task: Task) {

        val bundle = Bundle().apply {
            putBoolean(AddTaskFragment.ARG_SHOW_COMPLETE, true)
            putString(AddTaskFragment.ARG_task, task.convertObjectToString())
        }
        findNavController().navigate(R.id.addTaskFragment, bundle)
    }

    private fun onEditTaskClicked(task: Task) {
        val bundle = Bundle().apply {
            putBoolean(AddTaskFragment.ARG_SHOW_update, true)
            putString(AddTaskFragment.ARG_task, task.convertObjectToString())
        }
        findNavController().navigate(R.id.addTaskFragment, bundle)

    }

    private fun onDeleteTaskClicked(task: Task) {
        viewModel.deleteTask(task.copy(isComplete = !task.isComplete))
        Snackbar.make(
            taskListBinding.root,
            "Task Deleted Successfully!",
            Snackbar.LENGTH_SHORT
        ).show()

    }
}