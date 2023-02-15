package com.brodog.springframework.beans.factory.support;

import com.brodog.springframework.beans.BeansException;
import com.brodog.springframework.beans.factory.BeanFactory;
import com.brodog.springframework.beans.factory.config.BeanDefinition;
import com.brodog.springframework.beans.factory.config.BeanPostProcessor;
import com.brodog.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 抽象bean工厂
 * 继承了 DefaultSingleBeanRegistry类引入了获取和创建单例beanObject的能力
 * @author By-BroDog
 * @createTime 2023-02-13
 */
public abstract class AbstractBeanFactory extends DefaultSingleBeanRegistry implements ConfigurableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    /**
     * 通过beanName获取 bean的实例对象 Object
     * 重写 beanFactory接口方法
     * @param beanName  beanName
     * @return  bean的实例对象 Object
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        return doGetBean(beanName, null);
    }

    /**
     * 通过beanName获取 bean的实例对象 Object
     * 重写 beanFactory接口方法
     * @param beanName  beanName
     * @return  bean的实例对象 Object
     */
    @Override
    public Object getBean(String beanName, Object... args) throws BeansException {
        return doGetBean(beanName, args);
    }

    /**
     * 返回指定泛型的对象
     * @param beanName  要检索的bean的名称
     * @param requiredType 类型
     * @param <T>          泛型
     * @return             实例化的 Bean 对象
     * @throws BeansException 不能获取 Bean 对象，则抛出异常
     */
    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
        return (T) doGetBean(beanName, null);
    }

    /**
     * 返回指定泛型的对象
     * @param beanName  要检索的bean的名称
     * @param requiredType 类型
     * @param <T>          泛型
     * @return             实例化的 Bean 对象
     * @throws BeansException 不能获取 Bean 对象，则抛出异常
     */
    @Override
    public <T> T getBean(String beanName, Class<T> requiredType, Object... args) throws BeansException {
        return (T) doGetBean(beanName, args);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    /**
     * 通过beanName获取 beanDefinition
     * 抽象方法 等待下面的子类进行实现 提供给 当前类内部的 getBean()
     * @param beanName  beanName
     * @return
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 创建 beanObject 实例对象
     * 抽象方法 等待下面的子类进行实现 提供给 当前类内部的 getBean()
     * @param beanName  beanName
     * @param beanDefinition    beanDefinition
     * @param args  构造函数参数
     * @return
     */
    protected abstract Object createBeanObject(String beanName, BeanDefinition beanDefinition, Object[] args);


//    private  <T> T doGetBean(final String name, final Object[] args) {
//        Object bean = getSingletonBeanObject(name);
//        if (bean != null) {
//            return (T) bean;
//        }
//
//        BeanDefinition beanDefinition = getBeanDefinition(name);
//        return (T) createBeanObject(name, beanDefinition, args);
//    }

    /**
     * 获取beanObject 当不存在的时候则进行创建
     * @param beanName  beanName
     * @param args  可变参数
     * @return
     */
    private Object doGetBean(String beanName, Object[] args) {
        // 通过beanName获取单例bean的Object对象 用的是DefaultSingleBeanRegistry中定义的 getSingletonBeanObject
        Object singletonBeanObject = getSingletonBeanObject(beanName);
        // 如果不存在则创建该bean的Object对象
        if(Objects.isNull(singletonBeanObject)) {
            // 先通过子类实现的 getBeanDefinition 抽象方法获取bean的定义信息
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            // 再创建单例的beanObject
            singletonBeanObject = createBeanObject(beanName, beanDefinition, args);
        }
        return singletonBeanObject;
    }
}
