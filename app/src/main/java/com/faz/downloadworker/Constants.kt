package com.faz.downloadworker

import android.os.Environment
import java.io.File

const val PERMISSION_REQUEST = 100
const val KEY_DOWNLOAD_URL = "KEY_DOWNLOAD_URL"

var URL_FILE = "https://i.pinimg.com/originals/49/70/17/497017869c892b73b128ff72f2732035.jpg"
var FILE_NAME = "bbb.jpg"
//val FILE_PATH: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
var FILE_PATH: File = Environment.getExternalStorageDirectory()

const val VERBOSE_NOTIFICATION_CHANNEL_NAME = "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
const val NOTIFICATION_TITLE = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1
const val DELAY_TIME_MILLIS: Long = 3000
const val OUTPUT_PATH = "OUTPUT_PATH"