package org.jeecg.modules.demo.projectPaymentRecv.vo;

import lombok.Data;
import org.jeecg.modules.demo.projectPaymentRecv.entity.ProjectPaymentRecv_;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class ProjectPaymentRecvVo extends ProjectPaymentRecv_ {

    public ProjectPaymentRecvVo() {}
    public ProjectPaymentRecvVo(String id) {
        this.setId(id);
    }

    @Override
    public String getProjectFkId() {
        return super.getProjectFkId();
    }

    /**项目编号*/
    @Excel(name = "项目编号", width = 15)
    private java.lang.String projectIndex;

    /**项目名称*/
    @Excel(name = "项目名称", width = 15)
    private java.lang.String projectName;

    /**客户名称*/
    @Excel(name = "客户名称", width = 15)
    private java.lang.String clientName;
}
