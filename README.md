# Xposed-DataUsage
Xposed module to show mobile and Wi-Fi data usage in the status bar

![Screenshot](https://i.imgur.com/xXQbImJ.png)

Features:
* Pulls data usage and cycle date from system, no need to set manually
* By default uses 'smart' units (largest of K/M/G that will yield at least 1 unit in size)
* Can choose to monitor mobile, wifi, both, or none
* Can show a default network when not connected to any monitored network
* Can be any text colour, defaults to current clock colour
* Uses clock font

Planned features:
* Change colour based on % of cycle data limit
