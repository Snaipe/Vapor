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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

import me.snaipe.vapor.PeerWorld;
import me.snaipe.vapor.exception.TrackerConnectionException;
import me.snaipe.vapor.network.DatagramURL;

public class UDPTracker extends Tracker {

	private static int MAX_ROUNDS = 8;
	private static int ACTION_ID_ANNOUNCE = 1;
	private static int ACTION_ID_CONNECT  = 0;
	
	private static int CONNECT_BUFFER_SIZE = 16;
	private static int ANNOUNCE_BUFFER_SIZE = 98;
	
	private DatagramURL url;
	private DatagramSocket socket;
	
	protected long connection_id = 0L;
	protected long connection_time = 0L;
	
	public UDPTracker(DatagramURL url, PeerWorld peerWorld) {
		super(peerWorld);
		this.url = url;
	}
	
	private ByteBuffer getRawConnectMessage(int transaction_id) {
		return ByteBuffer.allocate(CONNECT_BUFFER_SIZE)
				.putLong(0x41727101980L)
	    		.putInt(ACTION_ID_CONNECT)
	    		.putInt(transaction_id);
	}
	
	private ByteBuffer getRawAnnounceMessage(AnnounceEvent event, long connection_id, int transaction_id) {
		return ByteBuffer.allocate(ANNOUNCE_BUFFER_SIZE)
				.order(ByteOrder.BIG_ENDIAN) 
				.putLong(connection_id) 
				.putInt(ACTION_ID_ANNOUNCE)
				.putInt(transaction_id)
				.put(peerWorld.getHash())
				.put(peerWorld.getPeerID())
				.putLong(0)                  // downloaded
				.putLong(0)                  // uploaded
				.putLong(0)                  // left
				.putInt(event.id)            // event
				.putInt(0)                   // ip, 0 = default
				.putInt(0)                   // key
				.putInt(50)                  // num_want
				.putChar((char) peerWorld.getPort());
	}
	
	public boolean isSocketOpen() {
		return socket != null && !socket.isClosed();
	}
	
	public boolean hasConnectionExpired() {
		return connection_time + 60000l - System.currentTimeMillis() < 0;
	}
	
	@Override
	public synchronized void update() throws TrackerConnectionException {
		checkAndInitSocket();
		if (hasConnectionExpired()) {
			connect();
		}
		super.update();
	}
	
	@Override
	public synchronized void close() throws TrackerConnectionException {
		checkAndInitSocket();
		if (hasConnectionExpired()) {
			connect();
		}
		super.close();
		socket.close();
	}
	
	private void checkAndInitSocket() throws TrackerConnectionException {
		if (!isSocketOpen()) {
			try {
				socket = new DatagramSocket();
			} catch (Exception e) {
				throw new TrackerConnectionException(e);
			}
		}
	}
	
	@Override
	protected void announce(AnnounceEvent event) {
		List<Peer> peers = new LinkedList<Peer>();
		
		try {
			boolean received = false;
			for (int rounds = 0; !received && rounds < MAX_ROUNDS; ++rounds) {
				
				int transaction_id = random.nextInt();
				byte[] msg = getRawAnnounceMessage(event, this.connection_id, transaction_id).array();
				DatagramPacket sentPacket = url.makePacket(msg);
				
				try {
					this.socket.send(sentPacket);
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
				
				DatagramPacket packet = url.makePacket(new byte[65508]);
				try {
					this.socket.setSoTimeout((int) (15000 * Math.pow(2, rounds)));
					this.socket.receive(packet);
					
					if (packet.getLength() >= 20) {
						ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
						int action = buffer.getInt();
						int received_tid = buffer.getInt();
						
						if (action == ACTION_ID_ANNOUNCE && received_tid == transaction_id) {
							
							int interval = buffer.getInt();
							this.next_allowed_announce = System.currentTimeMillis() + interval * 1000l;
							
							buffer.getInt(); // leechers
							buffer.getInt(); // seeders
							
							int remaining = packet.getLength() - 20;
							
							while (remaining >= 6) {
								byte[] ip = new byte[4];
								buffer.get(ip);
								short port = buffer.getShort();
								
								peers.add(new Peer(InetAddress.getByAddress(ip), port));
								remaining -= 6;
							}
							
							received = true;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		}
		
		this.peers = peers.toArray(new Peer[peers.size()]);
	}
	
	protected void connect() {
		try {
			boolean received = false;
			for (int rounds = 0; !received && rounds < MAX_ROUNDS; ++rounds) {
				int transaction_id = random.nextInt();
				
				DatagramPacket sentPacket = url.makePacket(getRawConnectMessage(transaction_id).array());
				try {
					this.socket.send(sentPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				DatagramPacket packet = url.makePacket(new byte[256]);
				try {
					this.socket.setSoTimeout((int) (15000 * Math.pow(2, rounds)));
					this.socket.receive(packet);
					
					ByteBuffer buffer = ByteBuffer.wrap(packet.getData());
					int action = buffer.getInt();
					int received_tid = buffer.getInt();
					long received_cid = buffer.getLong();
					
					if (action == ACTION_ID_CONNECT && received_tid == transaction_id) {
						this.connection_time = System.currentTimeMillis();
						this.connection_id = received_cid;
						received = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		}
	}
}
