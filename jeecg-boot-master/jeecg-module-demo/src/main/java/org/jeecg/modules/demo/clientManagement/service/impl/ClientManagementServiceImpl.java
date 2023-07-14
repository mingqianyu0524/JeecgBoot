package org.jeecg.modules.demo.clientManagement.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.jeecg.modules.demo.clientManagement.entity.ClientManagement;
import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import org.jeecg.modules.demo.clientManagement.mapper.ClientContactMapper;
import org.jeecg.modules.demo.clientManagement.mapper.ClientManagementMapper;
import org.jeecg.modules.demo.clientManagement.service.IClientManagementService;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import org.jeecg.modules.demo.projectManagement.entity.ProjectPaymentTerm;
import org.jeecg.modules.demo.projectManagement.mapper.ProjectPaymentTermMapper;
import org.jeecg.modules.util.IUpdateMainUtil;
import org.jeecg.modules.util.Impl.UpdateMainUtilImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 客户管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class ClientManagementServiceImpl extends ServiceImpl<ClientManagementMapper, ClientManagement> implements IClientManagementService {

	@Autowired
	private ClientManagementMapper clientManagementMapper;
	@Autowired
	private ClientContactMapper clientContactMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ClientManagement clientManagement, List<ClientContact> clientContactList) throws Exception {
		if (clientManagement.getClientName() == null) {
			throw new Exception("导入Excel缺少必填字段");
		}
		// 添加第一联系人
		if (clientContactList!=null && clientContactList.size()>0) {
			clientManagement.setContact(clientContactList.get(0).getName());
		}
		clientManagementMapper.insert(clientManagement);
		if (clientContactList!=null && clientContactList.size()>0) {
			for(ClientContact entity:clientContactList) {
				//外键设置
				entity.setClientFkId(clientManagement.getId());
				clientContactMapper.insert(entity);
			}
		}
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ClientManagement> mainList, List<List<ClientContact>> subList) throws Exception {
		for (int i = 0; i < mainList.size(); i++) {
			if (i > subList.size()) throw new Exception("列表长度不匹配");
			saveMain(mainList.get(i), subList.get(i));
		}
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(ClientManagement clientManagement,List<ClientContact> clientContactList) {
		// 1.添加第一联系人，更新主表数据
		if (clientContactList != null && clientContactList.size() > 0) {
			clientManagement.setContact(clientContactList.get(0).getName());
		} else {
			clientManagement.setContact("");
		}
		clientManagementMapper.updateById(clientManagement);
		// 2.选择子表数据，按id升序排序
		List<ClientContact> originalList = clientContactMapper.selectByMainId(clientManagement.getId());
		IUpdateMainUtil<ClientManagement, ClientContactMapper, ClientContact> updateMainUtil = new UpdateMainUtilImpl<>();
		try {
			updateMainUtil.updateMain(clientManagement, clientContactMapper, originalList, clientContactList, ClientContact.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		clientContactMapper.deleteByMainId(id);
		clientManagementMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			clientContactMapper.deleteByMainId(id.toString());
			clientManagementMapper.deleteById(id);
		}
	}

}
