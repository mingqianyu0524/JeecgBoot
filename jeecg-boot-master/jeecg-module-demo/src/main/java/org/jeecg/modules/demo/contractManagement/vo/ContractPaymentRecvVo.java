package org.jeecg.modules.demo.contractManagement.vo;

import lombok.Data;

import java.util.Date;
@Data
public class ContractPaymentRecvVo {
    private double paymentAmount;
    private Date kpDate;
    private Date payDate;
    private String remark;
}
