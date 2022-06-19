package com.example.contactbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Contact::class], version = 1)
abstract class ContactRoomDatabase: RoomDatabase() {

    abstract fun getContactDao(): ContactDao

    companion object {

        private var INTSANCE: ContactRoomDatabase? = null
        private const val CONTACT_DATABASE = "Contact.db"

        fun getInstance(context: Context): ContactRoomDatabase {
            if (INTSANCE == null) {
                synchronized(ContactRoomDatabase::class.java) {
                    if (INTSANCE == null) {
                        INTSANCE = Room.databaseBuilder(
                            context,
                            ContactRoomDatabase::class.java,
                            CONTACT_DATABASE
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INTSANCE!!
        }
    }
}