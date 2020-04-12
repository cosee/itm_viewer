package itmviewer.state;

import com.intellij.ide.util.PropertiesComponent;
import java.util.Map;


public final class ITMSettingsState {
    private static final String itmTclHostKey = "itmviewer-settings-tclhost";
    private static final String itmTclPortKey = "itmviewer-settings-tclport";
    private static final String itmLogPortKey = "itmviewer-settings-itmlogport-";

    public enum LOGGING_LEVEL {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public final static Map<LOGGING_LEVEL, Integer> itmDefaultLogPorts = Map.ofEntries(Map.entry(LOGGING_LEVEL.DEBUG, 0),
            Map.entry(LOGGING_LEVEL.INFO, 1),
            Map.entry(LOGGING_LEVEL.WARN, 2),
            Map.entry(LOGGING_LEVEL.ERROR, 3));

    public static String getTclHost() {
        return PropertiesComponent.getInstance().getValue(itmTclHostKey);
    }

    public static void setTclHost(String val) {
        PropertiesComponent.getInstance().setValue(itmTclHostKey, val);
    }

    public static void setTclPort(String val) {
        PropertiesComponent.getInstance().setValue(itmTclPortKey, val);
    }

    public static String getTclPort() {
        return PropertiesComponent.getInstance().getValue(itmTclPortKey);
    }

    public static void setChannelPort(LOGGING_LEVEL level, int port) {
        PropertiesComponent.getInstance().setValue(itmLogPortKey + level.name(), port, itmDefaultLogPorts.get(level));
    }

    public static int getLogLevelPort(LOGGING_LEVEL level) {
        String portStr = PropertiesComponent.getInstance().getValue(itmLogPortKey+level.name());
        if (portStr != null) {
            return Integer.parseInt(portStr);
        }
        return itmDefaultLogPorts.get(level);
    }
}
