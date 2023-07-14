package org.jeecg.modules.demo.contractManagement.service;

import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IContractPaymentRecvService extends IService<ContractPaymentRecv> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ContractPaymentRecv>
	 */
	public List<ContractPaymentRecv> selectByMainId(String mainId);
}
