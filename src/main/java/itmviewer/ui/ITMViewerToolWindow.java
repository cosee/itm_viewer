package itmviewer.ui;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import itmviewer.service.TclService;
import itmviewer.state.ITMSettingsState;
import org.jetbrains.annotations.NotNull;
import tcl.parser.TclEntity;

import javax.swing.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ITMViewerToolWindow implements Disposable {
    private ContentManager contentManager;
    ConsoleView consoleView = null;
    TclService service = null;
    TclEntity lastEntity = null;
    private static final String[] ACTIONS = {"ClearConsole", "OpenSettings", "StartClient", "StopClient"};

    @Override
    public void dispose() {

    }

    public void notifyOnTraceInitSuccess() {
        printMessageWithHeader("Activated Trace Data Output from RPC Server\n", ConsoleViewContentType.SYSTEM_OUTPUT);

    }

    public void notifyOnTraceInitFail() {
        printMessageWithHeader("FAILED to activate Trace Data Output from RPC Server\n", ConsoleViewContentType.ERROR_OUTPUT);
    }


    public void notifyOnServerClose() {
        printMessageWithHeader("Closed Connection to Server\n", ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    public void notifyOnConnect() {
        printMessageWithHeader("Opened Connection to " + ITMSettingsState.getTclHost() + ":" + ITMSettingsState.getTclPort() + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    public void initToolWindow(Project project, ToolWindow toolWindow) {
        contentManager = toolWindow.getContentManager();
        service = project.getService(TclService.class);
        recreateContent(project);
    }


    private static void addActions(DefaultActionGroup actionGroup) {
        ActionManager actionManager = ActionManager.getInstance();
        for (String action : ACTIONS) {
            actionGroup.addAction(actionManager.getAction("ITMViewer." + action));
        }
    }

    private static ActionToolbar createActionToolbar() {
        DefaultActionGroup defaultActionGroup = new DefaultActionGroup("", true);
        addActions(defaultActionGroup);
        return ActionManager.getInstance().createActionToolbar(ActionPlaces.CHANGES_VIEW_TOOLBAR, defaultActionGroup, false);
    }

    private JComponent createToolWindowComponents(JComponent content) {
        SimpleToolWindowPanel filterPanel = new SimpleToolWindowPanel(false);
        filterPanel.setToolbar(createActionToolbar().getComponent());
        filterPanel.setContent(content);
        return filterPanel;
    }


    private Content createTab(ContentFactory contentFactory, JComponent component, String title) {
        Content content = contentFactory.createContent(createToolWindowComponents(component), title, false);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
        return content;
    }

    public void printHeader(ConsoleViewContentType level) {
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        consoleView.print("[" + timestamp + "]: ", level);
    }

    public void printMessageWithHeader(String message, ConsoleViewContentType level) {
        printHeader(level);
        consoleView.print(message, level);
    }

    public void printMessageOnly(String message, ConsoleViewContentType level) {
        consoleView.print(message, level);
    }

    private Content createConsoleView(@NotNull Project project, ContentFactory contentFactory) {
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        printMessageWithHeader("ITM Viewer - Connect to TCL RPC Server to get started\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        printMessageOnly("Configuration:\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        if (ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.ERROR)) {
            printMessageOnly("\tERROR ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.ERROR) + "\n", ConsoleViewContentType.LOG_ERROR_OUTPUT);
        }
        if (ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.WARN)) {
            printMessageOnly("\tWARN ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.WARN) + "\n", ConsoleViewContentType.LOG_WARNING_OUTPUT);
        }
        if (ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.INFO)) {
            printMessageOnly("\tINFO ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.INFO) + "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
        }
        if (ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.DEBUG)) {
            printMessageOnly("\tDEBUG ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.DEBUG) + "\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        }
        return createTab(contentFactory, consoleView.getComponent(), "Output");
    }

    public void recreateContent(@NotNull Project project) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        contentManager.removeAllContents(true);
        Content content;
        content = createConsoleView(project, contentFactory);
        contentManager.addContent(content);
    }

    private ConsoleViewContentType getEnabledLogLevelByITMPort(int itmPort) {
        int errorPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.ERROR);
        int warnPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.WARN);
        int infoPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.INFO);
        int debugPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LoggingLevel.DEBUG);

        boolean errorLogLevelEnabled = ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.ERROR);
        boolean warnLogLevelEnabled = ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.WARN);
        boolean infoLogLevelEnabled = ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.INFO);
        boolean debugLogLevelEnabled = ITMSettingsState.getLogLevelEnabled(ITMSettingsState.LoggingLevel.DEBUG);

        if (itmPort == errorPort && errorLogLevelEnabled) {
            return ConsoleViewContentType.LOG_ERROR_OUTPUT;
        } else if (itmPort == warnPort && warnLogLevelEnabled) {
            return ConsoleViewContentType.LOG_WARNING_OUTPUT;
        } else if (itmPort == infoPort && infoLogLevelEnabled) {
            return ConsoleViewContentType.LOG_INFO_OUTPUT;
        } else if (itmPort == debugPort && debugLogLevelEnabled) {
            return ConsoleViewContentType.LOG_DEBUG_OUTPUT;
        } else {
            return null;
        }
    }

    public void addITMPackages(@NotNull List<TclEntity> entities) {
        for (TclEntity entity : entities) {
            ConsoleViewContentType contentType = getEnabledLogLevelByITMPort(entity.getChannel());

            if (contentType != null) {
                if (lastEntity == null || (lastEntity.getChannel() != entity.getChannel() || lastEntity.getContent().endsWith("\n"))) {
                    printMessageWithHeader(entity.getContent(), contentType);
                } else {
                    printMessageOnly(entity.getContent(), contentType);
                }
                lastEntity = entity;
            }
        }
    }

}
