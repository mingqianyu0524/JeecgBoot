package org.jeecg.modules.demo.projectCategory.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.segments.OrderBySegmentList;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.projectCategory.entity.ProjectCategory;
import org.jeecg.modules.demo.projectCategory.service.IProjectCategoryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementPage;
import org.jeecg.modules.demo.supplierManagement.entity.SupplierManagement;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 项目分类管理
 * @Author: jeecg-boot
 * @Date:   2023-05-26
 * @Version: V1.0
 */
@Api(tags="项目分类管理")
@RestController
@RequestMapping("/projectCategory/projectCategory")
@Slf4j
public class ProjectCategoryController extends JeecgController<ProjectCategory, IProjectCategoryService> {
	 @Autowired
	 private IProjectCategoryService projectCategoryService;

	 /**
	  * 分页列表查询
	  *
	  * @param projectCategory
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "项目分类管理-分页列表查询")
	 @ApiOperation(value="项目分类管理-分页列表查询", notes="项目分类管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<ProjectCategory>> queryPageList(ProjectCategory projectCategory,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		 QueryWrapper<ProjectCategory> queryWrapper = QueryGenerator.initQueryWrapper(projectCategory, req.getParameterMap());
//		 OrderBySegmentList orderBy = queryWrapper.getExpression().getOrderBy();
//		 if (orderBy.size() == 1) {
//			 String sqlSegment = orderBy.get(0).getSqlSegment();
//			 if (sqlSegment.equals("create_time DESC")) {
//				 orderBy.clear();
//			 }
//		 }
		 queryWrapper.orderByDesc("category_index");
		 Page<ProjectCategory> page = new Page<>(pageNo, pageSize);
		 IPage<ProjectCategory> pageList = projectCategoryService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  *   添加
	  *
	  * @param projectCategory
	  * @return
	  */
	 @AutoLog(value = "项目分类管理-添加")
	 @ApiOperation(value="项目分类管理-添加", notes="项目分类管理-添加")
	 @RequiresPermissions("projectCategory:project_category:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody ProjectCategory projectCategory) {
		 projectCategoryService.save(projectCategory);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param projectCategory
	  * @return
	  */
	 @AutoLog(value = "项目分类管理-编辑")
	 @ApiOperation(value="项目分类管理-编辑", notes="项目分类管理-编辑")
	 @RequiresPermissions("projectCategory:project_category:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody ProjectCategory projectCategory) {
		 projectCategoryService.updateById(projectCategory);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "项目分类管理-通过id删除")
	 @ApiOperation(value="项目分类管理-通过id删除", notes="项目分类管理-通过id删除")
	 @RequiresPermissions("projectCategory:project_category:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 projectCategoryService.removeById(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "项目分类管理-批量删除")
	 @ApiOperation(value="项目分类管理-批量删除", notes="项目分类管理-批量删除")
	 @RequiresPermissions("projectCategory:project_category:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.projectCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功!");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "项目分类管理-通过id查询")
	 @ApiOperation(value="项目分类管理-通过id查询", notes="项目分类管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<ProjectCategory> queryById(@RequestParam(name="id",required=true) String id) {
		 ProjectCategory projectCategory = projectCategoryService.getById(id);
		 if(projectCategory==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(projectCategory);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param projectCategory
    */
    @RequiresPermissions("projectCategory:project_category:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectCategory projectCategory) {
        return super.exportXls(request, projectCategory, ProjectCategory.class, "项目分类管理");
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "项目分类管理");
		 mv.addObject(NormalExcelConstants.CLASS, ProjectCategory.class);
		 ExportParams exportParams = new ExportParams("项目分类管理数据", "导入人:" + sysUser.getRealname(), "项目分类管理");
		 exportParams.setType(ExcelType.XSSF);
		 exportParams.setExclusions(new String[]{"项目分类编号","是否删除"});
		 mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		 mv.addObject(NormalExcelConstants.DATA_LIST, new ArrayList<ProjectCategory>());
		 return mv;
	 }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("projectCategory:project_category:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ProjectCategory.class);
    }

}
