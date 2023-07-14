package org.jeecg.modules.demo.contractManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import org.jeecg.modules.demo.contractManagement.mapper.ContractPaymentTermMapper;
import org.jeecg.modules.demo.contractManagement.mapper.ContractPaymentRecvMapper;
import org.jeecg.modules.demo.contractManagement.mapper.ContractManagementMapper;
import org.jeecg.modules.demo.contractManagement.service.IContractManagementService;
import org.jeecg.modules.demo.contractManagement.vo.ContractManagementListVo;
import org.jeecg.modules.demo.contractManagement.vo.ContractPaymentRecvVo;
import org.jeecg.modules.demo.contractManagement.vo.ContractPaymentTermVo;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectManagementMapper;
import org.jeecg.modules.util.IUpdateMainUtil;
import org.jeecg.modules.util.Impl.UpdateMainUtilImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class ContractManagementServiceImpl extends ServiceImpl<ContractManagementMapper, ContractManagement> implements IContractManagementService {

	@Autowired
	private ContractManagementMapper contractManagementMapper;
	@Autowired
	private ContractPaymentTermMapper contractPaymentTermMapper;
	@Autowired
	private ContractPaymentRecvMapper contractPaymentRecvMapper;
	@Autowired
	private ProjectManagementMapper projectManagementMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ContractManagement contractManagement, List<ContractPaymentTerm> contractPaymentTermList,List<ContractPaymentRecv> contractPaymentRecvList) throws Exception {
		if (contractManagement.getDeptName() == null || contractManagement.getContractIndex() == null) {
			throw new Exception("Excel中缺少必填字段");
		}
		contractManagementMapper.insert(contractManagement);
		if(contractPaymentTermList!=null && contractPaymentTermList.size()>0) {
			for(ContractPaymentTerm entity:contractPaymentTermList) {
				//外键设置
				entity.setContractFkId(contractManagement.getId());
				contractPaymentTermMapper.insert(entity);
			}
		}
		if(contractPaymentRecvList!=null && contractPaymentRecvList.size()>0) {
			for(ContractPaymentRecv entity:contractPaymentRecvList) {
				//外键设置
				entity.setContractFkId(contractManagement.getId());
				entity.setPaymentSerialNumber(generateSerialNumber(entity.getContractFkId()));
				contractPaymentRecvMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveBatch(List<ContractManagement> contractManagementList, List<List<ContractPaymentTerm>> contractPaymentTermList, List<List<ContractPaymentRecv>> contractPaymentRecvList) throws Exception {
		for (int i = 0; i < contractManagementList.size(); i++) {
			if (i > contractPaymentTermList.size() || i > contractPaymentRecvList.size()) throw new Exception("列表长度不匹配");
			saveMain(contractManagementList.get(i), contractPaymentTermList.get(i), contractPaymentRecvList.get(i));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(ContractManagement contractManagement,List<ContractPaymentTerm> contractPaymentTermList,List<ContractPaymentRecv> contractPaymentRecvList) {
		contractManagementMapper.updateById(contractManagement);
		for (ContractPaymentRecv contractPaymentRecv : contractPaymentRecvList) {
			contractPaymentRecv.setContractFkId(contractManagement.getId());
			if (contractPaymentRecv.getPaymentSerialNumber() == null || contractPaymentRecv.getPaymentSerialNumber().isEmpty()) {
				contractPaymentRecv.setPaymentSerialNumber(generateSerialNumber(contractManagement.getId()));
			}
		}
		List<ContractPaymentTerm> originalContractPaymentTermList = contractPaymentTermMapper.selectByMainId(contractManagement.getId());
		List<ContractPaymentRecv> originalContractPaymentRecvList = contractPaymentRecvMapper.selectByMainId(contractManagement.getId());
		try {
			IUpdateMainUtil<ContractManagement, ContractPaymentTermMapper, ContractPaymentTerm> updateMainUtil1 = new UpdateMainUtilImpl<>();
			updateMainUtil1.updateMain(contractManagement, contractPaymentTermMapper, originalContractPaymentTermList, contractPaymentTermList, ContractPaymentTerm.class);
			IUpdateMainUtil<ContractManagement, ContractPaymentRecvMapper, ContractPaymentRecv> updateMainUtil2 = new UpdateMainUtilImpl<>();
			updateMainUtil2.updateMain(contractManagement, contractPaymentRecvMapper, originalContractPaymentRecvList, contractPaymentRecvList, ContractPaymentRecv.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		contractPaymentTermMapper.deleteByMainId(id);
		contractPaymentRecvMapper.deleteByMainId(id);
		contractManagementMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			contractPaymentTermMapper.deleteByMainId(id.toString());
			contractPaymentRecvMapper.deleteByMainId(id.toString());
			contractManagementMapper.deleteById(id);
		}
	}

	@Override
	public boolean saveBatch(Collection<ContractManagement> entityList) {
		for (ContractManagement entity : entityList) {
			String projectFkId = entity.getProjectFkId();
			ProjectManagement projectManagement = projectManagementMapper.selectById(projectFkId);
			if (projectManagement == null) {
				throw new RuntimeException("部分记录外键不存在，请检查导入Excel表");
			}
		}
		return super.saveBatch(entityList);
	}

	@Override
	public List<ContractManagementListVo> selectContractManagementVoList(QueryWrapper<ContractManagement> queryWrapper) {
		List<ContractManagementListVo> contractManagementVoList = contractManagementMapper.selectContractManagementVoList();
		List<ContractManagement> contractManagementList = contractManagementMapper.selectList(queryWrapper);
		// 按照id筛选查询结果
		Set<String> IdsFromList = contractManagementList.stream()
				.map(ContractManagement::getId)
				.collect(Collectors.toSet());
		contractManagementVoList = contractManagementVoList.stream()
				.filter(ContractManagementListVo -> IdsFromList.contains(ContractManagementListVo.getId()))
				.collect(Collectors.toList());
		for (ContractManagementListVo cmv : contractManagementVoList) {
			// 设置合同周期
			ContractManagement cm = contractManagementMapper.selectById(cmv.getId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (cm.getContractStartDate() != null && cm.getContractEndDate() != null) {
				String startDate = sdf.format(cm.getContractStartDate());
				String endDate = sdf.format(cm.getContractEndDate());
				cmv.setContractPeriod(startDate + " 至 " + endDate);
			}
			// 设置金额
			BigDecimal amount = new BigDecimal(0);
			for (ContractPaymentTermVo cptv : cmv.getContractPaymentTermVos()) {
				amount = amount.add(BigDecimal.valueOf(cptv.getPaymentAmount()));
			}
			double totalAmount = amount.doubleValue();
			cmv.setTotalAmount(totalAmount);
			for (ContractPaymentRecvVo cprv : cmv.getContractPaymentRecvVos()) {
				amount = amount.subtract(BigDecimal.valueOf(cprv.getPaymentAmount()));
			}
			double unpaidAmount = amount.doubleValue();
			cmv.setUnpaidAmount(unpaidAmount);
		}
		return contractManagementVoList;
	}
}
