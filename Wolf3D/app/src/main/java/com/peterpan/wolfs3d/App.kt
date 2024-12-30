package com.peterpan.wolfs3d

import android.app.Application
import android.content.Context
import android.os.Build
import java.io.File
import kotlin.toString

class App : Application()  {
    companion object {
        lateinit var appContext : Context
        lateinit var directoryFile : File
        @JvmStatic
        lateinit var packageFolder: String
        var versionString = ""
        var versionBuild  = 0L
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this
        directoryFile = appContext.dataDir
        packageFolder = "/data/data/${packageName}/"
        versionString = getAppVersion(this)
        versionBuild  = getAppBuild(this)

    }
}

fun getAppBuild(ctx: Context) : Long {
    return if (Build.VERSION.SDK_INT >= 28) {
        ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).longVersionCode
    } else {
        ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode.toLong()
    }
}

fun getAppVersion(ctx: Context) : String {
    return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName.toString()
}

fun makeFilesDirectory(packageFolder: String) : String {
    val packageFiles = "${packageFolder}/files"
    val packageFile = File(packageFiles)

    packageFile.mkdir()

    return packageFiles
}
