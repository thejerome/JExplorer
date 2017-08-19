package com.efimchick.jexplorer.navigation.ftp.task;

import com.efimchick.jexplorer.navigation.ftp.FtpDirectory;
import com.efimchick.jexplorer.navigation.ftp.FtpFile;
import com.efimchick.jexplorer.navigation.ftp.FtpConnector;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListFilesTask extends FtpTask<List<FtpFile>> {
    private final FtpConnector ftpConnector;
    private final FtpDirectory parent;

    public ListFilesTask(FtpConnector ftpConnector, FtpDirectory parent) {
        this.ftpConnector = ftpConnector;
        this.parent = parent;
    }

    public ListFilesTask(FtpConnector ftpConnector) {
        this(ftpConnector, null);
    }

    @Override
    List<FtpFile> execute(FTPClient ftpClient) throws IOException {
        final FTPFile[] dirs = parent == null ? ftpClient.listFiles() : ftpClient.listFiles(parent.getFullName());
        return Arrays.stream(dirs)
                .filter(f -> f.isFile())
                .filter(f -> !f.getName().equals("."))
                .filter(f -> !f.getName().equals(".."))
                .map(f -> new FtpFile(ftpConnector, f, parent))
                .collect(Collectors.toList());
    }
}
