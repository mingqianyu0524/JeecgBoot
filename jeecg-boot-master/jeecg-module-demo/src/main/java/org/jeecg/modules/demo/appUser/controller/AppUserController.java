package org.jeecg.modules.demo.appUser.controller;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.aop.ExportExcel;
import org.jeecg.modules.demo.appUser.entity.AppUser;
import org.jeecg.modules.demo.appUser.service.IAppUserService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 测试app用户表
 * @Author: jeecg-boot
 * @Date:   2023-05-30
 * @Version: V1.0
 */
@Api(tags="测试app用户表")
@RestController
@RequestMapping("/appUser/appUser")
@Slf4j
public class AppUserController extends JeecgController<AppUser, IAppUserService> {
	@Autowired
	private IAppUserService appUserService;

	 /**
	 * 分页列表查询
	 *
	 * @param appUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "测试app用户表-分页列表查询")
	@ApiOperation(value="测试app用户表-分页列表查询", notes="测试app用户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AppUser>> queryPageList(AppUser appUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AppUser> queryWrapper = QueryGenerator.initQueryWrapper(appUser, req.getParameterMap());
		Page<AppUser> page = new Page<AppUser>(pageNo, pageSize);
		IPage<AppUser> pageList = appUserService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param appUser
	 * @return
	 */
	@AutoLog(value = "测试app用户表-添加")
	@ApiOperation(value="测试app用户表-添加", notes="测试app用户表-添加")
	@RequiresPermissions("appUser:app_user:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AppUser appUser) {
		appUserService.save(appUser);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param appUser
	 * @return
	 */
	@AutoLog(value = "测试app用户表-编辑")
	@ApiOperation(value="测试app用户表-编辑", notes="测试app用户表-编辑")
	@RequiresPermissions("appUser:app_user:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AppUser appUser) {
		appUserService.updateById(appUser);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试app用户表-通过id删除")
	@ApiOperation(value="测试app用户表-通过id删除", notes="测试app用户表-通过id删除")
	@RequiresPermissions("appUser:app_user:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		appUserService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试app用户表-批量删除")
	@ApiOperation(value="测试app用户表-批量删除", notes="测试app用户表-批量删除")
	@RequiresPermissions("appUser:app_user:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.appUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "测试app用户表-通过id查询")
	@ApiOperation(value="测试app用户表-通过id查询", notes="测试app用户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AppUser> queryById(@RequestParam(name="id",required=true) String id) {
		AppUser appUser = appUserService.getById(id);
		if(appUser==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(appUser);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param appUser
    */
    @RequiresPermissions("appUser:app_user:exportXls")
    @RequestMapping(value = "/exportXls")
	@ExportExcel(beanClass = AppUser.class, title = "测试app用户表")
    public ModelAndView exportXls(HttpServletRequest request, AppUser appUser) {
		Class<AppUser> clazz = AppUser.class;
		String title = "测试app用户表";
		// Step.1 组装查询条件
		QueryWrapper<AppUser> queryWrapper = QueryGenerator.initQueryWrapper(appUser, request.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		// 过滤选中数据
		String selections = request.getParameter("selections");
		if (oConvertUtils.isNotEmpty(selections)) {
			List<String> selectionList = Arrays.asList(selections.split(","));
			queryWrapper.in("id",selectionList);
		}
		// Step.2 获取导出数据
		List<AppUser> exportList = service.list(queryWrapper);

		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		//此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.FILE_NAME, title);
		mv.addObject(NormalExcelConstants.CLASS, clazz);
		ExportParams exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
		exportParams.setType(ExcelType.XSSF);
		// exportParams.setImageBasePath(upLoadPath);
		mv.addObject(NormalExcelConstants.PARAMS,exportParams);
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("appUser:app_user:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AppUser.class);
    }

}
