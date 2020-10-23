package cn.tycoding.controller.admin;

import cn.tycoding.entity.Result;
import cn.tycoding.entity.User;
import cn.tycoding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @auther TyCoding
 * @date 2018/9/28
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

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
    public Result register(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            userService.create(new User(username, password));
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            attributes.getRequest().getSession().setAttribute("user", new User(username, password)); //将登陆用户信息存入到session域对象中
            return new Result(true, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "发生未知错误");
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
    @RequestMapping(value = "/verifyUserName" ,method= RequestMethod.GET)
    public Result verifyUserName(@RequestParam("username") String username) {
        User byName = userService.findByName(username);
        System.out.println(username);
        System.out.println("--------------");
        System.out.println(byName);
        if (byName != null) {
            return new Result(true,"用户已存在");
        } else {
            return new Result(false,"用户名可用");
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
