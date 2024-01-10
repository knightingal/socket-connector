package org.nanking

import org.http4k.core.Method
import org.http4k.core.Request
import java.net.Socket
import kotlin.test.Test

class SocketConnector {
    val bodyString = "what is your name"

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun socketConnect() {
        val socket = Socket("127.0.0.1", 8080)
        val request = Request(Method.POST, "/examples/servlets/servlet/SocketConnectExample")
            .header("user-agent", "Thunder Client (https://www.thunderclient.com)")
            .header("Host", "127.0.0.1:8080")
            .header("content-type", "text/plain")
            .header("Transfer-Encoding", "chunked")


        val inputStream = socket.getInputStream()
        val outputStream = socket.getOutputStream()
        val requestMessage = request.toMessage()
        outputStream.write(requestMessage.toByteArray())
        Thread {
            for (i in 1..3) {
                outputStream.write("${bodyString.length.toHexString()}\r\n".toByteArray())
                outputStream.write("$bodyString\r\n".toByteArray())
                outputStream.flush()
                Thread.sleep(5 * 1000)
            }
            outputStream.write("0\r\n\r\n".toByteArray())
            outputStream.flush()
        }.start()
        while(true) {
            val b = ByteArray(128)
            val readLen = inputStream.read(b)
            if (readLen <= 0) {
                println("breaked")
                break
            }
            println(String(b, 0, readLen))
        }


    }

}
