package com.me.forum.services.impl;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.dao.ArticleMapper;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.Article;
import com.me.forum.model.Board;
import com.me.forum.model.User;
import com.me.forum.services.IArticleService;
import com.me.forum.services.IBoardService;
import com.me.forum.services.IUserService;
import com.me.forum.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ArticleServiceImpl
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: IArticleService的实现类
 */
@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {
    @Resource
    private ArticleMapper articleMapper;

    // 关联用户和版块的Service
    @Resource
    private IUserService userService;

    @Resource
    private IBoardService boardService;

    @Override
    public void create(Article article) {
        // 非空校验
        if (article == null || article.getUserId() == null || article.getBoardId() == null) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        if (StringUtil.isEmpty(article.getTitle())
        || StringUtil.isEmpty(article.getContent())) {
            // 打印日志
            log.warn(ResultCode.FAILED_NEW_ARTICLE_TITLE_OR_CONTENT_IS_NULL.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NEW_ARTICLE_TITLE_OR_CONTENT_IS_NULL));
        }

        // 设置默认值
        article.setVisitCount(0);
        article.setReplyCount(0);
        article.setLikeCount(0);
        article.setState((byte) 0);
        article.setDeleteState((byte) 0);
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);

        // 写入数据库
        int row = articleMapper.insertSelective(article);
        // 非空校验
        if (row != 1) {
            // 打印日志
            log.warn("{} row: {}", ResultCode.FAILED_NEW_ARTICLE_INSERT, row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NEW_ARTICLE_INSERT));
        }

        // 获取用户信息
        User user = userService.selectById(article.getUserId());
        if (user == null) {
            // 打印日志
            log.warn("{} userId: {}", ResultCode.FAILED_NEW_ARTICLE_INSERT, article.getUserId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NEW_ARTICLE_INSERT));
        }
        // 更新用户贴子数
        userService.addOneArticleCountById(user.getId());

        // 获取板块信息
        Board board = boardService.selectByPrimaryKey(article.getBoardId());
        if (board == null) {
            // 打印日志
            log.warn(ResultCode.FAILED_NEW_ARTICLE_INSERT.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_NEW_ARTICLE_INSERT));
        }
        // 更新版块贴子数
        boardService.addOneArticleCountById(board.getId());

        // 发布新贴成功
        log.info("{} articleId: {}, userId: {}, boardId: {}", ResultCode.SUCCESS_NEW_ARTICLE_INSERT,
                article.getId(), user.getId(), board.getId());
    }

    @Override
    public List<Article> selectAll() {
        // 调用DAO返回结果
        return articleMapper.selectAll();
    }

    @Override
    public List<Article> selectAllByBoardId(Long boardId) {
        // 非空校验
        if (boardId == null || boardId <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 校验版块是否存在
        Board board = boardService.selectByPrimaryKey(boardId);
        if (board == null) {
            // 打印日志
            log.warn("{} boardId: {}", ResultCode.FAILED_BOARD_NOT_EXISTS, boardId);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        //查询指定帖子列表并返回
        return articleMapper.selectAllByBoardId(boardId);
    }

    @Override
    public Article selectDetailById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            // 打印日志
            log.warn("{} id: {}", ResultCode.FAILED_PARAMS_VALIDATE, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 调用DAO查询帖子详情
        Article article = articleMapper.selectDetailById(id);
        // 非空校验
        if (article == null) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }

        // 更新帖子访问数
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setVisitCount(article.getVisitCount() + 1);
        // 调用DAO写入数据库
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        // 校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.ERROR_SERVICES.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }

        // 返回结果
        article.setVisitCount(article.getVisitCount() + 1);
        return article;
    }
}
