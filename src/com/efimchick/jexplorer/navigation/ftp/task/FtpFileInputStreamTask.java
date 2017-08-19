package com.efimchick.jexplorer.navigation.ftp.task;

import com.efimchick.jexplorer.navigation.ftp.FtpFile;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FtpFileInputStreamTask extends FtpTask<InputStream> {
    private final FtpFile ftpFile;

    public FtpFileInputStreamTask(FtpFile ftpFile) {
        this.ftpFile = ftpFile;
    }

    @Override
    ByteArrayInputStream execute(FTPClient ftpClient) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final boolean succeded;
            succeded = ftpClient.retrieveFile(ftpFile.getFullName(), byteArrayOutputStream);
            if (succeded) {
                return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
