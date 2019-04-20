IOC主要分为三大步骤 定位、加载、注册

spring支持多种配置源 注解、xml等等

`HBAbstractApplicationContext` 中定义了`refresh()`方法。

具体的加载方式 会有不同的方法，但是最终还是会使用`refresh()`来ioc操作

1. 定位

    通过`HBBeanDefinitionReader`，传入需要扫描的包地址，来扫描配置的包，获取到具体的类名
2. 加载

    通过`HBBeanDefinitionReader#loadBeanDefinitions`来加载具体的配置信息得到`HBBeanDefinition`数组，
    `HBBeanDefinition`主要储存的是类的全类名、是否懒加载、是否单例、等等信息
    
3. 注册

    循环`HBBeanDefinition`数组，将definition注册到伪IOC容器中
    ```java
     Map<String, HBBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, HBBeanDefinition>();
     beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
    ```
        
