package com.example.contactbook.presentation.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.presentation.features.main.view.ContactsListAdapter
import com.example.contactbook.presentation.features.main.viewmodel.ContactsListViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ContactsListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contactsList = findViewById<RecyclerView>(R.id.contact_list)
        val adapter = ContactsListAdapter()
        contactsList.adapter = adapter
        contactsList.layoutManager = GridLayoutManager(this, 1)
        loadDataToAdapter(adapter)
        viewModel.loadContacts()
    }


    private fun loadDataToAdapter(adapter: ContactsListAdapter) {
        viewModel.contacts.observe(this, Observer { contacts ->
            adapter.setData(contacts)
        })
    }
}