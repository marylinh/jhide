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

package jhide;

import java.awt.Point;
import javax.swing.UIManager;
import jhide.misc.Utils;
/**
 *
 * @author Mladen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    try  {
       UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
    } catch (Exception e)  {
      e.printStackTrace();
    }
        MainFrame mf = new MainFrame();
        mf.setLocationRelativeTo(null);
        Point p = Utils.getScreenCenterCoords(mf);
        mf.setLocation(p);
        mf.setVisible(true);
    }

}
