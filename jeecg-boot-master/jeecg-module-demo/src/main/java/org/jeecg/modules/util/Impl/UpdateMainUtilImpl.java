package org.jeecg.modules.util.Impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.util.IUpdateMainUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

public class UpdateMainUtilImpl<A, B extends BaseMapper<C>, C> implements IUpdateMainUtil<A, B, C> {

    @Override
    public void updateMain(A entity, B subEntityMapper, List<C> originalList, List<C> updatedList, Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method getId = clazz.getMethod("getId");
        Method getMainId = entity.getClass().getMethod("getId");
        Field field = clazz.getDeclaredField("setter");
        field.setAccessible(true);
        Object value = field.get(clazz);
        Method setFkId = clazz.getMethod(value.toString(), String.class);
        // 1.选择子表数据，按id升序排序
        if (originalList != null) originalList.sort((o1, o2) -> {
            try {
                return ((String) getId.invoke(o1)).compareTo((String) getId.invoke(o2));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        if (updatedList != null) updatedList.sort(new Comparator<C>() {
            @Override
            public int compare(C o1, C o2) {
                try {
                    if (getId.invoke(o1) == null) return -1;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (getId.invoke(o2) == null) return 1;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                try {
                    return ((String)getId.invoke(o1)).compareTo((String)getId.invoke(o2));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // 2.双指针遍历两个列表，分类讨论新增、删除、更改三种情况
        int i = 0, j = 0, m = 0, n = 0;
        if (originalList != null) m = originalList.size();
        if (updatedList != null) n = updatedList.size();
        while (i < m && j < n) {
            C original = originalList.get(i);
            C updated = updatedList.get(j);
            if (getId.invoke(updated) == null || ((String) getId.invoke(original)).compareTo((String) getId.invoke(updated)) > 0) {
                Object id = getMainId.invoke(entity);
                setFkId.invoke(updated, id.toString());
                subEntityMapper.insert(updated);
                j++;
            }
            else if (((String) getId.invoke(original)).compareTo((String) getId.invoke(updated)) == 0) {
                Object id = getMainId.invoke(entity);
                setFkId.invoke(updated, id.toString());
                subEntityMapper.updateById(updated);
                i++; j++;
            }
            else subEntityMapper.deleteById((Serializable) getId.invoke(originalList.get(i++)));
        }
        while (i < m) {
            subEntityMapper.deleteById((Serializable) getId.invoke(originalList.get(i++)));
        }
        while (j < n) {
            C updated = updatedList.get(j++);
            Object id = getMainId.invoke(entity);
            setFkId.invoke(updated, id.toString());
            subEntityMapper.insert(updated);
        }
    }
}