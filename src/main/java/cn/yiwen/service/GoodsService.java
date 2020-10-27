package cn.yiwen.service;

import cn.yiwen.entity.Goods;
import cn.yiwen.entity.PageBean;

/**
 * @auther 胡一文
 * @date 2020/9/19
 */
public interface GoodsService extends BaseService<Goods> {

    /**
     * 分页查询
     * @param goods 查询条件
     * @param pageCode 当前页
     * @param pageSize 每页的记录数
     * @return
     */
    PageBean findByPage(Goods goods, int pageCode, int pageSize);
}
