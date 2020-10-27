package cn.yiwen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther 胡一文
 * @date 2020/9/28
 */
@Data
public class User implements Serializable {

    private Long id; //编号
    private String username; //用户名
    private String password; //密码
    private String email;//邮箱
    private String photo;//头像图片路径

    public User(String username, String password, String email, String photo) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.photo = photo;
    }

    public User(){

    }
}
