This documentation is not completed yet but you will find main information.

## Get Started

The MCMMOAdditions plugin is really simple. It's easy to use and to customize. However there are some things that you 
need to know. Commands, permissions and config values instructions will be written down on this documentation wiki.

This is my first, plugin. I never developed in Java before. Also I have never developed any kind of plugin or mod for 
minecraft so I can understand that it is possible to find errors and bugs. If you find any bugs, want to suggest a 
feature or I missed something in this documentation feel free to write 
[on Github](https://github.com/Stephirio/MCMMOAdditions/issues), 
[on Spigot](https://www.spigotmc.org/threads/mcmmoadditions.472449/) or [on Discord](https://discord.gg/3DPeEfzT).

# Features

MCMMOAdditions adds some improvements to the mcMMO plugin without changing very much how it works. These are the main 
functions. The TODO list can be found [here](https://github.com/Stephirio/MCMMOAdditions/projects/1).

* Customisable GUI containing the skills showing information about every skill.
* Customisable amount of money when player levels up a skill.
* Blocking/customising messages coming from mcMMO.
* More coming soon. [TODO list](https://github.com/Stephirio/MCMMOAdditions/projects/1)

# Commands

There are some useful commands that you can use.

* /stats - The stats command allows the player to view his mcMMO skills stats (like level, XP ecc.)
    - /mcstats - Alias: It replaces the mcMMO command.
    - /mmostats - Alias
    - /skillstats - Alias
    - /skillsstats - Alias
    - /levels - Alias
    
# Permissions

* mcmmoa.* - Gives the player all the permissions related to the plugin.
    - mcmmoa.all - Alias
* mcmmoa.admin - Allows a player to execute mcmmoa commands related to admins.
* mcmmoa.stats - Allows the player to execute /stats command and all of its aliases.


* mcmmoa.levelup.* - Allows the player to execute any level-up related actions.
    - mcmmoa.levelup.all - Alias
* mcmmoa.levelup.money.* - With this permission the player will be able to gain money by leveling up any skill.
    - mcmmoa.levelup.money.all - Alias
* The following permissions will allow the player to gain money by leveling up a certain skill:
    - mcmmoa.levelup.money.mining - Mining
    - mcmmoa.levelup.money.woodcutting - Woodcutting
    - mcmmoa.levelup.money.herbalism - Herbalism
    - mcmmoa.levelup.money.excavation - Excavation
    - mcmmoa.levelup.money.fishing - Fishing
    - mcmmoa.levelup.money.acrobatics - Acrobatics
    - mcmmoa.levelup.money.archery - Archery
    - mcmmoa.levelup.money.axes - Axes
    - mcmmoa.levelup.money.swords - Swords
    - mcmmoa.levelup.money.unarmed - Unarmed
    - mcmmoa.levelup.money.alchemy - Alchemy
    - mcmmoa.levelup.money.repair - Repair
    - mcmmoa.levelup.money.taming - Taming
    - mcmmoa.levelup.money.salvage - Salvage
    - mcmmoa.levelup.money.smelting - Smelting
    

* mcmmoa.levelup.money.message.* - With this permission the player will be able to receive a message when gaining money 
by leveling up his skills 
    - mcmmoa.levelup.money.message.all - Alias                                                                                                             
* The following permissions will allow the player to receive a message when gaining money by leveling up a certain 
skill:
    - mcmmoa.levelup.money.message.mining - Mining
    - mcmmoa.levelup.money.message.woodcutting - Woodcutting
    - mcmmoa.levelup.money.message.herbalism - Herbalism
    - mcmmoa.levelup.money.message.excavation - Excavation
    - mcmmoa.levelup.money.message.fishing - Fishing
    - mcmmoa.levelup.money.message.acrobatics - Acrobatics
    - mcmmoa.levelup.money.message.archery - Archery
    - mcmmoa.levelup.money.message.axes - Axes
    - mcmmoa.levelup.money.message.swords - Swords
    - mcmmoa.levelup.money.message.unarmed - Unarmed
    - mcmmoa.levelup.money.message.alchemy - Alchemy
    - mcmmoa.levelup.money.message.repair - Repair
    - mcmmoa.levelup.money.message.taming - Taming
    - mcmmoa.levelup.money.message.salvage - Salvage
    - mcmmoa.levelup.money.message.smelting - Smelting



# Configuration Files

#### Example
The configuration file contains the main functions of the plugin. 
Economy, Permissions and some messages are contained in this file.
It' easy customisable once you understand how Yaml works.  
```yaml
    levelup-money-mining: "default"
```
This is the first value that you can configure. This one in parrticular allows you to change the amount of money that a
player gains by leveling up a specific skill. For this example we took the mining skill but you can change values for
any of the 15 skills that mcMMO contains. 

#### Syntax
You have many types of values that you can insert in various parts of configuration files. Here are the main types.

```yaml

    # String Values (Text values. If they contain quotes they have to be inside double quotes. If you want to insert a 
    # double quote you have to put a backslash behind it)
      value: "String value with \""

    # Integer/Double Values (Numbaers, where double=number with two decimals and integer a number without decimals.).
      another-value: 120.00
      another-int-value: 15
    # Note how with numbers you don't need any double quotes. With percentual or default you MUST use them.
    
    # Sometimes you will need to insert true or false values. As the name says true means true and false means false.
      a-boolean-value: true

    # The last values that you can insert are lists 
      a-list-value: 
        - "Here you can insert any type"
        - "of value you want"
        - also strings without quotes
        or other values:
          - 504
          - 420
```
.