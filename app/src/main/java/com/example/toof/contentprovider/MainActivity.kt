package com.example.toof.contentprovider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

const val PERMISSION_REQUES_CODE: Int = 999

class MainActivity : AppCompatActivity() {
    private val mContacts = ArrayList<Contact>()
    private lateinit var mAdapter: ContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = ContactAdapter(this, mContacts)
        recycler_view.adapter = mAdapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermission()
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUES_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    toast("Permission Granted")
                } else {
                    toast("Permission Denied")
                }
                return
            }
        }
    }

    private fun requestPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission Needed")
                builder.setMessage("READ_CONTACTS is the permission need for us show your contacts")
                builder.setNegativeButton("Cancel", null)
                builder.setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSION_REQUES_CODE)
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSION_REQUES_CODE)

            }
        } else {
            fetchContacts()

        }
    }

    private fun fetchContacts() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val nameIndex = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val numberIndex = ContactsContract.CommonDataKinds.Phone.NUMBER
        val projection = arrayOf(nameIndex, numberIndex)

        val resolver = contentResolver
        val cursor = resolver.query(uri, projection, null, null, null)

        while (cursor!!.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(nameIndex))
            val number = cursor.getString(cursor.getColumnIndex(numberIndex))
            Log.e("CONTACT", "name: $name \t number: $number")
            mContacts.add(Contact(name, number))
        }
    }
}

fun Context.toast(content: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, content, duration).show()
}