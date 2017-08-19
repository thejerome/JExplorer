package com.efimchick.jexplorer;

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
import static com.efimchick.jexplorer.navigation.zip.ZipHierarchyScanner.scanZipFile;

public class JExplorerStarter {

    private static JComponent mainLayout;

    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {

            JFrame frame = new JFrame(bundle.getString("jexplorer"));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(1,1));

            mainLayout = new Navigation(new FileSystemDirectory()).createMainLayout();
            frame.add(mainLayout);

            frame.setSize(1200, 900);

            final JMenuBar menubar = new JMenuBar();
            final JMenu fileMenu = new JMenu(bundle.getString("file"));
            menubar.add(fileMenu);

            addMenuItem(frame, fileMenu, bundle.getString("local"), new JExplorerMenuItemListener(frame, () -> new Navigation(new FileSystemDirectory()).createMainLayout()));
            addMenuItem(frame, fileMenu, bundle.getString("zip"), new JExplorerMenuItemListener(frame, new ChoosingZipLayoutSupplier(frame)));
            addMenuItem(frame, fileMenu, bundle.getString("ftp"), new JExplorerMenuItemListener( frame, new FtpLoggingLayoutSupplier(frame)));
            addMenuItem(frame, fileMenu, bundle.getString("exit"), e -> System.exit(0));
            frame.setJMenuBar(menubar);


            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void addMenuItem(JFrame frame, JMenu menu, String name, ActionListener actionListener) {
        final JMenuItem item = menu.add(new JMenuItem(name));
        item.addActionListener(actionListener);
    }

    private static class JExplorerMenuItemListener implements ActionListener {
        private final JFrame frame;
        private final Supplier<JComponent> layoutSupplier;

        public JExplorerMenuItemListener(JFrame frame, Supplier<JComponent> layoutSupplier) {
            this.frame = frame;
            this.layoutSupplier = layoutSupplier;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final JComponent newLayout = layoutSupplier.get();
            if (newLayout != null){
                frame.remove(mainLayout);
                mainLayout = newLayout;
                frame.add(mainLayout);
                frame.revalidate();
            }
        }
    }

    private static class ChoosingZipLayoutSupplier implements Supplier<JComponent> {
        private final JFrame frame;

        public ChoosingZipLayoutSupplier(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public JComponent get() {
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
                    return new Navigation(scanZipFile(selectedFile.getPath())).createMainLayout();
                } catch (IOException e) {
                    e.printStackTrace();
                    showErrorMsg( frame,bundle.getString("errorOpenZip"));
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

    private static class FtpLoggingLayoutSupplier implements Supplier<JComponent> {

        private JFrame frame;

        private FtpLoggingLayoutSupplier(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public JComponent get() {
            FtpLoginDialog ftpLoginDialog = new FtpLoginDialog(frame);
            ftpLoginDialog.setVisible(true);
            JComponent newLayout = ftpLoginDialog.getCreatedLayout();
            if (ftpLoginDialog.isSucceeded() && newLayout != null){
                return newLayout;
            } else {
                return null;
            }
        }
    }

}
