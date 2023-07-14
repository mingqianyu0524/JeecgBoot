package org.jeecg.modules.util;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IUpdateMainUtil<A, B extends BaseMapper<C>, C> {
    void updateMain(A entity, B subEntityMapper, List<C> originalList, List<C> updatedList, Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException;
}
