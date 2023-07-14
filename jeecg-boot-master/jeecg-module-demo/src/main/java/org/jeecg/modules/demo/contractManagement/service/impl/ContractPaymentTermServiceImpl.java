package org.jeecg.modules.demo.contractManagement.service.impl;

import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import org.jeecg.modules.demo.contractManagement.mapper.ContractPaymentTermMapper;
import org.jeecg.modules.demo.contractManagement.service.IContractPaymentTermService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 合同应付
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class ContractPaymentTermServiceImpl extends ServiceImpl<ContractPaymentTermMapper, ContractPaymentTerm> implements IContractPaymentTermService {
	
	@Autowired
	private ContractPaymentTermMapper contractPaymentTermMapper;
	
	@Override
	public List<ContractPaymentTerm> selectByMainId(String mainId) {
		return contractPaymentTermMapper.selectByMainId(mainId);
	}
}
