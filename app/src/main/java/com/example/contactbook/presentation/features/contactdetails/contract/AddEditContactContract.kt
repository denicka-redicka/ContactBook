package com.example.contactbook.presentation.features.contactdetails.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.contactbook.presentation.features.contactdetails.view.ContactDetailsActivity
import com.example.contactbook.presentation.features.contactdetails.view.ContactDetailsActivity.Companion.CONTACT_DETAIL

class AddEditContactContract : ActivityResultContract<Long?, Long?>() {

    override fun createIntent(context: Context, input: Long?): Intent {
        return if (input != null) Intent(context, ContactDetailsActivity::class.java).putExtra(CONTACT_DETAIL, input)
         else Intent(context, ContactDetailsActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Long? {
        if (resultCode != Activity.RESULT_OK) return null
        return if (intent == null) null else intent.extras?.getLong(CONTACT_DETAIL)
    }


}