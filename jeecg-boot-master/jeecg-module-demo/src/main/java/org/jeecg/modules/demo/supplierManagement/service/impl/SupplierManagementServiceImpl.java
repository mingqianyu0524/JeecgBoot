package org.jeecg.modules.demo.supplierManagement.service.impl;

import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectPaymentTermMapper;
import org.jeecg.modules.demo.supplierManagement.entity.SupplierManagement;
import org.jeecg.modules.demo.supplierManagement.entity.SupplierContact;
import org.jeecg.modules.demo.supplierManagement.mapper.SupplierContactMapper;
import org.jeecg.modules.demo.supplierManagement.mapper.SupplierManagementMapper;
import org.jeecg.modules.demo.supplierManagement.service.ISupplierManagementService;
import org.jeecg.modules.util.IUpdateMainUtil;
import org.jeecg.modules.util.Impl.UpdateMainUtilImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 供应商管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class SupplierManagementServiceImpl extends ServiceImpl<SupplierManagementMapper, SupplierManagement> implements ISupplierManagementService {

	@Autowired
	private SupplierManagementMapper supplierManagementMapper;
	@Autowired
	private SupplierContactMapper supplierContactMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SupplierManagement supplierManagement, List<SupplierContact> supplierContactList) throws Exception {
		if (supplierManagement.getSupplierName() == null) {
			throw new Exception("导入Excel缺少必填字段");
		}
		// 添加第一联系人
		if (supplierContactList!=null && supplierContactList.size()>0) {
			supplierManagement.setContact(supplierContactList.get(0).getName());
		}
		supplierManagementMapper.insert(supplierManagement);
		if(supplierContactList!=null && supplierContactList.size()>0) {
			for(SupplierContact entity:supplierContactList) {
				//外键设置
				entity.setSupplierFkId(supplierManagement.getId());
				supplierContactMapper.insert(entity);
			}
		}
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<SupplierManagement> mainList, List<List<SupplierContact>> subList) throws Exception {
		for (int i = 0; i < mainList.size(); i++) {
			if (i > subList.size()) throw new Exception("列表长度不匹配");
			saveMain(mainList.get(i), subList.get(i));
		}
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SupplierManagement supplierManagement,List<SupplierContact> supplierContactList) {
		// 1.添加第一联系人，更新主表数据
		if (supplierContactList != null && supplierContactList.size() > 0) {
			supplierManagement.setContact(supplierContactList.get(0).getName());
		} else {
			supplierManagement.setContact("");
		}
		supplierManagementMapper.updateById(supplierManagement);

		List<SupplierContact> originalList = supplierContactMapper.selectByMainId(supplierManagement.getId());
		IUpdateMainUtil<SupplierManagement, SupplierContactMapper, SupplierContact> updateMainUtil1 = new UpdateMainUtilImpl<>();
		try {
			updateMainUtil1.updateMain(supplierManagement, supplierContactMapper, originalList, supplierContactList, SupplierContact.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		supplierContactMapper.deleteByMainId(id);
		supplierManagementMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			supplierContactMapper.deleteByMainId(id.toString());
			supplierManagementMapper.deleteById(id);
		}
	}

}
