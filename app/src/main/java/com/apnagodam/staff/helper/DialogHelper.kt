package com.apnagodam.staff.helper

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.apnagodam.staff.R

object DialogHelper {
    fun showRejectDialog(activity: Activity, action: (Action, String) -> Unit) {

        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_reject_request)
        var btYes = dialog.findViewById<TextView>(R.id.btYes)
        var btNo = dialog.findViewById<TextView>(R.id.btNo)
        var etNotes = dialog.findViewById<EditText>(R.id.edNotes)
        btYes.setOnClickListener {
            action.invoke(Action.YES, etNotes.text.toString())
            if (dialog.isShowing) dialog.dismiss()

        }
        btNo.setOnClickListener {
            action.invoke(Action.NO, etNotes.text.toString())
            if (dialog.isShowing) dialog.dismiss()
        }

        dialog.show()
    }

    enum class Action {
        YES, NO
    }

}