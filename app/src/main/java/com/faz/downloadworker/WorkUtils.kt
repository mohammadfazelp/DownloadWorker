package com.faz.downloadworker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.File

class WorkUtils {

    companion object {
        private val TAG = WorkUtils::class.java.simpleName

        fun makeStatusNotification(message: String, context: Context) {

            // Make a channel if necessary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
                val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description

                // Add the channel
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager?.createNotificationChannel(channel)
            }

            // Create the notification
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))

            // Show the notification
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }

        /**
         * Method for sleeping for a fixed about of time to emulate slower work
         */
        fun sleep() {
            try {
                Thread.sleep(DELAY_TIME_MILLIS, 0)
            } catch (e: InterruptedException) {
                Log.d(TAG, e.message)
            }
        }

        fun getIsbn(url: String): String {
            return url.substring(url.lastIndexOf("/") + 1)
        }

        private fun getAbsolutePath(isbnFileName: String): String {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + isbnFileName + ".zip";
        }

        fun getFileSize(isbn: String): Long {

            val file: File = File(getAbsolutePath(isbn))
            if (!file.exists()) {
                return 0
            } else {
                return file?.length()
            }
        }

        fun deleteFile(isbn: String) {
            val file = File(getAbsolutePath(isbn))
            if (file.exists() && file.isFile) {
                file.delete()
            }
        }
    }
}