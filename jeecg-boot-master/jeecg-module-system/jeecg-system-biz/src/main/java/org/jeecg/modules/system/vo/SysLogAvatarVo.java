package org.jeecg.modules.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_log")
public class SysLogAvatarVo {

    @TableField("username")
    private String username;

    @TableField("log_content")
    private String logContent;

    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private String avatar;
}
