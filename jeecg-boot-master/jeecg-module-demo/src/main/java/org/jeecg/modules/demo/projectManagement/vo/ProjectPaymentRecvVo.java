package org.jeecg.modules.demo.projectManagement.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectPaymentRecvVo {
    private double paymentAmount;
    private Date kpDate;
    private String remark;
}
