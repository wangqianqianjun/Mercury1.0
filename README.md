# Mercury1.0
模仿springMVC实现的一个mvc框架

目前实现功能:
1:对请求进行自动分发适配controller
2:对容器进行自动注入,实现了IOC的功能
3:对类和方法实现注解化处理
4:对返回值进行处理

尚待实现:
1:对参数进行自适配,post请求体内参数尚未处理
2:代码重构
3:性能调优


本框架只引用了两个第三方类库:
javax.servlet,com.fasterxml.jackson.core

