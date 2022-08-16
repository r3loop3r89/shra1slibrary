package com.s1studios.shra1slibrary

import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.res.Resources
import android.util.Log
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.Int.Companion.MAX_VALUE

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

    fun cantBeEmptyActv(actv: AutoCompleteTextView): Boolean {
        return if (actv.text.toString().trim().isEmpty()) {
            actv.error = "Can't be empty!"
            true
        } else {
            false
        }
    }

    fun showDialogProperlyStretched(dialog: Dialog, resources:Resources){
        dialog.show()
        val width: Int = resources.displayMetrics.widthPixels
        dialog.window?.setLayout((7 * width) / 8, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    fun <T> AppCompatActivity.collectLatestLifeCycleFlow(flow: Flow<T>, collect: suspend (T)->Unit){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                flow.collectLatest(collect)
            }
        }
    }

    fun <T> AppCompatActivity.collectLifeCycleFlow(flow: Flow<T>, collect: FlowCollector<T>){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                flow.collect(collect)
            }
        }
    }

    fun <T> isMyServiceRunning(clazz:Class<T>):Boolean{
        val activityManager :ActivityManager = application?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningServices(MAX_VALUE).forEach { service->
            if (clazz.name.equals(service.service.className)){
                return true
            }
        }
        return false
    }
}