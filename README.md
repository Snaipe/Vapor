Vapor
=====

Copyright © 2013, Franklin "Snaipe" Mathieu < http://snaipe.me/ >

What is Vapor?
-----------------
Vapor is a java library that eases the process of connecting with peers on a p2p network.
It uses bittorrent trackers to find people on the same info hash

How do I use it ?
-----------------

The first thing you want to do is to join a "peer world".
A peer world is some kind of group that peers may join, identified by a unique 20-bytes-long string. Each world exists through the bittorrent trackers that you specify.

```
PeerWorld peerWorld = Join.as("Your nickname")
        .on("The group name")
        .withTrackers("udp://some-tracker.com:73840", "udp://some-other-tracker.com:38884")
        .update();
```

You may call update() at any time to update the peer list (note that update() is blocking).
Once updated, the PeerWorld will hold the peers that have joined the group :

```
Peer[] peers = peerWorld.getPeers();

// do things with your peers
```

And if you don't plan to update the peer list ever again, you'll need to close it :

```
peerWorld.close();

// you can continue to do access your peer list.
```

Source
------
The project is open source and available on [Github]

License
-------
Vapor is licensed under the LGPL3 (GNU Lesser General Public License Version 3). Please check the `LICENSE` file for the full license.

Compiling
---------
Vapor uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Clone the repository and run: `mvn`

Coding and Pull Request Formatting
----------------------------------
* Generally follow the Oracle coding standards.
* Use tabs for indentation, spaces for alignment.
* No trailing whitespaces.
* 160 chars column limit.
* Pull requests must compile, work, and be formatted properly.
* Number of commits in a pull request should be kept to *one commit* and all additional commits must be *squashed*.
* You may have more than one commit in a pull request if the commits are separate changes, otherwise squash them.

**Please follow the above conventions if you want your pull request(s) accepted.**

[GitHub]: https://github.com/Snaipe/Vapor