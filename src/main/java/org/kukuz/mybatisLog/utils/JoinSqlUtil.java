package org.kukuz.mybatisLog.utils;

import org.kukuz.mybatisLog.entity.MybatisLogEntity;
import org.kukuz.mybatisLog.entity.SelectedTextLineEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 拼接sql
 *
 * @author luhangqi
 * @date 2020/12/4
 */
public class JoinSqlUtil {

    private static final String REGEX = "\\?";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static String join(String selectedText) {
        String[] textList = selectedText.split("\\r?\\n");
        List<MybatisLogEntity> logList = new ArrayList<>();
        MybatisLogEntity logEntity = new MybatisLogEntity();
        for (String text : textList) {
            if (text.contains(Constant.SQL_PREPARING)) {
                logEntity.setSql(text.split(Constant.SQL_PREPARING)[1].trim());
            } else if (text.contains(Constant.SQL_PARAMETERS)) {
                String[] params = text.split(Constant.SQL_PARAMETERS);
                if (params.length > 1) {
                    logEntity.setParams(Arrays.asList(params[1].split(",")));
                    if (logEntity.getSql() != null) {
                        logList.add(logEntity.copy());
                    }
                    logEntity = new MybatisLogEntity();
                }
            }
        }
        if (!logList.isEmpty()) {
            for (MybatisLogEntity mybatisLogEntity : logList) {
                Matcher matcher = PATTERN.matcher(mybatisLogEntity.getSql());
                int index = 0;
                while (matcher.find()) {
                    String sql;
                    List<String> params = mybatisLogEntity.getParams();
                    if (params != null) {
                        if (params.size() > index) {
                            String param = params.get(index).trim();
                            String type = param.substring(param.indexOf("(") + 1, param.indexOf(")"));
                            if ("Long".equals(type) || "Integer".equals(type)) {
                                sql = mybatisLogEntity.getSql().replaceFirst(REGEX, param.substring(0, param.indexOf("(")));
                            } else {
                                sql = mybatisLogEntity.getSql().replaceFirst(REGEX, "'" + param.substring(0, param.indexOf("(")) + "'");
                            }
                            index++;
                        } else {
                            break;
                        }
                        mybatisLogEntity.setSql(sql);
                    }
                }
            }
        }
        return logList.stream().map(MybatisLogEntity::getSql).collect(Collectors.joining(";"));
    }
}
