package cn.yiwen.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;

/**
 * @program: gmall190401
 * @description: 测试redis  搞了两天都没能连上redis集群心态崩了，算了，老实连redis吧
 * @author: 胡一文
 * @create: 2020-10-20 12:35
 */
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test02(){
        redisTemplate.boundValueOps("pp").set("iii");
        System.out.println(redisTemplate.opsForValue().get("pp"));
    }

    @Test
    public void test() {
        //String 字符串
        //redisTemplate.opsForValue().set("str","test01");
        redisTemplate.boundValueOps("str").set("test02");
        System.out.println("str: " + redisTemplate.opsForValue().get("str"));
        //hash  散列
        redisTemplate.boundHashOps("h_key").put("test hash name", "test hash value");
        redisTemplate.boundHashOps("h_key").put("age", 23);
        Set h_key = redisTemplate.boundHashOps("h_key").keys();
        System.out.println("hash散列所有的域:"+h_key);
        List list = redisTemplate.boundHashOps("h_key").values();
        System.out.println("hash散列所有域的值: "+list);
        //list  列表
        redisTemplate.boundListOps("l_key").leftPush("a");
        redisTemplate.boundListOps("l_key").leftPush("b");
        redisTemplate.boundListOps("l_key").leftPush("c");
        List l_key = redisTemplate.boundListOps("l_key").range(0, -1);
        System.out.println("list列表中的所有的元素: "+l_key);
        //set  集合
        redisTemplate.boundSetOps("s_key").add("a","b","c");
        Set s_key = redisTemplate.boundSetOps("s_key").members();
        System.out.println("set集合中的所有元素: "+s_key);
        //sorted set  有序集合
        redisTemplate.boundZSetOps("z_key").add("a",45);
        redisTemplate.boundZSetOps("z_key").add("t",49);
        redisTemplate.boundZSetOps("z_key").add("h",47);
        Set z_key = redisTemplate.boundZSetOps("z_key").range(0, -1);
        System.out.println("zset有序集合中的所有元素: "+z_key);
    }
}
