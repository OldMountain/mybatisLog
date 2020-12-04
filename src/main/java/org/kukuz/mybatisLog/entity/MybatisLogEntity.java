package org.kukuz.mybatisLog.entity;

import java.util.List;

/**
 * sql查询拼接
 *
 * @author luhangqi
 * @date 2020/12/4
 */
public class MybatisLogEntity {

    /**
     * sql
     */
    private String sql;

    /**
     * 参数
     */
    private List<String> params;

    public MybatisLogEntity copy() {
        MybatisLogEntity result = new MybatisLogEntity();
        result.setSql(this.getSql());
        result.setParams(this.getParams());
        this.setSql(null);
        this.setParams(null);
        return result;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
