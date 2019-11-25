package com.example.reminderapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reminderapp.Reminder


//annotate with Database and specify all the entities, in this example we will just be working with Reminders
@Database(entities = [Reminder::class], version = 1, exportSchema = false)
abstract class ReminderRoomDatabase : RoomDatabase() {

    //not quite sure how /what this is for
    abstract fun reminderDao(): ReminderDao

    companion object {
        //The datbabase name
        private const val DATABASE_NAME = "REMINDER_DATABASE"

        @Volatile
        private var reminderRoomDatabaseInstance: ReminderRoomDatabase? = null

        //function to create the database object/ return a instance we can use for futrther communication
        fun getDatabase(context: Context): ReminderRoomDatabase? {
            if (reminderRoomDatabaseInstance == null) {
                synchronized(ReminderRoomDatabase::class.java) {
                    if (reminderRoomDatabaseInstance == null) {
                        reminderRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            ReminderRoomDatabase::class.java, DATABASE_NAME
                        )
                            .build()
                    }
                }
            }
            return reminderRoomDatabaseInstance
        }
    }

}
