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
import java.io.IOException;
import javax.swing.JOptionPane;
import jhide.MainFrame;
import org.steganography.carrier.CarrierImage;
import org.steganography.error.CarrierSizeException;
import org.steganography.error.CompatibilityException;
import org.steganography.error.SteganographyException;
import org.steganography.misc.ImageManager;
import org.steganography.schemes.modulation.ComponentModulationScheme;
import org.steganography.schemes.image.access.ImageAccessScheme;
import org.steganography.schemes.image.access.InvertedUniformImageAccessScheme;
import org.steganography.schemes.image.hiding.DualLSBImageHidingScheme;
import org.steganography.schemes.modulation.CyclicPasswordComponentModulationScheme;



/**
 *
 * @author ncottin
 */
public abstract class HideAndRevealProcess implements Runnable {

    private MainFrame parent;
    private File carrier;
    private File comp;
    private File dest;
    private char[] password;

    public HideAndRevealProcess(final MainFrame parent,final File carrier, final File comp, final File dest, char[] password) {
        this.parent = parent;
        this.carrier = carrier;
        this.comp = comp;
        this.dest = dest;
        this.password = transform(password);

    }
    private char[] transform(char[] p) {
        if (p == null)
            p = "".toCharArray();
        return p;
    }
    public final MainFrame getParent() {
        return parent;
    }

    public final File getCarrier() {
        return carrier;
    }

    public final File getComp() {
        return comp;
    }


    public final File getDest() {
        return dest;
    }

    public final void run() {
        if (!carrier.exists()) {
            JOptionPane.showMessageDialog(parent, parent.bundle.getString("MissingCarrier"), parent.bundle.getString("ErrorTitle"), JOptionPane.WARNING_MESSAGE);
            terminate(true);
            return;
        }

        if ((comp != null) && !comp.exists()) {
            JOptionPane.showMessageDialog(parent, parent.bundle.getString("MissingSecret"), parent.bundle.getString("ErrorTitle"), JOptionPane.WARNING_MESSAGE);
            terminate(true);
            return;
        }
        parent.setLockedControls(true);

        ImageManager imageManager = new ImageManager();
        boolean error = true;

        try {
            ComponentModulationScheme modulationScheme = new CyclicPasswordComponentModulationScheme(password);
            if (modulationScheme == null) {
                throw new Exception();
            }

            ImageAccessScheme accessScheme = new InvertedUniformImageAccessScheme();
            if (accessScheme == null) {
                throw new Exception();
            }

            CarrierImage ci = new CarrierImage(accessScheme, null, modulationScheme);
            ci.setCarrierImage(imageManager.loadImage(carrier));
            ci.setImageHidingScheme(new DualLSBImageHidingScheme());
                try {
                    process(ci, imageManager);
                } catch (CarrierSizeException e) {
                    JOptionPane.showMessageDialog(parent, parent.bundle.getString("HostTooSmall"), parent.bundle.getString("ErrorTitle"), JOptionPane.WARNING_MESSAGE);
                    cleanup(parent);
                    e.printStackTrace();
                    return;

                } catch (CompatibilityException e) {
                    JOptionPane.showMessageDialog(parent, parent.bundle.getString("HostNotCompatible"), parent.bundle.getString("ErrorTitle"), JOptionPane.WARNING_MESSAGE);
                    cleanup(parent);
                    e.printStackTrace();
                    return;
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, parent.bundle.getString("IllegalFile"), parent.bundle.getString("ErrorTitle"), JOptionPane.WARNING_MESSAGE);
            cleanup(parent);
            e.printStackTrace();
            return;
        }
        imageManager.terminate();
        terminate(error);
        cleanup(parent);
        JOptionPane.showMessageDialog(parent, parent.bundle.getString("SuccessMessage"), parent.bundle.getString("SuccessTitle"), JOptionPane.INFORMATION_MESSAGE);
    }
    private void cleanup(MainFrame parent) {
        parent.removeReferences();
        parent.setStatusPanel(parent.bundle.getString("StatusDone"), false);
        parent.setLockedControls(false);
    }

    private void terminate(boolean error) {
        // todo
    }

    public abstract void process(final CarrierImage ci, final ImageManager imageManager) throws IOException, SteganographyException;

}
