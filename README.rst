=========
Lightbulb
=========

Pachube android client to update datastream for controlling RGB LEDs

Requirements
============

* Pachube Feed with datastreams named ch1, ch2, ch3, ch4, switch
* Pachube API key which has read/write privilige for the feed
* Internet connectivity

Usage
=====

Create an XML file /res/values/secret.xml

::

<?xml version="1.0" encoding="utf-8"?>
<resources>
<string name="api_uri">http://api.pachube.com/v2/feeds/YOUR-PACHUBE-FEED-ID</string>
<string name="api_key">YOUR-PACHUBE-API-KEY</string>
</resources>


Screens
=======

* .. image:: https://github.com/outofjungle/Lightbulb/raw/master/Screenshot-1.png

* .. image:: https://github.com/outofjungle/Lightbulb/raw/master/Screenshot-2.png

* .. image:: https://github.com/outofjungle/Lightbulb/raw/master/Screenshot-3.png


