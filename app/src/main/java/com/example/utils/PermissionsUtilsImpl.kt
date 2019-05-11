package com.example.utils

import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.example.domain.ApplicationProvider
import javax.inject.Inject

class PermissionsUtilsImpl @Inject constructor(
        val applicationProvider: ApplicationProvider
    ): PermissionsUtils {

    override fun arePermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(applicationProvider.applicationContext, permission) == PackageManager.PERMISSION_GRANTED
    }

}