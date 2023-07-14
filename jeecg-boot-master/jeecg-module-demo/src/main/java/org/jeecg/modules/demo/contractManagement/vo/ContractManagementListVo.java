package org.jeecg.modules.demo.contractManagement.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Data
public class ContractManagementListVo {

    public ContractManagementListVo(String id) {
        this.id = id;
    }

    /**主键*/
    private java.lang.String id;

    /**序号*/
    private java.lang.Integer indexId;

    /**部门*/
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    private java.lang.String deptName;
    /**项目编号*/
    private java.lang.String projectIndex;

    /**项目id外键*/
    private java.lang.String projectFkId;

    /**合同编号*/
    private java.lang.String contractIndex;

    /**合同名称*/
    private java.lang.String contractName;

    /**合同签订时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date contractSignDate;

    /**合同开始日期*/
    private java.lang.String contractPeriod;

    /**合同金额**/
    private java.lang.Double totalAmount;

    /**待付金额**/
    private java.lang.Double unpaidAmount;

    /**合同扫描件*/
    private transient java.lang.String contractScannedString;

    private byte[] contractScanned;

    public byte[] getContractScanned(){
        if(contractScannedString==null){
            return null;
        }
        try {
            return contractScannedString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getContractScannedString(){
        if(contractScanned==null || contractScanned.length==0){
            return "";
        }
        try {
            return new String(contractScanned,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**备注*/
    private java.lang.String remark;

    private List<ContractPaymentTermVo> contractPaymentTermVos;

    private List<ContractPaymentRecvVo> contractPaymentRecvVos;

}
