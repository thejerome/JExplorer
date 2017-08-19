package com.efimchick.jexplorer.navigation.ftp.task;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class ProbeTask extends FtpTask<Boolean> {
    @Override
    Boolean execute(FTPClient ftpClient) throws IOException {
        return ftpClient.isConnected();
    }
}
