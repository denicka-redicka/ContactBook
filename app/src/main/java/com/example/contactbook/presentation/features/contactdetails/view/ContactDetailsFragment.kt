package com.example.contactbook.presentation.features.contactdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.contactbook.R
import com.example.contactbook.data.Contact
import com.example.contactbook.presentation.features.contactdetails.viewmodel.ContactDetailsViewModel

class ContactDetailsFragment(private val viewModel: ContactDetailsViewModel): Fragment() {

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
            bindUi(contact, view)
        }
    }

    private fun bindUi(contact: Contact, view: View) {
        val nameText = view.findViewById<TextView>(R.id.name_txt)
        val phoneText = view.findViewById<TextView>(R.id.phone_txt)
        val emailText = view.findViewById<TextView>(R.id.email_txt)
        val backArrow = view.findViewById<View>(R.id.arrow_back)
        val contactHeaderText = view.findViewById<TextView>(R.id.contact_text)


        nameText.text = "${contact.firstName} ${contact.secondName}"
        phoneText.text = contact.phoneNumber.toString()
        emailText.text = contact.email

        if (contact.imagePath.isEmpty()) {
            backArrow.background = resources.getDrawable(R.drawable.black_arrow)
            contactHeaderText.setTextColor(R.color.black)
        }
    }

    override fun onResume() {
        super.onResume()

        val phoneNumber = arguments?.get(PARAM_PHONE_NUMBER) as? Long ?: return
        viewModel.loadContactDetails(phoneNumber)
    }


    companion object {
        private const val PARAM_PHONE_NUMBER = "phoneNumber"

        fun create(phoneNumber: Long, viewModel: ContactDetailsViewModel) = ContactDetailsFragment(viewModel).also {
            val args = bundleOf(PARAM_PHONE_NUMBER to phoneNumber)

            it.arguments = args
        }
    }
}