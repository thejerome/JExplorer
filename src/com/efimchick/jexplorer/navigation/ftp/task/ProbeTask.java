package com.efimchick.jexplorer.navigation.ftp.task;

import com.efimchick.jexplorer.navigation.ftp.FtpFile;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ProbeTask extends FtpTask<Boolean> {
    @Override
    Boolean execute(FTPClient ftpClient) throws IOException {
        return ftpClient.isConnected();
    }
}
