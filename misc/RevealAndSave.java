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

import java.io.File;
import java.io.IOException;
import jhide.MainFrame;
import org.steganography.carrier.CarrierImage;
import org.steganography.error.SteganographyException;
import org.steganography.hiding.HidingFile;
import org.steganography.misc.ImageManager;

/**
 *
 * @author Mladen
 */
public class RevealAndSave extends HideAndRevealProcess {

    public RevealAndSave(MainFrame parent, File revealingHost, File revealingDest, char[] password) {
        super(parent, revealingHost, null, revealingDest, password);
        parent.setStatusPanel(parent.bundle.getString("StatusRevealingText"), true);
    }

    @Override
    public void process(CarrierImage ci, ImageManager imageManager) throws IOException, SteganographyException {
        HidingFile comp = new HidingFile();
        comp.setFile(getDest());
        ci.reveal(comp);
    }

}
