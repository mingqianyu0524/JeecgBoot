package org.jeecg.modules.demo.supplierManagement.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@ApiModel(value="supplier_contact对象", description="供应商联系人")
@Data
@TableName("supplier_contact")
public class SupplierContact implements Serializable {
    private static final String setter = "setSupplierFkId";
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
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**供应商id外键*/
    @ApiModelProperty(value = "供应商id外键")
    private java.lang.String supplierFkId;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15, dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**生日*/
	@Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private java.util.Date birthday;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private java.lang.String duty;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String phone;
	/**邮件*/
	@Excel(name = "邮件", width = 15)
    @ApiModelProperty(value = "邮件")
    private java.lang.String email;
	/**QQ*/
	@Excel(name = "QQ", width = 15)
    @ApiModelProperty(value = "QQ")
    private java.lang.String qq;
	/**工作地址*/
	@Excel(name = "工作地址", width = 15)
    @ApiModelProperty(value = "工作地址")
    private java.lang.String workAddress;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @TableLogic
    @ApiModelProperty(value = "是否删除")
    private java.lang.String isDelete;
}
