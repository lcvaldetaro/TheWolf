package com.peterpan.wolfs3d

import android.content.Intent
import androidx.activity.compose.setContent
import android.os.Bundle
import club.gepetto.circum.CircumActivity
import com.peterpan.doom.ui.GameLoadingState
import kotlin.jvm.java

open class InstallActivity : CircumActivity<Any>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCircumModel(WolfCircumModel::class.java)
    }

    override fun onStateUpdate(state: Any) {
        super.onStateUpdate(state)
        
        when (state) {
            is WolfState.Loading -> {
                setContent { GameLoadingState() }
            }
            is WolfState.Loaded -> {
                startWolfClientActivity()
            }
        }
    }

    fun startWolfClientActivity () {
        val intent = Intent(this, WolfActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        this.startActivity(intent)
    }
}

