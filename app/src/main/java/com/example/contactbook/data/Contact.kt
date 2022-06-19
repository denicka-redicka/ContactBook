package com.example.contactbook.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ( tableName = "Contact" )
data class Contact (
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "second_name")
    val secondName: String,
    @PrimaryKey
    @ColumnInfo(name = "phone_number")
    val phoneNumber: Long,
    @ColumnInfo(name = "email")
    val email: String
    ) {
}