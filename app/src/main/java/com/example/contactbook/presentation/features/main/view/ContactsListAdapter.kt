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
        holder.bind(contacts[position], onClickContact)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<ImageView>(R.id.image_contact)
        private val nameText = itemView.findViewById<TextView>(R.id.contact_name)
        private val phoneText = itemView.findViewById<TextView>(R.id.phone_text)

        fun bind(contact: Contact, onClick: (contactNumber: Long) -> Unit) {
            nameText.text = "${contact.firstName} ${contact.secondName}"
            phoneText.text = "${contact.phoneNumber}"

            itemView.setOnClickListener {
                onClick(contact.phoneNumber)
            }
        }
    }
}