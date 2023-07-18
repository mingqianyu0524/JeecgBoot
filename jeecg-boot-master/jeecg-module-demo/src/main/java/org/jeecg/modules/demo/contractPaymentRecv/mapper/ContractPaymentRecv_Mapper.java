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
    public List<ContractPaymentRecvVo> getVoList(@Param("contractNameList") List<String> contractNameList,
                                                 @Param("projectNameList") List<String> projectNameList,
                                                 @Param("projectIndexList") List<String> projectIndexList
    );
}
