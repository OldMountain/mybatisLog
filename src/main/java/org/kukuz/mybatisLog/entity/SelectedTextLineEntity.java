package org.kukuz.mybatisLog.entity;

/**
 * 选择文本行数据
 *
 * @author luhangqi
 * @date 2020/12/4
 */
public class SelectedTextLineEntity {

    public SelectedTextLineEntity() {
    }

    public SelectedTextLineEntity(String groupName, String text) {
        this.groupName = groupName;
        this.text = text;
    }

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 文本
     */
    private String text;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
