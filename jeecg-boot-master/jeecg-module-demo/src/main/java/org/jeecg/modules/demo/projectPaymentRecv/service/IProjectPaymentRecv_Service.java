package org.jeecg.modules.demo.projectPaymentRecv.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.demo.projectPaymentRecv.entity.ProjectPaymentRecv_;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.projectPaymentRecv.vo.ProjectPaymentRecvVo;

import java.util.Collection;
import java.util.List;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IProjectPaymentRecv_Service extends IService<ProjectPaymentRecv_> {
    @Override
    default boolean save(ProjectPaymentRecv_ entity) {
        entity.setPaymentSerialNumber(generateSerialNumber(entity.getProjectFkId()));
        return IService.super.save(entity);
    }

    @Override
    default boolean updateById(ProjectPaymentRecv_ entity) {
        if (entity.getPaymentSerialNumber() == null || entity.getPaymentSerialNumber().isEmpty()) {
            entity.setPaymentSerialNumber(generateSerialNumber(entity.getProjectFkId()));
        }
        return IService.super.updateById(entity);
    }

    List<ProjectPaymentRecvVo> getVoList(QueryWrapper<ProjectPaymentRecv_> queryWrapper,
                                         String projectIndex,
                                         String projectName,
                                         String clientName);
}
