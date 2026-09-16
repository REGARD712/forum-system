package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.config.AppConfig;
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
     * @param boardId 版块Id(若为空则是首页,查询所有帖子)
     * @return AppResult<List<Article>>
     * @description: 获取帖子列表
     */
    @ApiOperation("获取帖子列表")
    @GetMapping("/getAllByBoardId")
    public AppResult<List<Article>> getAllByBoardId (@ApiParam("版块Id") @RequestParam(value = "board", required = false) Long boardId) {
        // 调用Service查询所有帖子
        List<Article> articles = articleService.selectAll();
        // 非空校验
        if (articles == null) {
            articles = new ArrayList<>();
        }
        // 返回结果
        return AppResult.success(articles);
    }
}
