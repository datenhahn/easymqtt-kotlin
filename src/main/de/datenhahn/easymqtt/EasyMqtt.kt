/*
MIT License

Copyright (c) 2018 Jonas Hahn <jonas.hahn@datenhahn.de>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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

