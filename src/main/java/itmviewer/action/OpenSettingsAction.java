package itmviewer.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import itmviewer.state.ITMSettingsState;
import itmviewer.ui.ITMViewerSettings;
import itmviewer.ui.ITMViewerToolWindow;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsAction extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = getEventProject(e);
        if (project == null) {
            return;
        }
        ShowSettingsUtil.getInstance().showSettingsDialog(project, ITMViewerSettings.ITM_VIEWER_SETTINGS_NAME);
        System.out.println("blabla " + ITMSettingsState.getTclHost());
    }
}
