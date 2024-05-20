# Paper Utils

A small gradle plugin that helps you develop your bukkit/spigot/paper plugins.

## Features

- Setups a paper test server for you
- Updates your plugin in the test server when the project is build

## Usage

settings.gradle:

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
```

build.gradle:

```groovy
plugins {
    id 'de.blutmondgilde.paper-utils'
}

paperUtils {
    minecraftVersion = "1.20.5" // The minecraft version you're using
    testServerDir = file("testServer") // The directory where the test server should be created
    server {} // Configuration for the test server
}
```

Include plugins to the test server setup (optional):

```groovy
paperUtils {
    minecraftVersion = "1.20.5"
    testServerDir = file("testServer")
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

To set up or update the test server run

```shell
gradlew setupTestserver
```

This will download and install the lastest paper server jar and creates an Intellij run configuration to start the
server.

To update your plugin in the test server run

```shell
gradlew build
```

and then restart or reload the test server.

## Server Configuration

The test server can be configured with the `server` block in the `paperUtils` block.

### Plugin

The `plugin` function can be used to include plugins to the test server setup.
The function takes two string arguments:

1. The download URL of the plugin
2. The filename of the plugin in the plugins folder

```groovy
plugin(
        "https://download.plugin.com/plugin.jar", // Download URL for the plugin
        "PluginName" // The filename of the plugin in the plugins folder
)
```

after adding the plugin to the configuration, you have to run `gradlew setupTestserver` to download and install the
plugin.