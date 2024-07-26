package com.example.taskmanager.presentation.view.addTasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.DependencyTaskBinding
import com.example.taskmanager.domain.model.Task


class DependenciesAdapter(
    taskList: List<Task>,
    private val onDeleteDependency: (Task) -> Unit
) : RecyclerView.Adapter<DependenciesAdapter.DependenciesViewHolder>() {
    var tasks: ArrayList<Task> = ArrayList()

    init {
        this.tasks = taskList as ArrayList<Task>
    }


    inner class DependenciesViewHolder(var binding: DependencyTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(task: Task) {
            with(binding) {

                cbTaskName.text = task.name
                cbTaskName.isEnabled = false
                cbTaskName.isChecked = task.isComplete

                icDelete.setOnClickListener {

                    onDeleteDependency(task)
                }

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int
    ): DependenciesViewHolder {

        val binding: DependencyTaskBinding = DependencyTaskBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        )


        return DependenciesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DependenciesViewHolder,
                                  position: Int
    ) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    fun add(item: Task?) {
        tasks.add(item!!)
        notifyItemInserted(tasks!!.size - 1)
    }

    fun addAll(items: List<Task>) {
        for (item in items) {
            add(item)
        }
    }

    fun remove(r: Task) {
        val position = tasks.indexOf(r)
        if (position > -1) {
            tasks!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }


}