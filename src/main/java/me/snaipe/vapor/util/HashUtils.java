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
package me.snaipe.vapor.util;

import java.security.MessageDigest;

public class HashUtils {
	
	private static String[] HEX = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	public static byte[] SHA1(String text) { 
		try {
		    MessageDigest md = MessageDigest.getInstance("SHA-1");
		    md.update(text.getBytes("US-ASCII"), 0, text.length());
		    return md.digest();
		} catch (Throwable t) {
			throw new RuntimeException(t); // not happening ever, but you never know...
		}
    }
	
	private static boolean isAscii(byte c) {
		return c >= '0' && c <= '9'
		     || c >= 'a' && c <= 'z'
		     || c >= 'A' && c <= 'Z'
	         || c == '$'
	         || c == '-'
	         || c == '_'
	         || c == '.'
	         || c == '!';
	}

	public static String toURLString(byte[] hash) {
	    StringBuilder out = new StringBuilder(hash.length * 2);

	    for (int i = 0; i < hash.length; i++) {
	    	if (isAscii(hash[i])) {
	    		out.append((char) hash[i]);
	    	} else {
	    		out.append('%')
	    		   .append(HEX[(hash[i] & 0xF0) >>> 4 & 0x0F])
	    		   .append(HEX[hash[i] & 0x0F]);
    		}
	    }
	    return out.toString();
	}
	
}
