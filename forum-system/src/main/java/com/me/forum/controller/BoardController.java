package com.me.forum.controller;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.Board;
import com.me.forum.services.IBoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BoardController
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: 板块相关接口
 */
@Slf4j
@Api(tags = "版块接口")
@RequestMapping("/board")
@RestController
public class BoardController {
    @Resource
    private IBoardService boardService;

    // 从配置文件中获取主页中显示的版块个数，默认为9
    @Value("${me-forum.index.board-num:9}")
    private Integer indexBoardNum;

    /**
     * @param
     * @return AppResult<List<Board>> 返回封装统一返回格式后的板块列表
     * @description: 查询首页板块列表
     */
    @ApiOperation("查询首页板块列表")
    @GetMapping("/topList")
    public AppResult<List<Board>> topList () {
        // 打印日志
        log.info("首页板块个数: {}", indexBoardNum);
        // 调用Service查询结果
        List<Board> boards = boardService.selectByNum(indexBoardNum);
        // 非空校验
        if (boards == null || boards.isEmpty()) {
            boards = new ArrayList<>(); // 让其能够序列化以JSON格式返回
        }
        // 返回结果
        return AppResult.success(boards);
    }

    /**
     * @param id 版块id
     * @return AppResult<Board>
     * @description: 获取板块信息
     */
    @ApiOperation("获取板块信息")
    @GetMapping("/selectById")
    public AppResult<Board> selectById (@ApiParam("版块Id") @RequestParam("id") @NonNull Long id) {
        // 调用Service查询版块信息
        Board board = boardService.selectByPrimaryKey(id);

        // 非空校验
        if (board == null) {
            // 打印日志
            log.warn("{} id: {}", ResultCode.FAILED_BOARD_NOT_EXISTS, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        if (board.getDeleteState() == 1) {
            // 打印日志
            log.warn("{} id: {}", ResultCode.FAILED_BOARD_NOT_EXISTS, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        // 返回结果
        return AppResult.success(board);
    }
}
