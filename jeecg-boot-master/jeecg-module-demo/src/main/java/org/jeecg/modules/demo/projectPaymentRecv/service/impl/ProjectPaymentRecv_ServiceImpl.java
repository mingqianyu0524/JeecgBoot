package org.jeecg.modules.demo.projectPaymentRecv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectManagementMapper;
import org.jeecg.modules.demo.projectPaymentRecv.entity.ProjectPaymentRecv_;
import org.jeecg.modules.demo.projectPaymentRecv.mapper.ProjectPaymentRecv_Mapper;
import org.jeecg.modules.demo.projectPaymentRecv.service.IProjectPaymentRecv_Service;
import org.jeecg.modules.demo.projectPaymentRecv.vo.ProjectPaymentRecvVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jeecg.modules.util.SerialNumberUtil.generateSerialNumber;

/**
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class ProjectPaymentRecv_ServiceImpl extends ServiceImpl<ProjectPaymentRecv_Mapper, ProjectPaymentRecv_> implements IProjectPaymentRecv_Service {
    @Autowired
    ProjectPaymentRecv_Mapper projectPaymentRecv_mapper;
    @Autowired
    ProjectManagementMapper projectManagementMapper;

    @Override
    public boolean saveBatch(Collection<ProjectPaymentRecv_> entityList, int batchSize) {
        for (ProjectPaymentRecv_ entity : entityList) {
            String projectFkId = entity.getProjectFkId();
            ProjectManagement projectManagement = projectManagementMapper.selectById(projectFkId);
            if (projectManagement != null) {
                entity.setPaymentSerialNumber(generateSerialNumber(entity.getProjectFkId()));
            } else {
                throw new RuntimeException("部分记录外键不存在，请检查导入Excel表");
            }
        }
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public List<ProjectPaymentRecvVo> getVoList(QueryWrapper<ProjectPaymentRecv_> queryWrapper,
                                                String projectIndex, String projectName, String clientName) {
        List<ProjectPaymentRecvVo> projectPaymentRecvVoList = projectPaymentRecv_mapper.getVoList(projectIndex, projectName, clientName);
        List<ProjectPaymentRecv_> projectPaymentRecvList = projectPaymentRecv_mapper.selectList(queryWrapper);
        // 按照id筛选查询结果
        Set<String> IdsFromList = projectPaymentRecvList.stream()
                .map(ProjectPaymentRecv_::getId)
                .collect(Collectors.toSet());
        projectPaymentRecvVoList = projectPaymentRecvVoList.stream()
                .filter(ProjectPaymentRecvVo -> IdsFromList.contains(ProjectPaymentRecvVo.getId()))
                .collect(Collectors.toList());
        return projectPaymentRecvVoList;
    }
}
