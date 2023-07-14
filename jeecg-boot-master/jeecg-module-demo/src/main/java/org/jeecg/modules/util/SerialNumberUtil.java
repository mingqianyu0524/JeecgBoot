package org.jeecg.modules.util;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialNumberUtil {
    public static String generateSerialNumber(String id) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String serialNumber = format.format(new Date());
        if (!StringUtils.isEmpty(id)) {
            serialNumber += id.substring(13, 19);
        }
        return serialNumber;
    }
}
