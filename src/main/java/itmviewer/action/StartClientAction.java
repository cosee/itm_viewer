package itmviewer.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import itmviewer.service.TclService;
import org.jetbrains.annotations.NotNull;

public class StartClientAction extends AnAction implements DumbAware {
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);

        Project project = e.getProject();
        if (project != null && project.getService(TclService.class).isConnected()) {
            e.getPresentation().setEnabled(false);
        } else {
            e.getPresentation().setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = getEventProject(e);
        if (project == null) {
            return;
        }
        TclService.getInstance(project).openConnection();
    }
}