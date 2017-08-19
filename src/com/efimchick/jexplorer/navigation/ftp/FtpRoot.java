package com.efimchick.jexplorer.navigation.ftp;

import java.util.Collections;
import java.util.Map;

public class FtpRoot extends FtpDirectory{

    public FtpRoot(FtpConnector ftpConnector) {
        super(ftpConnector, null, null);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.emptyMap();
    }
}
