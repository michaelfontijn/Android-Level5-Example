package com.example.reminderapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.reminderapp.Reminder

class ReminderRepository(context: Context) {

    //and reference to the reminderDao (data access object) for actual communication to the database
    private var reminderDao: ReminderDao

    init {
        //use the supplied context to get an instance of the ReminderRoomDb, the dao is also initialized in this process.
        val reminderRoomDatabase = ReminderRoomDatabase.getDatabase(context)
        reminderDao = reminderRoomDatabase!!.reminderDao()
    }

    //use the Dao object to perform all the CRUD operations.
    fun getAllReminders(): LiveData<List<Reminder>>
    {
        return reminderDao.getAllReminders()
    }

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

}
