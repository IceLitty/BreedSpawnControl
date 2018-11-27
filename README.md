# BreedSpawnControl

You can use this plugin run command when player breed animals or small villager spawned.

### Default Sample Config:

```
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
      - console:say use console run command
      - 2#console:manuaddp <player> test.permission
      - 4#console:manudelp <player> test.permission
```

### Commands and Permissions:

/bsc - Show help (perm node `bsc`)

/bsc reload - Reload the plugin (perm node `bsc.reload`)

`bsc.*` - Get all permissions in any world triggerd.

`bsc.*.*` - Get all permissions in any world and any type animals triggerd.

Permission node format: `bsc.<worldName>.<entityName>`

##### All commands are default op.
##### If you want to test default config, please remember add `bsc.*` and `bsc.*.*` to self and de-op your self.
