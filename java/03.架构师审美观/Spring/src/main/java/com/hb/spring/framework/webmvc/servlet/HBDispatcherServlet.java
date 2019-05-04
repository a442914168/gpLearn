package com.hb.spring.framework.webmvc.servlet;

import com.hb.spring.framework.annotation.HBController;
import com.hb.spring.framework.annotation.HBRequestMapping;
import com.hb.spring.framework.context.HBApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: Spring
 * @description:
 * @author: bobobo
 * @create: 2019-05-04 19:16
 **/
public class HBDispatcherServlet extends HttpServlet {

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private HBApplicationContext context;

    private List<HBHandlerMapping> handlerMappings = new ArrayList<HBHandlerMapping>();

    private Map<HBHandlerMapping, HBHandlerAdapter> handlerAdapters = new HashMap<HBHandlerMapping, HBHandlerAdapter>();

    private List<HBViewResolver> viewResolvers = new ArrayList<HBViewResolver>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1. 初始化ApplicationContext
        context = new HBApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        //2. 初始化Spring MVC九大组件
        initStrategies(context);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }


    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1. 通过request中拿到url 去匹配一个HandlerMapping
        HBHandlerMapping handler = getHandler(req);

        if (handler == null) {
            processDispatchResult(req, resp, new HBModelAndView("404"));
            return;
        }

        //2. 准备调用前的参数
        HBHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        //3. 真正调用方法，返回ModelAndView储存了要传给页面上的值，和模板的名称
        HBModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);

        //4. 真正的输出
        processDispatchResult(req, resp, modelAndView);
    }

    private HBHandlerAdapter getHandlerAdapter(HBHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        HBHandlerAdapter ha = this.handlerAdapters.get(handler);
        if (ha.supports(handler)) {
            return ha;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, HBModelAndView hbModelAndView) throws Exception {
        //把给我的ModleAndView变成一个HTML、OuputStream、json、freemark、veolcity
        //ContextType
        if(null == hbModelAndView){return;}

        //如果ModelAndView不为null，怎么办？
        if(this.viewResolvers.isEmpty()){return;}

        for (HBViewResolver viewResolver : this.viewResolvers) {
            HBView view = viewResolver.resolveViewName(hbModelAndView.getViewName(),null);
            view.render(hbModelAndView.getModel(),req,resp);
            return;
        }
    }

    private HBHandlerMapping getHandler(HttpServletRequest req) throws Exception{
        if(this.handlerMappings.isEmpty()){ return null; }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (HBHandlerMapping handler : this.handlerMappings) {
            try{
                Matcher matcher = handler.getPattern().matcher(url);
                //如果没有匹配上继续下一个匹配
                if(!matcher.matches()){ continue; }

                return handler;
            }catch(Exception e){
                throw e;
            }
        }
        return null;
    }

    // 初始化九大组件
    private void initStrategies(HBApplicationContext context) {
        // 多文件上传组件
        initMultipartResolver(context);
        // 初始化本地语言环境
        initLocaleResolver(context);
        // 初始化模板处理器
        initThemeResolver(context);
        // handerMapping,实现
        initHandlerMappings(context);
        // 初始化参数适配器,实现
        initHandlerAdapters(context);
        // 初始化异常拦截器
        initHandlerExceptionResolvers(context);
        // 初始化视图预处理器
        initRequestToViewNameTranslator(context);
        // 初始化视图转换器,实现
        initViewResolvers(context);
        // 参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(HBApplicationContext context) {
    }

    private void initViewResolvers(HBApplicationContext context) {

        // 拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRoot);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i++) {
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new HBViewResolver(templateRoot));
        }

    }

    private void initRequestToViewNameTranslator(HBApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(HBApplicationContext context) {
    }

    private void initHandlerAdapters(HBApplicationContext context) {
        // 把一个request请求变成一个handler， 参数都是字符串的，自动配到handler中的形参

        // 可想而知，他要拿到HandlerMapping才能干活
        // 就意味着，有几个HandlerMapping就有几个HandlerAdapters
        for (HBHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new HBHandlerAdapter());
        }
    }

    private void initHandlerMappings(HBApplicationContext context) {

        String[] beanNames = context.getBeanDefinitionNames();

        try {

            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);

                Class<?> clazz = controller.getClass();

                if (!clazz.isAnnotationPresent(HBController.class)) {
                    continue;
                }

                String baseUrl = "";
                // 获取Controller的Url配置
                if (clazz.isAnnotationPresent(HBRequestMapping.class)) {
                    HBRequestMapping requestMapping = clazz.getAnnotation(HBRequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                // 获取Method的url配置
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {

                    // 没有加RequestMapping注解的直接忽略
                    if (!method.isAnnotationPresent(HBRequestMapping.class)) {
                        continue;
                    }

                    // 映射url
                    HBRequestMapping requestMapping = method.getAnnotation(HBRequestMapping.class);
                    //  /demo/query

                    //  (//demo//query)
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);

                    this.handlerMappings.add(new HBHandlerMapping(controller, method, pattern));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initThemeResolver(HBApplicationContext context) {
    }

    private void initLocaleResolver(HBApplicationContext context) {
    }

    private void initMultipartResolver(HBApplicationContext context) {
    }

}
