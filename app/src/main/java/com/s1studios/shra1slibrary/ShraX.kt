package com.s1studios.shra1slibrary

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlin.Int.Companion.MAX_VALUE

object ShraX {
    var application: Application? = null

    var progressDialog: ProgressDialog? = null

    fun toast(text: String) {
        Toast.makeText(ShraX.application, text, Toast.LENGTH_SHORT).show()
        logd("[Toast] : $text")
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

    fun showDialogProperlyStretched(dialog: Dialog, resources: Resources) {
        dialog.show()
        val width: Int = resources.displayMetrics.widthPixels
        dialog.window?.setLayout((7 * width) / 8, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    fun <T> AppCompatActivity.collectLatestLifeCycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    fun <T> AppCompatActivity.collectLifeCycleFlow(flow: Flow<T>, collect: FlowCollector<T>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect(collect)
            }
        }
    }

    fun <T> isMyServiceRunning(clazz: Class<T>): Boolean {
        val activityManager: ActivityManager =
            application?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningServices(MAX_VALUE).forEach { service ->
            if (clazz.name.equals(service.service.className)) {
                return true
            }
        }
        return false
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.enable() {
        this.isEnabled = true
    }

    fun View.disable() {
        this.isEnabled = false
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O, lambda = 0)
    fun sdkVersionOAndBeyond(block: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            block()
        } else {
            logd("Version is below O so will not execute this block")
        }
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O, lambda = 0)
    fun sdkVersionOAndBeyond(block: () -> Unit, elseBlock: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            block()
        } else {
            elseBlock()
        }
    }

    @ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
    fun sdkVersionAndBeyond(versionCode: Int, block: () -> Unit) {
        if (Build.VERSION.SDK_INT >= versionCode) {
            block()
        } else {
            logd("Version is below $versionCode so will not execute this block")
        }
    }

    @ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
    fun sdkVersionAndBeyond(versionCode: Int, block: () -> Unit, elseBlock: () -> Unit, ) {
        if (Build.VERSION.SDK_INT >= versionCode) {
            block()
        } else {
            elseBlock()
        }
    }

    fun sdkVersion(versionCode: Int, block: () -> Unit) {
        if (Build.VERSION.SDK_INT == versionCode) {
            block()
        } else {
            logd("Version is not $versionCode, so will not execute this block")
        }
    }

    fun sdkVersion(versionCode: Int, block: () -> Unit, elseBlock: () -> Unit, ) {
        if (Build.VERSION.SDK_INT == versionCode) {
            block()
        } else {
            elseBlock()
        }
    }

    fun main(work: suspend () -> Unit): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    }

    fun io(work: suspend () -> Unit): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            work()
        }
    }

    fun <T : Any> ioThenMain(bgWork: suspend () -> T, uiWork: suspend (T) -> Unit): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async rt@{ return@rt bgWork() }.await()
            uiWork(data)
        }
    }

    @SuppressLint("NewApi")
    fun isBatteryOptimizationDisabled(): Boolean {
        val powerManager = application?.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(application?.packageName)
    }

    fun showDisableBatteryOptimizationDialog(context: Context, message: String) {
        if (!isBatteryOptimizationDisabled()){
            AlertDialog.Builder(context)
                .setTitle("Disable Battery Optimization")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok"){_,_->
                    val intent = Intent().apply {
                        action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                        data = Uri.parse("package:${application?.packageName}")
                    }
                    application?.startActivity(intent)
                }.show()
        }
    }

}