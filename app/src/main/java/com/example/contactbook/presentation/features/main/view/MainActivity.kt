package com.example.contactbook.presentation.features.main.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.contactbook.R
import com.example.contactbook.presentation.features.contactdetails.contract.AddEditContactContract
import com.example.contactbook.presentation.features.main.viewmodel.ContactsListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ContactsListViewModel>()

    private val getPhoneNumber = registerForActivityResult(AddEditContactContract()) { phoneNumber ->
        if (phoneNumber != null) viewModel.loadContacts()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.apply {
            statusBarColor = Color.TRANSPARENT
        }

        val adapter = ContactsListAdapter { phoneNumber ->
            getPhoneNumber.launch(phoneNumber)
        }
        contactsList.adapter = adapter
        contactsList.layoutManager = GridLayoutManager(this, 1)
        loadDataToAdapter(adapter)
        if (savedInstanceState == null) {
            viewModel.loadContacts()
        }

        addContactButton.setOnClickListener {
            getPhoneNumber.launch(null)
        }
    }

    private fun loadDataToAdapter(adapter: ContactsListAdapter) {
        viewModel.contacts.observe(this, Observer { contacts ->
            adapter.setData(contacts)
        })
    }
}