package org.jeecg.modules.demo.contractManagement.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.modules.demo.contractManagement.vo.ContractManagementListVo;
import org.jeecg.modules.demo.contractManagement.vo.ContractManagementVo;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectManagementMapper;
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
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import org.jeecg.modules.demo.contractManagement.vo.ContractManagementPage;
import org.jeecg.modules.demo.contractManagement.service.IContractManagementService;
import org.jeecg.modules.demo.contractManagement.service.IContractPaymentTermService;
import org.jeecg.modules.demo.contractManagement.service.IContractPaymentRecvService;
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
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Api(tags="合同管理")
@RestController
@RequestMapping("/contractManagement/contractManagement")
@Slf4j
public class ContractManagementController {
	 @Autowired
	 private IContractManagementService contractManagementService;
	 @Autowired
	 private IContractPaymentTermService contractPaymentTermService;
	 @Autowired
	 private IContractPaymentRecvService contractPaymentRecvService;
	 @Autowired
	 private ISysLogService sysLogService;
	 @Autowired
	 private ProjectManagementMapper projectManagementMapper;

	 /**
	  * 分页列表查询
	  *
	  * @param contractManagement
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "合同管理-分页列表查询")
	 @ApiOperation(value="合同管理-分页列表查询", notes="合同管理-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<IPage<ContractManagementListVo>> queryPageList(ContractManagement contractManagement,
																  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																  HttpServletRequest req) {
		 QueryWrapper<ContractManagement> queryWrapper = QueryGenerator.initQueryWrapper(contractManagement, req.getParameterMap());
		 List<ContractManagementListVo> voList = contractManagementService.selectContractManagementVoList(queryWrapper);
		 IPage<ContractManagementListVo> result = new Page<>(pageNo, pageSize, voList.size());
		 result.setRecords(voList);
		 return Result.OK(result);
	 }

	 @ApiOperation(value="合同管理-操作日志查询", notes="合同管理-操作日志查询")
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

	 @ApiOperation(value="合同管理-根据项目ID查询", notes="合同管理-根据项目ID查询")
	 @GetMapping(value = "/listByProjectId")
	 public Result<List<ContractManagementVo>> queryContractManagementByMainId(
			 @RequestParam(name="projectFkId",required=true) String projectId,
			 HttpServletRequest req) {
		 ContractManagement contractManagement = new ContractManagement();
		 QueryWrapper<ContractManagement> queryWrapper = QueryGenerator.initQueryWrapper(contractManagement, req.getParameterMap());
		 queryWrapper.eq("project_fk_id", projectId);
		 ArrayList<ContractManagementVo> contractManagementVos = new ArrayList<>();
		 for (ContractManagement cm : contractManagementService.list(queryWrapper)) {
			 ContractManagementVo vo = new ContractManagementVo();
			 vo.setContractIndex(cm.getContractIndex());
			 vo.setContractName(cm.getContractName());
			 vo.setSupplierName(cm.getSupplierName());
			 vo.setContractPaymentTermList(queryContractPaymentTermListByMainId(cm.getId()).getResult());
			 vo.setContractPaymentRecvList(queryContractPaymentRecvListByMainId(cm.getId()).getResult());
			 contractManagementVos.add(vo);
		 }
		 return Result.OK(contractManagementVos);
	 }

	 /**
	  *   添加
	  *
	  * @param contractManagementPage
	  * @return
	  */
	 @AutoLog(value = "合同管理-添加")
	 @ApiOperation(value="合同管理-添加", notes="合同管理-添加")
     @RequiresPermissions("contractManagement:contract_management:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody ContractManagementPage contractManagementPage) {
		 ContractManagement contractManagement = new ContractManagement();
		 BeanUtils.copyProperties(contractManagementPage, contractManagement);
		 try {
			 contractManagementService.saveMain(contractManagement, contractManagementPage.getContractPaymentTermList(), contractManagementPage.getContractPaymentRecvList());
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 return Result.error("添加失败: " + e.getMessage());
		 }
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param contractManagementPage
	  * @return
	  */
	 @AutoLog(value = "合同管理-编辑")
	 @ApiOperation(value="合同管理-编辑", notes="合同管理-编辑")
    @RequiresPermissions("contractManagement:contract_management:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody ContractManagementPage contractManagementPage) {
		 ContractManagement contractManagement = new ContractManagement();
		 BeanUtils.copyProperties(contractManagementPage, contractManagement);
		 ContractManagement contractManagementEntity = contractManagementService.getById(contractManagement.getId());
		 if(contractManagementEntity==null) {
			 return Result.error("未找到对应数据");
		 }
		 contractManagementService.updateMain(contractManagement, contractManagementPage.getContractPaymentTermList(),contractManagementPage.getContractPaymentRecvList());
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "合同管理-通过id删除")
	 @ApiOperation(value="合同管理-通过id删除", notes="合同管理-通过id删除")
    @RequiresPermissions("contractManagement:contract_management:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 contractManagementService.delMain(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "合同管理-批量删除")
	 @ApiOperation(value="合同管理-批量删除", notes="合同管理-批量删除")
    @RequiresPermissions("contractManagement:contract_management:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.contractManagementService.delBatchMain(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功！");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "合同管理-通过id查询")
	 @ApiOperation(value="合同管理-通过id查询", notes="合同管理-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<ContractManagement> queryById(@RequestParam(name="id",required=true) String id) {
		 ContractManagement contractManagement = contractManagementService.getById(id);
		 if(contractManagement==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(contractManagement);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "合同应付通过主表ID查询")
	 @ApiOperation(value="合同应付主表ID查询", notes="合同应付-通主表ID查询")
	 @GetMapping(value = "/queryContractPaymentTermByMainId")
	 public Result<List<ContractPaymentTerm>> queryContractPaymentTermListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<ContractPaymentTerm> contractPaymentTermList = contractPaymentTermService.selectByMainId(id);
		 return Result.OK(contractPaymentTermList);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "付款管理通过主表ID查询")
	 @ApiOperation(value="付款管理主表ID查询", notes="付款管理-通主表ID查询")
	 @GetMapping(value = "/queryContractPaymentRecvByMainId")
	 public Result<List<ContractPaymentRecv>> queryContractPaymentRecvListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<ContractPaymentRecv> contractPaymentRecvList = contractPaymentRecvService.selectByMainId(id);
		 return Result.OK(contractPaymentRecvList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param contractManagement
    */
    @RequiresPermissions("contractManagement:contract_management:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractManagement contractManagement) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ContractManagement> queryWrapper = QueryGenerator.initQueryWrapper(contractManagement, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<ContractManagement> contractManagementList = contractManagementService.list(queryWrapper);

      // Step.3 组装pageList
      List<ContractManagementPage> pageList = new ArrayList<ContractManagementPage>();
      for (ContractManagement main : contractManagementList) {
          ContractManagementPage vo = new ContractManagementPage();
          BeanUtils.copyProperties(main, vo);
          List<ContractPaymentTerm> contractPaymentTermList = contractPaymentTermService.selectByMainId(main.getId());
          vo.setContractPaymentTermList(contractPaymentTermList);
          List<ContractPaymentRecv> contractPaymentRecvList = contractPaymentRecvService.selectByMainId(main.getId());
          vo.setContractPaymentRecvList(contractPaymentRecvList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "合同管理列表");
      mv.addObject(NormalExcelConstants.CLASS, ContractManagementPage.class);
		ExportParams params = new ExportParams("合同管理数据", "导出人:" + sysUser.getRealname(), "合同管理");
		params.setType(ExcelType.XSSF);
		mv.addObject(NormalExcelConstants.PARAMS, params);
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

	 @RequestMapping(value = "/downTemplate")
	 public ModelAndView downTemplate() {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "合同管理列表");
		 mv.addObject(NormalExcelConstants.CLASS, ContractManagementPage.class);
		 ExportParams exportParams = new ExportParams("合同管理数据", "导入人:" + sysUser.getRealname(), "合同管理");
		 exportParams.setType(ExcelType.XSSF);
		 exportParams.setExclusions(new String[]{"序号","项目id外键","是否删除","付款流水号"});
		 mv.addObject(NormalExcelConstants.PARAMS, exportParams);

		 List<ContractManagementPage> pageList = new ArrayList<>();
		 ContractManagementPage contractManagementPage = new ContractManagementPage();
		 contractManagementPage.setContractPaymentTermList(new ArrayList<>());
		 contractManagementPage.setContractPaymentRecvList(new ArrayList<>());
		 pageList.add(contractManagementPage);

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
    @RequiresPermissions("contractManagement:contract_management:importExcel")
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
              List<ContractManagementPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ContractManagementPage.class, params);
			  List<ContractManagement> contractManagementList = new ArrayList<>();
			  List<List<ContractPaymentTerm>> contractPaymentTermList = new ArrayList<>();
			  List<List<ContractPaymentRecv>> contractPaymentRecvList = new ArrayList<>();
			  for (ContractManagementPage page : list) {
				  page.setProjectFkId(projectManagementMapper.selectIdByProjectIndex(page.getProjectIndex()));
                  ContractManagement po = new ContractManagement();
                  BeanUtils.copyProperties(page, po);
				  if (projectManagementMapper.selectById(po.getProjectFkId()) == null) {
					  throw new RuntimeException("部分记录外键不存在，请检查导入Excel表");
				  }
				  contractManagementList.add(po);
				  contractPaymentTermList.add(page.getContractPaymentTermList());
				  contractPaymentRecvList.add(page.getContractPaymentRecvList());
//                  contractManagementService.saveMain(po, page.getContractPaymentTermList(),page.getContractPaymentRecvList());
              }
			  contractManagementService.saveBatch(contractManagementList, contractPaymentTermList, contractPaymentRecvList);
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
