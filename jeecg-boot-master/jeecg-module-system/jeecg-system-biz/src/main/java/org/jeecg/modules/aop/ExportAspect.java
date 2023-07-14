package org.jeecg.modules.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.handler.inter.IExcelExportServer;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出大数据excel切面类（记录数>50000条）
 * 参考：http://doc.jeecg.com/2595247
 *
 */
@Component
@Aspect
@Slf4j
public class ExportAspect {

    @Pointcut("@annotation(org.jeecg.modules.aop.ExportExcel)")
    public void exportPointcut() {
    }

    @Around("exportPointcut()")
    public void doExport(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed();
        if (o instanceof ModelAndView) {
            ModelMap map = ((ModelAndView) o).getModelMap();
            List list = (List) map.getAttribute(NormalExcelConstants.DATA_LIST);
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method;
            method = point.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes());
            ExportExcel annotation = method.getAnnotation(ExportExcel.class);
            //1.获取ExportExcel注解上beanClass的值
            Class<?> aClass = annotation.beanClass();
            String title = annotation.title();
            Object[] args = point.getArgs();
            //2.获取方法第一个参数值
            // HttpServletRequest request = (HttpServletResponse) args[0];
            exportUtils(list, aClass, title);
        }
    }

    public <T> void exportUtils(List<T> list, Class<T> aClass, String title) throws IOException {
        Workbook workbook = null;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ExportParams exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        exportParams.setType(ExcelType.XSSF);
        workbook = ExcelExportUtil.exportBigExcel(exportParams, aClass, new IExcelExportServer() {
            @Override
            public List<Object> selectListForExcelExport(Object obj, int page) {
                if ((int) obj == page) {
                    return null;
                }
                return new ArrayList<>(list);
            }
        },2);
        String home = System.getProperty("user.home");
        FileOutputStream fos = new FileOutputStream(home + "/Downloads/" + title + ".xlsx");
        workbook.write(fos);
        fos.close();
    }
}

