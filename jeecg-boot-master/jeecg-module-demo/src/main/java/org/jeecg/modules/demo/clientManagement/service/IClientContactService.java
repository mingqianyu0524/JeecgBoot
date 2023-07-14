package org.jeecg.modules.demo.clientManagement.service;

import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 客户联系人
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IClientContactService extends IService<ClientContact> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ClientContact>
	 */
	public List<ClientContact> selectByMainId(String mainId);
}
