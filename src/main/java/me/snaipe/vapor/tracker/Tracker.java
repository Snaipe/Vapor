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
package me.snaipe.vapor.tracker;

import java.util.Random;

import me.snaipe.vapor.PeerWorld;
import me.snaipe.vapor.exception.TrackerConnectionException;

public abstract class Tracker {
	
	public static final Peer[] NO_PEERS = {};
	
	protected final Random random;
	protected final PeerWorld peerWorld;
	
	protected boolean firstTime = true;
	protected long next_allowed_announce = 0L;
	protected Peer[] peers = NO_PEERS;
	
	public Tracker(PeerWorld peerWorld) {
		this.peerWorld = peerWorld;
		this.random = new Random();
	}
	
	protected abstract void announce(AnnounceEvent event);
	
	public synchronized void update() throws TrackerConnectionException {
		if (firstTime) {
			firstTime = false;
			announce(AnnounceEvent.STARTED);
		} else if (System.currentTimeMillis() >= next_allowed_announce) {
			announce(AnnounceEvent.NONE);
		}
	}
	
	public synchronized void close() throws TrackerConnectionException {
		announce(AnnounceEvent.STOPPED);
		firstTime = true;
	}
	
	public Peer[] getPeers() {
		return peers;
	}
	
}
