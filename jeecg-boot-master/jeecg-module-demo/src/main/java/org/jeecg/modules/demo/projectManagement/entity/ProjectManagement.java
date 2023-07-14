package org.jeecg.modules.demo.projectManagement.entity;

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
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@ApiModel(value="project_management对象", description="项目管理")
@Data
@TableName("project_management")
public class ProjectManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
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
	/**项目年限*/
	@Excel(name = "项目年限", width = 15, dicCode = "project_year")
    @Dict(dicCode = "project_year")
    @ApiModelProperty(value = "项目年限")
    private java.lang.String projectYear;
	/**项目编号*/
	@Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private java.lang.String projectIndex;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**项目分类*/
	@Excel(name = "项目分类", width = 15)
    @ApiModelProperty(value = "项目分类")
    private java.lang.String projectCategory;
	/**项目状态*/
	@Excel(name = "项目状态", width = 15, dicCode = "project_status")
    @Dict(dicCode = "project_status")
    @ApiModelProperty(value = "项目状态")
    private java.lang.String projectStatus;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
    private java.lang.String clientName;
	/**合同签订时间*/
	@Excel(name = "合同签订时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同签订时间")
    private java.util.Date signDate;
	/**合同开始时间*/
	@Excel(name = "合同开始时间", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同开始时间")
    private java.util.Date contractStartDate;
    /**合同结束时间*/
    @Excel(name = "合同结束时间", width = 15)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同结束时间")
    private java.util.Date contractEndDate;
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
	/**专家费*/
	@Excel(name = "专家费", width = 15)
    @ApiModelProperty(value = "专家费")
    private java.lang.Double consultFee;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private java.lang.Integer isDelete;
}
