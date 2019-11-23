package com.example.kontakty

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact.*

//DODANE ostatnie dwie permissions************
val permissions = arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,
    Manifest.permission.SEND_SMS,Manifest.permission.MODIFY_PHONE_STATE)//DODANE ostatnie dwie permissions************
val listaKontaktow = arrayListOf<String>()
val listaNumerow = arrayListOf<String>()

class MainActivity : AppCompatActivity() {
    val requestSendSms:Int = 2///DODANE************
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var perm = hasPermissions()

        if(!perm){
            requestPermission()
        }else
        {
            takeContacts()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter(this)//this


    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        if(hasPermissions()){
            takeContacts()
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = contactAdapter(this)//this
        }else{
            finish()
        }
    }

    private fun hasPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions,0)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun takeContacts(){
        val contentResolver = contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null,null)
        var phoneNumber=""
        try{

            cursor?.moveToFirst()
            while(!cursor?.isAfterLast!!){
                //var phoneNumber=""
                val contactId = cursor.getString( cursor.getColumnIndex( ContactsContract.Contacts._ID ))
                var name = cursor?.getString( cursor.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME_PRIMARY ))
                var hasPhone = cursor.getString( cursor.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER ))

                if (hasPhone.equals("1", ignoreCase = true))
                    hasPhone = "true"
                else
                    hasPhone = "false"

                if (java.lang.Boolean.parseBoolean(hasPhone)) {
                    val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
                    while (phones!!.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phones.close()
                }

                listaKontaktow.add(name)
                listaNumerow.add(phoneNumber)

                cursor.moveToNext()
            }
        }finally {
            cursor?.close()
        }
    }
}
