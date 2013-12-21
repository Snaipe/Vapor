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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import me.snaipe.vapor.PeerWorld;
import me.snaipe.vapor.network.URLDataBuilder;
import me.snaipe.vapor.util.CloseUtils;
import me.snaipe.vapor.util.HashUtils;

public class HTTPTracker extends Tracker {

	private URL url;

	public HTTPTracker(URL tracker, PeerWorld peerWorld) {
		super(peerWorld);
		this.url = tracker;
	}
	
	private String getHTTPAnnounceMessage(AnnounceEvent event) {
		return new URLDataBuilder()
				.append("info_hash", HashUtils.toURLString(peerWorld.hash))
				.append("peer_id", HashUtils.toURLString(peerWorld.peer_id))
				.append("port", peerWorld.getPort())
				.append("uploaded", 0)
				.append("downloaded", 0)
				.append("left", 0)
				.append("compact", 1)
				.append("no_peer_id", 1)
				.append("event", event)
				.build();
	}
	
	protected void announce(AnnounceEvent event) {
		OutputStreamWriter writer = null;
		BufferedReader in = null;
		try {
			URLConnection connection = this.url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(2000);
			
			writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(getHTTPAnnounceMessage(AnnounceEvent.STARTED));
			writer.flush();
			
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CloseUtils.close(writer);
			CloseUtils.close(in);
		}
	}
}
