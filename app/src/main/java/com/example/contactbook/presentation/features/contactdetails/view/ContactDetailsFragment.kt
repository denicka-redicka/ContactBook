package com.example.contactbook.presentation.features.contactdetails.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.contactbook.R
import com.example.contactbook.data.Contact
import com.example.contactbook.databinding.ActivityContactDetailsBinding
import com.example.contactbook.presentation.features.contactdetails.viewmodel.ContactDetailsViewModel
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*

class ContactDetailsFragment: Fragment() {

    private var onItemsClickedListener: OnItemsClickedListener? = null
    private val viewModel: ContactDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailsLiveData.observe(viewLifecycleOwner) { contact ->
            viewModel.bindShownUi(view, contact)
            bindTopUi(contact, view)
        }

        viewModel.updatingStateLiveData.observe(viewLifecycleOwner) { phoneNumber ->
            viewModel.loadContactDetails(phoneNumber)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemsClickedListener) onItemsClickedListener = context
    }

    override fun onDetach() {
        super.onDetach()
        onItemsClickedListener = null
    }

    private fun bindTopUi(contact: Contact, view: View) {
        val backArrow = view.arrowBack
        val contactHeaderText = view.contactText
        val deleteButton = view.deleteIcon
        val changeButton = view.changeIcon
        val backViewItem = view.arrowBack

        if (contact.imagePath.isEmpty()) {
            backArrow.background = resources.getDrawable(R.drawable.black_arrow)
            contactHeaderText.setTextColor(R.color.black)
        } else {
            backArrow.background = resources.getDrawable(R.drawable.back_arrow)
            contactHeaderText.setTextColor(R.color.white)
        }

        backViewItem.setOnClickListener {
            onItemsClickedListener?.onBackItemClicked()
        }

        deleteButton.setOnClickListener {
            onDeleteButtonPressed(contact.phoneNumber)
        }

        changeButton.setOnClickListener {
            onItemsClickedListener?.onChangedItemClicked(contact.phoneNumber)
        }
    }

    private fun onDeleteButtonPressed(phoneNumber: Long) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(R.string.delete_dialog_msg)
            .setPositiveButton(R.string.yes) { dialog, id ->
                viewModel.removeContactFromDB(phoneNumber)
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.dismiss()
            }
        dialogBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()

        val phoneNumber = arguments?.get(PARAM_PHONE_NUMBER) as? Long ?: return
        viewModel.loadContactDetails(phoneNumber)
    }

    interface OnItemsClickedListener {
        fun onBackItemClicked()
        fun onChangedItemClicked(phoneNumber: Long)
    }

    companion object {
        private const val PARAM_PHONE_NUMBER = "phoneNumber"

        fun create(phoneNumber: Long) = ContactDetailsFragment().also {
            val args = bundleOf(PARAM_PHONE_NUMBER to phoneNumber)
            it.arguments = args
        }
    }
}