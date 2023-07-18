package org.jeecg.modules.demo.projectPaymentRecv.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.projectPaymentRecv.entity.ProjectPaymentRecv_;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.demo.projectPaymentRecv.vo.ProjectPaymentRecvVo;

/**
 * @Description: 收款管理
 * @Author: jeecg-boot
 * @Date:   2023-05-24
 * @Version: V1.0
 */
public interface ProjectPaymentRecv_Mapper extends BaseMapper<ProjectPaymentRecv_> {
    List<ProjectPaymentRecvVo> getVoList(@Param("projectIndex") String projectIndex,
                                         @Param("projectNameList") List<String> projectNameList,
                                         @Param("clientName") String clientName);
}
