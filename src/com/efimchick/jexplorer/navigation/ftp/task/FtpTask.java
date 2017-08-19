package com.efimchick.jexplorer.navigation.ftp.task;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public abstract class FtpTask<T> {
    public T executeOrDefault(ConnectionProps props, T defaultValue){
        T result = defaultValue;
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(props.server, props.port);
            ftpClient.login(props.login, new String(props.password));

            result = execute(ftpClient);

        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    abstract T execute(FTPClient ftpClient) throws Exception;

    public static class ConnectionProps {
        private String server;
        private int port;
        private String login;
        private byte[] password;

        public ConnectionProps(String server, int port, String login, byte[] password) {
            this.server = server;
            this.port = port;
            this.login = login;
            this.password = password;
        }
    }
}
