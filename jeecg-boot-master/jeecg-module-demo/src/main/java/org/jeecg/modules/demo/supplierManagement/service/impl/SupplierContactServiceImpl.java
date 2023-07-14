package org.jeecg.modules.demo.supplierManagement.service.impl;

import org.jeecg.modules.demo.supplierManagement.entity.SupplierContact;
import org.jeecg.modules.demo.supplierManagement.mapper.SupplierContactMapper;
import org.jeecg.modules.demo.supplierManagement.service.ISupplierContactService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class SupplierContactServiceImpl extends ServiceImpl<SupplierContactMapper, SupplierContact> implements ISupplierContactService {
	
	@Autowired
	private SupplierContactMapper supplierContactMapper;
	
	@Override
	public List<SupplierContact> selectByMainId(String mainId) {
		return supplierContactMapper.selectByMainId(mainId);
	}
}
