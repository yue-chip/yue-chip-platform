package com.yue.chip.aop.exception;

/**
 * @author mr.liu
 * @title: SecurityException
 * @projectName lion
 * @description: SecurityException
 * @date 2020/7/29下午3:58
 */
//@RestController
//@ConditionalOnClass({HttpServletRequest.class})
//public class SecurityException implements ErrorController {
//
//    private static final String PATH = "/security/error";
//
//    @Autowired
//    private ErrorAttributes errorAttributes;
//
//    @RequestMapping(value = PATH)
//    public IResultData error(HttpServletRequest request, HttpServletResponse response) {
//        WebRequest requestAttributes = new ServletWebRequest(request);
//        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(requestAttributes, ErrorAttributeOptions.defaults());
//        ResultData resultData = new ResultData();
//        resultData.setData(errorMap);
//        resultData.setStatus(ResultDataState.ERROR.getKey());
////        resultData.setMessage();
//        response.setStatus(HttpServletResponse.SC_OK);
//        return resultData;
//    }
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//}
