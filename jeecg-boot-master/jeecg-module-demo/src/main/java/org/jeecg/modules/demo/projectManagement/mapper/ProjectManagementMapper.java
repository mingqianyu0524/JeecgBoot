package org.jeecg.modules.demo.projectManagement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.projectManagement.entity.ProjectManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.demo.projectManagement.vo.ProjectManagementVo;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface ProjectManagementMapper extends BaseMapper<ProjectManagement> {
    List<ProjectManagementVo> selectProjectManagementVoList();

    String selectIdByProjectIndex(@Param("projectIndex") String projectIndex);

    @Select("SELECT * FROM project_management WHERE project_index = #{projectIndex}")
    ProjectManagement selectByProjectIndex(@Param("projectIndex") String projectIndex);
}
