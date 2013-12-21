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
package me.snaipe.vapor.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

public class DatagramURL {

	private final String host;
	private final int port;

	public DatagramURL(String url) throws MalformedURLException {
		if (url == null) {
			throw new MalformedURLException("URL is null");
		}
		if (!url.startsWith("udp://")) {
			throw new MalformedURLException("URL is not using the udp protocol");
		}
		
		int portSep = url.indexOf(':', 6);
		
		if (portSep == -1) {
			throw new MalformedURLException("URL must specify the port");
		}
		
		this.host = url.substring(6, portSep);
		try {
	        this.port = Integer.parseInt(url.substring(portSep + 1));
		} catch (NumberFormatException nfe) {
	        throw new MalformedURLException("Invalid port number: " + url);
		}
		if (this.port < 1 || this.port > 65535) {
			throw new MalformedURLException("Invalid port number: " + url);
		}
	}

	/**
	 * 
	 * @return the port number of this url
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the host name defined by this url
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Creates a new datagram packet targeting the host and port of this url, using the specified buffer.
	 * @param buffer the packet byte buffer
	 * @return the new datagram packet
	 * @throws UnknownHostException if the host could not be resolved
	 */
	public DatagramPacket makePacket(byte[] buffer) throws UnknownHostException {
		return new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), port);
	}
	
}
