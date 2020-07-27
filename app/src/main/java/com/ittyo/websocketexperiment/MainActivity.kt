package com.ittyo.websocketexperiment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8

class MainActivity : AppCompatActivity() {

    lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        client = OkHttpClient()
    }

    override fun onResume() {
        super.onResume()
        startWebSocket()
    }

    fun startWebSocket() {
        val request = Request.Builder().url("ws://echo.websocket.org").build()
        val listener = EchoWebSocketListener

        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }

    object EchoWebSocketListener: WebSocketListener() {
        private const val NORMAL_CLOSURE_STATUS = 1000
        private const val GOING_AWAY_STATUS = 1001
        private const val PROTOCOL_ERROR_STATUS = 1002
        private const val UNKNOWN_DATA_STATUS = 1003

        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("Hello!")
            webSocket.send("From WebSocket!".encodeUtf8())
            webSocket.close(NORMAL_CLOSURE_STATUS, "Bye!")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("WebSocket", "onMessage using ByteString: ${bytes.utf8()}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "onMessage : $text")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "onFailure : ${t.message}, resp : $response")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "onClosing : $code, reason: $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "onClosed : $code, reason: $reason")
        }
    }
}