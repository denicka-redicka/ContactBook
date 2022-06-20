package com.example.contactbook.presentation.features.main.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.contactbook.data.Contact
import com.example.contactbook.data.ContactRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsListViewModel(application: Application) : AndroidViewModel(application) {

    private val contactsMutableData = MutableLiveData<List<Contact>>()

    val contacts: LiveData<List<Contact>> = contactsMutableData

    private val dao = ContactRoomDatabase.getInstance(application).getContactDao()

    fun loadContacts() {
        viewModelScope.launch{
            withContext(Dispatchers.IO) {
                contactsMutableData.postValue(dao.getAll())
            }
        }
    }
}
