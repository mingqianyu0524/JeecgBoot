package org.jeecg.modules.demo.projectManagement.service.impl;

import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectPaymentTermMapper;
import org.jeecg.modules.demo.projectManagement.service.IProjectPaymentTermService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 项目应收
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
@Service
public class ProjectPaymentTermServiceImpl extends ServiceImpl<ProjectPaymentTermMapper, ProjectPaymentTerm> implements IProjectPaymentTermService {
	
	@Autowired
	private ProjectPaymentTermMapper projectPaymentTermMapper;
	
	@Override
	public List<ProjectPaymentTerm> selectByMainId(String mainId) {
		return projectPaymentTermMapper.selectByMainId(mainId);
	}
}
