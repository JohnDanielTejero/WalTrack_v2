package com.jdt.waltrackv2.view.fragments

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdt.waltrackv2.databinding.DialogActionItemsBinding

class ItemActionsDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogActionItemsBinding
    private var onEditClickListener: (() -> Unit)? = null
    private var onDeleteClickListener: (() -> Unit)? = null

    fun setEditClickListener(listener: () -> Unit) {
        onEditClickListener = listener
    }

    fun setDeleteClickListener(listener: () -> Unit) {
        onDeleteClickListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogActionItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editButton = binding.editButton
        val deleteButton = binding.deleteButton

        editButton.setOnClickListener {
            onEditClickListener?.invoke()
            dismiss()
        }

        deleteButton.setOnClickListener {
            onDeleteClickListener?.invoke()
            dismiss()
        }
    }

}