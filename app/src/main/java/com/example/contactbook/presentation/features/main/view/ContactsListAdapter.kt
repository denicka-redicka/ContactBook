package com.example.contactbook.presentation.features.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.data.Contact
import java.util.*

class ContactsListAdapter(private val onClickContact: (contactNumber: Long) -> Unit): RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    private val contacts: MutableList<Contact> = LinkedList()

    fun setData(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_contact, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when {
            position == 0 -> holder.bind(contacts[position], true, onClickContact)
            contacts[position].getDisplayName().first().lowercase() == contacts[position-1].getDisplayName().first().lowercase() ->
                holder.bind(contacts[position], false, onClickContact)
            else -> holder.bind(contacts[position], true, onClickContact)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.image_contact)
        private val nameText = itemView.findViewById<TextView>(R.id.contact_name)
        private val phoneText = itemView.findViewById<TextView>(R.id.phone_text)
        private val letterHeader = itemView.findViewById<TextView>(R.id.letter_header)
        private companion object {
            private const val HEADER_EMPTY_NAME = "#"
        }

        fun bind(contact: Contact, isShowingHeader: Boolean, onClick: (contactNumber: Long) -> Unit) {
            nameText.text = contact.getDisplayName()
            phoneText.text = "${contact.phoneNumber}"

            if (isShowingHeader) {
                letterHeader.visibility = View.VISIBLE
                letterHeader.text = if (contact.getDisplayName().isNotEmpty())
                    contact.getDisplayName().first().toString() else HEADER_EMPTY_NAME
            } else letterHeader.visibility = View.GONE

            itemView.setOnClickListener {
                onClick(contact.phoneNumber)
            }
        }
    }
}