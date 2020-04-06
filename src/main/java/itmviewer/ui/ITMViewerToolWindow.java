package itmviewer.ui;

import com.google.common.collect.Maps;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ITMViewerToolWindow implements Disposable {
    private ContentManager contentManager;
    private static final String[] CONAN_ACTIONS = {"ReloadProject"};

    @Override
    public void dispose() {

    }

    public void initToolWindow(Project project, ToolWindow toolWindow) {
        contentManager = toolWindow.getContentManager();
        recreateContent(project);
    }

    /**
     * Add Conan actions to the tool bar.
     * @param actionGroup the action container.
     */
    private static void addActions(DefaultActionGroup actionGroup) {
        ActionManager actionManager = ActionManager.getInstance();
        for (String action : CONAN_ACTIONS) {
            actionGroup.addAction(actionManager.getAction("Conan." + action));
        }
    }

    /**
     * Create the action tool bar with all Conan actions.
     * @return the action tool bar with all Conan actions.
     */
    private static ActionToolbar createActionToolbar() {
        DefaultActionGroup defaultActionGroup = new DefaultActionGroup("", true);
        addActions(defaultActionGroup);
        return ActionManager.getInstance().createActionToolbar(ActionPlaces.CHANGES_VIEW_TOOLBAR, defaultActionGroup, false);
    }
    /**
     * Create a component with toolbar and content.
     * @param content the content of the tab.
     * @return the component.
     */
    private JComponent createToolWindowComponents(JComponent content) {
        SimpleToolWindowPanel filterPanel = new SimpleToolWindowPanel(false);
        filterPanel.setToolbar(createActionToolbar().getComponent());
        filterPanel.setContent(content);
        return filterPanel;
    }
    /**
     * Create a tab with conan icon.
     * @param contentFactory the content factory used to create the tabs.
     * @param component the content of the tab.
     * @param title the title of the tab.
     * @return the tab.
     */
    private Content createTab(ContentFactory contentFactory, JComponent component, String title) {
        Content content = contentFactory.createContent(createToolWindowComponents(component), title, false);
        content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
        return content;
    }
    /**
     * Create tabs of Conan profiles.
     * @param project Intellij project.
     * @param contentFactory the content factory used to create the tabs.
     */
    private void createConanProfilesTabs(@NotNull Project project, ContentFactory contentFactory) {
        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        consoleView.print("asd\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
        consoleView.print("asd\n", ConsoleViewContentType.LOG_ERROR_OUTPUT);
        consoleView.print("asd\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        consoleView.print("asd\n", ConsoleViewContentType.LOG_WARNING_OUTPUT);
        Content content = createTab(contentFactory, consoleView.getComponent(), "asd");
        contentManager.addContent(content);
    }
    /**
     * Remove old tabs if exists and create all necessary tabs.
     * @param project Intellij project.
     */
    public void recreateContent(@NotNull Project project) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        contentManager.removeAllContents(true);
        Content content = createTab(contentFactory, createUnsupportedView("Could not find Conan client in path."), "");
        createConanProfilesTabs(project, contentFactory);
        contentManager.addContent(content);
        return;
    }

    /**
     * Create a panel with "unsupported view" message.
     * @param message the message to show.
     * @return a panel with "unsupported view" message.
     */

    private JPanel createUnsupportedView(String message) {
        JLabel label = new JBLabel();
        label.setText(message);
        JBPanel panel = new JBPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(label, c);
        panel.setBackground(UIUtil.getTableBackground());
        return panel;
    }

}
