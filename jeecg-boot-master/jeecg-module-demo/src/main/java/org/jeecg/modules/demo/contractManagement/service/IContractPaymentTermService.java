package org.jeecg.modules.demo.contractManagement.service;

import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 合同应付
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IContractPaymentTermService extends IService<ContractPaymentTerm> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ContractPaymentTerm>
	 */
	public List<ContractPaymentTerm> selectByMainId(String mainId);
}
