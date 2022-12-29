package com.litmethod.android.utlis

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import com.gdacciaro.iOSDialog.iOSDialogBuilder

object DialogueBox {

    fun displayAlert(context: Context?, respMsg: String?) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)

            builder.setNegativeButton("OK") { dialog, id -> }
            builder.setTitle("Alert")
            builder.setMessage(respMsg)
            builder.create().show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showMsg(context: Context, title: String?, body: String?) {
        iOSDialogBuilder(context)
            .setTitle(title)
            .setSubtitle(body)
            .setBoldPositiveLabel(true)

            .setCancelable(false)
            .setPositiveListener("Dismiss") { dialog ->
                dialog.dismiss()
            }
            .build().show()
    }

    fun alertDialog(context: Context): String {
        val alert = androidx.appcompat.app.AlertDialog.Builder(context)
        alert.setTitle("Add email address")
        alert.setMessage("Please input your email address.")
        val input = EditText(context)
        alert.setView(input)
        input.hint = "Email"
        alert.setPositiveButton("Ok") { dialog, whichButton ->
        }
        alert.show()
        return input.text.toString()
    }
}