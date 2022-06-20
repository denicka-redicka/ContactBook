package com.example.contactbook.presentation.features.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.contactbook.data.Contact
import com.example.contactbook.data.ContactRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsListViewModel(application: Application) : AndroidViewModel(application) {

    private val contactsMutableData = MutableLiveData<List<Contact>>()
    private val errorMessageMutableData = MutableLiveData<String>()

    val contacts: LiveData<List<Contact>> = contactsMutableData
    val errorMessages: LiveData<String> = errorMessageMutableData

    private val dao = ContactRoomDatabase.getInstance(application).getContactDao()

    fun loadContacts() {
        viewModelScope.launch{
            val contacts = withContext(Dispatchers.IO) {
                return@withContext dao.getAll()
            }
            contactsMutableData.value = contacts
        }
    }
}
