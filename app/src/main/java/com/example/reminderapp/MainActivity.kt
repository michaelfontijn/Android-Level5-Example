package com.example.reminderapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.database.MainActivityViewModel
import com.example.reminderapp.database.ReminderRepository

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ADD_REMINDER_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private val reminders = arrayListOf<Reminder>()
    private val reminderAdapter = ReminderAdapter(reminders)
    private lateinit var viewModel: MainActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //configure the onclick listener of the floating action button
        fab.setOnClickListener { view ->
            startAddActivity()
        }

        //initialize the view/ view model
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews(){
        //Initialize the recycler view with a linear layout manager adapter.
        rvReminders.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

        //set our adapter to the recyclerView on the activity(screen)
        rvReminders.adapter = reminderAdapter

        //add a vertical divider (line/ border) at the bottom of the items in the collection
        rvReminders.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        //attach the item touch helper to the recycler view
        createItemTouchHelper().attachToRecyclerView(rvReminders)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        // Observe reminders from the view model, update the list when the data is changed.
        viewModel.reminders.observe(this, Observer { reminders ->
            this@MainActivity.reminders.clear()
            this@MainActivity.reminders.addAll(reminders)
            reminderAdapter.notifyDataSetChanged()
        })
    }

    /***
     * this method is used to bind to the onSwipe and onMove method to the recycler view.
     */
    private fun createItemTouchHelper(): ItemTouchHelper{

        //create / configure the callback method on swipe left
        val callback = object :  ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //get the position and use this to get the reminder object to delete.
                val position = viewHolder.adapterPosition
                val reminderToRemove = reminders[position]

                viewModel.deleteReminder(reminderToRemove)

            }
        }
        return ItemTouchHelper(callback)
    }

    private fun startAddActivity() {
        val intent = Intent(this, Add_Activity::class.java)

        //start the activity saying we expect a result and pass a unique request code so it know where to return the result?
        startActivityForResult(intent, ADD_REMINDER_REQUEST_CODE)
    }

    //This method is called when a result is retrieved from another activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //check if th result was successful
        if (resultCode == Activity.RESULT_OK) {
            //search the matching request code (the on we used when making the intent
            when (requestCode) {
                //when we find a match we know for which intent this result is (in case we have made multiple)
                ADD_REMINDER_REQUEST_CODE -> {
                    //retrieve / parse the reminder object from the response and add it to the recyclerView.
                    val reminder = data!!.getParcelableExtra<Reminder>(EXTRA_REMINDER)

                    viewModel.insertReminder(reminder)
                    }


                }
            }
        super.onActivityResult(requestCode, resultCode, data)
        }
    }




