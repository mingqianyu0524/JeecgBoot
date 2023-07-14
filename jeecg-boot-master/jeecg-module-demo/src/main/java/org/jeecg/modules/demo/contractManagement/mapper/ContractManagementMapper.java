package org.jeecg.modules.demo.contractManagement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.demo.contractManagement.vo.ContractManagementListVo;

/**
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ContractManagementMapper extends BaseMapper<ContractManagement> {
    public List<ContractManagementListVo> selectContractManagementVoList();

    @Select("SELECT * from contract_management WHERE contract_index = #{contractIndex} AND is_delete = 0 LIMIT 1")
    public ContractManagement selectByContractIndex(@Param("contractIndex") String contractIndex);
}
