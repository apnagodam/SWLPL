package com.apnagodam.staff.utils

import com.google.android.material.textfield.TextInputLayout

class Validationhelper {

    fun fieldEmpty(textInputLayout: TextInputLayout):Boolean{
        return textInputLayout.editText!!.text.isEmpty()
    }
}