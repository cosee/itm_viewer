package itmviewer.state;

import com.intellij.ide.util.PropertiesComponent;
import java.util.Map;


public final class ITMSettingsState {
    private static final String itmTclHostKey = "itmviewer-settings-tclhost";
    private static final String itmTclPortKey = "itmviewer-settings-tclport";
    private static final String itmLogPortKey = "itmviewer-settings-itmlogport-";
    private static final String itmLogPortEnabledKey = "itmviewer-settings-itmlogport-enabled-";

    public enum LOGGING_LEVEL {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public final static Map<LOGGING_LEVEL, Integer> itmDefaultLogPorts = Map.ofEntries(Map.entry(LOGGING_LEVEL.DEBUG, 24),
            Map.entry(LOGGING_LEVEL.INFO, 25),
            Map.entry(LOGGING_LEVEL.WARN, 26),
            Map.entry(LOGGING_LEVEL.ERROR, 27));

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
        String portStr = PropertiesComponent.getInstance().getValue(itmLogPortKey + level.name());
        if (portStr != null) {
            return Integer.parseInt(portStr);
        }
        return itmDefaultLogPorts.get(level);
    }

    public static void setLogLevelEnabled(LOGGING_LEVEL level, boolean enabled) {
        PropertiesComponent.getInstance().setValue(itmLogPortEnabledKey + level.name(), enabled, true);
    }

    public static boolean getLogLevelEnabled(LOGGING_LEVEL level) {
        String enabledStr = PropertiesComponent.getInstance().getValue(itmLogPortEnabledKey + level.name());
        if (enabledStr != null) {
            return Boolean.parseBoolean(enabledStr);
        }
        return true;
    }

}
