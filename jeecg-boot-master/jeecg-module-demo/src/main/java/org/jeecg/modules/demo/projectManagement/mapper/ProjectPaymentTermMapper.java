package org.jeecg.modules.demo.projectManagement.mapper;

import java.util.List;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 项目应收
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface ProjectPaymentTermMapper extends BaseMapper<ProjectPaymentTerm> {

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
   * @return List<ProjectPaymentTerm>
   */
	public List<ProjectPaymentTerm> selectByMainId(@Param("mainId") String mainId);
}
