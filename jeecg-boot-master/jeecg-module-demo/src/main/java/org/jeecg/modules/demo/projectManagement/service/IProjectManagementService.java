package org.jeecg.modules.demo.projectManagement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentRecv;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface IProjectManagementService extends IService<ProjectManagement> {

	/**
	 * 添加一对多
	 *
	 * @param projectManagement
	 * @param projectPaymentTermList
	 * @param projectPaymentRecvList
	 */
	public void saveMain(ProjectManagement projectManagement,List<ProjectPaymentTerm> projectPaymentTermList,List<ProjectPaymentRecv> projectPaymentRecvList) throws Exception;

	void saveBatch(List<ProjectManagement> entityList, List<List<ProjectPaymentTerm>> termList, List<List<ProjectPaymentRecv>> recvList) throws Exception;

	/**
	 * 修改一对多
	 *
   * @param projectManagement
   * @param projectPaymentTermList
   * @param projectPaymentRecvList
	 */
	public void updateMain(ProjectManagement projectManagement,List<ProjectPaymentTerm> projectPaymentTermList,List<ProjectPaymentRecv> projectPaymentRecvList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	public List<ProjectManagementVo> selectProjectManagementVoList(QueryWrapper<ProjectManagement> queryWrapper);
}
