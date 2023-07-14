package org.jeecg.modules.demo.contractPaymentRecv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.demo.contractManagement.entity.ContractManagement;
import org.jeecg.modules.demo.contractManagement.mapper.ContractManagementMapper;
import org.jeecg.modules.demo.contractPaymentRecv.entity.ContractPaymentRecv_;
import org.jeecg.modules.demo.contractPaymentRecv.mapper.ContractPaymentRecv_Mapper;
import org.jeecg.modules.demo.contractPaymentRecv.vo.ContractPaymentRecvVo;
import org.jeecg.modules.demo.contractPaymentRecv.service.IContractPaymentRecv_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 付款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
@Slf4j
public class ContractPaymentRecv_ServiceImpl extends ServiceImpl<ContractPaymentRecv_Mapper, ContractPaymentRecv_> implements IContractPaymentRecv_Service {
    @Autowired
    ContractPaymentRecv_Mapper contractPaymentRecv_mapper;
    @Autowired
    ContractManagementMapper contractManagementMapper;

    @Override
    public boolean save(ContractPaymentRecv_ entity) {
        entity.setPaymentSerialNumber(generateSerialNumber(entity.getContractFkId()));
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<ContractPaymentRecv_> entityList, int batchSize) {
        for (ContractPaymentRecv_ entity : entityList) {
            String contractFkId = entity.getContractFkId();
            ContractManagement contractManagement = contractManagementMapper.selectById(contractFkId);
            if (contractManagement != null) {
                entity.setPaymentSerialNumber(generateSerialNumber(entity.getContractFkId()));
            } else {
                throw new RuntimeException("部分记录外键不存在，请检查导入Excel表");
            }
        }
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean updateById(ContractPaymentRecv_ entity) {
        if (entity.getPaymentSerialNumber() == null || entity.getPaymentSerialNumber().isEmpty()) {
            entity.setPaymentSerialNumber(generateSerialNumber(entity.getContractFkId()));
        }
        return super.updateById(entity);
    }

    @Override
    public List<ContractPaymentRecvVo> getVoList(QueryWrapper<ContractPaymentRecv_> queryWrapper,
                                                 String contractName,
                                                 String contractIndex,
                                                 String projectName,
                                                 String projectIndex) {
        List<ContractPaymentRecvVo> contractPaymentRecvVoList = contractPaymentRecv_mapper.getVoList(contractName, contractIndex, projectName, projectIndex);
        List<ContractPaymentRecv_> contractPaymentRecvList = contractPaymentRecv_mapper.selectList(queryWrapper);
        // 按照id筛选查询结果
        Set<String> IdsFromList = contractPaymentRecvList.stream()
                .map(ContractPaymentRecv_::getId)
                .collect(Collectors.toSet());
        contractPaymentRecvVoList = contractPaymentRecvVoList.stream()
                .filter(ProjectPaymentRecvVo -> IdsFromList.contains(ProjectPaymentRecvVo.getId()))
                .collect(Collectors.toList());
        return contractPaymentRecvVoList;
    }
}
