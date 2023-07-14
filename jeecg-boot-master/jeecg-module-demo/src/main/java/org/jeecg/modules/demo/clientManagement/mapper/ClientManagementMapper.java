package org.jeecg.modules.demo.clientManagement.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.clientManagement.entity.ClientManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 客户管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ClientManagementMapper extends BaseMapper<ClientManagement> {
    //@Override
    //<P extends IPage<ClientManagement>> P selectPage(P page, Wrapper<ClientManagement> queryWrapper);
}
