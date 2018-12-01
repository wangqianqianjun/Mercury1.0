package test;


/**
 * Created by wangqianjun on 2018/12/1.
 */
public class test {
    public static void main(String[] args) {
        try {
            Class<?> c1=Class.forName("org.apache.catalina.connector.RequestFacade");
            Class<?> c2=Class.forName("javax.servlet.http.HttpServletRequest");
            if(c2.isAssignableFrom(c1)){
                System.out.println("true");
            }else {
                System.out.println("false");
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
