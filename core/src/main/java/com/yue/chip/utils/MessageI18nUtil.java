package com.yue.chip.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class MessageI18nUtil {

    private static MessageSource messageSource;

    public MessageI18nUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String getMessage(String code){
        return getMessage(code, null);
    }

    public static String getMessage(String code, Object[] args){
        String message = messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
        if (!StringUtils.hasText(message)){
            message = messageSource.getMessage(code,args, Locale.CHINA);
        }
        return message;
    }
}
