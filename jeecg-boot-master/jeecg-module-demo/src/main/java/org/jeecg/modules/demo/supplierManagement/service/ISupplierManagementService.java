package org.jeecg.modules.demo.supplierManagement.service;

import org.jeecg.modules.demo.supplierManagement.entity.SupplierContact;
import org.jeecg.modules.demo.supplierManagement.entity.SupplierManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 供应商管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ISupplierManagementService extends IService<SupplierManagement> {

	/**
	 * 添加一对多
	 *
	 * @param supplierManagement
	 * @param supplierContactList
	 */
	public void saveMain(SupplierManagement supplierManagement,List<SupplierContact> supplierContactList) throws Exception;

	public void saveBatch(List<SupplierManagement> mainList, List<List<SupplierContact>> subList) throws Exception;
	/**
	 * 修改一对多
	 *
   * @param supplierManagement
   * @param supplierContactList
	 */
	public void updateMain(SupplierManagement supplierManagement,List<SupplierContact> supplierContactList);
	
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
