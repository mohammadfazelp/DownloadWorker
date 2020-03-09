package com.faz.downloadworker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val TAG = this.javaClass.name
    private val liveDataHelper: LiveDataHelper? = LiveDataHelper.instance

    override fun doWork(): Result {
        lateinit var result: Result
        val inputData: String? = inputData.getString(KEY_DOWNLOAD_URL)
        inputData?.let {
            result = downloadFile(inputData)
        }
        return result
    }

    private fun downloadFile(urlString: String): Result {
        try {
            val outputFile = File(FILE_PATH, FILE_NAME)
            val url = URL(urlString)
            val urlConnection: URLConnection = url.openConnection()
            urlConnection.connect()
            val fileLength: Int = urlConnection.contentLength
            val fos = FileOutputStream(outputFile)
            val inputStream: InputStream = urlConnection.getInputStream()
            val buffer = ByteArray(1024)
            var len: Int
            var total: Long = 0
            while (inputStream.read(buffer).also { len = it } > 0) {
                total += len.toLong()
                val percentage = (total * 100 / fileLength).toInt()
                liveDataHelper?.updatePercentage(percentage)
                fos.write(buffer, 0, len)
            }
            fos.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }
}
