package android.example.apiminirobots

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip: String = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        hello_world.text = "Running on IP: $ip"
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch{
            embeddedServer(Netty, 8080) {
                install(ContentNegotiation) {
                    gson {}
                }
                routing {
                    get("/") {
                        call.respond(mapOf("message" to "Hello world from Moto G6-Plus"))
                    }
                }
            }.start(wait = true)
        }
    }
}
