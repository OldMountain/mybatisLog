package org.example.mybatisLog;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class formatLog extends AnAction {

    private static String regex = "\\?";

    private static Pattern pattern = Pattern.compile(regex);

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getData(PlatformDataKeys.PROJECT);
        String title = "Hello World!";
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        String message = "sql转换失败";
        if (editor != null && editor.getSelectionModel().hasSelection()) {
            String selectedText = Optional.ofNullable(editor.getSelectionModel().getSelectedText()).orElse("");
            String[] textList = selectedText.split("\\r?\\n");
            String sql = "";
            String[] params = null;
            for (String text : textList) {
                if (text.contains("Preparing:")) {
                    sql = text.split("Preparing:")[1].trim();
                } else if (text.contains("Parameters:")) {
                    params = text.split("Parameters:")[1].split(",");
                    break;
                }
            }
            if (!"".equals(sql)) {
                Matcher matcher = pattern.matcher(sql);
                int index = 0;
                while (matcher.find()) {
                    if (params != null) {
                        if (params.length > index) {
                            String param = params[index].trim();
                            String type = param.substring(param.indexOf("(") + 1, param.indexOf(")"));
                            if ("Long".equals(type) || "Integer".equals(type)) {
                                sql = sql.replaceFirst(regex, param.substring(0, param.indexOf("(")));
                            } else {
                                sql = sql.replaceFirst(regex, "\"" + param.substring(0, param.indexOf("(")) + "\"");
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
        Messages.showMessageDialog(project, message, title, Messages.getInformationIcon());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message), null);
    }
}
