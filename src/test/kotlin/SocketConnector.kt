package org.nanking

import kotlinx.coroutines.*
import org.http4k.core.Method
import org.http4k.core.Request
import java.net.Socket
import kotlin.test.Test

class SocketConnector {

    companion object {
        private const val HOST = "127.0.0.1"
        private const val PORT = 8080
        private const val URL_PATH = "/examples/servlets/servlet/SocketConnectExample"
        private const val BODY_CONTENT = "what is your name:"
    }

    @Test
    fun socketConnect() {
        val thread1 = Thread {
            socketConnectByName("user1")
        }
        val thread2 = Thread {
            socketConnectByName("user2")
        }
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun socketConnectByName(name: String) {
        val msgContent = BODY_CONTENT + name;
        val socket =
            Socket(HOST, PORT)
        val request = Request(Method.POST, URL_PATH)
            .header("user-agent", "Thunder Client (https://www.thunderclient.com)")
            .header("Host", "$HOST:$PORT")
            .header("content-type", "text/plain")
            .header("Transfer-Encoding", "chunked")


        val inputStream = socket.getInputStream()
        val outputStream = socket.getOutputStream()
        val requestMessage = request.toMessage()
        outputStream.write(requestMessage.toByteArray())
        Thread {
            for (i in 1..3) {
                outputStream.write("${msgContent.length.toHexString()}\r\n".toByteArray())
                outputStream.write("$msgContent\r\n".toByteArray())
                outputStream.flush()
                Thread.sleep(5 * 1000)
            }
            outputStream.write("0\r\n\r\n".toByteArray())
            outputStream.flush()
        }.start()
        while(true) {
            val b = ByteArray(256)
            val readLen = inputStream.read(b)
            if (readLen <= 0) {
                println("breaked")
                break
            }
            println("[$name]" + String(b, 0, readLen))
        }

    }

}
