package org.jeecg.modules.demo.contractPaymentRecv.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.contractPaymentRecv.entity.ContractPaymentRecv_;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.demo.contractPaymentRecv.vo.ContractPaymentRecvVo;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ContractPaymentRecv_Mapper extends BaseMapper<ContractPaymentRecv_> {
    public List<ContractPaymentRecvVo> getVoList(@Param("contractName") String contractName,
                                                 @Param("contractIndex") String contractIndex,
                                                 @Param("projectName") String projectName,
                                                 @Param("projectIndex") String projectIndex
    );
}
