package com.example.admin.testvk

import android.support.v7.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity() {
    fun showError(title: String, message: String?) {
        AlertDialog.Builder(this@BaseActivity)
                .setTitle(title)
                .setMessage(message ?: "Unknown request error")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("ОК", { dialog, _ -> dialog.cancel() })
                .create()
                .show()
    }
}