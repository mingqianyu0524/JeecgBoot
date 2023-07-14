package org.jeecg.modules.demo.contractManagement.mapper;

import java.util.List;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 合同应付
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ContractPaymentTermMapper extends BaseMapper<ContractPaymentTerm> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<ContractPaymentTerm>
   */
	public List<ContractPaymentTerm> selectByMainId(@Param("mainId") String mainId);
}
