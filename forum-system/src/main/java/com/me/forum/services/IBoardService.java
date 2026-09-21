package com.me.forum.services;

import com.me.forum.model.Board;

import java.util.List;

/**
 * @InterfaceName：IBoradService
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: 版块相关业务逻辑的接口
 */
public interface IBoardService {
    /**
     * @param id 版块id
     * @return Board 对应的板块信息
     * @description: 根据板块id查询对应板块信息
     */
    Board selectByPrimaryKey(Long id);

    /**
     * @param num 要查询的板块条数
     * @return List<Board> 返回的结果集
     * @description: 查询前Num条有效的版块记录
     */
    List<Board> selectByNum (Integer num);

    /**
     * @param id 版块id
     * @return void
     * @description: 更新对应板块的帖子数量(增加)
     */
    void addOneArticleCountById (Long id);

    /**
     * @param id 版块id
     * @return void
     * @description: 更新对应板块的帖子数量(减少)
     */
    void subOneArticleCountById (Long id);
}
