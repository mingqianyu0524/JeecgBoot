package org.jeecg.modules.demo.contractManagement.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@ApiModel(value="contract_management对象", description="合同管理")
@Data
@TableName("contract_management")
public class ContractManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer indexId;
	/**部门*/
	@Excel(name = "部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "部门")
    private java.lang.String deptName;
	/**项目id外键*/
	@Excel(name = "项目id外键", width = 15)
    @ApiModelProperty(value = "项目id外键")
    private java.lang.String projectFkId;
	/**合同编号*/
	@Excel(name = "合同编号", width = 15)
    @ApiModelProperty(value = "合同编号")
    private java.lang.String contractIndex;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private java.lang.String contractName;
	/**合同签订时间*/
	@Excel(name = "合同签订时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同签订时间")
    private java.util.Date contractSignDate;
	/**合同开始日期*/
	@Excel(name = "合同周期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同开始日期")
    private java.util.Date contractStartDate;
    /**合同结束日期*/
    @Excel(name = "合同周期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同结束日期")
    private java.util.Date contractEndDate;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private java.lang.String clientName;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private java.lang.String supplierName;
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
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private java.lang.Integer isDelete;
}
