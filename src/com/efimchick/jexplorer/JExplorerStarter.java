package com.efimchick.jexplorer;

import com.efimchick.jexplorer.navigation.FileExtensionFilter;
import com.efimchick.jexplorer.navigation.files.FileSystemDirectory;
import com.efimchick.jexplorer.navigation.ui.Navigation;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import static com.efimchick.jexplorer.I18n.bundle;
import static com.efimchick.jexplorer.navigation.FileExtensionFilter.empty;
import static com.efimchick.jexplorer.navigation.zip.ZipHierarchyScanner.scanZipFile;

public class JExplorerStarter {

    private static Navigation navigation;
    private static JComponent mainLayout;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(bundle.getString("jexplorer"));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(1, 1));

            navigation = new Navigation(new FileSystemDirectory());
            mainLayout = navigation.createMainLayout();
            frame.add(mainLayout);

            frame.setSize(1200, 900);

            final JMenuBar menubar = new JMenuBar();
            frame.setJMenuBar(menubar);

            final JMenu fileMenu = new JMenu(bundle.getString("main"));
            menubar.add(fileMenu);

            final JMenu fileFilterMenu = new JMenu(bundle.getString("fileFilter"));
            menubar.add(fileFilterMenu);

            final JMenu displayFilterMenu = new JMenu(bundle.getString("noFilter"));
            displayFilterMenu.setEnabled(false);
            menubar.add(displayFilterMenu);

            addMenuItem(fileMenu, bundle.getString("localMenuItem"), new JExplorerMenuItemListener(frame, () -> new Navigation(new FileSystemDirectory())));
            addMenuItem(fileMenu, bundle.getString("zipMenuItem"), new JExplorerMenuItemListener(frame, new ChoosingZipLayoutSupplier(frame)));
            addMenuItem(fileMenu, bundle.getString("ftpMenuItem"), new JExplorerMenuItemListener(frame, new FtpLoggingLayoutSupplier(frame)));
            addMenuItem(fileMenu, bundle.getString("exit"), e -> System.exit(0));


            addMenuItem(fileFilterMenu, bundle.getString("setFEF"), e -> {
                String extension = JOptionPane.showInputDialog(frame, bundle.getString("setExtension"));
                if (extension != null) {
                    extension = extension.replaceAll("[^a-zA-Z]", "").toLowerCase();
                }
                if (extension != null && !extension.isEmpty()) {
                    navigation.setFileExtensionFilter(new FileExtensionFilter(extension));
                    displayFilterMenu.setText("." + extension);
                } else {
                    JOptionPane.showMessageDialog(frame, bundle.getString("badExtension"), bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
                }

            });
            addMenuItem(fileFilterMenu, bundle.getString("removeFEF"), e -> {
                navigation.setFileExtensionFilter(empty);
                displayFilterMenu.setText(bundle.getString("noFilter"));
            });


            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void addMenuItem(JMenu menu, String name, ActionListener actionListener) {
        final JMenuItem item = menu.add(new JMenuItem(name));
        item.addActionListener(actionListener);
    }

    private static class JExplorerMenuItemListener implements ActionListener {
        private final JFrame frame;
        private final Supplier<Navigation> navigationSupplier;

        public JExplorerMenuItemListener(JFrame frame, Supplier<Navigation> navigationSupplier) {
            this.frame = frame;
            this.navigationSupplier = navigationSupplier;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            navigation = navigationSupplier.get();
            JComponent newLayout = navigation.createMainLayout();
            if (newLayout != null) {
                frame.remove(mainLayout);
                mainLayout = newLayout;
                frame.add(mainLayout);
                frame.revalidate();
            }
        }
    }

    private static class ChoosingZipLayoutSupplier implements Supplier<Navigation> {
        private final JFrame frame;

        public ChoosingZipLayoutSupplier(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public Navigation get() {
            JFileChooser zipChooser = new JFileChooser();
            zipChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            zipChooser.setMultiSelectionEnabled(false);
            zipChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".zip");
                }

                @Override
                public String getDescription() {
                    return "ZIP files";
                }
            });
            final int choosingResult = zipChooser.showOpenDialog(frame);
            if (choosingResult == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = zipChooser.getSelectedFile();
                try {
                    return new Navigation(scanZipFile(selectedFile.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                    showErrorMsg(frame, bundle.getString("errorOpenZip"));
                }
            }
            return null;
        }


    }

    static void showErrorMsg(JFrame frame, String errorMsg) {
        JOptionPane.showMessageDialog(frame,
                errorMsg,
                bundle.getString("error"),
                JOptionPane.ERROR_MESSAGE);
    }

    private static class FtpLoggingLayoutSupplier implements Supplier<Navigation> {

        private JFrame frame;

        private FtpLoggingLayoutSupplier(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public Navigation get() {
            FtpLoginDialog ftpLoginDialog = new FtpLoginDialog(frame);
            ftpLoginDialog.setVisible(true);
            Navigation newNavigation = ftpLoginDialog.getCreatedNavigation();
            if (ftpLoginDialog.isSucceeded() && newNavigation != null) {
                return newNavigation;
            } else {
                return null;
            }
        }
    }

}
