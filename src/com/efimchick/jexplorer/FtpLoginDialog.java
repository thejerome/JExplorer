package com.efimchick.jexplorer;

import com.efimchick.jexplorer.navigation.ftp.FtpConnector;
import com.efimchick.jexplorer.navigation.ftp.FtpRoot;
import com.efimchick.jexplorer.navigation.ui.Navigation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.efimchick.jexplorer.I18n.bundle;

public class FtpLoginDialog extends JDialog {

    private JComponent createdLayout = null;
    private boolean succeeded;

    private JTextField serverField;
    private JSpinner portSpinner;
    private JTextField loginField;
    private JPasswordField passwordField;

    private JLabel serverLabel;
    private JLabel portLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JButton cancelButton;

    public FtpLoginDialog(Frame parent) {
        super(parent, bundle.getString("login"), true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        serverLabel = new JLabel(bundle.getString("server") + ": ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(serverLabel, cs);

        serverField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(serverField, cs);

        portLabel = new JLabel(bundle.getString("port") + ": ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(portLabel, cs);

        portSpinner = new JSpinner(new SpinnerNumberModel(
                21, 1, 99999, 1
        ));
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(portSpinner, cs);

        loginLabel = new JLabel(bundle.getString("username") + ": ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(loginLabel, cs);

        loginField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(loginField, cs);

        passwordLabel = new JLabel(bundle.getString("password") + ": ");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        panel.add(passwordLabel, cs);

        passwordField = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 3;
        cs.gridwidth = 2;
        panel.add(passwordField, cs);


        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        loginButton = new JButton(bundle.getString("login"));

        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                final FtpConnector ftpConnector = new FtpConnector(getServer(), getPort(), getLogin(), getPassword().getBytes());
                if (ftpConnector.isConnectable()){
                    createdLayout = new Navigation(new FtpRoot(ftpConnector)).createMainLayout();
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(FtpLoginDialog.this,
                            bundle.getString("connectFailed"),
                            bundle.getString("error"),
                            JOptionPane.ERROR_MESSAGE);

                    serverField.setText("");
                    portSpinner.setValue(21);
                    loginField.setText("");
                    passwordField.setText("");
                    succeeded = false;
                }
            }
        });

        cancelButton = new JButton(bundle.getString("cancel"));
        cancelButton.addActionListener(e -> dispose());
        JPanel bp = new JPanel();
        bp.add(loginButton);
        bp.add(cancelButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getServer() {
        return serverField.getText().trim();
    }

    public int getPort() {
        return Integer.parseInt(String.valueOf(portSpinner.getValue()));
    }

    public String getLogin() {
        return loginField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public JComponent getCreatedLayout(){
        return createdLayout;
    }
}
