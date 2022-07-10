package com.example.contactbook.presentation.features.contactdetails.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.Contact
import com.example.contactbook.data.ContactRoomDatabase
import kotlinx.android.synthetic.main.fragment_add_edit_contact.view.*
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*
import kotlinx.coroutines.*

class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val detailsMutableLiveData = MutableLiveData<Contact>()
    private val updatingStateMutableLiveData = MutableLiveData<Long>()

    val detailsLiveData: LiveData<Contact> = detailsMutableLiveData
    val updatingStateLiveData: LiveData<Long> = updatingStateMutableLiveData


    private val handlerException = CoroutineExceptionHandler {
        coroutineContext, throwable ->
        Log.d("DB exception", "exception handled: ${throwable.message}")
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO
                                                        + SupervisorJob()
                                                        + handlerException)


    fun bindEditableUi(view: View, contact: Contact) {

        view.editPhoneText.setText(contact.phoneNumber.toString())
        if (contact.firstName.isNotEmpty()) view.editNameText.setText(contact.firstName)
        if (contact.secondName.isNotEmpty()) view.editSecondNameText.setText(contact.secondName)
        if (contact.email.isNotEmpty()) view.editEmailText.setText(contact.email)
    }

    fun bindShownUi(view: View, contact: Contact) {
        view.nameText.text = contact.getDisplayName()
        view.phoneText.text = contact.phoneNumber.toString()
        view.emailText.text = contact.email
    }
    private val dao = ContactRoomDatabase.getInstance(application).getContactDao()

    fun loadContactDetails(phoneNumber: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val contact = dao.getContact(phoneNumber)
                if (contact != null) {
                    detailsMutableLiveData.postValue(contact)
                }
            }
        }
    }

    fun updateContactInDb(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.update(contact)
                updatingStateMutableLiveData.postValue(contact.phoneNumber)
            }
        }
    }

    fun updateContactWithNumber(oldContact: Contact, newContact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteContact(oldContact.phoneNumber)
                dao.insert(newContact)
                updatingStateMutableLiveData.postValue(newContact.phoneNumber)
            }
        }
    }

    fun loadContactToDb(contact: Contact) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                dao.insert(contact)
                updatingStateMutableLiveData.postValue(contact.phoneNumber)
            }
        }
    }

    fun removeContactFromDB(phoneNumber: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteContact(phoneNumber)
                updatingStateMutableLiveData.postValue(phoneNumber)
            }
        }
    }
}