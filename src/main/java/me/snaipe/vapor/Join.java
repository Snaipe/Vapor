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

import java.io.UnsupportedEncodingException;

import me.snaipe.vapor.exception.MalformedPeerIdentityException;
import me.snaipe.vapor.util.HashUtils;

public class Join {

	public static Join as(String identity) throws MalformedPeerIdentityException {
		byte[] peer_id;
		try {
			peer_id = identity.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {peer_id = new byte[0];} // not happening buddies.
		
		if (peer_id.length > 20) {
			throw new MalformedPeerIdentityException(identity);
		}
		if (peer_id.length < 20) {
			byte[] buffer = new byte[20];
			for (int i = 0; i < peer_id.length; i++) {
				buffer[i] = peer_id[i];
			}
			peer_id = buffer;
		}
		return new Join(peer_id);
	}
	
	private byte[]	peer_id;
	
	private Join(byte[] peer_id) {
		this.peer_id = peer_id;
	}
	
	public PeerWorld on(String group) {
		return new PeerWorld(peer_id, HashUtils.SHA1(group));
	}

}
