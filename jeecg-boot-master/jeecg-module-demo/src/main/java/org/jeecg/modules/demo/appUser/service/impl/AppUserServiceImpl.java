package org.jeecg.modules.demo.appUser.service.impl;

import org.jeecg.modules.demo.appUser.entity.AppUser;
import org.jeecg.modules.demo.appUser.mapper.AppUserMapper;
import org.jeecg.modules.demo.appUser.service.IAppUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 测试app用户表
 * @Author: jeecg-boot
 * @Date:   2023-05-30
 * @Version: V1.0
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

}
