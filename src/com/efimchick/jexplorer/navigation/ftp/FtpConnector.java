package com.efimchick.jexplorer.navigation.ftp;

import com.efimchick.jexplorer.navigation.FileExtensionFilter;
import com.efimchick.jexplorer.navigation.ftp.task.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Collections.emptyList;

public class FtpConnector {

    private final FtpTask.ConnectionProps props;

    public FtpConnector(String server, int port, String login, byte[] password) {
        props = new FtpTask.ConnectionProps(server, port,login, password);
    }

    List<FtpFile> getFiles(FtpDirectory ftpDirectory, FileExtensionFilter filter){
        return new ListFilesTask(this, ftpDirectory, filter).executeOrDefault(props, emptyList());
    }

    List<FtpDirectory> getSubDirs(FtpDirectory ftpDirectory) throws IOException {
        return new ListDirectoriesTask(this, ftpDirectory).executeOrDefault(props, emptyList());
    }

    InputStream getFtpFileInputStream(FtpFile ftpFile) {
        return new FtpFileInputStreamTask(ftpFile).executeOrDefault(props, null);
    }

    public boolean isConnectable(){
        return new ProbeTask().executeOrDefault(props, false);
    }

    @Override
    public String toString() {
        return "root";
    }
}
