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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mladen
 */
public class Utils {

    public static ArrayList<FileNameExtensionFilter> getHostFileFilters() {
        ArrayList<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
        filters.add(new FileNameExtensionFilter("Supported formats (*.bmp, *.png, *.tif, *.tiff)", "bmp", "png", "tif", "tiff"));
        return filters;
    }

    public static ArrayList<FileNameExtensionFilter> getSecretFileFilters() {
        ArrayList<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
        return filters;
    }
    public static ArrayList<FileNameExtensionFilter> getHideDestFileFilters() {
        ArrayList<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
        filters.add(new FileNameExtensionFilter("Supported formats (*.bmp, *.png, *.tif, *.tiff)", "bmp", "png", "tif", "tiff"));
        return filters;
    }

    public static ArrayList<FileNameExtensionFilter> getRevealingDestFileFilters() {
        ArrayList<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
        filters.add(new FileNameExtensionFilter("Archive file (*.zip)", "zip"));
        return filters;
    }
    public static Point getScreenCenterCoords(Window form) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = form.getSize();
        int windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
        int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2);
        return new Point(windowX, windowY);
    }

}
