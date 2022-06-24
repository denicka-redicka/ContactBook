package com.example.contactbook.presentation.features.main

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactbook.R
import com.example.contactbook.presentation.features.contactdetails.view.ContactDetailsActivity
import com.example.contactbook.presentation.features.contactdetails.view.ContactDetailsActivity.Companion.CONTACT_DETAIL
import com.example.contactbook.presentation.features.main.view.ContactsListAdapter
import com.example.contactbook.presentation.features.main.viewmodel.ContactsListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ContactsListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.apply {
            statusBarColor = Color.TRANSPARENT
        }

        val contactsList = findViewById<RecyclerView>(R.id.contact_list)
        val adapter = ContactsListAdapter{ phoneNumber ->
            intent = Intent(applicationContext, ContactDetailsActivity::class.java)
            intent.putExtra(CONTACT_DETAIL, phoneNumber)
            startActivity(intent)
        }
        contactsList.adapter = adapter
        contactsList.layoutManager = GridLayoutManager(this, 1)
        loadDataToAdapter(adapter)
        if (savedInstanceState == null) {
            viewModel.loadContacts()
        }
        val addContactButton = findViewById<FloatingActionButton>(R.id.add_contact_button)
        addContactButton.setOnClickListener {
            startActivity(Intent(applicationContext, ContactDetailsActivity::class.java))
        }
    }

    private fun loadDataToAdapter(adapter: ContactsListAdapter) {
        viewModel.contacts.observe(this, Observer { contacts ->
            adapter.setData(contacts)
        })
    }
}