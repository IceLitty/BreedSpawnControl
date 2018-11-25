# BreedSpawnControl
## Config:
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
      - temp.permission.for.bypass.prefix.commands
    Commands:
      # Run command by player.
      - say plain command
      # Run command by player after temp add permissions.
      - bypass:say bypass permission command
      # Run command by console.
      - console:say use console run command
```
### Commands and Permissions:
/bsc - Show help

/bsc reload - Reload the plugin

##### All commands are default op.
