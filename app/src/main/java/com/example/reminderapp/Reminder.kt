package com.example.reminderapp

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


//TODO IDE is complaining but it still compiles, this is a know bug with the Parcelize..
@Parcelize
@Entity(tableName = "reminderTable")
data class Reminder (
    @ColumnInfo(name = "reminder")
    var reminder: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)  : Parcelable