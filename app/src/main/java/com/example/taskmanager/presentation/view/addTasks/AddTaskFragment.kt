package com.example.taskmanager.presentation.view.addTasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentAddTaskBinding
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.presentation.viewmodels.TaskViewModel
import com.example.taskmanager.utils.ItemSelectionBottomSheetFragment
import com.example.taskmanager.utils.ItemSelectionCallback
import com.example.taskmanager.utils.Resources
import com.example.taskmanager.utils.convertJsonToModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private lateinit var dependenciesAdapter: DependenciesAdapter

    // TODO: Rename and change types of parameters

    private var allTask = ArrayList<Task>()
    private var dependencies = ArrayList<Task>()
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var fragmentAddTaskBinding: FragmentAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if (it.containsKey(ARG_task)) {
                val taskStr = it.getString(ARG_task)
                viewModel.myTask = taskStr?.convertJsonToModel(Task::class.java)

            }
            viewModel.showCompleteButton = it.getBoolean(ARG_SHOW_COMPLETE)
            viewModel.showUpdateButton = it.getBoolean(ARG_SHOW_update)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        fragmentAddTaskBinding.taskvm = viewModel

        viewModel.reset.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
            }
        }
        viewModel.tasks.onEach { tasks ->

            /**
             * filter myTask from allTask
             */
            val filterTasks = tasks.filter { it.id != viewModel.myTask?.id }
            allTask.addAll(filterTasks)

            viewModel.myTask?.let {
                val setOFIds = it.dependencies.map { it }.toSet()

                dependencies = allTask.filter { task ->
                    task.id in setOFIds
                } as ArrayList<Task>

                viewModel.addDependent(dependencies)

                if (this::dependenciesAdapter.isInitialized) {
                    dependenciesAdapter.addAll(dependencies)
                }
            }

        }.launchIn(lifecycleScope)

        viewModel.myTask?.let {

            viewModel.name = it.name
            fragmentAddTaskBinding.etTask.setText(it.name)


            if (viewModel.showCompleteButton) {
                viewModel.getTaskDependent(it)
                fragmentAddTaskBinding.dependencies.isVisible = false
                fragmentAddTaskBinding.btnAddTask.text = "Mark as Completed"
                fragmentAddTaskBinding.btnAddTask.backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireContext(),
                        android.R.color.holo_green_dark
                    )
            } else {
                fragmentAddTaskBinding.dependencies.isVisible = false
                fragmentAddTaskBinding.btnAddTask.text = "Update"
            }
        }

        dependenciesAdapter = DependenciesAdapter(
            dependencies,
            this::onDeleteDependencyClicked
        )

        fragmentAddTaskBinding.rvDependencies.run {
            setAdapter(dependenciesAdapter)
            setLayoutManager(LinearLayoutManager(requireContext()))
        }


        clickListeners()

        setObservers()


        return fragmentAddTaskBinding.root
    }

    private fun setObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Error -> Snackbar.make(
                    fragmentAddTaskBinding.root,
                    it.message.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()

                is Resources.Loading -> Log.e("TAG", "setObservers: ")
                is Resources.Success -> Log.e("TAG", "setObservers: ")
            }
        }
    }

    private fun clickListeners() {
        fragmentAddTaskBinding.dependencies.setOnClickListener {
            var itemSelectionBottomSheetBinding = ItemSelectionBottomSheetFragment.newInstance(
                allTask,
                object : ItemSelectionCallback {
                    override fun selectedItem(parentCategoryData: Task) {

                        dependencies.add(parentCategoryData)
                        if (::dependenciesAdapter.isInitialized) {
                            dependenciesAdapter.notifyDataSetChanged()
                        }
                        viewModel.addDependent(dependencies)

                    }

                })
            itemSelectionBottomSheetBinding.show(
                childFragmentManager, "Dependencies"
            )

        }
    }

    private fun onDeleteDependencyClicked(task: Task) {

    }

    companion object {
        const val ARG_SHOW_update = "show_update"
        const val ARG_SHOW_COMPLETE = "show_complete"
        const val ARG_task = "arg_task"

    }
}