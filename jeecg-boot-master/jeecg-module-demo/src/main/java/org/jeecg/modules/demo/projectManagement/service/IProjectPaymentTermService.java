package org.jeecg.modules.demo.projectManagement.service;

import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 项目应收
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface IProjectPaymentTermService extends IService<ProjectPaymentTerm> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ProjectPaymentTerm>
	 */
	public List<ProjectPaymentTerm> selectByMainId(String mainId);
}
