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
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.ui.UIUtil;
import itmviewer.service.TclService;
import itmviewer.state.ITMSettingsState;
import org.jetbrains.annotations.NotNull;
import tcl.parser.TclEntity;

import javax.swing.*;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ITMViewerToolWindow implements Disposable {
    private ContentManager contentManager;
    ConsoleView consoleView = null;
    TclService service = null;
    int lastLogPort = 0;
    private static final String[] ACTIONS = {"ReloadProject", "OpenSettings", "StartClient", "StopClient"};

    @Override
    public void dispose() {

    }

    public void initToolWindow(Project project, ToolWindow toolWindow) {
        contentManager = toolWindow.getContentManager();
        service = ServiceManager.getService(TclService.class);
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

    public void addMessage(String message) {
        consoleView.print(message, ConsoleViewContentType.LOG_ERROR_OUTPUT);
    }

    private Content createConsoleView(@NotNull Project project, ContentFactory contentFactory) {
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();

        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("uuuu.MM.dd.HH.mm.ss"));
        consoleView.print(timestamp + ": ITM Viewer - Connect to TCL RPC Server to get started\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        consoleView.print("Configuration:\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        consoleView.print("\tERROR ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.ERROR) + "\n", ConsoleViewContentType.LOG_ERROR_OUTPUT);
        consoleView.print("\tWARN ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.WARN) + "\n", ConsoleViewContentType.LOG_WARNING_OUTPUT);
        consoleView.print("\tINFO ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.INFO) + "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
        consoleView.print("\tDEBUG ITM Port: " + ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.DEBUG) + "\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        return createTab(contentFactory, consoleView.getComponent(), "ITM Viewer");
    }

    public void recreateContent(@NotNull Project project) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        contentManager.removeAllContents(true);
        Content content;
        content = createConsoleView(project, contentFactory);
        contentManager.addContent(content);
        return;
    }

    private ConsoleViewContentType getLogLevelByITMPort(int itmPort) {
        int errorPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.ERROR);
        int warnPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.WARN);
        int infoPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.INFO);
        int debugPort = ITMSettingsState.getLogLevelPort(ITMSettingsState.LOGGING_LEVEL.DEBUG);
        if (itmPort == errorPort) {
            return ConsoleViewContentType.LOG_ERROR_OUTPUT;
        } else if (itmPort == warnPort) {
            return ConsoleViewContentType.LOG_WARNING_OUTPUT;
        } else if (itmPort == infoPort) {
            return ConsoleViewContentType.LOG_INFO_OUTPUT;
        } else if (itmPort == debugPort) {
            return ConsoleViewContentType.LOG_DEBUG_OUTPUT;
        } else {
            return null;
        }
    }

    public void addITMPackages(List<TclEntity> entities) {
        for (TclEntity entity : entities) {
            ConsoleViewContentType contentType = getLogLevelByITMPort(entity.getChannel());

            if (contentType != null) {
                if (lastLogPort != entity.getChannel()) {
                    String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("uuuu.MM.dd.HH.mm.ss"));
                    consoleView.print("\n"+timestamp, contentType);
                }
                consoleView.print(entity.getContent(), contentType);
                lastLogPort = entity.getChannel();
            }
        }
    }
}
