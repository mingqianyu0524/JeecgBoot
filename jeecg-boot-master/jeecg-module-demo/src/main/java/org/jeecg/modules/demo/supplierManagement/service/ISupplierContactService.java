package org.jeecg.modules.demo.supplierManagement.service;

import org.jeecg.modules.demo.supplierManagement.entity.SupplierContact;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ISupplierContactService extends IService<SupplierContact> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SupplierContact>
	 */
	public List<SupplierContact> selectByMainId(String mainId);
}
