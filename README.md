# Permissions-ContactMenager

## Fragmenty kodu
* Sprawdzenie uprawnień
``` kotlin
     private fun hasPermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }
```

* Pobranie listy kontaktów z telefonu
``` Kotlin
fun takeContacts(){
        val contentResolver = contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null,null)
        var phoneNumber=""

        try{
            cursor?.moveToFirst()
            while(!cursor?.isAfterLast!!){
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
```
## test
