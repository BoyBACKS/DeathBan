# DeathBan

  Simple plugin that run custom command after player die, you can download plugin [here](https://www.spigotmc.org/resources/deathban.91723/)
  
# Commands
  `/deathban [nick]` - That command shows gui where can you see your last death stats/your or other players deathscore
  
  `/deathban reset [nick]` - That command reset your or other players deathscore

  `/deathban toggle` - That command turn off/on custom event after death
  
  `/deathban help` - That command shows help page

# Permissions
  `deathban.bypass` - That permission gives you protection against custom event after death

  `deathban.score/scoreother` - That permission gives you access to use `/deathban` command

  `deathban.reset/resetother` - That permission gives you access to use `/deathban reset [nick]` command

  `deathban.toggle` - That permission gives you access to use `/deathban toggle` command

  `deathban.help` - That permission gives you access to use `/deathban help` command

  `deathban.admin` - That permission gives you access to use admin gui `/deathban` 

## What have we added new?
V1.7
  * We added gui where can you manage entire plugin or see your death stats
  * We added custom Update message
  * We change java version form 8 to 11

V1.6
  * We added support for versions 1.8.x - 1.16.x 
  * We added new permissions, and new commands
  * We change **config.yml** adding new messages
  * We added access to enable or disable custom event after death

V1.5.3
  * We added the 'Time' function in the **config.yml** file, this function allows you to set your own     command execution time after death

V1.5.2
  * We fixed bugs in the plugin
  * We fixed the problem with clone items
  * We have made another code optimization
  * Removed support for version 1.12.x (Current support for version 1.13+)
  * We added support for **ColorCodes** (&)

V1.5
  * ~~We added support for version 1.12 +~~

V1.4.2
  * We optimized the code 
  * We added a message that shows for players who have the **deathban.bypass** permissions 
  * We added the death counter in **playerdata.yml**

V1.4.1
  * We decided to remove the command imposed by the plugin, now you can use your own commands

V1.3.3
  * ~~We fixed the problem with clone items~~ - this problem only appeared when **KeepInventory** was set on the server

V1.3.2
  * ~~We added a random Ban time to the setting in **Config.yml**~~ - the plugin no longer uses this option, it may appear in the future

V1.3
  * We added **config.yml**
  * We optimized the code

V1.2
  * ~~We added separate variables (Time, Time units, Ban Reason)~~ - the plugin doesn't use this option anymore, it may appear in the future
  
V1.1
  * We have written in working code

## What are we going to add?

  * MySQL support
  * Effects  after death 
  * Player LootCrate after death
  * Custom death titles
  * Add 1.17+ version support
  * ~~Gui where you can manage the entire plugin~~ For now we add gui but we want improve by adding full customization
    * Custom title/slots/item names
    * Transfer all commands to use in gui
  * ~~Ability to disable custom event~~
  * ~~We are going to add support for 1.8.x - 1.12.x~~
