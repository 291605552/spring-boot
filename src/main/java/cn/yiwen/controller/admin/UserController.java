package cn.yiwen.controller.admin;

import cn.yiwen.entity.User;
import cn.yiwen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther 胡一文
 * @date 2020/9/29
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }

}
