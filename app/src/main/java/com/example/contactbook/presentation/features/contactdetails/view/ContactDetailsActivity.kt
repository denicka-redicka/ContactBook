package com.example.contactbook.presentation.features.contactdetails.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.contactbook.R
import com.example.contactbook.presentation.features.contactdetails.viewmodel.ContactDetailsViewModel

class ContactDetailsActivity : AppCompatActivity() {

    private val viewModel by viewModels<ContactDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contcat_details)
        window.apply {
            statusBarColor = Color.TRANSPARENT

            if (savedInstanceState == null) {
                intent?.extras?.getLong(CONTACT_DETAIL).also {openFragment(it)}
            }
        }

    }

    private fun openFragment(phoneNumber: Long?) {
        if (phoneNumber != null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_contact_detail, ContactDetailsFragment.create(phoneNumber, viewModel))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_contact_detail, AddEditContactFragment(viewModel))
                .commit()
        }
    }


    companion object {
        const val CONTACT_DETAIL = "contactDetail"
    }
}