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
    // Configuration for the test server
    server {
        plugin(
                "https://download.plugin.com/plugin.jar", // Download URL for the plugin
                "PluginName" // The filename of the plugin in the plugins folder
        )
        plugin(
                "https://download.plugin.com/another-plugin.jar", // Download URL for the plugin
                "AnotherPluginName" // The filename of the plugin in the plugins folder
        )
    }
}
```