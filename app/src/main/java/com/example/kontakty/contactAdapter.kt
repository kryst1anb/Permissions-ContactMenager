package com.example.kontakty

import android.app.AlertDialog
import android.content.DialogInterface
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact.view.*


//contex poniżej
class contactAdapter(private val context: MainActivity): RecyclerView.Adapter<contactAdapter.MyViewHolder>() {

    val text = "wysłane z mojej pierwszej aplikacji mobilnej"

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaKontaktow.size
    }

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

    private fun sendSMS(number: String) {
        SmsManager.getDefault().sendTextMessage(number, null, text, null, null)
    }
}