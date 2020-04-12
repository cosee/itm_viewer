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

import javax.swing.*;
import java.awt.*;

public class ITMViewerToolWindow implements Disposable {
    private ContentManager contentManager;
    ConsoleView consoleView = null;
    TclService service = null;
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
        consoleView.print("ITM Viewer - Connect to TCL RPC Server to get started\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
        consoleView.print("Configuration:\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
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

}
