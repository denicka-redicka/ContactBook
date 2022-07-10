package com.example.contactbook.presentation.features.contactdetails.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.contactbook.R
import com.example.contactbook.databinding.ActivityContactDetailsBinding
import com.example.contactbook.presentation.features.contactdetails.viewmodel.ContactDetailsViewModel
import kotlinx.android.synthetic.main.activity_contact_details.*

class ContactDetailsActivity : AppCompatActivity(),
    ContactDetailsFragment.OnItemsClickedListener {

    private lateinit var binding: ActivityContactDetailsBinding
    private val viewModel by viewModels<ContactDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_details)
        binding.viewModel = viewModel

        window.apply { statusBarColor = Color.TRANSPARENT }

        if (savedInstanceState == null) {
            intent?.extras?.getLong(CONTACT_DETAIL).also { openFragment(it) }
        }

        viewModel.updatingStateLiveData.observe(this) { phoneNumber ->
            setResult(Activity.RESULT_OK, Intent().putExtra(CONTACT_DETAIL, phoneNumber))
            if (supportFragmentManager.backStackEntryCount == 0) {
                onBackPressed()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    private fun openFragment(phoneNumber: Long?) {
        if (phoneNumber != null) {
            supportFragmentManager.beginTransaction()
                .add(
                    frameContactList.id,
                    ContactDetailsFragment.create(phoneNumber)
                )
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(frameContactList.id, AddEditContactFragment())
                .commit()
        }
    }


    companion object {
        const val CONTACT_DETAIL = "contactDetail"
    }

    override fun onBackItemClicked() {
        onBackPressed()
    }

    override fun onChangedItemClicked(phoneNumber: Long) {
        supportFragmentManager.beginTransaction()
            .add(frameContactList.id,
                AddEditContactFragment
                    .loadFragment(
                        AddEditContactFragment.STATE_EDIT_CONTACT,
                        phoneNumber,
                        viewModel
                    )
            )
            .addToBackStack(null)
            .commit()
    }
}
