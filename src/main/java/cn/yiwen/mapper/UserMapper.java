package cn.yiwen.mapper;

import cn.yiwen.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther 胡一文
 * @date 2020/9/28
 */
@Mapper
public interface UserMapper {

    List<User> findAll();

    List<User> findById(Long id);

    void create(User user);

    void delete(Long id);

    void update(User user);

    User findByName(String name);
}
