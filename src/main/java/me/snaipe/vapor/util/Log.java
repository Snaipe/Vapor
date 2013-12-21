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

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	
	private final static Logger logger = Logger.getLogger("Vapor");
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static void log(Level level, String message, Object... args) {
		logger.log(level, "[Vapor] " + MessageFormat.format(message, args));
	}
	
	public static void info(String message, Object... args) {
		log(Level.INFO, message, args);
	}
	
	public static void warning(String message, Object... args) {
		log(Level.WARNING, message, args);
	}
	
	public static void severe(String message, Object... args) {
		log(Level.SEVERE, message, args);
	}
	
	public static void fine(String message, Object... args) {
		log(Level.FINE, message, args);
	}
	
	public static void finer(String message, Object... args) {
		log(Level.FINER, message, args);
	}
	
	public static void finest(String message, Object... args) {
		log(Level.FINEST, message, args);
	}

}
