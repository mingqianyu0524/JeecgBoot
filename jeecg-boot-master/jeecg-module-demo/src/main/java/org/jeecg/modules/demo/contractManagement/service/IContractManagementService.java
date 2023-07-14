package org.jeecg.modules.demo.contractManagement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.contractManagement.vo.ContractManagementListVo;
import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IContractManagementService extends IService<ContractManagement> {

	/**
	 * 添加一对多
	 *
	 * @param contractManagement
	 * @param contractPaymentTermList
	 * @param contractPaymentRecvList
	 */
	public void saveMain(ContractManagement contractManagement,List<ContractPaymentTerm> contractPaymentTermList,List<ContractPaymentRecv> contractPaymentRecvList) throws Exception;

	public void saveBatch(List<ContractManagement> contractManagementList, List<List<ContractPaymentTerm>> contractPaymentTermList, List<List<ContractPaymentRecv>> contractPaymentRecvList) throws Exception;

	/**
	 * 修改一对多
	 *
   * @param contractManagement
   * @param contractPaymentTermList
   * @param contractPaymentRecvList
	 */
	public void updateMain(ContractManagement contractManagement,List<ContractPaymentTerm> contractPaymentTermList,List<ContractPaymentRecv> contractPaymentRecvList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	public List<ContractManagementListVo> selectContractManagementVoList(QueryWrapper<ContractManagement> queryWrapper);
}
