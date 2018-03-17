package de.datenhahn.easymqtt

import com.github.sylvek.embbededmosquitto.Mosquitto

fun main(args: Array<String>) {
    println("Starting example server")
    Mosquitto.getInstance().start()

    val client = EasyMqtt()
    client.subscribe("easymqtt/example", onMessage = { topic, message -> println(topic + " | " + message) })
    client.publish("easymqtt/example", "MQTT is easy!")
    client.disconnect()
}