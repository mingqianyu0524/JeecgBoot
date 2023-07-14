package org.jeecg.modules.demo.clientManagement.service.impl;

import org.jeecg.modules.demo.clientManagement.entity.ClientContact;
import org.jeecg.modules.demo.clientManagement.mapper.ClientContactMapper;
import org.jeecg.modules.demo.clientManagement.service.IClientContactService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客户联系人
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
@Service
public class ClientContactServiceImpl extends ServiceImpl<ClientContactMapper, ClientContact> implements IClientContactService {
	
	@Autowired
	private ClientContactMapper clientContactMapper;
	
	@Override
	public List<ClientContact> selectByMainId(String mainId) {
		return clientContactMapper.selectByMainId(mainId);
	}
}
