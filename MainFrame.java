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

/**
 *
 * @author Mladen
 */

import jhide.misc.RevealAndSave;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import jhide.misc.HideAndSave;
import jhide.misc.Utils;
public class MainFrame extends javax.swing.JFrame {


    private File hidingHostDirectory;
    private File hidingHost;
    private File secretDirectory;
    private File secret;
    private File hidingDestDirectory;
    private File hidingDest;
    private File revealingHostDirectory;
    private File revealingHost;
    private File revealingDestDirectory;
    private File revealingDest;
    private static String BUNDLE_PATH = "jhide/Bundle";
    public static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PATH);
    private ImageIcon appIcon = new ImageIcon(MainFrame.class.getResource("resources/topsecret32x32.png"));

    /** Creates new form MainFrame2 */
    public MainFrame() {
        initComponents();
        setIconImage(appIcon.getImage());
        setBundle(null);
         moveToFront(pocetniPanel);
         revealingPasswordCheckBoxStateChanged(null);
         hidingPasswordCheckBoxStateChanged(null);
         setStatusPanel(bundle.getString("StatusInit"), false);

    }

    public synchronized void setBundle(Locale locale) {
        ResourceBundle.clearCache();
        if (locale == null) {
            // default locale
            locale = Locale.getDefault();
        }
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_PATH, locale);
        } catch (MissingResourceException  mrb) {
            bundle = ResourceBundle.getBundle(BUNDLE_PATH);
        }
        setSelectedLanguageItem(bundle.getLocale().getLanguage());
        mainButtonsLanguageUpdate();
        menuLanguageUpdate();
        welcomePanelLanguageUpdate();
        hidePanelLanguageUpdate();
        revealPanelLanguageUpdate();
        miscLanguageUpdate();
    }
    private void mainButtonsLanguageUpdate() {
        mainHideLabel.setText(bundle.getString("MainHideLabel"));
        mainRevealLabel.setText(bundle.getString("MainRevealLabel"));
    }
    private void menuLanguageUpdate() {
        // "File" menu
        JMenu menu = menuBar.getMenu(0);
        menu.setText(bundle.getString("FileMenu"));
        JMenuItem item = menu.getItem(0);
        item.setText(bundle.getString("ExitMenuItem"));

        // "View" menu
        menu = menuBar.getMenu(1);
        menu.setText(bundle.getString("ViewMenu"));
        languageMenu.setText(bundle.getString("LanguageMenu"));

        // "Help" menu
        menu = menuBar.getMenu(2);
        menu.setText(bundle.getString("HelpMenu"));
        item = menu.getItem(0);
        item.setText(bundle.getString("AboutMenuItem"));
    }
    private void welcomePanelLanguageUpdate() {
        // later
    }
    private void hidePanelLanguageUpdate() {
        hidingTitleLabel.setText(bundle.getString("HidingTitle"));
        hidingDescLabel.setText(bundle.getString("HidingDesc"));
        hidingHostLabel.setText(bundle.getString("HidingHost"));
        hidingSecretLabel.setText(bundle.getString("HidingSecret"));
        hidingDestLabel.setText(bundle.getString("HidingDest"));
        hidingPasswordLabel.setText(bundle.getString("HidingPassword"));
        hidingHostBrowseBtn.setText(bundle.getString("HidingBrowse"));
        hidingSecretBrowseBtn.setText(bundle.getString("HidingBrowse"));
        hidingDestBrowseBtn.setText(bundle.getString("HidingBrowse"));
        hidingExecuteBtn.setText(bundle.getString("HidingExecute"));
    }
    private void revealPanelLanguageUpdate() {
        revealingTitleLabel.setText(bundle.getString("RevealingTitle"));
        revealingDescLabel.setText(bundle.getString("RevealingDesc"));
        revealingHostLabel.setText(bundle.getString("RevealingHost"));
        revealingDestLabel.setText(bundle.getString("RevealingDest"));
        revealingPasswordLabel.setText(bundle.getString("RevealingPassword"));
        revealingHostBrowseBtn.setText(bundle.getString("RevealingBrowse"));
        revealingDestBrowseBtn.setText(bundle.getString("RevealingBrowse"));
        revealingExecuteBtn.setText(bundle.getString("RevealingExecute"));
    }
    private void miscLanguageUpdate() {
        // status
        String currentText = statusMsg.getText();
        boolean isSerbian = serbianMenuItem.isSelected();
        ResourceBundle origBundle,destBundle;
        if (isSerbian) {
            destBundle = ResourceBundle.getBundle(BUNDLE_PATH, new Locale("sr"));
            origBundle = ResourceBundle.getBundle(BUNDLE_PATH, Locale.ENGLISH);
        } else {
            origBundle = ResourceBundle.getBundle(BUNDLE_PATH, new Locale("sr"));
            destBundle = ResourceBundle.getBundle(BUNDLE_PATH, Locale.ENGLISH);
        }
        Iterator<String> it = origBundle.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (origBundle.getString(key).equals(currentText)) {
                setStatusPanel(destBundle.getString(key), statusProgress.isVisible());
            }
        }
    }
    private void setSelectedLanguageItem(String langIso639Code) {
        if (langIso639Code != null) {
            boolean isSerbian = langIso639Code.equalsIgnoreCase("sr") || langIso639Code.equalsIgnoreCase("scr");
            serbianMenuItem.setSelected(isSerbian);
            englishMenuItem.setSelected(!isSerbian);
        }
        
    }

    private File getHidingDestinationFile() {
       StringBuilder sb = new StringBuilder();
        if (hidingDestDirectory != null) {
            sb.append(hidingDestDirectory.getAbsolutePath());
        } else if (hidingHost != null) {
            sb.append(hidingHost.getParent());
        }
        sb.append(File.separator);
        sb.append("stego_");
        if (hidingHost != null) {
            sb.append(hidingHost.getName());
        }
        return new File(sb.toString());
    }


    private void moveToFront(javax.swing.JPanel visiblePanel) {
        pocetniPanel.setVisible(false);
        sakrijPanel.setVisible(false);
        otkrijPanel.setVisible(false);
        visiblePanel.setVisible(true);
        desniSlojevitiPanel.moveToFront(visiblePanel);
    }
    private void selectHidingHostAction() {
        File file = selectFile(Utils.getHostFileFilters(), hidingHostDirectory, false);
        if (file == null || !file.exists()) {
            return;
        }

        hidingHost = file;
        hidingHostDirectory = file.getParentFile();
        hidingHostTextField.setText(hidingHost.getPath());
        if (hidingDest == null) {
            hidingDest = getHidingDestinationFile();
            hidingDestTextField.setText(hidingDest.getPath());
            hidingDestDirectory = file.getParentFile();
        }

    }
    private void selectHidingSecretAction() {
        File file = selectFile(Utils.getSecretFileFilters(), secretDirectory, true);
        if (file == null || !file.exists()) {
            return;
        }

        secret = file;
        secretDirectory = file.getParentFile();
        hidingSecretTextField.setText(secret.getPath());
        System.out.println(secret.getPath());
        hidingExecuteBtn.setEnabled(true);
    }
    private void selectHidingDestAction() {
        File file = selectFile(Utils.getHideDestFileFilters(), hidingDestDirectory, false);
        if (file == null) {
            return;
        }
        hidingDestDirectory = file;
        hidingDestTextField.setText(hidingDestDirectory.getPath());
    }
    private File selectFile(ArrayList<FileNameExtensionFilter> filters, File directory, boolean selectAllFiles) {
        JFileChooser fc = new JFileChooser();


        if (directory != null) {
            fc.setCurrentDirectory(directory);
        }

        if (filters != null) {
            for (FileFilter filter : filters) {
                if (filter != null) {
                    fc.setFileFilter(filter);
                }
            }
        }
        fc.setAcceptAllFileFilterUsed(selectAllFiles);

        int returnVal = fc.showOpenDialog(this);
        return (returnVal == JFileChooser.APPROVE_OPTION)
                ? fc.getSelectedFile()
                : null;
    }
    private void selectRevealingDestAction() {
        File file = selectFile(Utils.getRevealingDestFileFilters(), revealingDestDirectory, false);
        if (file == null) {
            return;
        }
        revealingDestDirectory = file;
        revealingDestTextField.setText(revealingDestDirectory.getPath());
        hidingDestTextField.setText(hidingDestDirectory.getPath());
    }

    private void selectRevealingHostAction() {
        File file = selectFile(Utils.getHostFileFilters(), revealingHostDirectory, false);
        if (file == null || !file.exists()) {
            return;
        }
        revealingHost = file;
        revealingHostDirectory = file.getParentFile();
        revealingHostTextField.setText(revealingHost.getPath());
        if (revealingDest == null) {
            revealingDest = getRevealingDestinationFile();
            revealingDestTextField.setText(revealingDest.getPath());
            revealingDestDirectory = file.getParentFile();
        }
        revealingExecuteBtn.setEnabled(true);
    }

    private File getRevealingDestinationFile() {
        StringBuilder sb = new StringBuilder();
        if (revealingDestDirectory != null) {
            sb.append(revealingDestDirectory.getAbsolutePath());
        } else if (revealingHost != null) {
            sb.append(revealingHost.getParent());
        }
        sb.append(File.separator);
        sb.append("revealed_");
        if (revealingHost != null) {
            sb.append(revealingHost.getName());
            sb.append(".zip");
        }
        return new File(sb.toString());
    }

    private void resetHidingFields() {
        hidingHost = null;
        hidingHostDirectory = null;
        hidingHostTextField.setText("");
        secret = null;
        secretDirectory = null;
        hidingSecretTextField.setText("");
        hidingDest = null;
        hidingDestDirectory = null;
        hidingDestTextField.setText("");
        hidingExecuteBtn.setEnabled(false);
        hidingPasswordField.setText("");
        hidingPasswordField.setVisible(false);
        hidingPasswordLabel.setVisible(false);
        hidingPasswordCheckBox.setSelected(false);        
    }
    private void resetRevealingFields() {
        revealingHost = null;
        revealingHostDirectory = null;
        revealingHostTextField.setText("");
        revealingDest = null;
        revealingDestDirectory = null;
        revealingDestTextField.setText("");
        revealingExecuteBtn.setEnabled(false);
        revealingPasswordField.setText("");
        revealingPasswordField.setVisible(false);
        revealingPasswordLabel.setVisible(false);
        revealingPasswordCheckBox.setSelected(false);
    }

    private void selectHidingExecuteAction() {
        new Thread(new HideAndSave(this, hidingHost, secret, hidingDest, hidingPasswordField.getPassword())).start();
    }

    public void removeReferences() {
        resetHidingFields();
        resetRevealingFields();
    }

    private void selectRevealingExecuteAction() {
        new Thread(new RevealAndSave(this, revealingHost, revealingDest, revealingPasswordField.getPassword())).start();
    }
    public void setStatusPanel(String statusText, boolean isProgressVisible) {
        if (statusText != null)
            statusMsg.setText(statusText);
        statusProgress.setVisible(isProgressVisible);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        statusProgress = new javax.swing.JProgressBar();
        statusMsg = new javax.swing.JLabel();
        desniSlojevitiPanel = new javax.swing.JLayeredPane();
        pocetniPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sakrijPanel = new javax.swing.JPanel();
        hidingTitleLabel = new javax.swing.JLabel();
        hidingSecretTextField = new javax.swing.JTextField();
        hidingDestTextField = new javax.swing.JTextField();
        hidingExecuteBtn = new javax.swing.JButton();
        hidingHostLabel = new javax.swing.JLabel();
        hidingSecretLabel = new javax.swing.JLabel();
        hidingDestLabel = new javax.swing.JLabel();
        hidingHostBrowseBtn = new javax.swing.JButton();
        hidingDestBrowseBtn = new javax.swing.JButton();
        hidingSecretBrowseBtn = new javax.swing.JButton();
        hidingHostTextField = new javax.swing.JTextField();
        hidingPasswordLabel = new javax.swing.JLabel();
        hidingPasswordField = new javax.swing.JPasswordField();
        hidingPasswordCheckBox = new javax.swing.JToggleButton();
        hidingDescLabel = new javax.swing.JLabel();
        otkrijPanel = new javax.swing.JPanel();
        revealingTitleLabel = new javax.swing.JLabel();
        revealingDescLabel = new javax.swing.JLabel();
        revealingHostTextField = new javax.swing.JTextField();
        revealingDestTextField = new javax.swing.JTextField();
        revealingHostLabel = new javax.swing.JLabel();
        revealingDestLabel = new javax.swing.JLabel();
        revealingHostBrowseBtn = new javax.swing.JButton();
        revealingDestBrowseBtn = new javax.swing.JButton();
        revealingExecuteBtn = new javax.swing.JButton();
        revealingPasswordField = new javax.swing.JPasswordField();
        revealingPasswordCheckBox = new javax.swing.JToggleButton();
        revealingPasswordLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        mainRevealLabel = new javax.swing.JLabel();
        btnSakrij = new javax.swing.JButton();
        mainHideLabel = new javax.swing.JLabel();
        btnOtkrij = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        languageMenu = new javax.swing.JMenu();
        serbianMenuItem = new javax.swing.JRadioButtonMenuItem();
        englishMenuItem = new javax.swing.JRadioButtonMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JHide!");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(586, 482));
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/yellow_header3.png"))); // NOI18N

        statusPanel.setBackground(new java.awt.Color(255, 255, 255));

        statusProgress.setEnabled(false);
        statusProgress.setIndeterminate(true);

        statusMsg.setText("Done");

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, Short.MAX_VALUE)
                .addComponent(statusProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusMsg))
                .addContainerGap())
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(22, Short.MAX_VALUE)))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/welcome screen.png"))); // NOI18N
        jLabel2.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout pocetniPanelLayout = new javax.swing.GroupLayout(pocetniPanel);
        pocetniPanel.setLayout(pocetniPanelLayout);
        pocetniPanelLayout.setHorizontalGroup(
            pocetniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pocetniPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pocetniPanelLayout.setVerticalGroup(
            pocetniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pocetniPanelLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pocetniPanel.setBounds(0, 0, 490, 350);
        desniSlojevitiPanel.add(pocetniPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        sakrijPanel.setPreferredSize(new java.awt.Dimension(460, 370));

        hidingTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        hidingTitleLabel.setText("Proces sakrivanja");

        hidingSecretTextField.setEditable(false);
        hidingSecretTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hidingSecretTextFieldMouseClicked(evt);
            }
        });

        hidingDestTextField.setEditable(false);
        hidingDestTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hidingDestTextFieldMouseClicked(evt);
            }
        });

        hidingExecuteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/process_accept24x24.png"))); // NOI18N
        hidingExecuteBtn.setText("Izvrsi");
        hidingExecuteBtn.setEnabled(false);
        hidingExecuteBtn.setMaximumSize(new java.awt.Dimension(102, 33));
        hidingExecuteBtn.setMinimumSize(new java.awt.Dimension(102, 33));
        hidingExecuteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hidingExecuteBtnActionPerformed(evt);
            }
        });

        hidingHostLabel.setText("Noseći fajl (host):");

        hidingSecretLabel.setText("Tajni fajl:");

        hidingDestLabel.setText("Odrediste:");

        hidingHostBrowseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/browse_btn24x24.png"))); // NOI18N
        hidingHostBrowseBtn.setText("Traži...");
        hidingHostBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hidingHostBrowseBtnActionPerformed(evt);
            }
        });

        hidingDestBrowseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/browse_btn24x24.png"))); // NOI18N
        hidingDestBrowseBtn.setText("Traži...");
        hidingDestBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hidingDestBrowseBtnActionPerformed(evt);
            }
        });

        hidingSecretBrowseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/browse_btn24x24.png"))); // NOI18N
        hidingSecretBrowseBtn.setText("Traži...");
        hidingSecretBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hidingSecretBrowseBtnActionPerformed(evt);
            }
        });

        hidingHostTextField.setEditable(false);
        hidingHostTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        hidingHostTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hidingHostTextFieldMouseClicked(evt);
            }
        });

        hidingPasswordLabel.setText("Lozinka:");

        hidingPasswordCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/lock_button_light24x24.png"))); // NOI18N
        hidingPasswordCheckBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/lock_button_red24x24.png"))); // NOI18N
        hidingPasswordCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hidingPasswordCheckBoxStateChanged(evt);
            }
        });

        hidingDescLabel.setText("Kratko uputstvo");

        javax.swing.GroupLayout sakrijPanelLayout = new javax.swing.GroupLayout(sakrijPanel);
        sakrijPanel.setLayout(sakrijPanelLayout);
        sakrijPanelLayout.setHorizontalGroup(
            sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sakrijPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sakrijPanelLayout.createSequentialGroup()
                        .addComponent(hidingTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(282, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sakrijPanelLayout.createSequentialGroup()
                        .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, sakrijPanelLayout.createSequentialGroup()
                                .addComponent(hidingDestTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(hidingDestBrowseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(hidingDescLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                            .addComponent(hidingDestLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hidingSecretLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hidingHostLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sakrijPanelLayout.createSequentialGroup()
                                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(sakrijPanelLayout.createSequentialGroup()
                                        .addComponent(hidingPasswordCheckBox)
                                        .addGap(28, 28, 28)
                                        .addComponent(hidingPasswordLabel)
                                        .addGap(10, 10, 10)
                                        .addComponent(hidingPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                                    .addComponent(hidingSecretTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                    .addComponent(hidingHostTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hidingExecuteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(hidingHostBrowseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                        .addComponent(hidingSecretBrowseBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))))
                        .addGap(62, 62, 62))))
        );
        sakrijPanelLayout.setVerticalGroup(
            sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sakrijPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hidingTitleLabel)
                .addGap(28, 28, 28)
                .addComponent(hidingDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(hidingHostLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hidingHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hidingHostBrowseBtn))
                .addGap(11, 11, 11)
                .addComponent(hidingSecretLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hidingSecretTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hidingSecretBrowseBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hidingDestLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hidingDestTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hidingDestBrowseBtn))
                .addGap(16, 16, 16)
                .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hidingPasswordCheckBox)
                    .addGroup(sakrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hidingPasswordLabel)
                        .addComponent(hidingPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hidingExecuteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        sakrijPanel.setBounds(0, 0, 490, 350);
        desniSlojevitiPanel.add(sakrijPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        revealingTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        revealingTitleLabel.setText("Proces otkrivanja");

        revealingDescLabel.setText("Poruka sa kratkim uputstvom");

        revealingHostTextField.setEditable(false);
        revealingHostTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                revealingHostTextFieldMouseClicked(evt);
            }
        });

        revealingDestTextField.setEditable(false);

        revealingHostLabel.setText("Stego fajl:");

        revealingDestLabel.setText("Odrediste:");

        revealingHostBrowseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/browse_btn24x24.png"))); // NOI18N
        revealingHostBrowseBtn.setText("Traži...");
        revealingHostBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revealingHostBrowseBtnActionPerformed(evt);
            }
        });

        revealingDestBrowseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/browse_btn24x24.png"))); // NOI18N
        revealingDestBrowseBtn.setText("Traži...");
        revealingDestBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revealingDestBrowseBtnActionPerformed(evt);
            }
        });

        revealingExecuteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/process_accept24x24.png"))); // NOI18N
        revealingExecuteBtn.setText("Izvrsi");
        revealingExecuteBtn.setEnabled(false);
        revealingExecuteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revealingExecuteBtnActionPerformed(evt);
            }
        });

        revealingPasswordCheckBox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/lock_button_light24x24.png"))); // NOI18N
        revealingPasswordCheckBox.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/lock_button_red24x24.png"))); // NOI18N
        revealingPasswordCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                revealingPasswordCheckBoxStateChanged(evt);
            }
        });

        revealingPasswordLabel.setText("Lozinka:");

        javax.swing.GroupLayout otkrijPanelLayout = new javax.swing.GroupLayout(otkrijPanel);
        otkrijPanel.setLayout(otkrijPanelLayout);
        otkrijPanelLayout.setHorizontalGroup(
            otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otkrijPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(revealingDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(otkrijPanelLayout.createSequentialGroup()
                        .addComponent(revealingTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(otkrijPanelLayout.createSequentialGroup()
                        .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(revealingHostLabel)
                            .addGroup(otkrijPanelLayout.createSequentialGroup()
                                .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(revealingHostTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(revealingDestTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, otkrijPanelLayout.createSequentialGroup()
                                        .addComponent(revealingPasswordCheckBox)
                                        .addGap(45, 45, 45)
                                        .addComponent(revealingPasswordLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(revealingPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(revealingHostBrowseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(revealingDestBrowseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(revealingExecuteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(55, 55, 55))
                    .addComponent(revealingDestLabel))
                .addContainerGap())
        );
        otkrijPanelLayout.setVerticalGroup(
            otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(otkrijPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(revealingTitleLabel)
                .addGap(31, 31, 31)
                .addComponent(revealingDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(revealingHostLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(revealingHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(revealingHostBrowseBtn))
                .addGap(13, 13, 13)
                .addComponent(revealingDestLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(revealingDestTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(revealingDestBrowseBtn))
                .addGap(18, 18, 18)
                .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(otkrijPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(revealingExecuteBtn)
                        .addComponent(revealingPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(revealingPasswordLabel))
                    .addComponent(revealingPasswordCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        otkrijPanel.setBounds(0, 0, 480, 350);
        desniSlojevitiPanel.add(otkrijPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        mainRevealLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mainRevealLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainRevealLabel.setText("Reveal");

        btnSakrij.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/right_icon64x64.png"))); // NOI18N
        btnSakrij.setBorder(null);
        btnSakrij.setBorderPainted(false);
        btnSakrij.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSakrijActionPerformed(evt);
            }
        });

        mainHideLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        mainHideLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainHideLabel.setText("Hide");

        btnOtkrij.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/left_icon64x64.png"))); // NOI18N
        btnOtkrij.setBorder(null);
        btnOtkrij.setBorderPainted(false);
        btnOtkrij.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtkrijActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOtkrij)
                            .addComponent(btnSakrij))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(mainRevealLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(mainHideLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(mainHideLabel)
                        .addGap(2, 2, 2)
                        .addComponent(btnSakrij)
                        .addGap(71, 71, 71)
                        .addComponent(btnOtkrij)
                        .addGap(2, 2, 2)
                        .addComponent(mainRevealLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fileMenu.setMnemonic('0');
        fileMenu.setText("File");

        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/delete.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        viewMenu.setText("View");

        languageMenu.setText("Language");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jhide/Bundle"); // NOI18N
        serbianMenuItem.setText(bundle.getString("SerbianLanguageItem")); // NOI18N
        serbianMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/serbia.gif"))); // NOI18N
        serbianMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serbianMenuItemActionPerformed(evt);
            }
        });
        languageMenu.add(serbianMenuItem);

        englishMenuItem.setText("English");
        englishMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jhide/resources/unitedkingdom.gif"))); // NOI18N
        englishMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                englishMenuItemActionPerformed(evt);
            }
        });
        languageMenu.add(englishMenuItem);

        viewMenu.add(languageMenu);

        menuBar.add(viewMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(520, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 620, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(133, Short.MAX_VALUE)
                    .addComponent(desniSlojevitiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(78, 78, 78)
                    .addComponent(desniSlojevitiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSakrijActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSakrijActionPerformed
        int positionInLayer = desniSlojevitiPanel.getPosition(sakrijPanel);
        if (positionInLayer != 0) {
            resetHidingFields();
            moveToFront(sakrijPanel);
            setStatusPanel(bundle.getString("StatusHide"), false);
        }
    }//GEN-LAST:event_btnSakrijActionPerformed

    private void btnOtkrijActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtkrijActionPerformed
        int positionInLayer = desniSlojevitiPanel.getPosition(otkrijPanel);
        if (positionInLayer != 0) {
            resetRevealingFields();
            moveToFront(otkrijPanel);
            setStatusPanel(bundle.getString("StatusReveal"), false);
        }
}//GEN-LAST:event_btnOtkrijActionPerformed

    private void revealingHostTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_revealingHostTextFieldMouseClicked
        selectRevealingHostAction();
}//GEN-LAST:event_revealingHostTextFieldMouseClicked

    private void revealingHostBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revealingHostBrowseBtnActionPerformed
        selectRevealingHostAction();
}//GEN-LAST:event_revealingHostBrowseBtnActionPerformed

    private void revealingDestBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revealingDestBrowseBtnActionPerformed
        selectRevealingDestAction();
}//GEN-LAST:event_revealingDestBrowseBtnActionPerformed

    private void revealingExecuteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revealingExecuteBtnActionPerformed
        selectRevealingExecuteAction();
}//GEN-LAST:event_revealingExecuteBtnActionPerformed

    private void hidingHostTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hidingHostTextFieldMouseClicked
        selectHidingHostAction();
}//GEN-LAST:event_hidingHostTextFieldMouseClicked

    private void hidingSecretBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hidingSecretBrowseBtnActionPerformed
        selectHidingSecretAction();
}//GEN-LAST:event_hidingSecretBrowseBtnActionPerformed

    private void hidingDestBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hidingDestBrowseBtnActionPerformed
        selectHidingDestAction();
}//GEN-LAST:event_hidingDestBrowseBtnActionPerformed

    private void hidingHostBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hidingHostBrowseBtnActionPerformed
        selectHidingHostAction();
}//GEN-LAST:event_hidingHostBrowseBtnActionPerformed

    private void hidingExecuteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hidingExecuteBtnActionPerformed
        selectHidingExecuteAction();
}//GEN-LAST:event_hidingExecuteBtnActionPerformed

    private void hidingDestTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hidingDestTextFieldMouseClicked
        selectHidingDestAction();
}//GEN-LAST:event_hidingDestTextFieldMouseClicked

    private void hidingSecretTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hidingSecretTextFieldMouseClicked
        selectHidingSecretAction();
}//GEN-LAST:event_hidingSecretTextFieldMouseClicked

    private void revealingPasswordCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_revealingPasswordCheckBoxStateChanged
        revealingPasswordField.setText("");
        revealingPasswordField.setVisible(revealingPasswordCheckBox.isSelected());
        revealingPasswordLabel.setVisible(revealingPasswordCheckBox.isSelected());
    }//GEN-LAST:event_revealingPasswordCheckBoxStateChanged

    private void hidingPasswordCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_hidingPasswordCheckBoxStateChanged
        hidingPasswordField.setText("");
        hidingPasswordField.setVisible(hidingPasswordCheckBox.isSelected());
        hidingPasswordLabel.setVisible(hidingPasswordCheckBox.isSelected());
    }//GEN-LAST:event_hidingPasswordCheckBoxStateChanged

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void serbianMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serbianMenuItemActionPerformed
        Locale loc = new Locale("sr");
        if (bundle.getLocale().equals(loc)) {
            return;
        }
        this.setBundle(loc);
    }//GEN-LAST:event_serbianMenuItemActionPerformed

    private void englishMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_englishMenuItemActionPerformed
        Locale loc = new Locale("en");
        if (bundle.getLocale().equals(loc)) {
            return;
        }
        this.setBundle(loc);
    }//GEN-LAST:event_englishMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        AboutDialog ad = new AboutDialog(this, true);
        ad.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton btnOtkrij;
    private javax.swing.JButton btnSakrij;
    private javax.swing.JLayeredPane desniSlojevitiPanel;
    private javax.swing.JRadioButtonMenuItem englishMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel hidingDescLabel;
    private javax.swing.JButton hidingDestBrowseBtn;
    private javax.swing.JLabel hidingDestLabel;
    private javax.swing.JTextField hidingDestTextField;
    private javax.swing.JButton hidingExecuteBtn;
    private javax.swing.JButton hidingHostBrowseBtn;
    private javax.swing.JLabel hidingHostLabel;
    private javax.swing.JTextField hidingHostTextField;
    private javax.swing.JToggleButton hidingPasswordCheckBox;
    private javax.swing.JPasswordField hidingPasswordField;
    private javax.swing.JLabel hidingPasswordLabel;
    private javax.swing.JButton hidingSecretBrowseBtn;
    private javax.swing.JLabel hidingSecretLabel;
    private javax.swing.JTextField hidingSecretTextField;
    private javax.swing.JLabel hidingTitleLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JMenu languageMenu;
    private javax.swing.JLabel mainHideLabel;
    private javax.swing.JLabel mainRevealLabel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel otkrijPanel;
    private javax.swing.JPanel pocetniPanel;
    private javax.swing.JLabel revealingDescLabel;
    private javax.swing.JButton revealingDestBrowseBtn;
    private javax.swing.JLabel revealingDestLabel;
    private javax.swing.JTextField revealingDestTextField;
    private javax.swing.JButton revealingExecuteBtn;
    private javax.swing.JButton revealingHostBrowseBtn;
    private javax.swing.JLabel revealingHostLabel;
    private javax.swing.JTextField revealingHostTextField;
    private javax.swing.JToggleButton revealingPasswordCheckBox;
    private javax.swing.JPasswordField revealingPasswordField;
    private javax.swing.JLabel revealingPasswordLabel;
    private javax.swing.JLabel revealingTitleLabel;
    private javax.swing.JPanel sakrijPanel;
    private javax.swing.JRadioButtonMenuItem serbianMenuItem;
    private javax.swing.JLabel statusMsg;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JProgressBar statusProgress;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables


    public void setLockedControls(boolean isLocked) {
        isLocked = !isLocked;
        btnSakrij.setEnabled(isLocked);
        btnOtkrij.setEnabled(isLocked);
        hidingExecuteBtn.setEnabled(isLocked);
        hidingHostTextField.setEnabled(isLocked);
        hidingSecretTextField.setEnabled(isLocked);
        hidingDestTextField.setEnabled(isLocked);
        revealingExecuteBtn.setEnabled(isLocked);
        revealingHostTextField.setEnabled(isLocked);
        revealingDestTextField.setEnabled(isLocked);
        hidingPasswordCheckBox.setEnabled(isLocked);
        revealingPasswordCheckBox.setEnabled(isLocked);

    }

}
