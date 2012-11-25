/*
 * JHide Copyright (C) 2010 Mladen Krstic <mkrstich@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package jhide.misc;

/**
 *
 * @author Mladen
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jhide.MainFrame;
import org.steganography.carrier.CarrierImage;
import org.steganography.error.SteganographyException;
import org.steganography.hiding.HidingFile;
import org.steganography.misc.ImageManager;

/**
 *
 * @author ncottin
 */
public final class HideAndSave extends HideAndRevealProcess {

    private static final String TMP_FILE_PREFIX = "_hr";
    private static final String TMP_FILE_SUFFIX = null;

    public HideAndSave(final MainFrame parent, final File carrier, final File comp, final File dest, char[] password) {
        super(parent, carrier, comp, dest, password);
        parent.setStatusPanel(parent.bundle.getString("StatusHidingText"), true);
    }

    public final void process(final CarrierImage ci, final ImageManager imageManager) throws IOException, SteganographyException {
        File tmp = null;
        OutputStream out = null;
        try {
            HidingFile hidingFile = new HidingFile();
            File comp = getComp();
            boolean hasComponent = (comp != null);
            tmp = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);

            out = new FileOutputStream(tmp);
            ZipManager zipManager = new ZipManager(out);

            // Add secret file to archive
            String compName;
            if (hasComponent) {
                compName = comp.getName();
                zipManager.addEntry(compName, comp);
            }
            else {
                compName = null;
            }

            zipManager.terminate();
            out.close();
            out = null;
            hidingFile.setFile(tmp);
            ci.hide(hidingFile);
            imageManager.terminate();

            // Explicitly delete file
            tmp.delete();

            // Save to another file
            File dest = getDest();
            ImageManager.writeImage(ci.getCarrierImage(), dest, true);
        } finally {
            // Make some final cleanup
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    System.out.println("Exception unutar finally bloka");
                    e.printStackTrace();
                }
            }

            // Make sure the temporay file is deleted
            if (tmp != null) {
                tmp.delete();
            }
        }
    }
}
