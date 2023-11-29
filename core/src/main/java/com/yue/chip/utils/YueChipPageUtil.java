package com.yue.chip.utils;

import com.yue.chip.constant.GlobalConstant;
import com.yue.chip.core.YueChipPage;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class YueChipPageUtil {

    public static YueChipPage instance() {
        return new YueChipPage(getPage(),getSize(), Sort.unsorted());
    }

    /**
     * 获取当前页码
     * @return
     */
    private static Integer getPage(){
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)){
            return 0;
        }
        String pageNumber = request.getParameter(GlobalConstant.PAGE_NUMBER);
        if (NumberUtils.isDigits(pageNumber)) {
            return Integer.valueOf(pageNumber)-1;
        }
        return 0;
    }

    /**
     * 获取分页大小
     * @return
     */
    private static int getSize(){
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)){
            return 30;
        }
        String pageSize = request.getParameter(GlobalConstant.PAGE_SIZE);
        if (NumberUtils.isDigits(pageSize)) {
            return Integer.valueOf(pageSize);
        }
        return 30;
    }

    private static HttpServletRequest getHttpServletRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(requestAttributes)){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        return request;
    }
}
