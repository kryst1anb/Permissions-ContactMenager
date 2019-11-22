package com.example.kontakty

import android.os.Debug
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact.view.*

class contactAdapter: RecyclerView.Adapter<contactAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View):RecyclerView.ViewHolder(view)

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
        button.setOnClickListener{
            sendSMS()
        }
    }

    private fun sendSMS() {
        SmsManager.getDefault().sendTextMessage(phoneNumber,null,text,null, null)
    }
}