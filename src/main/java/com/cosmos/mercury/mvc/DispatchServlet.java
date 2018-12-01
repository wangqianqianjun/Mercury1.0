package com.cosmos.mercury.mvc;

import com.cosmos.mercury.mvc.annotation.Controller;
import com.cosmos.mercury.mvc.annotation.Qualifier;
import com.cosmos.mercury.mvc.annotation.RequestMapping;
import com.cosmos.mercury.mvc.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author wangqianjun
 *         Created by wangqianjun on 2018/12/1.
 */
public class DispatchServlet extends HttpServlet {
    /**
     * 扫描的配置信息
     */
    private Properties properties = new Properties();
    /**
     * 所有class集合
     */
    private List<String> classNames = new ArrayList<>();

    /**
     * 存放controller的ioc容器
     */
    private Map<String, Object> controllerIoc = new HashMap<>();

    /**
     * 存放service的ioc容器
     */
    private Map<String, Object> serviceIoc = new HashMap<>();

    /**
     * handlerMapping  url的key和method的映射
     */
    private Map<String, Object> handlerMapping = new HashMap<>();

    private final static String classPathPrefix="classpath:";

    private static String toLowerFirstWorld(String key) {
        if (Character.isLowerCase(key.charAt(0))) {
            return key;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(key.charAt(0))).append(key.substring(1)).toString();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        doScanner(properties.getProperty("scanPackage"));
        doInstance();
        doHandlerMapping();
        doIoc();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri=req.getRequestURI();
        String url=replaceUrl(uri);
        Object o=handlerMapping.get(url);
        if(o == null){
            resp.getWriter().write("No mapping!");
        }
        Enumeration<String> parameterNames=req.getParameterNames();


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri=req.getRequestURI();
        String url=replaceUrl(uri);
        Object methodMapping=handlerMapping.get(url);
        if(methodMapping == null){
            resp.getWriter().write("No mapping!");
        }
        Method method=(Method)methodMapping;
        Object controller=controllerIoc.get(url);
        try {
            method.setAccessible(true);
            method.invoke(controller,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        InputStream in=req.getInputStream();


    }

    /**
     * 加载配置文件
     *
     * @param location
     */
    private void doLoadConfig(String location) {
        if(location.startsWith(classPathPrefix)){
            location=location.replace(classPathPrefix,"");
        }
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(location)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描配置文件中定义的包
     *
     * @param packageName
     */
    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归扫描包获取class
                doScanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className);
                System.out.println("scan load one class:" + className);
            }
        }
    }

    /**
     * 对ioc对象进行装载
     */
    private void doInstance() {
        if (classNames == null || classNames.size() < 1) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Controller controller = clazz.getAnnotation(Controller.class);
                    String key = controller.value();
                    if (!"".equals(key.trim())) {
                        controllerIoc.put(key, clazz.newInstance());
                    } else {
                        controllerIoc.put(toLowerFirstWorld(clazz.getSimpleName()), clazz.newInstance());
                    }
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String key = service.value();
                    if (!"".equals(key.trim())) {
                        serviceIoc.put("key", clazz.newInstance());
                    } else {
                        serviceIoc.put(toLowerFirstWorld(clazz.getSimpleName()), clazz.newInstance());
                    }
                } else {
                    continue;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立映射
     */
    private void doHandlerMapping() {
        if (controllerIoc == null || controllerIoc.size() < 1) {
            return;
        }
        Map<String, Object> urlMethod = new HashMap<>(64);
        for (Map.Entry<String, Object> entry : controllerIoc.entrySet()) {
            Object o = entry.getValue();
            Class<? extends Object> clazz = o.getClass();

            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                method.setAccessible(true);
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String methodUrl = annotation.value();
                String url = fixUrl(baseUrl, methodUrl);
                handlerMapping.put(url, method);
                urlMethod.put(url, o);
                System.out.println(url + "," + method);
            }
        }
        controllerIoc.putAll(urlMethod);
    }

    private String fixUrl(String baseUrl, String methodUrl) {
        String url = "/" + baseUrl + "/" + methodUrl;
        return replaceUrl(url);
    }

    private String replaceUrl(String url){
        return url.replaceAll("/+", "/");
    }

    private void doIoc(){
        if(controllerIoc==null||controllerIoc.size()<1){
            return;
        }
        for(Map.Entry<String,Object> entry:controllerIoc.entrySet()){
            Object o=entry.getValue();
            Class<? extends Object> clazz=o.getClass();
            Field[] fields=clazz.getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                if(field.isAnnotationPresent(Qualifier.class)){
                    Qualifier qualifier=field.getAnnotation(Qualifier.class);
                    String value=qualifier.value();
                    String key;
                    if(!"".equals(value)){
                        key=value;
                    }else{
                        key=field.getName();
                    }

                    try {
                        field.set(o,serviceIoc.get(key));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
