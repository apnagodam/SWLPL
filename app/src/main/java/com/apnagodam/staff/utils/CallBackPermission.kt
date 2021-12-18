package com.apnagodam.staff.utils



interface CallBackPermission {
    fun onGranted(success: Boolean, code: Permission)
}