package itmviewer.ui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class ITMViewerToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ITMViewerToolWindow itmViewerToolWindow = project.getService(ITMViewerToolWindow.class);
        itmViewerToolWindow.initToolWindow(project, toolWindow);
    }
}
