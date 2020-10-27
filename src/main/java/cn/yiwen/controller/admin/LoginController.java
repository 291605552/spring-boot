package cn.yiwen.controller.admin;

import cn.yiwen.entity.Result;
import cn.yiwen.entity.User;
import cn.yiwen.service.UserService;
import cn.yiwen.util.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @auther 胡一文
 * @date 2020/9/28
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /*@Autowired
    private RedisTemplate redisTemplate;*/

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //存储验证码
    private String code;

    //拼接redis中的key值
    private String key;

    //redis验证码发送的计数器
    private String count;

    private String defaultPhotoUrl = "../../image/3.png";

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println("username:" + username + ", password:" + password);
        User user = userService.findByName(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                attributes.getRequest().getSession().setAttribute("user", user); //将登陆用户信息存入到session域对象中
                return new Result(true, user.getUsername());
            }
        }
        return new Result(false, "登录失败");
    }

    /**
     * 注销
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        attributes.getRequest().getSession().removeAttribute("user");
        return "home/login";
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("code") String verify_code
                           ) throws Exception {
        //校验验证码
        //如果验证码为空
        if(verify_code==null){
            return new Result(false,"验证码不正确");
        }
        //组装拼接key
        key = "Verify_code" + email + ":code";
        //String code = (String) redisTemplate.opsForValue().get(key);
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println("通过邮箱查找到的验证码:"+s);
        //判断验证码输入是否正确
        if (verify_code.equals(s)){
            userService.create(new User(username, password, email, defaultPhotoUrl));
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            User user = new User(username, password, email, defaultPhotoUrl);
            //将登陆用户信息存入到session域对象中
            attributes.getRequest().getSession().setAttribute("user", user);
            return new Result(true, username);
        }else {
            return new Result(false,"验证码不正确");
        }

    }


    /**
     * @Description: 验证用户名是否已经存在
     * @Param:
     * @return:
     * @Author: 胡一文
     * @Date: 2020/10/23
     * @Time: 20:40
     */
    @ResponseBody
    @RequestMapping(value = "/verifyUserName", method = RequestMethod.GET)
    public Result verifyUserName(@RequestParam("username") String username) {
        User byName = new User();
        byName=userService.findByName(username);
        System.out.println("想要注册的用户名:"+username);
        System.out.println("--------------");
        System.out.println("通过查找得到的用户名:"+byName);
        if (byName != null) {
            return new Result(true, "用户已存在");
        } else {
            return new Result(false, "用户名可用");
        }
    }

    /**
     * @Description: 发送验证码并存入redis
     * @Param: 参数String email
     * @return: 返回执行情况
     * @Author: 胡一文
     * @Date: 2020/10/25
     * @Time: 12:36
     */
    @ResponseBody
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public Result sendCode(@RequestBody Map map) throws Exception {
        String email = (String) map.get("email");

        //将验证码存入redis
        key = "Verify_code" + email + ":code";
        count = "Verify_code" + email + ":count";
        //String countNum = (String) redisTemplate.opsForValue().get(count);
        String countNum = stringRedisTemplate.opsForValue().get(count);

        if (countNum == null) {
            //第一次访问
            //redisTemplate.boundValueOps(count).set("0");
            stringRedisTemplate.opsForValue().set(count,"0");
            countNum = "0";
        }
        if (Integer.parseInt(countNum) == 0) {
            //每日第一次访问
            //发送邮件 ,获得验证码
            code = SendEmail.send(email);
            //redisTemplate.boundValueOps(key).set(code, 120);
            //redisTemplate.boundValueOps(count).set("1", 24 * 60 * 60);
            stringRedisTemplate.opsForValue().set(key,code,120, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(count,"1",24 * 60 * 60,TimeUnit.SECONDS);
            System.out.println("第一次发验证码");

            System.out.println(code);

            return new Result(true, "验证码已发送");
        } else if (Integer.parseInt(countNum) >= 1 && Integer.parseInt(countNum) <= 2) {
            //发送邮件 ,获得验证码
            code = SendEmail.send(email);
            System.out.println(code);
            //redisTemplate.boundValueOps(key).set(code, 120);
            //redisTemplate.boundValueOps(count).increment();
            stringRedisTemplate.opsForValue().set(key,code,120,TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().increment(count);
            System.out.println("第二次发送验证码");
            return new Result(true, "验证码已经发送");
        } else {
            return new Result(false, "今日次数已经达上限制");
        }
    }
    /**
     * @Description: 验证验证码是否正确
     * @Param:
     * @return: true:表示验证成功,false:表示验证失败
     * @Author: 胡一文
     * @Date: 2020/10/26
     * @Time: 23:13
     */
    @ResponseBody
    @RequestMapping(value = "/verifyCode",method = RequestMethod.POST)
    public Result verifyCode(@RequestBody Map map){
        String email = (String) map.get("email");
        String verify_code = (String) map.get("code");
        //如果验证码为空
        if(verify_code==null){
            return new Result(false,"验证码不正确");
        }
        //组装拼接key
        key = "Verify_code" + email + ":code";
        //String code = (String) redisTemplate.opsForValue().get(key);
        String s = stringRedisTemplate.opsForValue().get(key);
        //判断验证码输入是否正确
        if (verify_code.equals(s)){
            return new Result(true,"验证成功");
        }else {
            return new Result(false,"验证码不正确");
        }
    }

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "home/login";
    }

    /**
     * 注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "home/register";
    }

}
