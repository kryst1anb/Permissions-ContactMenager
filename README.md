# Permissions-ContactMenager
## Aplikacja korzystająca z pozwoleń na wykład z AMSA
* Aplikacja służy to wysyłania SMSów. 
* Początkowo wyświetlone zostaną wymagane uprawnienia, które są niezbędne do poprawnego działania aplikacji. 
* Odmówienie któreś z nich powoduje zamknięcie aplikacji. 
* Po udzieleniu uprawnień, pobrane zostają wszystkie kontakty z telefonu do listy: Nazwa - Nr telefonu. 
* Próba wysłania SMSa uruchomi ``` AlertDialog ```, który zapyta się czy na pewno chcemy wiadomość wysłać.

## Znaczące funkcje aplikacji
<details>
      <summary>Sprawdzenie uprawnień </summary>
      
``` kotlin
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
    <uses-permission android:name="android.permission.CALL_PHONE"/>
    <uses-permission android:name="android.permission.READ_PHONE_NUMBERS"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.SEND_SMS"/>
```

</details>

<details>
     <summary>Pobranie listy kontaktów z telefonu</summary>
      
``` kotlin
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

</details>
<details>
      <summary>Wysyłanie SMSów oraz AlertDialog</summary>

``` kotlin
 override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val name = holder.view.contact_name
        val number = holder.view.contact_number
        val button = holder.view.send_button

        name.text = listaKontaktow[position]
        number.text = listaNumerow[position]

        button.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialog.setTitle("UWAGA!")
            alertDialog.setMessage("Czy na pewno chcesz wysłać wiadomość? ")

            alertDialog.setPositiveButton("NIE",DialogInterface.OnClickListener {
                        dialog, which -> dialog.cancel()
            })

            alertDialog.setNegativeButton("TAK",DialogInterface.OnClickListener {
                        dialog, which ->
                sendSMS(number.text.toString())
                Toast.makeText(context, "wysłano SMS do "+name.text, Toast.LENGTH_LONG).show()
            })
            val dialog: AlertDialog = alertDialog.create()
            dialog.show()
        }

    }
```

</details>

## 
