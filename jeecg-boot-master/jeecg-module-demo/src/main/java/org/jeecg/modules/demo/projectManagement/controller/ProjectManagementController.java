package org.jeecg.modules.demo.projectManagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementVo;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.vo.SysLogAvatarVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentRecv;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementPage;
import org.jeecg.modules.demo.projectManagement.service.IProjectManagementService;
import org.jeecg.modules.demo.projectManagement.service.IProjectPaymentTermService;
import org.jeecg.modules.demo.projectManagement.service.IProjectPaymentRecvService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Api(tags="项目管理")
@RestController
@RequestMapping("/projectManagement/projectManagement")
@Slf4j
public class ProjectManagementController {
	 @Autowired
	 private IProjectManagementService projectManagementService;
	 @Autowired
	 private IProjectPaymentTermService projectPaymentTermService;
	 @Autowired
	 private IProjectPaymentRecvService projectPaymentRecvService;
	 @Autowired
	 private ISysLogService sysLogService;
	 @Autowired
	 private SysDepartMapper sysDepartMapper;

	 /**
	  * 分页列表查询
	  *
	  * @param projectManagement
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "项目管理-分页列表查询")
	 @ApiOperation(value="项目管理-分页列表查询", notes="项目管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<ProjectManagementVo>> queryPageList(ProjectManagement projectManagement,
														   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														   HttpServletRequest req) {
		 QueryWrapper<ProjectManagement> queryWrapper = QueryGenerator.initQueryWrapper(projectManagement, req.getParameterMap());
		 List<ProjectManagementVo> projectManagementVoList = projectManagementService.selectProjectManagementVoList(queryWrapper);
		 IPage<ProjectManagementVo> result = new Page<>(pageNo, pageSize, projectManagementVoList.size());
		 result.setRecords(projectManagementVoList);
		 return Result.OK(result);
	 }

	 @ApiOperation(value="项目管理-操作日志查询", notes="项目管理-操作日志查询")
	 @GetMapping(value = "/queryOperationLog")
	 public Result<IPage<SysLogAvatarVo>> queryOperationLog(
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													HttpServletRequest req) {
		 List<SysLogAvatarVo> sysLogAvatarVoList = sysLogService.selectSysLogAvatarVoList();
		 IPage<SysLogAvatarVo> pageList = new Page<>(pageNo, pageSize);
		 pageList.setRecords(sysLogAvatarVoList);
		 pageList.setTotal(sysLogAvatarVoList.size());
		 return Result.OK(pageList);
	 }

	 /**
	  *   添加
	  *
	  * @param projectManagementPage
	  * @return
	  */
	 @AutoLog(value = "项目管理-添加")
	 @ApiOperation(value="项目管理-添加", notes="项目管理-添加")
    @RequiresPermissions("projectManagement:project_management:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody ProjectManagementPage projectManagementPage) {
		 ProjectManagement projectManagement = new ProjectManagement();
		 BeanUtils.copyProperties(projectManagementPage, projectManagement);
		 try {
			 projectManagementService.saveMain(projectManagement, projectManagementPage.getProjectPaymentTermList(),projectManagementPage.getProjectPaymentRecvList());
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 return Result.error("添加失败:"+e.getMessage());
		 }
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param projectManagementPage
	  * @return
	  */
	 @AutoLog(value = "项目管理-编辑")
	 @ApiOperation(value="项目管理-编辑", notes="项目管理-编辑")
	 @RequiresPermissions("projectManagement:project_management:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody ProjectManagementPage projectManagementPage) {
		 ProjectManagement projectManagement = new ProjectManagement();
		 BeanUtils.copyProperties(projectManagementPage, projectManagement);
		 ProjectManagement projectManagementEntity = projectManagementService.getById(projectManagement.getId());
		 if (projectManagementEntity == null) {
			 return Result.error("未找到对应数据");
		 }
		 projectManagementService.updateMain(projectManagement, projectManagementPage.getProjectPaymentTermList(),projectManagementPage.getProjectPaymentRecvList());
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "项目管理-通过id删除")
	 @ApiOperation(value="项目管理-通过id删除", notes="项目管理-通过id删除")
     @RequiresPermissions("projectManagement:project_management:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 projectManagementService.delMain(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "项目管理-批量删除")
	 @ApiOperation(value="项目管理-批量删除", notes="项目管理-批量删除")
     @RequiresPermissions("projectManagement:project_management:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.projectManagementService.delBatchMain(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "项目管理-通过id查询")
	 @ApiOperation(value="项目管理-通过id查询", notes="项目管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<ProjectManagement> queryById(@RequestParam(name="id",required=true) String id) {
		 ProjectManagement projectManagement = projectManagementService.getById(id);
		 if(projectManagement==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(projectManagement);

	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "项目应收通过主表ID查询")
	 @ApiOperation(value="项目应收主表ID查询", notes="项目应收-通主表ID查询")
	 @GetMapping(value = "/queryProjectPaymentTermByMainId")
	 public Result<List<ProjectPaymentTerm>> queryProjectPaymentTermListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<ProjectPaymentTerm> projectPaymentTermList = projectPaymentTermService.selectByMainId(id);
		 return Result.OK(projectPaymentTermList);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "收款管理通过主表ID查询")
	 @ApiOperation(value="收款管理主表ID查询", notes="收款管理-通主表ID查询")
	 @GetMapping(value = "/queryProjectPaymentRecvByMainId")
	 public Result<List<ProjectPaymentRecv>> queryProjectPaymentRecvListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<ProjectPaymentRecv> projectPaymentRecvList = projectPaymentRecvService.selectByMainId(id);
		 return Result.OK(projectPaymentRecvList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param projectManagement
    */
    @RequiresPermissions("projectManagement:project_management:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectManagement projectManagement) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ProjectManagement> queryWrapper = QueryGenerator.initQueryWrapper(projectManagement, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<ProjectManagement> projectManagementList = projectManagementService.list(queryWrapper);

      // Step.3 组装pageList
      List<ProjectManagementPage> pageList = new ArrayList<ProjectManagementPage>();
      for (ProjectManagement main : projectManagementList) {
          ProjectManagementPage vo = new ProjectManagementPage();
          BeanUtils.copyProperties(main, vo);
          List<ProjectPaymentTerm> projectPaymentTermList = projectPaymentTermService.selectByMainId(main.getId());
          vo.setProjectPaymentTermList(projectPaymentTermList);
          List<ProjectPaymentRecv> projectPaymentRecvList = projectPaymentRecvService.selectByMainId(main.getId());
          vo.setProjectPaymentRecvList(projectPaymentRecvList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "项目管理列表");
      mv.addObject(NormalExcelConstants.CLASS, ProjectManagementPage.class);
	  ExportParams exportParams = new ExportParams("项目管理数据", "导出人:" + sysUser.getRealname(), "项目管理");
	  exportParams.setType(ExcelType.XSSF);
	  mv.addObject(NormalExcelConstants.PARAMS, exportParams);
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
	 	LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	 	ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 	mv.addObject(NormalExcelConstants.FILE_NAME, "项目管理列表");
	 	mv.addObject(NormalExcelConstants.CLASS, ProjectManagementPage.class);
	 	ExportParams exportParams = new ExportParams("项目管理数据", "导入人:" + sysUser.getRealname(), "项目管理");
	 	exportParams.setType(ExcelType.XSSF);
		exportParams.setExclusions(new String[]{"序号","是否删除","收款流水号"});
		mv.addObject(NormalExcelConstants.PARAMS, exportParams);

		List<ProjectManagementPage> pageList = new ArrayList<ProjectManagementPage>();
		ProjectManagementPage projectManagementPage = new ProjectManagementPage();
		projectManagementPage.setProjectPaymentTermList(new ArrayList<>());
		projectManagementPage.setProjectPaymentRecvList(new ArrayList<>());
		pageList.add(projectManagementPage);

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
    @RequiresPermissions("projectManagement:project_management:importExcel")
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
              List<ProjectManagementPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ProjectManagementPage.class, params);
			  List<ProjectManagement> pmList = new ArrayList<>();
			  List<List<ProjectPaymentTerm>> termList = new ArrayList<>();
			  List<List<ProjectPaymentRecv>> recvList = new ArrayList<>();
			  for (ProjectManagementPage page : list) {
                  ProjectManagement po = new ProjectManagement();
                  BeanUtils.copyProperties(page, po);
				  pmList.add(po);
				  termList.add(page.getProjectPaymentTermList());
				  recvList.add(page.getProjectPaymentRecvList());
              }
			  projectManagementService.saveBatch(pmList, termList, recvList);
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
