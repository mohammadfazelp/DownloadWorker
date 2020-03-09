/*
 * Copyright © 2010 - 2019 Space-O Technologies.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.faz.downloadworker

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class RunTimePermission(private var context: Context) {

    lateinit var permissionCallback: PermissionCallback

    interface PermissionCallback {

        fun onGranted()
        fun onDenied()
    }

    /**
     * TODO request for permission & set callback
     *
     * @param arrPermissionName List of permissions
     * @param permissionCallback provided callback to update about permissions
     */
    fun requestPermission(arrPermissionName: List<String>, permissionCallback: PermissionCallback) {
        this.permissionCallback = permissionCallback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkAllPermissionGranted(arrPermissionName)) {
                (context as Activity).requestPermissions(
                    arrPermissionName.toTypedArray(),
                    PERMISSION_REQUEST
                )
            } else {
                permissionCallback.onGranted()
            }
        } else {
            permissionCallback.onGranted()
        }
    }

    /**
     *
     * @param arrPermissionName array of permissions to check if granted or not
     */
    private fun checkAllPermissionGranted(arrPermissionName: List<String>): Boolean {
        for (i in arrPermissionName.indices) {
            if (
                ContextCompat.checkSelfPermission(context, arrPermissionName[i])
                !== PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    /**
     * TODO requested permission result passing through activity filter
     * permitted permissions & set to callback
     *
     * @param grantResults list of allowed & not granted permissions
     */
    fun onRequestPermissionsResult(grantResults: IntArray) {
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                permissionCallback.onGranted()
            } else {
                permissionCallback.onDenied()
                break
            }
        }
    }
}