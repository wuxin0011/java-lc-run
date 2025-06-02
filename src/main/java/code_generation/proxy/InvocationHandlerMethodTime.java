package code_generation.proxy;

import code_generation.utils.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * An invocation handler that measures and logs method execution time for {@link LogarithmicDevice} implementations.
 * This class provides proxy-based timing functionality through Java's dynamic proxy mechanism.
 * @author wuxin0011
 * @since 1.0
 */
public class InvocationHandlerMethodTime implements InvocationHandler {

    /**
     * The target LogarithmicDevice instance being proxied
     */
    public LogarithmicDevice target;

    /**
     * Constructs an invocation handler for the specified target LogarithmicDevice.
     * @param target The LogarithmicDevice instance to be proxied and timed
     */
    public InvocationHandlerMethodTime(LogarithmicDevice target) {
        this.target = target;
    }

    /**
     * Invokes the method on the target object and measures execution time.
     * @param proxy The proxy instance that the method was invoked on
     * @param method The Method instance corresponding to the interface method invoked
     * @param args An array of objects containing the method arguments
     * @return The result of the method invocation on the target object
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Object result = null;
        String info = null;
        String methodInfo = ReflectUtils.getMethodInfo(method);
        if (methodInfo == null || "".equals(methodInfo)) {
            info = ReflectUtils.getClassInfo(target.getClass());
        }
        try {
            System.out.println(info);
            long l1 = System.currentTimeMillis();
            result = method.invoke(target, args);
            long l2 = System.currentTimeMillis();
            System.out.println("\n本次测试耗时 :" + (l2 - l1) + "ms");
        } catch (Exception e) {
            System.err.println("测试失败！");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Creates a timing proxy for the specified LogarithmicDevice instance.
     * @param logarithmicDevice The LogarithmicDevice instance to be timed
     */
    public static void getRunTime(LogarithmicDevice logarithmicDevice) {
        // 创建 InvocationHandler 实例
        InvocationHandler invocationHandler = new InvocationHandlerMethodTime(logarithmicDevice);
        // 创建代理对象
        LogarithmicDevice proxy = (LogarithmicDevice) Proxy.newProxyInstance(
                logarithmicDevice.getClass().getClassLoader(),
                logarithmicDevice.getClass().getInterfaces(),
                invocationHandler
        );
        // 执行代理方法
        proxy.logarithmicDevice();
    }

    /**
     * Creates a timing proxy for a new instance of the specified LogarithmicDevice class.
     * @param <T> The type of LogarithmicDevice
     * @param c The Class object of the LogarithmicDevice implementation
     * @throws RuntimeException if instantiation fails
     */
    public static <T extends LogarithmicDevice> void getRunTime(Class<T> c) {
        try {
            getRunTime(c.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}