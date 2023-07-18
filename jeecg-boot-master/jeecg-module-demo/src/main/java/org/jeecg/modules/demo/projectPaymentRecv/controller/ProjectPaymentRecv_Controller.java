package org.jeecg.modules.demo.projectPaymentRecv.controller;

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
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectManagementMapper;
import org.jeecg.modules.demo.projectPaymentRecv.vo.ProjectPaymentRecvVo;
import org.jeecg.modules.demo.projectPaymentRecv.entity.ProjectPaymentRecv_;
import org.jeecg.modules.demo.projectPaymentRecv.service.IProjectPaymentRecv_Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Api(tags="收款管理")
@RestController
@RequestMapping("/projectPaymentRecv/projectPaymentRecv_")
@Slf4j
public class ProjectPaymentRecv_Controller extends JeecgController<ProjectPaymentRecv_, IProjectPaymentRecv_Service> {
	 @Autowired
	 private IProjectPaymentRecv_Service projectPaymentRecv_Service;
	 @Autowired
	 private ProjectManagementMapper projectManagementMapper;

	 /**
	  * 分页列表查询
	  *
	  * @param ProjectPaymentRecvVo
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "收款管理-分页列表查询")
	 @ApiOperation(value="收款管理-分页列表查询", notes="收款管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<ProjectPaymentRecvVo>> queryPageList(ProjectPaymentRecvVo projectPaymentRecvVo,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) throws InvocationTargetException, IllegalAccessException {
		 String projectIndex = projectPaymentRecvVo.getProjectIndex();
		 // 包含查询 - 项目名称
		 String projectName = req.getParameter("projectName");
		 projectName = projectName != null ? projectName.replaceAll("\\*","%") : null;
		 List<String> projectNameList = new ArrayList<>();
		 if (oConvertUtils.isNotEmpty(projectName)) {
			 projectNameList = Arrays.asList(projectName.split(","));
		 }
		 String clientName = projectPaymentRecvVo.getClientName();
		 ProjectPaymentRecv_ projectPaymentRecv_ = new ProjectPaymentRecv_();
		 BeanUtils.copyProperties(projectPaymentRecvVo, projectPaymentRecv_);
		 QueryWrapper<ProjectPaymentRecv_> queryWrapper = QueryGenerator.initQueryWrapper(projectPaymentRecv_, req.getParameterMap());
		 List<ProjectPaymentRecvVo> list = projectPaymentRecv_Service.getVoList(queryWrapper, projectIndex, projectNameList, clientName);
		 IPage<ProjectPaymentRecvVo> result = new Page<>(pageNo, pageSize);
		 result.setRecords(list);
		 return Result.OK(result);
	 }

	 /**
	  *   添加
	  *
	  * @param projectPaymentRecv_
	  * @return
	  */
	 @AutoLog(value = "收款管理-添加")
	 @ApiOperation(value="收款管理-添加", notes="收款管理-添加")
	 @RequiresPermissions("projectPaymentRecv:project_payment_recv:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody ProjectPaymentRecv_ projectPaymentRecv_) {
		 projectPaymentRecv_Service.save(projectPaymentRecv_);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param projectPaymentRecv_
	  * @return
	  */
	 @AutoLog(value = "收款管理-编辑")
	 @ApiOperation(value="收款管理-编辑", notes="收款管理-编辑")
	 @RequiresPermissions("projectPaymentRecv:project_payment_recv:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody ProjectPaymentRecv_ projectPaymentRecv_) {
		 projectPaymentRecv_Service.updateById(projectPaymentRecv_);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "收款管理-通过id删除")
	 @ApiOperation(value="收款管理-通过id删除", notes="收款管理-通过id删除")
	 @RequiresPermissions("projectPaymentRecv:project_payment_recv:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 projectPaymentRecv_Service.removeById(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "收款管理-批量删除")
	 @ApiOperation(value="收款管理-批量删除", notes="收款管理-批量删除")
	 @RequiresPermissions("projectPaymentRecv:project_payment_recv:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.projectPaymentRecv_Service.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功!");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "收款管理-通过id查询")
	 @ApiOperation(value="收款管理-通过id查询", notes="收款管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<ProjectPaymentRecv_> queryById(@RequestParam(name="id",required=true) String id) {
		 ProjectPaymentRecv_ projectPaymentRecv_ = projectPaymentRecv_Service.getById(id);
		 if(projectPaymentRecv_==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(projectPaymentRecv_);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param projectPaymentRecv_
    */
    @RequiresPermissions("projectPaymentRecv:project_payment_recv:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectPaymentRecv_ projectPaymentRecv_) {
        return super.exportXls(request, projectPaymentRecv_, ProjectPaymentRecv_.class, "收款管理");
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "收款管理列表");
		 mv.addObject(NormalExcelConstants.CLASS, ProjectPaymentRecvVo.class);
		 ExportParams exportParams = new ExportParams("收款管理数据", "导入人:" + sysUser.getRealname(), "收款管理");
		 exportParams.setType(ExcelType.XSSF);
		 exportParams.setExclusions(new String[]{"收款流水号","项目id外键","是否删除"});
		 mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		 mv.addObject(NormalExcelConstants.DATA_LIST, new ArrayList<ProjectPaymentRecvVo>());
		 return mv;
	 }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("projectPaymentRecv:project_payment_recv:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
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
				List<ProjectPaymentRecvVo> list = ExcelImportUtil.importExcel(file.getInputStream(), ProjectPaymentRecvVo.class, params);
				List<ProjectPaymentRecv_> targetList = new ArrayList<>();
				for (ProjectPaymentRecvVo page : list) {
					ProjectPaymentRecv_ po = new ProjectPaymentRecv_();
					ProjectManagement projectManagement = projectManagementMapper.selectByProjectIndex(page.getProjectIndex());
					if (projectManagement == null || page.getPaymentAmount() == null) {
						throw new Exception("导入Excel缺少必填字段");
					}
					page.setProjectFkId(projectManagement.getId());
					page.setClientName(projectManagement.getClientName());
					page.setProjectName(projectManagement.getProjectName());
					BeanUtils.copyProperties(page, po);
					po.setPaymentSerialNumber(generateSerialNumber(po.getProjectFkId()));
					targetList.add(po);
				}
				projectPaymentRecv_Service.saveBatch(targetList);
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
