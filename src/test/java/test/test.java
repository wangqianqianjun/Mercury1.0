package test;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by wangqianjun on 2018/12/1.
 */
public class test {
    public static void main(String[] args) {
        try {
            Class clazz=Class.forName("com.cosmos.mercury.mvc.controller.TestController");
            Method [] ms=clazz.getDeclaredMethods();
            for(Method method:ms){
                method.setAccessible(true);
                Parameter[] params = method.getParameters();
                for(Parameter parameter : params){
                    System.out.println(parameter.getName());
                    System.out.println(parameter.getType());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static  String fixUrl(String baseUrl,String methodUrl){
        String url="/"+baseUrl+"/"+methodUrl;
        return url.replaceAll("/+","/");
    }
}
