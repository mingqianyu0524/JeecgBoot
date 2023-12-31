package org.jeecg.modules.demo.supplierManagement.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.demo.clientManagement.entity.ClientManagement;
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
import org.jeecg.modules.demo.supplierManagement.entity.SupplierContact;
import org.jeecg.modules.demo.supplierManagement.entity.SupplierManagement;
import org.jeecg.modules.demo.supplierManagement.vo.SupplierManagementPage;
import org.jeecg.modules.demo.supplierManagement.service.ISupplierManagementService;
import org.jeecg.modules.demo.supplierManagement.service.ISupplierContactService;
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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 供应商管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Api(tags="供应商管理")
@RestController
@RequestMapping("/supplierManagement/supplierManagement")
@Slf4j
public class SupplierManagementController {
	 @Autowired
	 private ISupplierManagementService supplierManagementService;
	 @Autowired
	 private ISupplierContactService supplierContactService;

	 /**
	  * 分页列表查询
	  *
	  * @param supplierManagement
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "供应商管理-分页列表查询")
	 @ApiOperation(value="供应商管理-分页列表查询", notes="供应商管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<SupplierManagement>> queryPageList(SupplierManagement supplierManagement,
															@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															HttpServletRequest req) {
		 QueryWrapper<SupplierManagement> queryWrapper = QueryGenerator.initQueryWrapper(supplierManagement, req.getParameterMap());
		 Page<SupplierManagement> page = new Page<SupplierManagement>(pageNo, pageSize);
		 IPage<SupplierManagement> pageList = supplierManagementService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  *   添加
	  *
	  * @param supplierManagementPage
	  * @return
	  */
	 @AutoLog(value = "供应商管理-添加")
	 @ApiOperation(value="供应商管理-添加", notes="供应商管理-添加")
    @RequiresPermissions("supplierManagement:supplier_management:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody SupplierManagementPage supplierManagementPage) {
		 SupplierManagement supplierManagement = new SupplierManagement();
		 BeanUtils.copyProperties(supplierManagementPage, supplierManagement);
		 try {
			 supplierManagementService.saveMain(supplierManagement, supplierManagementPage.getSupplierContactList());
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 return Result.error("添加失败: " + e.getMessage());
		 }
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param supplierManagementPage
	  * @return
	  */
	 @AutoLog(value = "供应商管理-编辑")
	 @ApiOperation(value="供应商管理-编辑", notes="供应商管理-编辑")
    @RequiresPermissions("supplierManagement:supplier_management:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody SupplierManagementPage supplierManagementPage) {
		 SupplierManagement supplierManagement = new SupplierManagement();
		 BeanUtils.copyProperties(supplierManagementPage, supplierManagement);
		 SupplierManagement supplierManagementEntity = supplierManagementService.getById(supplierManagement.getId());
		 if(supplierManagementEntity==null) {
			 return Result.error("未找到对应数据");
		 }
		 supplierManagementService.updateMain(supplierManagement, supplierManagementPage.getSupplierContactList());
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "供应商管理-通过id删除")
	 @ApiOperation(value="供应商管理-通过id删除", notes="供应商管理-通过id删除")
    @RequiresPermissions("supplierManagement:supplier_management:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 supplierManagementService.delMain(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "供应商管理-批量删除")
	 @ApiOperation(value="供应商管理-批量删除", notes="供应商管理-批量删除")
    @RequiresPermissions("supplierManagement:supplier_management:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.supplierManagementService.delBatchMain(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "供应商管理-通过id查询")
	 @ApiOperation(value="供应商管理-通过id查询", notes="供应商管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<SupplierManagement> queryById(@RequestParam(name="id",required=true) String id) {
		 SupplierManagement supplierManagement = supplierManagementService.getById(id);
		 if(supplierManagement==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(supplierManagement);

	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "供应商联系人通过主表ID查询")
	 @ApiOperation(value="供应商联系人主表ID查询", notes="供应商联系人-通主表ID查询")
	 @GetMapping(value = "/querySupplierContactByMainId")
	 public Result<List<SupplierContact>> querySupplierContactListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<SupplierContact> supplierContactList = supplierContactService.selectByMainId(id);
		 return Result.OK(supplierContactList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param supplierManagement
    */
    @RequiresPermissions("supplierManagement:supplier_management:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SupplierManagement supplierManagement) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SupplierManagement> queryWrapper = QueryGenerator.initQueryWrapper(supplierManagement, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<SupplierManagement> supplierManagementList = supplierManagementService.list(queryWrapper);

      // Step.3 组装pageList
      List<SupplierManagementPage> pageList = new ArrayList<SupplierManagementPage>();
      for (SupplierManagement main : supplierManagementList) {
          SupplierManagementPage vo = new SupplierManagementPage();
          BeanUtils.copyProperties(main, vo);
          List<SupplierContact> supplierContactList = supplierContactService.selectByMainId(main.getId());
          vo.setSupplierContactList(supplierContactList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "供应商管理列表");
      mv.addObject(NormalExcelConstants.CLASS, SupplierManagementPage.class);
		ExportParams exportParams = new ExportParams("供应商管理数据", "导出人:" + sysUser.getRealname(), "供应商管理");
		exportParams.setType(ExcelType.XSSF);
		mv.addObject(NormalExcelConstants.PARAMS, exportParams);
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "供应商管理");
		 mv.addObject(NormalExcelConstants.CLASS, SupplierManagementPage.class);
		 ExportParams exportParams = new ExportParams("供应商管理数据", "导入人:" + sysUser.getRealname(), "供应商管理");
		 exportParams.setType(ExcelType.XSSF);
		 exportParams.setExclusions(new String[]{"供应商编号","是否删除"});
		 mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		 List<SupplierManagementPage> pageList = new ArrayList<>();
		 SupplierManagementPage supplierManagementPage = new SupplierManagementPage();
		 supplierManagementPage.setSupplierContactList(new ArrayList<>());
		 pageList.add(supplierManagementPage);
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
    @RequiresPermissions("supplierManagement:supplier_management:importExcel")
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
              List<SupplierManagementPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SupplierManagementPage.class, params);
			  List<SupplierManagement> supplierList = new ArrayList<>();
			  List<List<SupplierContact>> contactList = new ArrayList<>();
			  for (SupplierManagementPage page : list) {
                  SupplierManagement po = new SupplierManagement();
                  BeanUtils.copyProperties(page, po);
				  supplierList.add(po);
                  contactList.add(page.getSupplierContactList());
              }
			  supplierManagementService.saveBatch(supplierList, contactList);
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
