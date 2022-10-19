package cn.y3lq.blog.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: Y3lq
 * @description: 测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test1 {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1() throws InterruptedException {
//        String script=" if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                " then " +
                " return redis.call('del', KEYS[1]) " +
                " else " +
                " return 0 " +
                " end ";
        List<String> keys = new ArrayList<>();
        keys.add("1abc");
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript(script);
        defaultRedisScript.setResultType(Long.class);
        Object execute = redisTemplate.execute(defaultRedisScript, keys, "cba");
        System.out.println(execute);
    }
}
