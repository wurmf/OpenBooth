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
package org.openbooth.camera.libgphoto2java;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.io.Closeable;
import org.openbooth.camera.libgphoto2java.jna.GPhoto2Native;


/**
 * Represents a file on the simcam.
 * @author Martin Vysny
 */
public class CameraFile implements Closeable {

    final Pointer cf;

    /**
     * Creates a new file link. The file is not yet linked to any particular simcam file - the link is performed later on, by invoking gphoto functions.
     */
    CameraFile() {
	final PointerByReference p = new PointerByReference();
	CameraUtils.check(GPhoto2Native.INSTANCE.gp_file_new(p), "gp_file_new");
	cf = p.getValue();
    }

    public void clean() {
        CameraUtils.check(GPhoto2Native.INSTANCE.gp_file_clean(cf), "gp_file_clean");
    }
    
    /**
     * Closes this file link and frees allocated resources.
     */
    @Override
    public void close() {
	CameraUtils.check(GPhoto2Native.INSTANCE.gp_file_free(cf), "gp_file_free");
    }

    /**
     * Saves the file from the simcam to the local file system.
     * @param filename OS-dependent path on the local file system.
     */
    public void save(String filename) {
		CameraUtils.check(GPhoto2Native.INSTANCE.gp_file_save(cf, filename), "gp_file_save");
    }

    void ref() {
	CameraUtils.check(GPhoto2Native.INSTANCE.gp_file_ref(cf), "gp_file_ref");
    }

    void unref() {
	CameraUtils.check(GPhoto2Native.INSTANCE.gp_file_unref(cf), "gp_file_unref");
    }

    /**
     * Represents a path of a simcam file.
     */
    static class Path
	{

		final String filename;
		final String path;

		/**
		 * Creates new path.
		 * @param filename the file name, without the path, gphoto-dependent.
		 * @param path the path, gphoto-dependent.
		 */
		public Path(String filename, String path)
		{
			this.filename = filename;
			this.path = path;
		}

		public Path(GPhoto2Native.CameraFilePath path)
		{
			filename = CameraUtils.toString(path.name);
			this.path = CameraUtils.toString(path.folder);
		}

		@Override
		public String toString() {
			return "Path{" + path + " " + filename + '}';
		}

		/**
		 * Returns a referenced simcam file.
		 * @param cam the simcam handle.
		 * @return simcam file.
		 */
		CameraFile newFile(Pointer cam)
		{
			boolean returnedOk = false;
			final CameraFile cf = new CameraFile();
			try
			{
				CameraUtils.check(GPhoto2Native.INSTANCE.gp_camera_file_get(cam, path, filename, GPhoto2Native.GP_FILE_TYPE_NORMAL, cf.cf, CameraList.CONTEXT), "gp_camera_file_get");
				returnedOk = true;
				return cf;
			}
			finally
			{
				if (!returnedOk)
				{
					CameraUtils.closeQuietly(cf);
				}
			}
		}
    }
}
