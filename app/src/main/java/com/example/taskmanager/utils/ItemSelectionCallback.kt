package com.example.taskmanager.utils

import com.example.taskmanager.domain.model.Task


interface ItemSelectionCallback {
    fun selectedItem(parentCategoryData: Task)


}