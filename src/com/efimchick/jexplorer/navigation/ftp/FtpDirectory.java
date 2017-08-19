package com.efimchick.jexplorer.navigation.ftp;

import com.efimchick.jexplorer.I18n;
import com.efimchick.jexplorer.navigation.Directory;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.efimchick.jexplorer.I18n.bundle;

public class FtpDirectory implements Directory {
    private final FtpConnector ftpConnector;
    private final FTPFile directory;
    private final FtpDirectory parent;

    public FtpDirectory(FtpConnector ftpConnector, FTPFile directory, FtpDirectory parent) {
        this.ftpConnector = ftpConnector;
        this.directory = directory;
        this.parent = parent;
    }

    @Override
    public List<FtpFile> getFiles() throws IOException {
        return ftpConnector.getFiles(this);
    }

    @Override
    public List<FtpDirectory> getSubDirs() throws IOException {
        return ftpConnector.getSubDirs(this);
    }

    @Override
    public String getName() {
        return directory.getName();
    }

    @Override
    public String getFullName() {
        return parent == null ? getName() : parent.getFullName() + pathDivisor + getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public Map<String, String> getProperties() {
        return ImmutableMap.<String, String>builder()
                .put(bundle.getString("name"), directory.getName())
                .put(bundle.getString("size"), String.valueOf(directory.getSize()))
                .put(bundle.getString("timestamp"), String.valueOf(directory.getTimestamp().getTime()))
                .build();
    }
}
