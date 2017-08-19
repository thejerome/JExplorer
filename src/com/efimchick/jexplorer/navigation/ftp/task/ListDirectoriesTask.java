package com.efimchick.jexplorer.navigation.ftp.task;

import com.efimchick.jexplorer.navigation.ftp.FtpConnector;
import com.efimchick.jexplorer.navigation.ftp.FtpDirectory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListDirectoriesTask extends FtpTask<List<FtpDirectory>> {
    private final FtpConnector ftpConnector;
    private final FtpDirectory parent;

    public ListDirectoriesTask(FtpConnector ftpConnector, FtpDirectory parent) {
        this.ftpConnector = ftpConnector;
        this.parent = parent;
    }

    public ListDirectoriesTask(FtpConnector ftpConnector) {
        this(ftpConnector, null);
    }

    @Override
    List<FtpDirectory> execute(FTPClient ftpClient) throws IOException {
        final FTPFile[] dirs = parent == null ? ftpClient.listDirectories() : ftpClient.listDirectories(parent.getFullName());
        return Arrays.stream(dirs)
                .filter(f -> f.isDirectory())
                .filter(f -> !f.getName().equals("."))
                .filter(f -> !f.getName().equals(".."))
                .map(d -> new FtpDirectory(ftpConnector, d, parent))
                .collect(Collectors.toList());
    }
}
