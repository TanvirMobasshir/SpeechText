package com.speechtext.audiorecorder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.speechtext.audiorecorder.playback.AndroidAudioPlayer
import com.speechtext.audiorecorder.record.AndroidAudioRecorder
import com.speechtext.audiorecorder.theme.SpeechTextTheme
import java.io.File
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            0
        )
        setContent {
            SpeechTextTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Start Recording
                    Button(onClick = {
                        File(cacheDir, "audio.mp3").also {
                            recorder.start(it)
                            audioFile = it
                        }
                    }) {
                        Text(text = "Start Recording ")
                    }

                    // Stop Recording
                    Button(onClick = {
                        recorder.stop()
                        audioFile?.let {
                            saveAudioFile(it)
                        }
                    }) {
                        Text(text = "Stop Recording ")
                    }

                    // Play
                    Button(onClick = {
                        player.playFile(audioFile ?: return@Button)
                    }) {
                        Text(text = "Play ")
                    }

                    // Stop Playing
                    Button(onClick = {
                        player.stop()
                    }) {
                        Text(text = "Stop Playing 20 ")
                    }
                }
            }
        }
    }

    private fun saveAudioFile(sourceFile: File) {
        try {
            val destinationFile = File(getExternalFilesDir(null), "saved_audio.mp3")
            sourceFile.copyTo(destinationFile)
            // If you want to delete the original file after copying, uncomment the line below
            // sourceFile.delete()
            println("working")
        } catch (e: IOException) {
            e.printStackTrace()
            println("not working")
        }
    }
}
