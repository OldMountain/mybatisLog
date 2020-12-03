package org.lhq.mybatisLog;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.lhq.mybatisLog.utils.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class formatLog extends AnAction {

    private static final String REGEX = "\\?";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String message = null;
        if (editor != null && editor.getSelectionModel().hasSelection()) {
            String selectedText = Optional.ofNullable(editor.getSelectionModel().getSelectedText()).orElse("");
            String[] textList = selectedText.split("\\r?\\n");
            String sql = "";
            String[] params = null;
            for (String text : textList) {
                if (text.contains(Constant.SQL_PREPARING)) {
                    sql = text.split(Constant.SQL_PREPARING)[1].trim();
                } else if (text.contains(Constant.SQL_PARAMETERS)) {
                    params = text.split(Constant.SQL_PARAMETERS)[1].split(",");
                    break;
                }
            }
            if (!"".equals(sql)) {
                Matcher matcher = PATTERN.matcher(sql);
                int index = 0;
                while (matcher.find()) {
                    if (params != null) {
                        if (params.length > index) {
                            String param = params[index].trim();
                            String type = param.substring(param.indexOf("(") + 1, param.indexOf(")"));
                            if ("Long".equals(type) || "Integer".equals(type)) {
                                sql = sql.replaceFirst(REGEX, param.substring(0, param.indexOf("(")));
                            } else {
                                sql = sql.replaceFirst(REGEX, "'" + param.substring(0, param.indexOf("(")) + "'");
                            }
                            index++;
                        } else {
                            break;
                        }
                    }
                }
                message = sql;
            }
        }
        //显示对话框
        Icon informationIcon = Messages.getInformationIcon();
        if (message == null) {
            message = "sql转换失败";
            informationIcon = Messages.getErrorIcon();
        }
        Messages.showMessageDialog(project, message, Constant.PROJECT_NAME, informationIcon);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message), null);
    }
}
