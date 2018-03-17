# EasyMqtt

![EasyMqtt Logo](./doc/easy-mqtt-logo.png)

A kotlin paho wrapper with easy to use interface.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


## Add to your project

### Gradle

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
	dependencies {
   	        compile 'com.github.datenhahn:easymqtt-kotlin:-SNAPSHOT'
   	}

```

### Maven

```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```
	<dependency>
	    <groupId>com.github.datenhahn</groupId>
	    <artifactId>easymqtt-kotlin</artifactId>
	    <version>-SNAPSHOT</version>
	</dependency>
```

## Example

See also: https://github.com/datenhahn/easymqtt-kotlin-example

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

## Logo

Based on

https://openclipart.org/detail/34159/tango-mail-forward
https://openclipart.org/detail/223782/smile