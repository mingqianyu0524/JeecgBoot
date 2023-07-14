package org.jeecg.modules.demo.contractManagement.service.impl;

import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import org.jeecg.modules.demo.contractManagement.mapper.ContractPaymentRecvMapper;
import org.jeecg.modules.demo.contractManagement.service.IContractPaymentRecvService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class ContractPaymentRecvServiceImpl extends ServiceImpl<ContractPaymentRecvMapper, ContractPaymentRecv> implements IContractPaymentRecvService {
	
	@Autowired
	private ContractPaymentRecvMapper contractPaymentRecvMapper;
	
	@Override
	public List<ContractPaymentRecv> selectByMainId(String mainId) {
		return contractPaymentRecvMapper.selectByMainId(mainId);
	}
}
