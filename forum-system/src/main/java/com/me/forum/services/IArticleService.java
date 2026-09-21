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

    /**
     * @param id 帖子id
     * @return Article 帖子详情
     * @Description: 根据id查询帖子详情(关联三表:帖子表,用户表,板块表)
     */
    Article selectDetailById (Long id);

    /**
     * 根据Id查询帖子信息
     * @param id
     * @return Article
     */
    Article selectById (Long id);

    /**
     * 修改帖子
     * @param id 帖子Id
     * @param title 帖子标题
     * @param content 帖子内容
     * @return void
     */
    void modify (Long id, String title, String content);

    /**
     * 点赞帖子
     * @param id 帖子Id
     * @return void
     */
    void thumbsUpById (Long id);
}
