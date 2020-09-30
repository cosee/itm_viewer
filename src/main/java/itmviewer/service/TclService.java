package itmviewer.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import itmviewer.state.ITMSettingsState;
import itmviewer.task.BackgroundClientTask;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Future;

public class TclService {
    final Project project;
    BackgroundClientTask task = null;
    Future threadFuture = null;
    public TclService(Project project) {
        this.project = project;
    }

    public boolean isConnected() {
        return task != null && task.clientIsConnected();
    }

    public boolean openConnection() {
        String host = ITMSettingsState.getTclHost();
        String port = ITMSettingsState.getTclPort();
        if(!isConnected()) {
            task = new BackgroundClientTask(project, host, port);
            threadFuture = ApplicationManager.getApplication().executeOnPooledThread(task);
            return true;
        }
        return false;
    }

    public boolean closeConnection() {
        boolean result = false;
        if(threadFuture != null) {
            try {
                task.closeConnection();
            } catch (IOException e) {
                return false;
            }
            result = threadFuture.cancel(true);
            threadFuture = null;
            task = null;

        }
        return result;
    }

    public static TclService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, TclService.class);
    }
}
