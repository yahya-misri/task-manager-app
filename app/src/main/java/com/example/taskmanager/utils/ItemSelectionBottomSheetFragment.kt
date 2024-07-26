package com.example.taskmanager.utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.NonNull
import com.example.taskmanager.databinding.ItemSelectionBottomSheetBinding
import com.example.taskmanager.domain.model.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.taskmanager.presentation.view.ParentCategoryAdapter


class ItemSelectionBottomSheetFragment(var itemCallback: ItemSelectionCallback) :
    BottomSheetDialogFragment() {
    private lateinit var itemSelectionBottomSheetBinding: ItemSelectionBottomSheetBinding
    private var behaviour: BottomSheetBehavior<View>? = null

    companion object {
        fun newInstance(
            list: ArrayList<Task>,
            ib: ItemSelectionCallback
        ): ItemSelectionBottomSheetFragment {
            val args = Bundle()
            args.putSerializable("data", list)
            val fragment = ItemSelectionBottomSheetFragment(ib)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemSelectionBottomSheetBinding =
            ItemSelectionBottomSheetBinding.inflate(inflater, container, false)


        var data = arguments?.getSerializable("data") as ArrayList<*>


        itemSelectionBottomSheetBinding.btnCancel.setOnClickListener {
            dismiss()
        }

        itemSelectionBottomSheetBinding.tvTitle.setOnClickListener {
            dismiss()
        }

        var parentCatAdapter = ParentCategoryAdapter(requireContext(),
            data as ArrayList<Task>, object : ItemSelectionCallback {
                override fun selectedItem(parentCategoryData: Task) {
                    itemCallback.selectedItem(parentCategoryData)
                    dismiss()
                }



            })
        itemSelectionBottomSheetBinding.recvItem.adapter = parentCatAdapter


        return itemSelectionBottomSheetBinding.root
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog: BottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout: View? =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour!!.isHideable = false
                behaviour!!.isDraggable = false
                behaviour?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }



}