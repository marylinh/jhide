/*
 * This file is part of Hide & Reveal <www.hidereveal.org>.

    Hide & Reveal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

     Hide & Reveal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.

 */

package jhide.misc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author ncottin
 */
public final class ZipManager {

    public static final int DEFAULT_BUFFER_SIZE = 512;
    private final ZipOutputStream out;
    private final int bufferSize;

    public ZipManager(OutputStream dest) {
        this(dest, DEFAULT_BUFFER_SIZE);
    }

     public ZipManager(final OutputStream dest, int bufferSize) {
        out = new ZipOutputStream(dest);
        this.bufferSize = bufferSize;
    }

    public void addEntry(String name, InputStream value) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        out.putNextEntry(entry);
        byte[] buffer = new byte[bufferSize];
        int nbRead;
        while ((nbRead = value.read(buffer)) != -1) {
            out.write(buffer, 0, nbRead);
        }

        out.closeEntry();
    }

    public void addEntry(String name, byte[] value) throws IOException {
        InputStream in = new ByteArrayInputStream(value);
        addEntry(name, in);
        in.close();
    }

    public void addEntry(String name, File value) throws IOException {
        InputStream in = new FileInputStream(value);
        addEntry(name, in);
        in.close();
    }

    public void terminate() throws IOException {
        out.finish();
    }
}
