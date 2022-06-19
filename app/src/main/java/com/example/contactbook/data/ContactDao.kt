package com.example.contactbook.data

import androidx.room.*

@Dao
interface ContactDao {

    @Query("SELECT * from Contact")
    fun getAll(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(contact: Contact): Long

    @Update
    fun update(contact: Contact)

    @Query("DELETE FROM Contact WHERE phone_number = :phoneNumber")
    fun deleteContact(phoneNumber: Long)
}