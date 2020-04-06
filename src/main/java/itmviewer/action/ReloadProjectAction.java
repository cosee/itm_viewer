package itmviewer.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import itmviewer.ui.ITMViewerToolWindow;

public class ReloadProjectAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = getEventProject(anActionEvent);
        if (project == null) {
            return;
        }
        ITMViewerToolWindow itmViewerToolWindow = ServiceManager.getService(project, ITMViewerToolWindow.class);
        itmViewerToolWindow.recreateContent(project);
    }
}