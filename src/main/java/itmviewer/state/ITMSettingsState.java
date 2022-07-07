package itmviewer.state;

import com.intellij.ide.util.PropertiesComponent;

import java.util.Map;


public final class ITMSettingsState {
    private static final String ITM_TCL_HOST_KEY = "itmviewer-settings-tclhost";
    private static final String ITM_TCL_PORT_KEY = "itmviewer-settings-tclport";
    private static final String ITM_LOG_PORT_KEY = "itmviewer-settings-itmlogport-";
    private static final String ITM_LOG_PORT_ENABLED_KEY = "itmviewer-settings-itmlogport-enabled-";

    public enum LoggingLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public static final Map<LoggingLevel, Integer> ITM_DEFAULT_LOG_PORTS = Map.of(
            LoggingLevel.DEBUG, 24,
            LoggingLevel.INFO, 25,
            LoggingLevel.WARN, 26,
            LoggingLevel.ERROR, 27);

    public static String getTclHost() {
        return PropertiesComponent.getInstance().getValue(ITM_TCL_HOST_KEY);
    }

    public static void setTclHost(String val) {
        PropertiesComponent.getInstance().setValue(ITM_TCL_HOST_KEY, val);
    }

    public static void setTclPort(String val) {
        PropertiesComponent.getInstance().setValue(ITM_TCL_PORT_KEY, val);
    }

    public static String getTclPort() {
        return PropertiesComponent.getInstance().getValue(ITM_TCL_PORT_KEY);
    }

    public static void setChannelPort(LoggingLevel level, int port) {
        PropertiesComponent.getInstance().setValue(ITM_LOG_PORT_KEY + level.name(), port, ITM_DEFAULT_LOG_PORTS.get(level));
    }

    public static int getLogLevelPort(LoggingLevel level) {
        String portStr = PropertiesComponent.getInstance().getValue(ITM_LOG_PORT_KEY + level.name());
        if (portStr != null) {
            return Integer.parseInt(portStr);
        }
        return ITM_DEFAULT_LOG_PORTS.get(level);
    }

    public static void setLogLevelEnabled(LoggingLevel level, boolean enabled) {
        PropertiesComponent.getInstance().setValue(ITM_LOG_PORT_ENABLED_KEY + level.name(), enabled, true);
    }

    public static boolean getLogLevelEnabled(LoggingLevel level) {
        String enabledStr = PropertiesComponent.getInstance().getValue(ITM_LOG_PORT_ENABLED_KEY + level.name());
        return enabledStr == null || Boolean.parseBoolean(enabledStr);
    }

}
