package com.efimchick.jexplorer.navigation.ftp;

import com.efimchick.jexplorer.navigation.File;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.net.ftp.FTPFile;

import java.io.InputStream;
import java.util.Map;

import static com.efimchick.jexplorer.I18n.bundle;
import static com.efimchick.jexplorer.navigation.File.TypeMapper.getExtension;
import static com.efimchick.jexplorer.navigation.File.TypeMapper.mapExtension;

public class FtpFile implements File {
    private final FTPFile ftpFile;
    private final FtpConnector ftpConnector;
    private final FtpDirectory parent;

    public FtpFile(FtpConnector ftpConnector, FTPFile ftpFile, FtpDirectory parent) {
        this.ftpFile = ftpFile;
        this.ftpConnector = ftpConnector;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return ftpFile.getName();
    }

    @Override
    public Map<String, String> getProperties() {
        return ImmutableMap.<String, String>builder()
                .put(bundle.getString("name"), ftpFile.getName())
                .put(bundle.getString("size"), String.valueOf(ftpFile.getSize()))
                .put(bundle.getString("timestamp"), String.valueOf(ftpFile.getTimestamp().getTime()))
                .build();
    }

    @Override
    public String getFullName() {
        return parent == null ? getName() : parent.getFullName() + pathDivisor + getName();
    }

    @Override
    public InputStream getPreviewInputStream() {
        return ftpConnector.getFtpFileInputStream(this);
    }

    @Override
    public Type getType() {
        return mapExtension(getExtension(getName()));
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isPreviewable() {
        return (getType() == Type.TEXT || getType() == Type.IMAGE) && ftpFile.getSize() < 10 * 1024 * 1024;
    }
}
