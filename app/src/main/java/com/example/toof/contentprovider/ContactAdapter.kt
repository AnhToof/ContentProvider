package com.example.toof.contentprovider

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactAdapter(val context: Context, private val contacts: ArrayList<Contact>?) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))
    }

    override fun getItemCount(): Int {
        return contacts?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        contacts?.get(position)?.let { viewHolder.bindData(it) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mTextViewName = view.text_contact_name
        private val mTextViewNumber = view.text_contact_number

        fun bindData(contact: Contact) {
            mTextViewName?.text = contact.name
            mTextViewNumber?.text = contact.number
        }
    }
}