package org.jeecg.modules.demo.clientManagement.mapper;

import java.util.List;
import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 客户联系人
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ClientContactMapper extends BaseMapper<ClientContact> {

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
   * @return List<ClientContact>
   */
	public List<ClientContact> selectByMainId(@Param("mainId") String mainId);
}
