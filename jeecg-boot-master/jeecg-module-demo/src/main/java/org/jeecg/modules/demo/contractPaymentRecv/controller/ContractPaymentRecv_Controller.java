package org.jeecg.modules.demo.contractPaymentRecv.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import org.jeecg.modules.demo.contractManagement.mapper.ContractManagementMapper;
import org.jeecg.modules.demo.contractPaymentRecv.entity.ContractPaymentRecv_;
import org.jeecg.modules.demo.contractPaymentRecv.service.IContractPaymentRecv_Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.demo.contractPaymentRecv.vo.ContractPaymentRecvVo;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectManagementMapper;
import org.jeecg.modules.demo.projectPaymentRecv.entity.ProjectPaymentRecv_;
import org.jeecg.modules.demo.projectPaymentRecv.vo.ProjectPaymentRecvVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Api(tags="付款管理")
@RestController
@RequestMapping("/contractPaymentRecv/contractPaymentRecv_")
@Slf4j
public class ContractPaymentRecv_Controller extends JeecgController<ContractPaymentRecv_, IContractPaymentRecv_Service> {
	 @Autowired
	 private IContractPaymentRecv_Service contractPaymentRecv_Service;
	 @Autowired
	 private ContractManagementMapper contractManagementMapper;
	 @Autowired
	 private ProjectManagementMapper projectManagementMapper;
	 /**
	  * 分页列表查询
	  *
	  * @param contractPaymentRecv_
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "付款管理-分页列表查询")
	 @ApiOperation(value="付款管理-分页列表查询", notes="付款管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<ContractPaymentRecvVo>> queryPageList(ContractPaymentRecvVo contractPaymentRecvVo,
															   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															   HttpServletRequest req) throws InvocationTargetException, IllegalAccessException {
		 ContractPaymentRecv_ contractPaymentRecv_ = new ContractPaymentRecv_();
		 BeanUtils.copyProperties(contractPaymentRecvVo,contractPaymentRecv_);
		 QueryWrapper<ContractPaymentRecv_> queryWrapper = QueryGenerator.initQueryWrapper(contractPaymentRecv_, req.getParameterMap());

		 // 使用SQL进行查询合同名称，项目编号和名称
		 String contractName = contractPaymentRecvVo.getContractName();
		 contractName = contractName != null ? contractName.replaceAll("\\*","%") : null;
		 List<String> contractNameList = new ArrayList<>();
		 if (oConvertUtils.isNotEmpty(contractName)) {
			 contractNameList = Arrays.asList(contractName.split(","));
		 }
		 String projectName = contractPaymentRecvVo.getProjectName();
		 projectName = projectName != null ? projectName.replaceAll("\\*","%") : null;
		 List<String> projectNameList = new ArrayList<>();
		 if (oConvertUtils.isNotEmpty(projectName)) {
			 projectNameList = Arrays.asList(projectName.split(","));
		 }
		 String projectIndex = contractPaymentRecvVo.getProjectIndex();
		 projectIndex = projectIndex != null ? projectIndex.replaceAll("\\*","%") : null;
		 List<String> projectIndexList = new ArrayList<>();
		 if (oConvertUtils.isNotEmpty(projectIndex)) {
			 projectIndexList = Arrays.asList(projectIndex.split(","));
		 }

		 List<ContractPaymentRecvVo> pageList = contractPaymentRecv_Service.getVoList(queryWrapper, contractNameList, projectNameList, projectIndexList);
		 IPage<ContractPaymentRecvVo> result = new Page<>(pageNo, pageSize);
		 result.setRecords(pageList);
		 return Result.OK(result);
	 }

	 /**
	  *   添加
	  *
	  * @param contractPaymentRecv_
	  * @return
	  */
	 @AutoLog(value = "付款管理-添加")
	 @ApiOperation(value="付款管理-添加", notes="付款管理-添加")
	 @RequiresPermissions("contractPaymentRecv:contract_payment_recv:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody ContractPaymentRecv_ contractPaymentRecv_) {
		 contractPaymentRecv_Service.save(contractPaymentRecv_);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param contractPaymentRecv_
	  * @return
	  */
	 @AutoLog(value = "付款管理-编辑")
	 @ApiOperation(value="付款管理-编辑", notes="付款管理-编辑")
	 @RequiresPermissions("contractPaymentRecv:contract_payment_recv:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody ContractPaymentRecv_ contractPaymentRecv_) {
		 contractPaymentRecv_Service.updateById(contractPaymentRecv_);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "付款管理-通过id删除")
	 @ApiOperation(value="付款管理-通过id删除", notes="付款管理-通过id删除")
	 @RequiresPermissions("contractPaymentRecv:contract_payment_recv:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 contractPaymentRecv_Service.removeById(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "付款管理-批量删除")
	 @ApiOperation(value="付款管理-批量删除", notes="付款管理-批量删除")
	 @RequiresPermissions("contractPaymentRecv:contract_payment_recv:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.contractPaymentRecv_Service.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功!");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "付款管理-通过id查询")
	 @ApiOperation(value="付款管理-通过id查询", notes="付款管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<ContractPaymentRecv_> queryById(@RequestParam(name="id",required=true) String id) {
		 ContractPaymentRecv_ contractPaymentRecv_ = contractPaymentRecv_Service.getById(id);
		 if(contractPaymentRecv_==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(contractPaymentRecv_);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param contractPaymentRecv_
    */
    @RequiresPermissions("contractPaymentRecv:contract_payment_recv:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractPaymentRecv_ contractPaymentRecv_) {
        return super.exportXls(request, contractPaymentRecv_, ContractPaymentRecv_.class, "付款管理");
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "付款管理");
		 mv.addObject(NormalExcelConstants.CLASS, ContractPaymentRecvVo.class);
		 ExportParams exportParams = new ExportParams("付款管理数据", "导入人:" + sysUser.getRealname(), "付款管理");
		 exportParams.setType(ExcelType.XSSF);
		 exportParams.setExclusions(new String[]{"付款流水号","合同id外键","是否删除"});
		 mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		 List<ContractPaymentRecvVo> pageList = new ArrayList<>();
		 mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		 return mv;
	 }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("contractPaymentRecv:contract_payment_recv:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, ContractPaymentRecv_.class);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			// 获取上传文件对象
			MultipartFile file = entity.getValue();
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<ContractPaymentRecvVo> list = ExcelImportUtil.importExcel(file.getInputStream(), ContractPaymentRecvVo.class, params);
				List<ContractPaymentRecv_> targetList = new ArrayList<>();
				for (ContractPaymentRecvVo page : list) {
					ContractPaymentRecv_ po = new ContractPaymentRecv_();
					ContractManagement contractManagement = contractManagementMapper.selectByContractIndex(page.getContractIndex());
					if (contractManagement == null || page.getPaymentAmount() == null) {
						throw new Exception("导入Excel缺少必填字段");
					}
					page.setContractFkId(contractManagement.getId());
					page.setContractName(contractManagement.getContractName());
					ProjectManagement projectManagement = projectManagementMapper.selectById(contractManagement.getProjectFkId());
					page.setProjectIndex(projectManagement.getProjectIndex());
					page.setProjectName(projectManagement.getProjectName());
					BeanUtils.copyProperties(page, po);
					po.setPaymentSerialNumber(generateSerialNumber(po.getContractFkId()));
					targetList.add(po);
				}
				contractPaymentRecv_Service.saveBatch(targetList);
				return Result.OK("文件导入成功！数据行数:" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Result.error("文件导入失败:"+e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.OK("文件导入失败！");
    }
}
