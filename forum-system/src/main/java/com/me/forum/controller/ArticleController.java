package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.config.AppConfig;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.Article;
import com.me.forum.model.Board;
import com.me.forum.model.User;
import com.me.forum.services.IArticleService;
import com.me.forum.services.IBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ArticleController
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: 帖子相关接口
 */
@Slf4j
@Api(tags = "文章接口")
@RequestMapping("/article")
@RestController
public class ArticleController {
    @Resource
    private IArticleService articleService;

    @Resource
    private IBoardService boardService;

    /**
     * @param boardId 版块Id
     * @param title 文章标题
     * @param content 文章内容
     * @return AppResult 统一的返回格式
     * @description: 发布新贴
     */
    @ApiOperation("发布新贴")
    @PostMapping("/create")
    public AppResult create (HttpServletRequest request,
                             @ApiParam("版块Id") @RequestParam("boardId") @NonNull Long boardId,
                             @ApiParam("文章标题") @RequestParam("title") @NonNull String title,
                             @ApiParam("文章内容") @RequestParam("content") @NonNull String content) {
        // 校验当前用户是否被禁言
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        if (user.getState() == 1) {
            // 打印日志
            log.warn("{} userId: {}", ResultCode.FAILED_USER_BANNED, user.getId());
            // 当前用户已被禁言
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 校验板块信息
        Board board = boardService.selectByPrimaryKey(boardId);
        if (board == null || board.getState() == 1 || board.getDeleteState() == 1) {
            // 打印日志
            assert board != null;
            log.warn("{} boardId: {}", ResultCode.FAILED_BOARD_BANNED, board.getId());
            // 板块信息无效
            return AppResult.failed(ResultCode.FAILED_BOARD_BANNED);
        }

        // 封装文章对象
        Article article = new Article();
        article.setUserId(user.getId());
        article.setBoardId(boardId);
        article.setTitle(title);
        article.setContent(content);

        // 调用Service
        articleService.create(article);
        // 返回结果
        return AppResult.success(ResultCode.SUCCESS_NEW_ARTICLE_INSERT);
    }

    /**
     * @param boardId 版块Id(若为空则是首页,查询所有帖子,否则根据版块Id查询帖子)
     * @return AppResult<List<Article>>
     * @description: 获取帖子列表
     */
    @ApiOperation("获取帖子列表")
    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId (@ApiParam("版块Id") @RequestParam(value = "boardId", required = false) Long boardId) {
        // 定义返回的结果为null
        List<Article> articles = null;

        // 判断boardId是否为空
        if (boardId != null) {
            // 查询指定板块下的帖子
            articles = articleService.selectAllByBoardId(boardId);
        } else {
            // 查询所有帖子
            articles = articleService.selectAll();
        }
        // 非空校验
        if (articles == null) {
            articles = new ArrayList<>();
        }

        // 返回结果
        return AppResult.success(articles);
    }

    /**
     * @param id 帖子Id
     * @return AppResult<Article>
     * @Description: 根据Id查询帖子详情
     */
    @ApiOperation("查询帖子详情")
    @GetMapping("/details")
    public AppResult<Article> getDetails (HttpServletRequest request,
                                          @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id) {
        // 从Session中获取用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);

        // 调用Service获取帖子详情
        Article article = articleService.selectDetailById(id);

        // 非空校验
        if (article == null) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }

        // 判断当前用户是否为作者
        if (user.getId().equals(article.getUserId())) {
            // 是作者
            article.setOwn(true);
        }

        // 返回结果
        return AppResult.success(article);
    }

    /**
     * 更新帖子
     * @param request HTTP请求
     * @param id      帖子Id
     * @param title   帖子标题
     * @param content 帖子内容
     * @return AppResult
     */
    @ApiOperation("更新帖子")
    @PostMapping("/modify")
    public AppResult modify (HttpServletRequest request,
                             @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id,
                             @ApiParam("帖子标题") @RequestParam("title") @NonNull String title,
                             @ApiParam("帖子正文") @RequestParam("content") @NonNull String content) {
        // 获取当前登录用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        // 校验用户状态
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 获取帖子信息
        Article article = articleService.selectById(id);
        // 非空校验
        if (article == null) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 校验帖子状态
        if (article.getState() == 1 || article.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }


        // 判断用户是否为作者
        if (!article.getUserId().equals(user.getId())) {
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        // 调用Service更新帖子
        articleService.modify(id, title, content);
        // 打印日志
        log.info("更新帖子成功, articleId: {}, userId: {}. ", id, user.getId());

        // 返回成功结果
        return AppResult.success();
    }

    /**
     * 点赞帖子
     * @param id 帖子Id
     * @return AppResult
     */
    @ApiOperation("点赞帖子")
    @PostMapping("thumbsUp")
    public AppResult thumbsUp (HttpServletRequest request,
                               @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id) {
        // 校验用户状态
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 获取帖子信息
        Article article = articleService.selectById(id);
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

        // 调用Service
        articleService.thumbsUpById(id);
        // 打印日志
        log.info("点赞成功, articleId: {}", article.getId());

        // 返回成功结果
        return AppResult.success();
    }

    /**
     * 删除帖子
     * @param request HTTP请求
     * @param id 帖子Id
     * @return AppResult
     */
    @ApiOperation("删除帖子")
    @PostMapping("/delete")
    public AppResult deleteById (HttpServletRequest request,
                                 @ApiParam("帖子Id") @RequestParam("id") @NonNull Long id) {
        // 校验用户状态
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION_KEY);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 获取帖子信息
        Article article = articleService.selectById(id);
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
        // 校验当前登录用户是否为作者
        if (!article.getUserId().equals(user.getId())) {
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }

        // 调用Service删除帖子
        articleService.deleteById(id);
        // 返回成功结果
        return AppResult.success();
    }
}
