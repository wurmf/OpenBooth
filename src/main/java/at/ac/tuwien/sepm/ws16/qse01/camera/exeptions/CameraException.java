/**
 * Java bindings for the libgphoto2 library.
 * Copyright (C) 2011 Innovatrics s.r.o.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package at.ac.tuwien.sepm.ws16.qse01.camera.exeptions;


/**
 * Thrown by the GPhoto Java bindings.
 * @author Martin Vysny
 */
public class CameraException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int result;

    public CameraException(String message, Throwable cause, int result) {
	super(message, cause);
	this.result = result;
    }

    public CameraException(String message, int result) {
	super(message);
	this.result = result;
    }

    public int getResult()
    {
        return result;
    }
}
