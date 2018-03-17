package de.datenhahn.easymqtt

import com.github.sylvek.embbededmosquitto.Mosquitto
import org.awaitility.Awaitility.await
import org.awaitility.Duration.ONE_SECOND
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class EasyMqttTest {

    @org.junit.Before
    fun setUp() {
        Mosquitto.getInstance().start()
    }

    @org.junit.After
    fun tearDown() {
        Mosquitto.getInstance().stop()
    }

    @org.junit.Test
    fun connect() {
        val client = EasyMqtt()

        client.connect()
        assertTrue(client.isConnected())

        client.disconnect()
        assertFalse(client.isConnected())
    }

    @org.junit.Test
    fun publish() {

        var messageReceived = false

        val client = EasyMqtt()

        client.subscribe("ktmqttclient/test", onMessage = { topic, message ->
            if (message == "testmessage") {
                messageReceived = true
            }
        })

        client.publish("ktmqttclient/test", "testmessage")

        await().atMost(ONE_SECOND).until({ -> messageReceived })

    }
}