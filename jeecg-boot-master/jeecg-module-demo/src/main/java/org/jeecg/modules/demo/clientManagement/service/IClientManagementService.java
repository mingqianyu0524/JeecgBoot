package org.jeecg.modules.demo.clientManagement.service;

import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import org.jeecg.modules.demo.clientManagement.entity.ClientManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 客户管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface IClientManagementService extends IService<ClientManagement> {

	/**
	 * 添加一对多
	 *
	 * @param clientManagement
	 * @param clientContactList
	 */
	public void saveMain(ClientManagement clientManagement,List<ClientContact> clientContactList) throws Exception;

	public void saveBatch(List<ClientManagement> mainList, List<List<ClientContact>> subList) throws Exception;

	/**
	 * 修改一对多
	 *
   * @param clientManagement
   * @param clientContactList
	 */
	public void updateMain(ClientManagement clientManagement,List<ClientContact> clientContactList);
	
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
	
}
