package cn.yiwen.mapper;

import cn.yiwen.entity.Goods;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @auther 胡一文
 * @date 2020/9/19
 */
@Mapper
public interface GoodsMapper {

    List<Goods> findAll();

    Page<Goods> findByPage(Goods goods);

    List<Goods> findById(Long id);

    void create(Goods goods);

    void update(Goods goods);

    void delete(Long id);
}
