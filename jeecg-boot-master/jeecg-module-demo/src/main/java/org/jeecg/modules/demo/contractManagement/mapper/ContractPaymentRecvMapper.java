package org.jeecg.modules.demo.contractManagement.mapper;

import java.util.List;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ContractPaymentRecvMapper extends BaseMapper<ContractPaymentRecv> {

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
   * @return List<ContractPaymentRecv>
   */
	public List<ContractPaymentRecv> selectByMainId(@Param("mainId") String mainId);
}
