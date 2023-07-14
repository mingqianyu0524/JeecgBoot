package org.jeecg.modules.demo.supplierManagement.entity;

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
 * @Description: 供应商管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@ApiModel(value="supplier_management对象", description="供应商管理")
@Data
@TableName("supplier_management")
public class SupplierManagement implements Serializable {
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
	/**供应商编号*/
	@Excel(name = "供应商编号", width = 15)
    @ApiModelProperty(value = "供应商编号")
    private java.lang.Integer supplierIndex;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private java.lang.String supplierName;
	/**所在城市*/
	@Excel(name = "所在城市", width = 15)
    @ApiModelProperty(value = "所在城市")
    private java.lang.String city;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String contact;
	/**银行账号*/
	@Excel(name = "银行账号", width = 15)
    @ApiModelProperty(value = "银行账号")
    private java.lang.String bankAccount;
	/**开户银行*/
	@Excel(name = "开户银行", width = 15)
    @ApiModelProperty(value = "开户银行")
    private java.lang.String bankName;
	/**开户名称*/
	@Excel(name = "开户名称", width = 15)
    @ApiModelProperty(value = "开户名称")
    private java.lang.String accountName;
	/**开票地址*/
	@Excel(name = "开票地址", width = 15)
    @ApiModelProperty(value = "开票地址")
    private java.lang.String kpAddress;
	/**税号*/
	@Excel(name = "税号", width = 15)
    @ApiModelProperty(value = "税号")
    private java.lang.String taxId;
	/**开票电话*/
	@Excel(name = "开票电话", width = 15)
    @ApiModelProperty(value = "开票电话")
    private java.lang.String kpPhone;
	/**开票要求*/
	@Excel(name = "开票要求", width = 15)
    @ApiModelProperty(value = "开票要求")
    private java.lang.String kpRemark;
	/**收票人*/
	@Excel(name = "收票人", width = 15)
    @ApiModelProperty(value = "收票人")
    private java.lang.String receiver;
	/**收票电话*/
	@Excel(name = "收票电话", width = 15)
    @ApiModelProperty(value = "收票电话")
    private java.lang.String recvPhone;
	/**收票地址*/
	@Excel(name = "收票地址", width = 15)
    @ApiModelProperty(value = "收票地址")
    private java.lang.String recvAddress;
	/**邮编*/
	@Excel(name = "邮编", width = 15)
    @ApiModelProperty(value = "邮编")
    private java.lang.Integer zipcode;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private java.lang.Integer isDelete;
}
