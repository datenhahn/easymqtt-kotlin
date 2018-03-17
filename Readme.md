# EasyMqtt

A kotlin paho wrapper with easy to use interface.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Example

```kotlin
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
```

## Todo

- [ ] add SSL example and tests 
- [ ] stabilize API and tag stable version
