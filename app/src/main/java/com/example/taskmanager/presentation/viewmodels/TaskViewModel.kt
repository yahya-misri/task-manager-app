package com.example.taskmanager.presentation.viewmodels

import android.text.Editable
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.DependencyTask
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.usecase.AddDependentTask
import com.example.taskmanager.domain.usecase.AddTaskUseCase
import com.example.taskmanager.domain.usecase.CanCompleteTaskUseCase
import com.example.taskmanager.domain.usecase.CheckDependents
import com.example.taskmanager.domain.usecase.DeleteDependentUseCase
import com.example.taskmanager.domain.usecase.DeleteTaskUseCase
import com.example.taskmanager.domain.usecase.GetDependents
import com.example.taskmanager.domain.usecase.GetTasksUseCase
import com.example.taskmanager.domain.usecase.UpdateTaskUseCase
import com.example.taskmanager.utils.Resources
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val canCompleteTaskUseCase: CanCompleteTaskUseCase,
    private val addDependentTask: AddDependentTask,
    private val getDependentTask: GetDependents,
    private val checkDependents: CheckDependents,
    private val deleteDependentUseCase: DeleteDependentUseCase

) : ViewModel() {
    private lateinit var dpTask: List<Task>
    var name: String = ""
    var showCompleteButton: Boolean = false
    var showUpdateButton: Boolean = false
    var myTask: Task? = null

    var name_error: ObservableField<String> = ObservableField()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks
    var reset: MutableLiveData<Boolean> = MutableLiveData()
    private var dependentTask: ArrayList<Task> = arrayListOf()
    private var taskDependents: ArrayList<DependencyTask> = arrayListOf()
    val uiState: MutableLiveData<Resources<Unit>> = MutableLiveData()

    init {
        viewModelScope.launch {
            getTasksUseCase().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun getTaskDependent(task: Task)
    {
        viewModelScope.launch {
            getDependentTask(task.id).collect{
                    taskDependents.addAll(it)


            }

        }

    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
            deleteDependentUseCase.invoke(task.id)
        }
    }

    fun addDependent(task: ArrayList<Task>) {
        dependentTask.addAll(task)

    }

    fun addDepTask(dependentTask: DependencyTask) {
        viewModelScope.launch {
            addDependentTask(dependentTask)
        }
    }



    fun nameValidator(editable: Editable) {
        if (editable.toString().isEmpty()) {
            name_error.set("Task Should not be Empty!")
        } else {
            name = editable.toString()
            name_error.set(null)
        }
    }

    fun addTaskClick(view: View) {
        if (showCompleteButton) {
            CoroutineScope(Dispatchers.IO).launch {
                var ids = taskDependents.map { it.dependentId } as ArrayList<Int>
                dpTask= checkDependents.invoke(ids)
                if(dpTask.isNotEmpty())
                {
                    /**
                     * All dependent tasks are not complete
                     */
                    uiState.postValue(Resources.Error("Complete Dependency Task First to Mark this task as Complete"))

                }
                else{

                    myTask?.isComplete=true
                    updateTask(myTask!!)
                    Snackbar.make(
                        view,
                        "Task Completed Successfully!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    reset.postValue(true)
                }
            }





        } else if (showUpdateButton) {

            if (name.isBlank()) {
                name_error.set("Task Should not be Empty!")
                return
            }
            myTask?.name=name
            updateTask(myTask!!)
            Snackbar.make(
                view,
                "Task Updated Successfully!",
                Snackbar.LENGTH_SHORT
            ).show()
            reset.postValue(true)

        } else {
            if (name.isBlank()) {
                name_error.set("Task Should not be Empty!")
                return
            }
            var ids = ArrayList<Int>()
            if (dependentTask.isNotEmpty()) {
                dependentTask.forEach {
                    ids.add(it.id)
                }
            }
            myTask = Task(0, name, false, ids)
            addTask(myTask!!)
            viewModelScope.launch {
                if (dependentTask.isNotEmpty()) {
                    dependentTask.forEach {
                        var deptask = DependencyTask(0,_tasks.value.last().id+1,it.id)
                        addDepTask(deptask)

                    }
                }
            }
            Snackbar.make(
                view,
                "New Task Added!",
                Snackbar.LENGTH_SHORT
            ).show()
            reset.postValue(true)
        }

    }
}