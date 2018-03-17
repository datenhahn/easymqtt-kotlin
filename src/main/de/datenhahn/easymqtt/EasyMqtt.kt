package de.datenhahn.easymqtt

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.properties.Delegates


class EasyMqtt(host: String = "localhost", port: Int = 1883, ssl: Boolean = false, clientId: String = MqttClient.generateClientId()) {

    var client: MqttClient by Delegates.notNull()

    init {
        var protocol = "tcp://"
        if (ssl) {
            protocol = "ssl://"
        }
        val uri = "%s%s:%d".format(protocol, host, port)
        this.client = MqttClient(uri, clientId)
    }

    fun connect() {
        if (!this.isConnected()) {
            this.client.connect()
        }
    }

    fun isConnected(): Boolean {
        return this.client.isConnected()
    }

    fun disconnect() {
        this.client.disconnect()
    }

    fun publish(topic: String, message: String, qos: Int = 0, retain: Boolean = false) {
        this.publish(topic, message.toByteArray(), qos, retain)
    }

    fun publish(topic: String, message: ByteArray, qos: Int = 0, retain: Boolean = false) {

        this.connect()

        this.client.publish(topic, message, qos, retain)
    }

    fun subscribe(topic: String,
                  onMessage: ((topic: String, message: String) -> Unit)? = null,
                  onConnectionLost: ((throwable: Throwable) -> Unit)? = null,
                  onDeliveryComplete: ((t: IMqttDeliveryToken) -> Unit)? = null,
                  qos: Int = 1) {

        this.client.setCallback(object : MqttCallback {
            override fun connectionLost(throwable: Throwable) {
                if (onConnectionLost != null) {
                    onConnectionLost(throwable)
                }
            }

            override fun messageArrived(t: String, m: MqttMessage) {
                if (onMessage != null) {
                    onMessage(t, String(m.getPayload()))
                }
            }

            override fun deliveryComplete(t: IMqttDeliveryToken) {
                if (onDeliveryComplete != null) {
                    onDeliveryComplete(t)
                }
            }
        })

        this.connect()

        this.client.subscribe(topic, qos)
    }

}

