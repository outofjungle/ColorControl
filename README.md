# Color Control
Cosm android client to update datastream for controlling RGB LEDs

## Requirements
* Cosm Feed with datastreams named 0 (switch), 1 (red), 2 (green), 3 (blue), 4 (white)
* Cosm API key which has read/write privilige for the feed
* Internet connectivity

## Usage
Create an XML file /res/values/secret.xml

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
    <string name="cosm_feed_id">YOUR-PACHUBE-FEED-ID</string>
    <string name="cosm_api_key">YOUR-PACHUBE-API-KEY</string>
    </resources>

## Screens
![Screenshot 1](https://github.com/outofjungle/ColorControl/raw/master/Screenshot-1.png)
![Screenshot 2](https://github.com/outofjungle/ColorControl/raw/master/Screenshot-2.png)
![Screenshot 3](https://github.com/outofjungle/ColorControl/raw/master/Screenshot-3.png)


