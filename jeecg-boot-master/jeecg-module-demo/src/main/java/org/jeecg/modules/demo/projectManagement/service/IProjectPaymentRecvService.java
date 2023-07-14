package org.jeecg.modules.demo.projectManagement.service;

import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentRecv;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface IProjectPaymentRecvService extends IService<ProjectPaymentRecv> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ProjectPaymentRecv>
	 */
	public List<ProjectPaymentRecv> selectByMainId(String mainId);
}
