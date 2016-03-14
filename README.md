# Xposed-DataUsage
Xposed module to show current cycle's data usage on the status bar

![Screenshot](https://i.imgur.com/xXQbImJ.png)

Features:
* Pulls data usage and cycle date from system, no need to set manually
* Shows only when connected to a mobile network
* Uses clock text colour and typeface
* By default uses 'smart' units (largest of K/M/G that will yield at least 1 unit in size)

Bugs:
* Doesn't work on Project Fi

Planned features:
* Settings page for various customizations
* Change colour based on % of cycle data limit
* Position on screen (left, center, right)
