/*
 * This file is part of Vapor.
 *
 * Copyright (c) 2013-2013, Snaipe <http://snaipe.me/>
 * Vapor is licensed under the GNU Lesser General Public License Version 3.
 *
 * Vapor is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Vapor is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 * for the GNU Lesser General Public License.
 */
package me.snaipe.vapor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import me.snaipe.vapor.exception.TrackerConnectionException;
import me.snaipe.vapor.network.DatagramURL;
import me.snaipe.vapor.tracker.HTTPTracker;
import me.snaipe.vapor.tracker.Peer;
import me.snaipe.vapor.tracker.Tracker;
import me.snaipe.vapor.tracker.UDPTracker;

public class PeerWorld {

	private Tracker[] trackers = {};
	
	public final byte[] peer_id;
	public final byte[] hash;
	
	private int port;

	private Peer[]	peers;

	public PeerWorld(byte[] peer_id, byte[] hash) {
		this.peer_id = peer_id;
		this.hash = hash;
		
		if (hash.length != 20 || peer_id.length != 20) {
			throw new IllegalArgumentException("info_hash or peer_id must be a 20-bytes array.");
		}
		
		this.port = 56679;
	}
	
	/**
	 * Optional.
	 * The port number you will be listening on. (defaults to 56679)
	 * @param port number
	 * @return the vaporizer
	 */
	public PeerWorld withPort(short port) {
		this.port = port;
		return this;
	}
	
	public PeerWorld withTrackers(String... trackers) {
		List<Tracker> result = new LinkedList<Tracker>();
		for (String tracker : trackers) {
			try {
				if (tracker.startsWith("udp://")) {
					result.add(new UDPTracker(new DatagramURL(tracker), this));
				} else {
					result.add(new HTTPTracker(new URL(tracker), this));
				}
			} catch (MalformedURLException ex) {}
		}
		this.trackers = result.toArray(new Tracker[result.size()]);
		return this;
	}
	
	public byte[] getPeerID() {
		return peer_id.clone();
	}

	public byte[] getHash() {
		return hash.clone();
	}

	public int getPort() {
		return port;
	}
	
	public Peer[] getPeers() {
		return peers;
	}
	
	public PeerWorld update() {
		List<Peer> peers = new LinkedList<Peer>();
		for (Tracker t : trackers) {
			try {
				t.update();
			} catch (TrackerConnectionException e) {
				e.printStackTrace();
			}
			Collections.addAll(peers, t.getPeers());
		}
		this.peers = peers.toArray(new Peer[peers.size()]);
		return this;
	}
	
	public PeerWorld close() {
		for (Tracker t : trackers) {
			try {
				t.close();
			} catch (TrackerConnectionException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
}
