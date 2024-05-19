# Paper Utils
A small gradle plugin that helps you develop your bukkit/spigot/paper plugins.

## Features

Creates a paper test server for you with includes your plugin.

## Usage

```groovy
plugins {
    id 'de.blutmondgilde.paper-utils'
}

paperUtils {
    minecraftVersion = "1.20.5" // The minecraft version you're using
    testServerDir = file("testServer") // The directory where the test server should be created
}
```