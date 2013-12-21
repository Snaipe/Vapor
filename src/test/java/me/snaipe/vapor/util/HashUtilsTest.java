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

import org.junit.Assert;
import org.junit.Test;

public class HashUtilsTest {
	
	@Test
	public void test_toURLString() {
		byte[] hash = {
				(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9a, 
				(byte) 0xbc, (byte) 0xde, (byte) 0xf1, (byte) 0x23, (byte) 0x45, 
				(byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef, 
				(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9a
		};
		
		Assert.assertEquals("%124Vx%9A%BC%DE%F1%23Eg%89%AB%CD%EF%124Vx%9A", HashUtils.toURLString(hash));
	}

}
