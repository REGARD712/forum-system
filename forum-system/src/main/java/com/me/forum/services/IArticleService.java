package com.me.forum.services;

import com.me.forum.model.Article;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @InterfaceName：IArticleService
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: 帖子相关业务逻辑的接口
 */
public interface IArticleService {
    /**
     * @param article 要被发布的帖子信息
     * @return void
     * @description: 发布帖子
     */
    @Transactional // 被事务管理，要么全部成功要么全部失败(1条insert,2条update)
    void create (Article article);

    /**
     * @param
     * @return List<Article>
     * @description: 查询帖子列表
     */
    List<Article> selectAll ();

    /**
     * @param boardId 版块Id
     * @return List<Article>
     * @description: 查询指定板块的帖子列表
     */
    List<Article> selectAllByBoardId (Long boardId);
}
