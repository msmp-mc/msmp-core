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

### Config

Creating configurations' file is pretty annoying, so we have decided to fix this by providing a class doing this for you.

#### Create a new config

To create a new config, just create a new `.yml` file in the resources' folder.
These files will be copied at the right place when the plugin is launched for the first time or when a file is deleted.

#### Get and use a config

To get a config, you must create a new instance of the `Config` class.
It takes two arguments: 
- The instance of your plugin's main file 
- The name of the config
The name of the config is the name of the `.yml` file present in the resources' folder.

Example:
```kotlin
val config = Config(YourPlugin.getInstance(), "config") // get the file "config.yml" presents in the config file of the plugin "YourPlugin"
```

To use it, there are two methods very useful:
- `get()` - Get the `FileConfiguration` of the config (and use it like every classical `FileConfiguration`)
- `save()` - Save the config file

Example:
```kotlin
config.get().setString("version", "1.0.0") // get the "FileConfiguration" and set a string with the key "version" and the value "1.0.0"
config.save() // save the config file
```

## Technologies

- Kotlin 1.8.21
- Gradle 7
- Spigot for Minecraft 1.19.4
