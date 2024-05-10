package com.barisaslankan.sleeptimer.presentation.media

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer

class MediaManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private val audioManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    fun muteAllMedia() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}
