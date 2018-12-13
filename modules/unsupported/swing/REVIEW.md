# Swing Extension

The Swing Module is developed for Geotools and provides classes for
GUI elements including a map display pane with associated cursor
tools.

### IP Review

 - Jody Garnett, April 2015

SwingWorker mentioned in the initial review is no longer included.

Factored out icon LICENSE.txt as a distinct file.

### IP Review

 - Michael Bedward, September 2009
 - Jody Garnett, September 2009

STATUS: Clean


```
org.getools.swing
```

Classes for map display, a progress window and exception reporting dialog

```
org.geotools.swing.action
```

Classes to link GUI control actions to the map pane.

```
org.geotools.swing.dndlist
```

Classes that implement a JList with drag and drop reordering of elements;
used as a table of contents for map layers.

```
org.geotools.swing.data
```

Dialog and wizard classes to get user input for data store choices.

```
org.geotools.swing.event
```

Mouse and map pane event and listener classes.

```
org.geotools.swing.styling
```

  
```
org.geotools.swing.table
```

Support a JTable model for feature collections.
    
```
org.geotools.swing.tool
```

Cursor tool classes for zooming, panning etc.

```
org.geotools.swing.wizard
```

Classes to consturct wizards to get user input

```
org.geotools.swing.icons (resources)
```

Control and cursor icons. Some created specifically for this module by
mbedward. Those named with prefix mAction were produced by
Robert Szczepane and are distributed under the licence that appears below.

LICENSE

Original GIS icons theme was created by Robert Szczepanek [1] and is licensed
under a Creative Commons Attribution-Share Alike 3.0 Unported License [2].
Fill free to use it for GIS software or for any other purposes. I only ask
you to let me know about that and to include licence.txt file in your work.

 - [1] http://robert.szczepanek.pl/
 - [2] http://creativecommons.org/licenses/by-sa/3.0/

```
TITLE:         gis-0.1
DESCRIPTION:   GIS icon theme
AUTHOR:        Robert Szczepanek
CONTACT:       robert at szczepanek pl
SITE:          http://robert.szczepanek.pl/

Cracow 2008,
Poland
```

As requested in the licence, Robert has been informed of the icons use
here.

```
org.jdesktop.swingworker
```

SwingWorker class from https://swingworker.dev.java.net/ - under an LGPL license.
This class is available in Java 6 but we cannot wait.
