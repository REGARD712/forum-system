package com.me.forum.services;

import com.me.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 帖子回复相关业务逻辑处理层的接口
 *
 * @InterfaceName: IArticleReplyService
 * @Author: JacksonLee
 * @Date: 2026/9/23
 */
public interface IArticleReplyService {
    /**
     * 新增一条帖子回复(将帖子回复写入帖子回复表+在帖子表中更新帖子回复数)
     * @param articleReply 帖子回复信息对象
     * @return void
     */
    @Transactional  // (将帖子回复写入帖子回复表+在帖子表中更新帖子回复数)
    void create (ArticleReply articleReply);

    /**
     * 根据帖子Id查询回复列表
     * @param articleId 帖子Id
     * @return List<ArticleReply>
     */
    List<ArticleReply> selectByArticleId (Long articleId);
}
