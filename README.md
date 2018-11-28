# BreedSpawnControl [![Build Status](https://travis-ci.org/IceLitty/BreedSpawnControl.svg?branch=master)](https://travis-ci.org/IceLitty/BreedSpawnControl)

You can use this plugin run command when player breed animals or small villager spawned.

### Default Sample Config Paragraph:

```
BreedAnimals:
  # Check in ANY worlds when WorldSpecialThemSelf is false
  "*":
    # Animals internal name.
    CHICKEN:
      # If cancel is true, this animal breed event will cancel.
      Cancel: false
      # If set to -1, will use vanilla experience give, otherwise set exp value to this.
      Experience: 0
      RunCustom:
        # If is true, plugin will remove child entity after breed successfully.
        RemoveChild: true
        # Temp permission for bypass command use.
        TempPermissions:
          # You can add negative permission like 1#.
          - 0#temp.permission.for.bypass.prefix.commands
          - 0#essentials.chat.color
          - 1#-essentials.helpop
          - 3#!test.permission
          - 3#groupmanager.manucheckp
        Commands:
          # Run command by player.
          # command without number prefix will auto insert list with this order.
          - I can't send message with &bcolor&r code.
          - /say I can't use say command too.
          # Run command by player after temp add permissions.
          # bypass command is key, cannot same as the other!
          # In 3#, permission with ! prefix will remove it before run command and add it after.
          # If uncertainty what permission this command have, you can write bypass command only without write in TempPermissions node like 5#.
          - 0#bypass:I can send message with &bcolor&f code.
          - 1#bypass:/helpop I can't use helpop command when default got it perm!
          - 3#bypass:/manucheckp <player> test.permission
          - 5#bypass:/say I don't know what permission need this command, but i can got temp op.
          # Run command by console.
          # If you want to use same command and order# together, like 13# 14# 15# you need use quote the command string and add space at last.
          - console:say use console run command
          - 2#console:manuaddp <player> test.permission
          - 4#console:manudelp <player> test.permission
          - 10#15%console:say This command only have <chance> percents chance to run.
          - 11#console:say And I can use <chance.int> or <chance.float2> or <chance.float4> random number in commands.
          - 12#console:tellraw @a {"text":"","extra":[{"text":"Also can use <chance.each.int.5.7> <chance.each.int.8.15> <chance.each.float.5.7.2> <chance.each.float.8.15.4> in commands."}]}
          - "13#33.33$console:say This three message will only show once."
          - "14#33.33$console:say This three message will only show once. "
          - "15#33.33$console:say This three message will only show once.  "
    COW:
      Cancel: false
      Experience: -1
      RunCustom:
        RemoveChild: false
        TempPermissions: []
        Commands:
          # Also can specify command run order.
          - 0#console:say first
          - 2#console:say third
          # Now Command Format:
          # [order#][chance%][globalChance$][|bypass:|console:]<command>
          # All acceptable variable:
          # <world>       Event triggered world name.
          # <father.x>    Father entity location axis x.
          # <father.y>    Father entity location axis y.
          # <father.z>    Father entity location axis z.
          # <mother.x>    Mother entity location axis x.
          # <mother.y>    Mother entity location axis y.
          # <mother.z>    Mother entity location axis z.
          # <breeder.x>   Breeder entity location axis x.
          # <breeder.y>   Breeder entity location axis y.
          # <breeder.z>   Breeder entity location axis z.
          # <player>      If breeder is player, get player's name.
          # <random1;random2;random3>   Random apply one of these strings.
          # <chance>         This command active chance.
          # <chance.int>     This command active chance (Integer value).
          # <chance.float2>  This command active chance (Float value with 2 digits after decimal places).
          # <chance.float4>  This command active chance (Float value with 4 digits after decimal places).
          # <chance.each.int.MIN.MAX>                 Random a int value from MIN to MAX for this.
          # <chance.each.float.MIN.MAX.DECIMAL_PLACE> Random a float value from MIN to MAX and limit DECIMAL_PLACE digits after decimal places.
          #                                           Notice: MIN, MAX and DECIMAL_PLACE must be a Integer number.
          - 3#console:say <world> <father.x>,<father.y>,<father.z> <mother.x>,<mother.y>,<mother.z> <breeder.x>,<breeder.y>,<breeder.z> => <player> <randomString1;randomString2;randomString3>
          # Sample use % and $ together:
          - 50%40$console:say If first 50 percent is match, then judge second chance.
          - 50%30$console:say If pre command not match that 50 percent chance, then this 30$ chance is 30% not 30/(100-40) chances.
          - 100$console:say If you use % and $ together, recommand add one 100% and 100$ command at last.
  # Always check in world name matches them self, must be lowercase!
  world:
    COW:
      # All in world settings without in "*" world must set Use to true!
      Use: true
      Cancel: false
      Experience: -1
      RunCustom:
        RemoveChild: false
        TempPermissions: []
        Commands:
          # Also can specify command run order.
          - 1#console:say second
```

### Commands and Permissions:

/bsc - Show help (perm node `bsc`)

/bsc reload - Reload the plugin (perm node `bsc.reload`)

`bsc.*` - Get all permissions in any world triggerd.

`bsc.*.*` - Get all permissions in any world and any type animals triggerd.

Permission node format: `bsc.<worldName>.<entityName>`

##### All commands are default op.
##### If you want to test default config, please remember add `bsc.*` and `bsc.*.*` to self and de-op your self.
