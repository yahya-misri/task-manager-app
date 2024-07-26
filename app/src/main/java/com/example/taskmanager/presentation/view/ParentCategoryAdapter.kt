package com.example.taskmanager.presentation.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.utils.ItemSelectionCallback
import com.example.taskmanager.R
import com.example.taskmanager.databinding.ItemParentCategorySelectionLayoutBinding
import com.example.taskmanager.domain.model.Task


class ParentCategoryAdapter(
    var context: Context,
    var items: ArrayList<Task>,
    var itemSelectionCallback: ItemSelectionCallback
) : RecyclerView.Adapter<ParentCategoryAdapter.ParentCatHolder>() {


    class ParentCatHolder(var itemParentCategorySelectionLayoutBinding: ItemParentCategorySelectionLayoutBinding) :
        RecyclerView.ViewHolder(itemParentCategorySelectionLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCatHolder {
        var itemParentCategorySelectionLayoutBinding =
            DataBindingUtil.inflate<ItemParentCategorySelectionLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_parent_category_selection_layout,
                parent,
                false
            )
        return ParentCatHolder(itemParentCategorySelectionLayoutBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ParentCatHolder, position: Int) {

        holder.itemParentCategorySelectionLayoutBinding.item = items[position]
        holder.itemParentCategorySelectionLayoutBinding.tvName.setOnClickListener {
            itemSelectionCallback.selectedItem(items[position])
        }

    }

}