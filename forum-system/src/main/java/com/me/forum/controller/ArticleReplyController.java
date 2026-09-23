package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.config.AppConfig;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.Article;
import com.me.forum.model.ArticleReply;
import com.me.forum.model.User;
import com.me.forum.services.IArticleReplyService;
import com.me.forum.services.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 帖子回复接口
 *
 * @ClassName: ArticleReplyController
 * @Author: JacksonLee
 * @Date: 2026/9/23
 */
@Api(tags = "帖子回复接口")
@Slf4j
@RequestMapping("/reply")
@RestController
public class ArticleReplyController {
    @Resource
    private IArticleReplyService articleReplyService;

    @Resource
    private IArticleService articleService;

    /**
     * 发布帖子回复
     * @param request HTTP请求
     * @param articleId 帖子Id
     * @param content 回复内容
     * @return AppResult
     */
    @ApiOperation("发布帖子回复")
    @PostMapping("/create")
    public AppResult create (HttpServletRequest request,
                             @ApiParam("帖子Id") @RequestParam("articleId") @NonNull Long articleId,
                             @ApiParam("回复内容") @RequestParam("content") @NonNull String content) {
        // 校验当前用户是否被禁言
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        if (user.getState() == 1) {
            // 打印日志
            log.warn("{} userId: {}", ResultCode.FAILED_USER_BANNED, user.getId());
            // 当前用户已被禁言
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 获取帖子信息
        Article article = articleService.selectById(articleId);
        // 非空校验
        if (article == null || article.getDeleteState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 校验帖子状态
        if (article.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED));
        }

        // 构建回复对象
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(articleId);
        articleReply.setContent(content);
        articleReply.setPostUserId(user.getId());

        // 调用Service写入数据库
        articleReplyService.create(articleReply);

        // 返回成功结果
        return AppResult.success(ResultCode.SUCCESS_ARTICLE_REPLY_INSERT);
    }
}
