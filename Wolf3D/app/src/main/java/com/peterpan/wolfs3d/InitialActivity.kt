package com.peterpan.wolfs3d

import android.content.Intent
import androidx.activity.compose.setContent
import android.os.Bundle
import club.gepetto.circum.CircumActivity
import club.gepetto.circum.circumIntentProcessor
import com.peterpan.doom.ui.GameLoadingState
import kotlin.jvm.java

open class InitialActivity : CircumActivity<Any>() {
    private lateinit var wIp : WolfIntentProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setIntentProcessor(WolfIntentProcessor::class.java)
    }

    override fun onResume() {
        super.onResume()
        wIp = circumIntentProcessor<WolfIntentProcessor>(this)
        wIp.onStateUpdated { state ->
            when (state) {
                is WolfState.Loading -> {
                    setContent { GameLoadingState() }
                }
                is WolfState.Loaded -> {
                    val intent = Intent(this, WolfActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    this.startActivity(intent)
                }
            }
        }
    }
}

