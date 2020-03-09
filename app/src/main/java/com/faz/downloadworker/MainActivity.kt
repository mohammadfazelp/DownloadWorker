package com.faz.downloadworker

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.name

    /**
     * Runtime permissions object init to check storage persmissions
     */
    private var runtimePermission: RunTimePermission = RunTimePermission(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnDownload.setOnClickListener {
            progressBarRound.visibility = View.VISIBLE
            startDownload()
        }
    }

    private fun startDownload() {
        runtimePermission.requestPermission(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            object : RunTimePermission.PermissionCallback {
                override fun onGranted() {
                    downloadFile()
                }

                override fun onDenied() {
                    //show message if not allow storage permission
                }
            })
    }

    private fun createInputData(): Data {
        val builder = Data.Builder()
        builder.putString(KEY_DOWNLOAD_URL, URL_FILE)
        return builder.build()
    }

    @SuppressLint("SetTextI18n")
    fun downloadFile() {
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
            .setInputData(createInputData())
            .build()
        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
        LiveDataHelper.instance?.observePercentage()
            ?.observe(this, Observer<Int?> { progress ->
                if (progress != null) {
                    runOnUiThread {
                        if (progressBarRound.visibility == View.VISIBLE) {
                            progressBarRound.visibility = View.GONE
                        }
                        txtViewProgress.text = "$progress %"
                        progressBar.progress = progress
                    }
                }
            })
    }

    /**
     * Request permission result pass to RuntimePermission.kt
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST) {
            runtimePermission.onRequestPermissionsResult(grantResults)
        }
    }
}
