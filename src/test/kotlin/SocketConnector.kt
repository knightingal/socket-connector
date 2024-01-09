package org.nanking

import org.http4k.core.Method
import org.http4k.core.Request
import java.net.Socket
import kotlin.test.Test

class SocketConnector {
    @Test
    fun socketConnect() {
        val socket = Socket("localhost", 8080)
        val request = Request(Method.POST, "/examples/servlets/servlet/SocketConnectExample")
        val inputStream = socket.getInputStream()
        val outputStream = socket.getOutputStream()


    }

}
