package com.yue.chip.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yue.chip.config.RestTemplateConfiguration;
import com.yue.chip.core.common.enums.EnumPersistenceBean;
import com.yue.chip.utils.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mr.liu
 * @Description:
 * @date 2020/9/16上午9:26
 */
public abstract class AbstractEnumPersistence implements CommandLineRunner {

    @Value("${yue-chip.enums.persistence.url:http://common}")
    private String LB_URL ;

    @Autowired
    @Qualifier(RestTemplateConfiguration.REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    private RestTemplate restTemplate;

    private String packageName;

    @Autowired
    private ObjectMapper objectMapper;

    public AbstractEnumPersistence(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void run(String... args) {
        try {
            List<EnumPersistenceBean> list = EnumUtil.getAllEnumsInPackage(this.packageName);
            if (list.size() == 0){
                return;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(list),headers);
            Thread.sleep(2000);
            ResponseEntity response = restTemplate.postForEntity(LB_URL+"/enum/persistence", request, Object.class);
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }
}
