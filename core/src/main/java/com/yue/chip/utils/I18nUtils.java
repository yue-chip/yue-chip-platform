package com.yue.chip.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class I18nUtils {

    private static MessageSource messageSource;

    public I18nUtils(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }

    public static String getMessage(String key) {
        return getMessage(key, null);
    }

    public static String getMessage(String key, Object[] args) {
        if (messageSource == null) {
            throw new IllegalStateException("MessageSource is not initialized");
        }
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(key, args, locale);
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
