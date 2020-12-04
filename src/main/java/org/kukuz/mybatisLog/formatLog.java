package org.kukuz.mybatisLog;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.kukuz.mybatisLog.utils.Constant;
import org.kukuz.mybatisLog.utils.JoinSqlUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Optional;

public class formatLog extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String message = null;
        if (editor != null && editor.getSelectionModel().hasSelection()) {
            String selectedText = Optional.ofNullable(editor.getSelectionModel().getSelectedText()).orElse("");
            message = JoinSqlUtil.join(selectedText);
        }
        //显示对话框
        Icon informationIcon = Messages.getInformationIcon();
        if (message == null || "".equals(message)) {
            message = "sql转换失败";
            informationIcon = Messages.getErrorIcon();
        }
        Messages.showMessageDialog(project, message, Constant.PROJECT_NAME, informationIcon);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message), null);
    }
}
