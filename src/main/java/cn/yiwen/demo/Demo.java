package cn.yiwen.demo;

/**
 * @program: springboot
 * @description: 突发奇想
 * @author: 胡一文
 * @create: 2020-10-25 00:31
 */
public class Demo {
    private boolean a;
    public Integer method1(boolean a){
        if(a){
            return 0;
        }else{
            return 1;
        }

    }
    public Integer method2(boolean a){
        if(a){
            return 0;
        }
        return 1;
    }
}
