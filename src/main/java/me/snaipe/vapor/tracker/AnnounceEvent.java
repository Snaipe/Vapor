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

public enum AnnounceEvent {
	
	NONE      (0),
	STARTED   (1),
	STOPPED   (2),
	COMPLETED (3);
	
	public final int id;
	
	private AnnounceEvent(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
