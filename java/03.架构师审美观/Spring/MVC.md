1. #### springMVC的主要类

   1. `DispatcherServlet`

      MVC的主入口，继承HttpServlet类，重写了`doGet`、`doPost`、`init`方法

      `init`方法主要是根据web.xml所配置的servlet信息进行初始化ApplicationContext，并且创建mvc的九大组件

      `doGet` 和 `doPost`方法 主要处理请求调度

   2. `HandlerAdapter`

      将 处理请求的参数 和 方法的参数 进行一一对应，并返回ModelAndView

   3. `HandlerMapping`

      储存 方法对应的Controller 、Method 、请求的Url

   4. `ModelAndView`

      储存 响应视图的名称 和 对应的返回model

   5. `View`

      渲染view，将返回的model 进行 {{xxxx}}匹配

   6. `ViewResolver`

      找出具体的资源地址

      

2. #### springMVC的主要流程

   1. 在web.xml中配置

      ```xml
      <servlet>
      		<servlet-name>hbmvc</servlet-name>
      		<servlet-class>com.hb.spring.framework.webmvc.servlet.HBDispatcherServlet</servlet-class>
      		<init-param>
      			<param-name>contextConfigLocation</param-name>
      			<param-value>classpath:application.properties</param-value>
      		</init-param>
      		<load-on-startup>1</load-on-startup>
      	</servlet>
      
      	<servlet-mapping>
      		<servlet-name>hbmvc</servlet-name>
      		<url-pattern>/*</url-pattern>
      	</servlet-mapping>
      ```

      

   2. 以DispatcherServlet为入口

      1. 经过`init`方法，获取contextConfigLocation得到对应的配置文件application.properties，并且加载对应的容器

         ```properties
         scanPackage=com.hb.spring.demo
         
         templateRoot=layouts
         ```

      2. 初始化springMVC九大组件

         1. `initFlashMapManager` 参数缓存器
         2. `initLocaleResolver` 本地语言环境
         3. `initThemeResolver`模板处理器
         4. `initHandlerMappings`路径映射
            1. 循环`ApplicationContext`上下文获取到的`beanDefinitionNames`，将存在`Controller`注解的类，获取对应的`RequestMapping`的值`baseUrl`
            2. 循环该类的方法，将存在`RequestMapping`注解的方法，获取对应的值。
            3. 每获取到一个方法的url值，就组装成一个`HandlerMapping` 将其存入`handleMappings`中
         5. `initHandlerAdapters` 参数适配器
            1. 每一个`HandlerMapping`都应该有对应的参数适配器，所以进行循环遍历
            2. new 一个 `HandlerAdapter` 和 `HandlerMapping`储存在`handlerAdapters`中
         6. `initHandleExceptionResolvers`异常拦截器
         7. `initRequestToViewNameTranslator` 视图预处理器
         8. `initViewResolvers`视图转换器
            1. 拿到配置文件中的`templateRoot`进行加载，储存在`viewResolvers`中
         9. `initFlashMapManager` 参数缓存器

   3. 在网络请求经过`doGet`/`doPost`请求时

      1. `doDispatch`进行url调度
         1. 通过request中拿到url 去匹配一个`handleMapping`
            1. `getHandler`方法，循环遍历`handlerMappings`数组
            2. 根据储存的url地址进行匹配
         2. 准备调用前的参数
            1. `getHandlerAdapter`方法，循环遍历`handlerAdapters`
            2. 根据`handlerMapping`获取`HandlerAdapter`
         3. 调用方法`HandlerAdapter#handle`，返回ModelAndView
            1. 将url请求的参数 和 该目标方法的参数 进行一一匹配
            2. 反射执行目标方法，判断目标方法返回值是否是ModelAndView，是的话转换返回
         4. 调用`processDispatchResult`进行输出
            1. 循环遍历`viewResolvers`，将`modelAndView`中的viewName进行组装匹配，返回`View`
            2. 调用`view#render`进行解析
               1. 循环读每一行
               2. 正则匹配{{…}}，等特殊符号的值，进行参数匹配
               3. 在进行response输出

