package com.me.forum.services.impl;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.dao.ArticleReplyMapper;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.ArticleReply;
import com.me.forum.services.IArticleReplyService;
import com.me.forum.services.IArticleService;
import com.me.forum.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * IArticleReplyService的实现类
 *
 * @ClassName: ArticleReplyServiceImpl
 * @Author: JacksonLee
 * @Date: 2026/9/23
 */
@Slf4j
@Service
public class ArticleReplyServiceImpl implements IArticleReplyService {
    @Resource
    private ArticleReplyMapper articleReplyMapper;

    @Resource
    private IArticleService articleService;

    @Override
    public void create(ArticleReply articleReply) {
        // 非空校验
        if (articleReply == null
        || articleReply.getArticleId() == null
        || articleReply.getPostUserId() == null) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        if (StringUtil.isEmpty(articleReply.getContent())) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_REPLY_CONTENT_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_REPLY_CONTENT_IS_NULL));
        }

        // 设置属性默认值
        articleReply.setReplyId(null);
        articleReply.setReplyUserId(null);
        articleReply.setLikeCount(0);
        articleReply.setState((byte) 0);
        articleReply.setDeleteState((byte) 0);
        Date date = new Date();
        articleReply.setCreateTime(date);
        articleReply.setUpdateTime(date);

        // 调用DAO写入数据库
        int row = articleReplyMapper.insertSelective(articleReply);
        if (row != 1) {
            // 打印日志
            log.warn("{} row: {}", ResultCode.FAILED_ARTICLE_REPLY_INSERT, row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_REPLY_INSERT));
        }

        //更新帖子表中的回复数
        articleService.addOneArticleReplyCountById(articleReply.getArticleId());
        // 打印成功日志
        log.info("{}, articleId: {}, userId: {}", ResultCode.SUCCESS_ARTICLE_REPLY_INSERT, articleReply.getArticleId(), articleReply.getPostUserId());
    }

    @Override
    public List<ArticleReply> selectByArticleId(Long articleId) {
        // 非空校验
        if (articleId == null || articleId <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 调用DAO查询并返回
        return articleReplyMapper.selectByArticleId(articleId);
    }
}
