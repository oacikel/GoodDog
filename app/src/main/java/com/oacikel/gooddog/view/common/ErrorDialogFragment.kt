package com.oacikel.gooddog.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.oacikel.gooddog.R
import com.oacikel.gooddog.databinding.DialogErrorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialogFragment : DialogFragment() {
    lateinit var binding: DialogErrorBinding

    companion object {
        var errorMessage: String? = null
        var onlyMessage: Boolean? = false
        var dialogOkButtonCallback: DialogOkButtonCallback? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_error, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        binding.dialogFragment = this@ErrorDialogFragment
        binding.errorMessage = errorMessage
        binding.buttonOk.setOnClickListener {
            this@ErrorDialogFragment.dismiss()
            dialogOkButtonCallback?.onOKClicked()
        }
        if (onlyMessage != null && onlyMessage as Boolean)
            binding.textViewTitle.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            dialog.setCanceledOnTouchOutside(true)
        }
    }
}