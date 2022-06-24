package com.example.contactbook.presentation.features.contactdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.contactbook.R
import com.example.contactbook.data.Contact
import com.example.contactbook.presentation.features.contactdetails.viewmodel.ContactDetailsViewModel

class AddEditContactFragment(private val viewModel: ContactDetailsViewModel) : Fragment() {

    private var state: Int? = null
    private var oldContact: Contact? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = view.findViewById<EditText>(R.id.edit_text_name)
        val secondNameEditText = view.findViewById<EditText>(R.id.edit_text_second_name)
        val phoneEditText = view.findViewById<EditText>(R.id.edit_text_phone)
        val emailEditText = view.findViewById<EditText>(R.id.edit_text_email)

        state = arguments?.getInt(PARAM_STATE) ?: STATE_ADD_CONTACT
        viewModel.detailsLiveData.observe(viewLifecycleOwner) { contact ->
            oldContact = contact
            bindUi(
                phoneEditText,
                nameEditText,
                secondNameEditText,
                emailEditText,
                contact
            )

        }
        if (state == STATE_EDIT_CONTACT) {
            val phoneNumber = arguments?.getLong(PARAM_PHONE_NUMBER)
            if (phoneNumber != null)
                viewModel.loadContactDetails(phoneNumber)
            else Toast.makeText(context, "Something wrong with contact", Toast.LENGTH_SHORT).show()
        }

        val saveButton = view.findViewById<Button>(R.id.save_button)
        if (state == STATE_EDIT_CONTACT) saveButton.text = "Update contact"
        saveButton.setOnClickListener {
            val newContact = createContactFromScreen(
                phoneEditText,
                nameEditText,
                secondNameEditText,
                emailEditText
            )

            when (state) {
                STATE_ADD_CONTACT -> if (newContact != null) viewModel.loadContactToDb(newContact)
                STATE_EDIT_CONTACT -> if (newContact != null && newContact != oldContact) viewModel.updateContactInDb(
                    newContact
                )
            }
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

    private fun bindUi(
        phoneEditText: EditText,
        nameEditText: EditText,
        secondNameEditText: EditText,
        emailEditText: EditText,
        contact: Contact
    ) {
        phoneEditText.setText(contact.phoneNumber.toString())
        if (contact.firstName.isNotEmpty()) nameEditText.setText(contact.firstName)
        if (contact.secondName.isNotEmpty()) secondNameEditText.setText(contact.secondName)
        if (contact.email.isNotEmpty()) emailEditText.setText(contact.email)
    }

    companion object {
        const val STATE_ADD_CONTACT = 0
        const val STATE_EDIT_CONTACT = 1
        private const val PARAM_STATE = "state"
        private const val PARAM_PHONE_NUMBER = "phoneNumber"

        fun loadFragment(state: Int, phoneNumber: Long, viewModel: ContactDetailsViewModel) =
            AddEditContactFragment(viewModel).also {
                val args = bundleOf(
                    PARAM_STATE to state,
                    PARAM_PHONE_NUMBER to phoneNumber
                )
                it.arguments = args
            }
    }
}