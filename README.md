# MSMP Core

Core plugin of MSMP.

## Installation

Only gradle is supported.

```gradle
repositories {
    maven {
        name = 'jitpack'
        url = 'https://jitpack.io'
    }
}

dependencies {
    implementation 'com.github.msmp-mc:msmp-core:VERSION'
}
```

## Documentations

### Minecraft Plugin helper

When you create a new plugin, you can use `PluginBase` and `CompanionBase` to help.

These two classes are helping you to create a new plugin by simplifying some redundant things like creating static access
to the actual instance (create a `YourPlugin.getInstance()`).

It also creates static access to the plugin Logger (`YourPlugin.LOGGER`) and log when your plugin is enabled and when it
is disabled.

Of course, it supports custom instructions with the methods `enable()` and `disabled()` called respectively when the plugin
is started and is stopped.

## Technologies

- Kotlin 1.8.21
- Gradle 7
- Spigot for Minecraft 1.19.4
