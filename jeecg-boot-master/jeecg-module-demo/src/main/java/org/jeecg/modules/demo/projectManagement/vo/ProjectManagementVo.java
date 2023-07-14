package org.jeecg.modules.demo.projectManagement.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.demo.contractManagement.vo.ContractPaymentRecvVo;
import org.jeecg.modules.demo.contractManagement.vo.ContractPaymentTermVo;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Data
public class ProjectManagementVo{
    public ProjectManagementVo(String id) {
        this.setId(id);
    }

    private String id;

    private Integer indexId;

    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    private String deptName;

    @Dict(dicCode = "project_year")
    private String projectYear;

    private String projectIndex;

    private String projectName;

    private String projectCategory;

    @Dict(dicCode = "project_status")
    private String projectStatus;

    private String clientName;

    private java.util.Date signDate;

    private String contractPeriod;

    /**合同扫描件*/
    @Excel(name = "合同扫描件", width = 15)
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

    private double totalAmount;

    private double unpaidAmount;

    private List<ProjectPaymentTermVo> projectPaymentTermVos;

    private List<ProjectPaymentRecvVo> projectPaymentRecvVos;

    private List<ContractPaymentTermVo> contractPaymentTermVos;

    private List<ContractPaymentRecvVo> contractPaymentRecvVos;

    private double consultFee;

    private String remark;
}
