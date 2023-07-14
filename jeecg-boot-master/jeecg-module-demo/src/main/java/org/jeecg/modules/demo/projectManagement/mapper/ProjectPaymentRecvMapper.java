package org.jeecg.modules.demo.projectManagement.mapper;

import java.util.List;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentRecv;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface ProjectPaymentRecvMapper extends BaseMapper<ProjectPaymentRecv> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<ProjectPaymentRecv>
   */
	public List<ProjectPaymentRecv> selectByMainId(@Param("mainId") String mainId);
}
