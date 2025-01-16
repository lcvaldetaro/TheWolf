package com.peterpan.wolfs3d

import android.os.Looper
import android.util.Log
import club.gepetto.circum.CircumIntentProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wolf.util.DialogTool.Toast
import wolf.util.WolfTools
import java.io.File
import java.lang.Thread.sleep

class WolfIntentProcessor : CircumIntentProcessor<WolfState, WolfIntentCommand, WolfEffect>() {
    val TAG = "WolfIntentProcessor"

    override fun onAttach() {
        setState(WolfState.Loading)
        installGameFiles()
    }

    private fun installGameFiles() {
        CoroutineScope(Dispatchers.IO).launch {
          Looper.prepare() // needed to issue toasts

          val folder = "${App.packageFolder}/files/"
          val dir = File(folder)

            if (!dir.exists() ) {
                if (!dir.mkdir() ) {
                    Toast(App.appContext, "Invalid game base folder: ${folder}")
                    Log.e(TAG, "Cannot create folder ${folder}")
                }
            }

            try {
                WolfTools.installGame(App.appContext, folder, dir)
            }
            catch (e: Exception) {
                Toast(App.appContext, "Fatal Error Unable to install game files")
                Log.e(TAG, "Cannot install files on folder ${folder}")
                e.printStackTrace()
                System.exit(0)

            }
            Toast(App.appContext, "All game files installed")
            sleep (2000) // wait for all the toasts to finish
            setState(WolfState.Loaded)
        }
    }
}

sealed interface WolfState {
   data object Loading : WolfState
   data object Loaded : WolfState
}

data object WolfIntentCommand
data object WolfEffect