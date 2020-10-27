package cn.yiwen.service;

import cn.yiwen.entity.User;

/**
 * @auther 胡一文
 * @date 2020/9/28
 */
public interface UserService extends BaseService<User> {

    User findByName(String name);
}
