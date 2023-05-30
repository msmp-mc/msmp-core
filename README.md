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
It takes two parameters: 
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

### MPlayer

The `MPlayer` class is a class that represents a player with custom information stored.
It must be used when we want to interact with the player.

#### MPlayer's Manager

The `MPlayerManager` object is an object that manages the `MPlayer` instances.
It must be used when we want to interact with the `MPlayer` instances.

The most important method of this object is the `get(Player)` method.
See the next section for more information.

The public property `maxLives` is the maximum remaining lives for each player.
It is set manually by the MSMPCore plugin.
To change it, you must change the value in the `config.yml` file of the MSMPCore plugin.

The methods `setup(Int)`, `connectionOfPlayer(Player)`, `importPlayersFromConfig(Player)` and `savePlayersInConfig(Player)`
should not be used by other plugins.
Use it carefully.

#### Get a MPlayer

To get a `MPlayer`, you must use the `MPlayerManager` object and call the `get(Player)` method.
This method has a `Player` in parameter. 
This parameter is the player that you want to get the `MPlayer` instance.

If the player is not registered, the method will create a new `MPlayer` instance and register it automatically.

Example:
```kotlin
val player = Bukkit.getPlayer("PlayerName") // get the player
val mplayer = MPlayerManager.get(player) // get the MPlayer instance of the player
```

The method `isAlive()` checks if the player is alive.
Returns the result.

The method `setImmortal()` set the player's immortality.

The method `toPureData()` returns the `MPlayer.PureData` data class of the `MPlayer` instance.
See the section about `MPlayer's Pure Data` for more information.

The methods `died(EntityDamageEvent)`, `updateOnlineStatus()`, `MPlayer.fromPlayer(Player)` and `MPlayer.fromPureData(MPlayer.PureData)`
should not be used by other plugins.
Use it carefully.

#### Information stored in a MPlayer

The `MPlayer` class stores some information about the player.

The stored information are:
- The player's instance (`Player`)
- The player's lives (`MPlayer.Lives`)

##### MPlayer.Lives

The `MPlayer.Lives` data class is a data class storing the lives of a player.

It stores the lives of the player and the maximum lives of the player.
- `maxLives` - The maximum lives of the player
- `remainingLives` - The remaining lives of the player

#### MPlayer's Pure Data

To store data about `MPlayer` in a config file, we use the `MPlayer.PureData` data class.

This data class just contains the important information about the `MPlayer` instance:
- `player` - UUID of the `Player` instance
- `remainingLives` - Remaining lives
- `isImmortal` - Immortality status

### Utils

The package `world.anhgelus.msmp.msmpcore.utils` contains some useful classes.

#### ChatHelper

The `ChatHelper` object is an object that helps to send messages with a consistent style.

There are two types of static methods:
- `send...ToPlayer(Player, String)`- Send a message to a player 
- `send...(String)`- Broadcast a message

The `...` could be removed or replaced by `Success`, `Warning`, `Error`, `Fatal` or `Info`.
`Success` is for a successful message, `Warning` for a warning, etc.

The color code is:
- `Success` - `ChatColor.GREEN`
- `Warning` - `ChatColor.YELLOW`
- `Error` - `ChatColor.RED`
- `Fatal` - `ChatColor.DARK_RED`
- `Info` - `ChatColor.GRAY`


## Technologies

- Kotlin 1.8.21
- Gradle 7
- Spigot for Minecraft 1.19.4
