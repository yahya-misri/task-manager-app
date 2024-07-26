package com.example.taskmanager.presentation.view.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemTaskBinding
import com.example.taskmanager.domain.model.Task


class TaskAdapter(
    private val tasks: List<Task>,
    private val onTaskClicked: (Task) -> Unit,
    private val onEditTaskClicked: (Task) -> Unit,
    private val onDeleteTaskClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(var itemTaskBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val binding: ItemTaskBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_task, parent, false
        )

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(tasks[position]) {


            holder.itemTaskBinding.tdata = this
            if (this.isComplete) {
                holder.itemTaskBinding.icEdit.isVisible = false
//                holder.itemTaskBinding.icDelete.isVisible = false
                holder.itemTaskBinding.taskName.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_done_24,
                    0,
                    0,
                    0
                )

            }

            holder.itemTaskBinding.icEdit.setOnClickListener {
                onEditTaskClicked(this)
            }
            holder.itemTaskBinding.icDelete.setOnClickListener {
                onDeleteTaskClicked(this)
            }

            holder.itemView.setOnClickListener {
                onTaskClicked(this)
            }
        }
    }

    override fun getItemCount() = tasks.size
}