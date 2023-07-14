package org.jeecg.modules.demo.projectManagement.service.impl;

import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentRecv;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectPaymentRecvMapper;
import org.jeecg.modules.demo.projectManagement.service.IProjectPaymentRecvService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Service
public class ProjectPaymentRecvServiceImpl extends ServiceImpl<ProjectPaymentRecvMapper, ProjectPaymentRecv> implements IProjectPaymentRecvService {
	
	@Autowired
	private ProjectPaymentRecvMapper projectPaymentRecvMapper;
	
	@Override
	public List<ProjectPaymentRecv> selectByMainId(String mainId) {
		return projectPaymentRecvMapper.selectByMainId(mainId);
	}
}
