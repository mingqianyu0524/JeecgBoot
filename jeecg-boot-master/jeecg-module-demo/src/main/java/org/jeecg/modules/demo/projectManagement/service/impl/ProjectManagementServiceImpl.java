package org.jeecg.modules.demo.projectManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentRecv;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectPaymentTermMapper;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectPaymentRecvMapper;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectManagementMapper;
import org.jeecg.modules.demo.projectManagement.service.IProjectManagementService;
import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementVo;
import org.jeecg.modules.demo.projectManagement.vo.ProjectPaymentRecvVo;
import org.jeecg.modules.demo.projectManagement.vo.ProjectPaymentTermVo;
import org.jeecg.modules.util.IUpdateMainUtil;
import org.jeecg.modules.util.Impl.UpdateMainUtilImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Service
public class ProjectManagementServiceImpl extends ServiceImpl<ProjectManagementMapper, ProjectManagement> implements IProjectManagementService {

	@Autowired
	private ProjectManagementMapper projectManagementMapper;
	@Autowired
	private ProjectPaymentTermMapper projectPaymentTermMapper;
	@Autowired
	private ProjectPaymentRecvMapper projectPaymentRecvMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ProjectManagement projectManagement, List<ProjectPaymentTerm> projectPaymentTermList,List<ProjectPaymentRecv> projectPaymentRecvList) throws Exception {
		if (projectManagement.getProjectYear() == null || projectManagement.getProjectIndex() == null
				|| projectManagement.getProjectName() == null || projectManagement.getProjectCategory() == null
				|| projectManagement.getProjectStatus() == null || projectManagement.getClientName() == null) {
			throw new Exception("导入Excel缺少必填字段");
		}
		projectManagementMapper.insert(projectManagement);
		if(projectPaymentTermList!=null && projectPaymentTermList.size()>0) {
			for(ProjectPaymentTerm entity:projectPaymentTermList) {
				//外键设置
				entity.setProjectFkId(projectManagement.getId());
				projectPaymentTermMapper.insert(entity);
			}
		}
		if(projectPaymentRecvList!=null && projectPaymentRecvList.size()>0) {
			for(ProjectPaymentRecv entity:projectPaymentRecvList) {
				//外键设置
				if (entity.getPaymentSerialNumber() == null || entity.getPaymentSerialNumber().isEmpty()) {
					entity.setPaymentSerialNumber(generateSerialNumber(projectManagement.getId()));
				}
				entity.setProjectFkId(projectManagement.getId());
				projectPaymentRecvMapper.insert(entity);
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveBatch(List<ProjectManagement> entityList,List<List<ProjectPaymentTerm>> termList, List<List<ProjectPaymentRecv>> recvList) throws Exception {
		for (int i = 0; i < entityList.size(); i++) {
			if (i > termList.size() || i > recvList.size()) throw new Exception("列表长度不匹配");
			saveMain(entityList.get(i), termList.get(i), recvList.get(i));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(ProjectManagement projectManagement,List<ProjectPaymentTerm> projectPaymentTermList,List<ProjectPaymentRecv> projectPaymentRecvList) {
		projectManagementMapper.updateById(projectManagement);
		// 更新收款流水号
		for (ProjectPaymentRecv entity : projectPaymentRecvList) {
			if (entity.getPaymentSerialNumber() == null || entity.getPaymentSerialNumber().isEmpty()) {
				entity.setPaymentSerialNumber(generateSerialNumber(projectManagement.getId()));
			}
		}
		List<ProjectPaymentTerm> originalProjectPaymentTermList = projectPaymentTermMapper.selectByMainId(projectManagement.getId());
		List<ProjectPaymentRecv> originalProjectPaymentRecvList = projectPaymentRecvMapper.selectByMainId(projectManagement.getId());
		try {
			IUpdateMainUtil<ProjectManagement, ProjectPaymentTermMapper, ProjectPaymentTerm> updateMainUtil1 = new UpdateMainUtilImpl<>();
			updateMainUtil1.updateMain(projectManagement, projectPaymentTermMapper, originalProjectPaymentTermList, projectPaymentTermList, ProjectPaymentTerm.class);
			IUpdateMainUtil<ProjectManagement, ProjectPaymentRecvMapper, ProjectPaymentRecv> updateMainUtil2 = new UpdateMainUtilImpl<>();
			updateMainUtil2.updateMain(projectManagement, projectPaymentRecvMapper, originalProjectPaymentRecvList, projectPaymentRecvList, ProjectPaymentRecv.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		projectPaymentTermMapper.deleteByMainId(id);
		projectPaymentRecvMapper.deleteByMainId(id);
		projectManagementMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			projectPaymentTermMapper.deleteByMainId(id.toString());
			projectPaymentRecvMapper.deleteByMainId(id.toString());
			projectManagementMapper.deleteById(id);
		}
	}

	public List<ProjectManagementVo> selectProjectManagementVoList(QueryWrapper<ProjectManagement> queryWrapper) {
		List<ProjectManagementVo> projectManagementVoList = projectManagementMapper.selectProjectManagementVoList();
		List<ProjectManagement> projectManagementList = projectManagementMapper.selectList(queryWrapper);
		// 按照id筛选查询结果
		Set<String> IdsFromList = projectManagementList.stream()
				.map(ProjectManagement::getId)
				.collect(Collectors.toSet());
		projectManagementVoList = projectManagementVoList.stream()
				.filter(ProjectManagementVo -> IdsFromList.contains(ProjectManagementVo.getId()))
				.collect(Collectors.toList());
		for (ProjectManagementVo pmv : projectManagementVoList) {
			// 计算合同周期
			ProjectManagement pm = projectManagementMapper.selectById(pmv.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (pm.getContractStartDate() != null) {
				String startDate = sdf.format(pm.getContractStartDate());
				if (pm.getContractEndDate() != null) {
					String endDate = sdf.format(pm.getContractEndDate());
					pmv.setContractPeriod(startDate + " 至 " + endDate);
				}
//				else {
//					pmv.setContractPeriod(startDate + " 至今");
//				}
			}
			// 计算合同金额
			BigDecimal amount = new BigDecimal(0);
			for (ProjectPaymentTermVo pptv : pmv.getProjectPaymentTermVos()) {
				amount = amount.add(BigDecimal.valueOf(pptv.getPaymentAmount()));
			}
			double totalAmount = amount.doubleValue();
			pmv.setTotalAmount(totalAmount);
			for (ProjectPaymentRecvVo ppr : pmv.getProjectPaymentRecvVos()) {
				amount = amount.subtract(BigDecimal.valueOf(ppr.getPaymentAmount()));
			}
			double unpaidAmount = amount.doubleValue();
			pmv.setUnpaidAmount(unpaidAmount);
		}
		return projectManagementVoList;
	}
}
