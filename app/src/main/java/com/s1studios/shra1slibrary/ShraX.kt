package com.s1studios.shra1slibrary

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast

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

    fun dismissProgressDialog(){
        progressDialog?.dismiss()
    }
}