package sandipchitale.gradle.taskinfo;

import com.intellij.ide.actions.runAnything.RunAnythingManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.settings.GradleSettings;

import java.util.Objects;

public class TaskinfoAction extends AnAction {

    public static final String GRADE_TASKINFO_DOT_GRADLE =
            Objects.requireNonNull(PluginManagerCore.getPlugin(PluginId.getId("sandipchitale.gradle.taskinfo")))
                    .getPluginPath()
                    .resolve("gradle-init")
                    .resolve("gradle-taskinfo.gradle")
                    .toString();

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        if (actionEvent.getProject() == null) {
            return;
        }
        runTiTask(this, actionEvent);
    }

    @Override
    public void update(@NotNull AnActionEvent actionEvent) {
        Project project = actionEvent.getProject();
        if (project == null) {
            actionEvent.getPresentation().setEnabledAndVisible(false);
            return;
        }
        if (GradleSettings.getInstance(project).getLinkedProjectsSettings().isEmpty()) {
            actionEvent.getPresentation().setEnabledAndVisible(false);
        }
        actionEvent.getPresentation().setEnabledAndVisible(true);
    }

    static void runTiTask(AnAction action, AnActionEvent actionEvent) {
        // This is a hack to get the taskinfo task name from the action ID.
        String tiTask = actionEvent.getActionManager().getId(action).replace("sandipchitale.gradle.taskinfo.", "");
        RunAnythingManager.getInstance(Objects.requireNonNull(actionEvent.getProject()))
                .show(String.format("gradle --console=plain -I %s :%s ", GRADE_TASKINFO_DOT_GRADLE, tiTask), false, actionEvent);
    }
}
