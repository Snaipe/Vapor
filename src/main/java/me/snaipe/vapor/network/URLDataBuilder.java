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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLDataBuilder {
	
	private StringBuilder builder;
	
	public URLDataBuilder() {
		this.builder = new StringBuilder();
	}
	
	public URLDataBuilder append(String key, Object value) {
		if (value == null || key == null || key.trim().length() == 0) {
			throw new IllegalArgumentException("Key and values must not be null and key must not be blank.");
		}
		if (this.builder.length() > 0) {
			this.builder.append("&");
		}
		try {
			this.builder
				.append(URLEncoder.encode(key, "UTF-8"))
				.append("=")
				.append(URLEncoder.encode(value.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); // does not happen.
		}
		return this;
	}
	
	public String build() {
		return this.builder.toString();
	}

}
