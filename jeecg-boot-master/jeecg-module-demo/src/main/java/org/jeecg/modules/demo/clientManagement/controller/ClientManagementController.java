package org.jeecg.modules.demo.clientManagement.controller;

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

import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import org.jeecg.modules.demo.supplierManagement.vo.SupplierManagementPage;
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
import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import org.jeecg.modules.demo.clientManagement.entity.ClientManagement;
import org.jeecg.modules.demo.clientManagement.vo.ClientManagementPage;
import org.jeecg.modules.demo.clientManagement.service.IClientManagementService;
import org.jeecg.modules.demo.clientManagement.service.IClientContactService;
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
 * @Description: 客户管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Api(tags="客户管理")
@RestController
@RequestMapping("/clientManagement/clientManagement")
@Slf4j
public class ClientManagementController {
	 @Autowired
	 private IClientManagementService clientManagementService;
	 @Autowired
	 private IClientContactService clientContactService;

	 /**
	  * 分页列表查询
	  *
	  * @param clientManagement
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "客户管理-分页列表查询")
	 @ApiOperation(value="客户管理-分页列表查询", notes="客户管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<ClientManagement>> queryPageList(ClientManagement clientManagement,
														  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														  HttpServletRequest req) {
		 QueryWrapper<ClientManagement> queryWrapper = QueryGenerator.initQueryWrapper(clientManagement, req.getParameterMap());
		 Page<ClientManagement> page = new Page<>(pageNo, pageSize);
		 IPage<ClientManagement> pageList = clientManagementService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	  *   添加
	  *
	  * @param clientManagementPage
	  * @return
	  */
	 @AutoLog(value = "客户管理-添加")
	 @ApiOperation(value="客户管理-添加", notes="客户管理-添加")
    @RequiresPermissions("clientManagement:client_management:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody ClientManagementPage clientManagementPage) {
		 ClientManagement clientManagement = new ClientManagement();
		 BeanUtils.copyProperties(clientManagementPage, clientManagement);
		 try {
			 clientManagementService.saveMain(clientManagement, clientManagementPage.getClientContactList());
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 return Result.error("添加失败: " + e.getMessage());
		 }
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param clientManagementPage
	  * @return
	  */
	 @AutoLog(value = "客户管理-编辑")
	 @ApiOperation(value="客户管理-编辑", notes="客户管理-编辑")
    @RequiresPermissions("clientManagement:client_management:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody ClientManagementPage clientManagementPage) {
		 ClientManagement clientManagement = new ClientManagement();
		 BeanUtils.copyProperties(clientManagementPage, clientManagement);
		 ClientManagement clientManagementEntity = clientManagementService.getById(clientManagement.getId());
		 if(clientManagementEntity==null) {
			 return Result.error("未找到对应数据");
		 }
		 clientManagementService.updateMain(clientManagement, clientManagementPage.getClientContactList());
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "客户管理-通过id删除")
	 @ApiOperation(value="客户管理-通过id删除", notes="客户管理-通过id删除")
    @RequiresPermissions("clientManagement:client_management:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 clientManagementService.delMain(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "客户管理-批量删除")
	 @ApiOperation(value="客户管理-批量删除", notes="客户管理-批量删除")
    @RequiresPermissions("clientManagement:client_management:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.clientManagementService.delBatchMain(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "客户管理-通过id查询")
	 @ApiOperation(value="客户管理-通过id查询", notes="客户管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<ClientManagement> queryById(@RequestParam(name="id",required=true) String id) {
		 ClientManagement clientManagement = clientManagementService.getById(id);
		 if(clientManagement==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(clientManagement);

	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "客户联系人通过主表ID查询")
	 @ApiOperation(value="客户联系人主表ID查询", notes="客户联系人-通主表ID查询")
	 @GetMapping(value = "/queryClientContactByMainId")
	 public Result<List<ClientContact>> queryClientContactListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<ClientContact> clientContactList = clientContactService.selectByMainId(id);
		 return Result.OK(clientContactList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param clientManagement
    */
    @RequiresPermissions("clientManagement:client_management:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ClientManagement clientManagement) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ClientManagement> queryWrapper = QueryGenerator.initQueryWrapper(clientManagement, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<ClientManagement> clientManagementList = clientManagementService.list(queryWrapper);

      // Step.3 组装pageList
      List<ClientManagementPage> pageList = new ArrayList<ClientManagementPage>();
      for (ClientManagement main : clientManagementList) {
          ClientManagementPage vo = new ClientManagementPage();
          BeanUtils.copyProperties(main, vo);
          List<ClientContact> clientContactList = clientContactService.selectByMainId(main.getId());
          vo.setClientContactList(clientContactList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "客户管理列表");
      mv.addObject(NormalExcelConstants.CLASS, ClientManagementPage.class);
		ExportParams exportParams=new ExportParams("客户管理数据", "导出人:"+sysUser.getRealname(), "客户管理");
		exportParams.setType(ExcelType.XSSF);
      mv.addObject(NormalExcelConstants.PARAMS, exportParams);
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "客户管理");
		 mv.addObject(NormalExcelConstants.CLASS, ClientManagementPage.class);
		 ExportParams exportParams = new ExportParams("客户管理数据", "导入人:" + sysUser.getRealname(), "客户管理");
		 exportParams.setExclusions(new String[]{"客户编号","是否删除"});
		 exportParams.setType(ExcelType.XSSF);
		 mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		 List<ClientManagementPage> pageList = new ArrayList<>();
		 ClientManagementPage clientManagementPage = new ClientManagementPage();
		 clientManagementPage.setClientContactList(new ArrayList<>());
		 pageList.add(clientManagementPage);
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
    @RequiresPermissions("clientManagement:client_management:importExcel")
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
              List<ClientManagementPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ClientManagementPage.class, params);
              List<ClientManagement> clientList = new ArrayList<>();
			  List<List<ClientContact>> contactList = new ArrayList<>();
			  for (ClientManagementPage page : list) {
                  ClientManagement po = new ClientManagement();
                  BeanUtils.copyProperties(page, po);
				  clientList.add(po);
				  contactList.add(page.getClientContactList());
              }
			  clientManagementService.saveBatch(clientList, contactList);
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
