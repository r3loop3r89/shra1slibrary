package com.s1studios.shra1slibrary

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

object ShraX {
    var application: Application? = null

    var progressDialog: ProgressDialog? = null

    fun toast(text: String) {
        Toast.makeText(ShraX.application, text, Toast.LENGTH_SHORT).show()
    }

    fun logd(text: String) {
        Log.d("ShraX", text)
    }

    fun <T> logd(text: String, clazz: Class<T>) {
        Log.d("ShraX", "[${clazz.simpleName}] $text")
    }

    fun pleaseWaitDialog(context: Context) {
        progressDialog = ProgressDialog(context)
        progressDialog?.setTitle("Please wait...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    fun cantBeEmptyTil(til: TextInputLayout): Boolean {
        return if (til.editText?.text.toString().trim().isEmpty()) {
            til.editText?.error = "Can't be empty!"
            true
        } else {
            false
        }
    }

    fun cantBeEmptyEt(et: EditText): Boolean {
        return if (et.text.toString().trim().isEmpty()) {
            et.error = "Can't be empty!"
            true
        } else {
            false
        }
    }
}