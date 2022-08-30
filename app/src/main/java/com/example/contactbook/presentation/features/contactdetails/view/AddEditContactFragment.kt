package com.example.contactbook.presentation.features.contactdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.contactbook.R
import com.example.contactbook.data.Contact
import com.example.contactbook.databinding.ActivityContactDetailsBinding
import com.example.contactbook.presentation.features.contactdetails.viewmodel.ContactDetailsViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_contact.view.*

class AddEditContactFragment : Fragment() {

    private var state: Int? = null
    private var oldContact: Contact? = null
    private val viewModel: ContactDetailsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        state = arguments?.getInt(PARAM_STATE) ?: STATE_ADD_CONTACT
        viewModel.detailsLiveData.observe(viewLifecycleOwner) { contact ->
            oldContact = contact
            viewModel.bindEditableUi(view, contact)
        }

        if (state == STATE_EDIT_CONTACT) {
            val phoneNumber = arguments?.getLong(PARAM_PHONE_NUMBER)
            if (phoneNumber != null)
                viewModel.loadContactDetails(phoneNumber)
            else Toast.makeText(context, R.string.toast_error_msg, Toast.LENGTH_SHORT).show()
        }

        val saveButton = view.saveButton
        if (state == STATE_EDIT_CONTACT) saveButton.text = "Update contact"
        saveButton.setOnClickListener {
            val newContact = createContactFromScreen(
                view.editPhoneText,
                view.editNameText,
                view.editSecondNameText,
                view.editEmailText
            )

            when (state) {
                STATE_ADD_CONTACT -> if (newContact != null) viewModel.loadContactToDb(newContact)
                STATE_EDIT_CONTACT -> saveEditedContact(oldContact, newContact)
            }
        }
    }

    private fun saveEditedContact(oldContact: Contact?, newContact: Contact?) {
        if (newContact != null && oldContact != null )
            when {
                oldContact.phoneNumber != newContact.phoneNumber -> viewModel.updateContactWithNumber(oldContact, newContact)
                newContact == oldContact -> Toast.makeText(context, R.string.toast_error_msg, Toast.LENGTH_SHORT).show()
                oldContact.phoneNumber == newContact.phoneNumber -> viewModel.updateContactInDb(newContact)
            }
    }

    private fun createContactFromScreen(
        phoneEditText: EditText,
        nameEditText: EditText,
        secondNameEditText: EditText,
        emailEditText: EditText
    ): Contact? = if (phoneEditText.text.toString().isNotEmpty()) {
        Contact(
            phoneEditText.text.toString().toLong(),
            nameEditText.text.toString(),
            secondNameEditText.text.toString(),
            emailEditText.text.toString(),
            ""
        )
    } else {
        Toast.makeText(context, "Contact can't be empty!", Toast.LENGTH_SHORT).show()
        null
    }

    companion object {
        const val STATE_ADD_CONTACT = 0
        const val STATE_EDIT_CONTACT = 1
        private const val PARAM_STATE = "state"
        private const val PARAM_PHONE_NUMBER = "phoneNumber"

        fun loadFragment(state: Int, phoneNumber: Long, viewModel: ContactDetailsViewModel) =
            AddEditContactFragment().also {
                val args = bundleOf(
                    PARAM_STATE to state,
                    PARAM_PHONE_NUMBER to phoneNumber
                )
                it.arguments = args
            }
    }
}