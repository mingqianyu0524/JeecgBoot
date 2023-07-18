package org.jeecg.modules.demo.contractPaymentRecv.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.demo.contractPaymentRecv.entity.ContractPaymentRecv_;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.contractPaymentRecv.vo.ContractPaymentRecvVo;

import java.util.List;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IContractPaymentRecv_Service extends IService<ContractPaymentRecv_> {
    @Override
    default boolean save(ContractPaymentRecv_ entity) {
        entity.setPaymentSerialNumber(generateSerialNumber(entity.getContractFkId()));
        return IService.super.save(entity);
    }

    List<ContractPaymentRecvVo> getVoList(QueryWrapper<ContractPaymentRecv_> queryWrapper,
                                          List<String> contractNameList,
                                          List<String> projectNameList,
                                          List<String> projectIndexList);
}
