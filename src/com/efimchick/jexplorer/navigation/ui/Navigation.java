package com.efimchick.jexplorer.navigation.ui;

import com.efimchick.jexplorer.navigation.Directory;
import com.efimchick.jexplorer.navigation.File;
import com.efimchick.jexplorer.navigation.FileExtensionFilter;
import com.efimchick.jexplorer.navigation.ui.content.FilesList;
import com.efimchick.jexplorer.navigation.ui.properties.FilePropertiesPane;
import com.efimchick.jexplorer.navigation.ui.structure.DirectoryTreeNode;
import com.efimchick.jexplorer.navigation.ui.structure.StructureTreeCellRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.efimchick.jexplorer.I18n.bundle;
import static java.awt.BorderLayout.*;

public class Navigation {

    private final Directory root;

    private JPanel structurePanel = new JPanelScrollable(new BorderLayout());
    private JPanel contentPanel = new JPanelScrollable(new BorderLayout());
    private JPanel propertiesPanel = new JPanelScrollable(new BorderLayout());
    private JPanel previewPanel = new JPanelScrollable(new BorderLayout());

    private SwingWorker<String, String> creatingFileListSwingWorker;
    private SwingWorker<String, String> creatingPropertiesPaneSwingWorker;
    private SwingWorker<String, String> creatingPreviewPaneSwingWorker;
    private FileExtensionFilter fileExtensionFilter = FileExtensionFilter.empty;
    private Directory currentDirectory;

    {
        structurePanel.add(new JLabel(bundle.getString("structure")), BorderLayout.NORTH);
        contentPanel.add(new JLabel(bundle.getString("content")), BorderLayout.NORTH);
        propertiesPanel.add(new JLabel(bundle.getString("properties")), BorderLayout.NORTH);
        previewPanel.add(new JLabel(bundle.getString("preview")), BorderLayout.NORTH);
    }

    public static final DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);


    public Navigation(Directory root) {
        Objects.requireNonNull(root);
        currentDirectory = root;
        this.root = root;
    }

    public JPanel createMainLayout() {

        JPanel mainPanel = new JPanel(new GridLayout(1, 1));

        structurePanel.add(createStructurePane(), CENTER);
        contentPanel.add(createFileListPane(root), CENTER);
        propertiesPanel.add(createEmptyPropertiesPanel(), CENTER);
        previewPanel.add(createNoPreviewPane(), CENTER);


        JPanel centerPane = new JPanel(new BorderLayout());
        centerPane.add(contentPanel, CENTER);
        centerPane.add(propertiesPanel, SOUTH);

        JSplitPane centerAndPreviewPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPane, previewPanel);
        centerAndPreviewPane.setOneTouchExpandable(true);
        centerAndPreviewPane.setContinuousLayout(true);
        centerAndPreviewPane.setDividerLocation(0.5);
        centerAndPreviewPane.setResizeWeight(0.5);
        centerAndPreviewPane.setDividerSize(8);

        JSplitPane allPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, structurePanel, centerAndPreviewPane);
        allPane.setContinuousLayout(true);
        allPane.setDividerLocation(0.3);
        allPane.setResizeWeight(0.3);
        allPane.setDividerSize(5);

        mainPanel.add(allPane);

        return mainPanel;
    }

    public JScrollPane createStructurePane() {
        JPanel treePanel = new JPanel();
        treePanel.setLayout(new GridLayout(1, 1));
        TreeNode rootNode = new DirectoryTreeNode(root, null);
        JTree directoryTree = new JTree(rootNode);

        directoryTree.setCellRenderer(new StructureTreeCellRenderer());

        directoryTree.addTreeSelectionListener(this::selectCurrentDirectory);

        treePanel.add(directoryTree);
        return new JScrollPane(treePanel);
    }

    public void setFileExtensionFilter(FileExtensionFilter fileExtensionFilter) {
        this.fileExtensionFilter = fileExtensionFilter;
        updateFileList();
    }

    private void selectCurrentDirectory(TreeSelectionEvent e) {
        DirectoryTreeNode selectedDirectoryNode = (DirectoryTreeNode) e.getPath().getLastPathComponent();
        currentDirectory = selectedDirectoryNode.getDirectory();
        updateFileList();
    }

    private void updateFileList() {
        replaceCentralContent(propertiesPanel, createEmptyPropertiesPanel());
        replaceCentralContent(previewPanel, createNoPreviewPane());

        cancelIfExists(creatingFileListSwingWorker);

        creatingFileListSwingWorker = new SwingWorker<String, String>() {

            private JComponent fileListPane;

            @Override
            protected String doInBackground() throws Exception {
                replaceCentralContentWithLoadingPane(contentPanel);
                fileListPane = createFileListPane(currentDirectory);
                return "";
            }

            @Override
            protected void done() {
                replaceCentralContent(contentPanel, fileListPane);
            }
        };
        creatingFileListSwingWorker.execute();
    }

    public JComponent createFileListPane(Directory directory) {
        JPanel containerPanel = new JPanelScrollable();
        containerPanel.setLayout(new GridLayout(1, 1));

        List<? extends File> files = null;
        try {
            files = Stream.concat(
                    directory.getSubDirs().stream(),
                    directory.getFiles(fileExtensionFilter).stream()
            ).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return createNoResultsPane();
        }

        JList<File> list = new FilesList(this, files);

        list.addListSelectionListener(e -> selectFileFromList(list));

        containerPanel.add(list);
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        return scrollPane;
    }

    private void selectFileFromList(JList<File> list) {
        File file = list.getSelectedValue();

        cancelIfExists(creatingPropertiesPaneSwingWorker);
        cancelIfExists(creatingPreviewPaneSwingWorker);

        creatingPropertiesPaneSwingWorker = new SwingWorker<String, String>() {

            private JComponent filePropertiesPane;

            @Override
            protected String doInBackground() throws Exception {
                replaceCentralContentWithLoadingPane(propertiesPanel);
                filePropertiesPane = createFilePropertiesPane(file);
                return "";
            }

            @Override
            protected void done() {
                replaceCentralContent(propertiesPanel, filePropertiesPane);
            }
        };

        creatingPreviewPaneSwingWorker = new SwingWorker<String, String>() {

            private JComponent previewPane;

            @Override
            protected String doInBackground() throws Exception {
                replaceCentralContentWithLoadingPane(previewPanel);
                previewPane = createPreviewPane(file);
                return "";
            }

            @Override
            protected void done() {
                replaceCentralContent(previewPanel, previewPane);
            }
        };

        creatingPreviewPaneSwingWorker.execute();
        creatingPropertiesPaneSwingWorker.execute();

    }

    private static void cancelIfExists(SwingWorker<String, String> swingWorker) {
        if (swingWorker != null)
            swingWorker.cancel(true);
    }

    private static void replaceCentralContent(JPanel target, JComponent content){
        if (content != null) {
            try {
                target.remove(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            target.add(content, CENTER);
            target.revalidate();
        }
    }

    private static void replaceCentralContentWithLoadingPane(JPanel target){
        replaceCentralContent(target, createLoadingPane());
    }

    private JComponent createEmptyPropertiesPanel() {
        return new JLabel();
    }

    private static JComponent createNoResultsPane() {
        return createSimpleTextPane(bundle.getString("noResult"));
    }
    private static JComponent createLoadingPane() {
        return createSimpleTextPane(bundle.getString("loading"));
    }

    private static JComponent createSimpleTextPane(String text) {
        final JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return new JScrollPane(label);
    }


    public JComponent createFilePropertiesPane(File file) {
        try {
            return new JScrollPane(new FilePropertiesPane(file));
        } catch (Exception e) {
            e.printStackTrace();
            return createEmptyPropertiesPanel();
        }
    }

    public JComponent createPreviewPane(File file) {


        JComponent mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1,1));

        JComponent previewPane;
        if (file != null && file.isPreviewable()) {

            switch (file.getType()) {
                case IMAGE: {
                    previewPane = createImagePreviewPane(file);
                    break;
                }

                case TEXT: {
                    previewPane = createTextPreviewPane(file);
                    break;
                }

                default: {
                    previewPane = createNoPreviewPane();
                }
            }

        } else {
            previewPane = createNoPreviewPane();
        }

        mainPanel.add(previewPane, EAST);
        return new JScrollPane(mainPanel);
    }

    private static JScrollPane createNoPreviewPane() {
        JLabel label = new JLabel(bundle.getString("noPreview"));
        label.setVerticalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return new JScrollPane(label);
    }

    private static JComponent createImagePreviewPane(File file) {
        JPanel pane = null;
        InputStream previewInputStream = file.getPreviewInputStream();
        if (previewInputStream != null) {
            try {
                BufferedImage previewImage;
                previewImage = ImageIO.read(previewInputStream);
                pane = new ImagePane(previewImage);
            } catch (IOException e) {
                e.printStackTrace();
                return createNoPreviewPane();
            }
        }

        return new JScrollPane(pane);
    }

    private static JComponent createTextPreviewPane(File file) {

        InputStream textStream = file.getPreviewInputStream();
        JEditorPane pane = null;
        if (textStream != null) {
            try {
                final String text = new BufferedReader(new InputStreamReader(textStream)).lines()
                        .collect(Collectors.joining(System.lineSeparator()));
                pane = new JEditorPane();
                pane.setEditable(false);
                pane.setContentType("plain/text");
                pane.setText(text);
                pane.setBackground(Color.white);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pane == null ? createNoPreviewPane() : new JScrollPane(pane);
    }

}
