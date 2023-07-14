package org.jeecg.modules.demo.contractPaymentRecv.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class ContractPaymentRecvVo extends org.jeecg.modules.demo.contractPaymentRecv.entity.ContractPaymentRecv_ {

    public ContractPaymentRecvVo() {}
    public ContractPaymentRecvVo(String id) {
        this.setId(id);
    }

    /**合同编号*/
    @Excel(name = "合同编号", width = 15)
    private String contractIndex;

    private String contractName;

    private String projectIndex;

    private String projectName;
}
