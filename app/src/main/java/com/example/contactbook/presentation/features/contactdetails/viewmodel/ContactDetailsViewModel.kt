package com.example.contactbook.presentation.features.contactdetails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.contactbook.data.Contact
import com.example.contactbook.data.ContactRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val detailsMutableLiveData = MutableLiveData<Contact>()

    val detailsLiveData: LiveData<Contact> = detailsMutableLiveData

    private val dao = ContactRoomDatabase.getInstance(application).getContactDao()

    fun loadContactDetails(phoneNumber: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                detailsMutableLiveData.postValue(dao.getContact(phoneNumber))
            }
        }
    }

    fun updateContactInDb(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.update(contact)
            }
        }
    }

    fun loadContactToDb(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.insert(contact)
            }
        }
    }

    fun removeContactFromDB(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteContact(contact.phoneNumber)
            }
        }
    }
}