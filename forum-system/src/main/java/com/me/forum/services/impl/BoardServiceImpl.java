package com.me.forum.services.impl;

import com.me.forum.common.AppResult;
import com.me.forum.common.ResultCode;
import com.me.forum.dao.BoardMapper;
import com.me.forum.exception.ApplicationException;
import com.me.forum.model.Board;
import com.me.forum.services.IBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: BoardServiceImpl
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: IBoardService的实现类
 */
@Slf4j
@Service
public class BoardServiceImpl implements IBoardService {
    @Resource
    private BoardMapper boardMapper;


    @Override
    public Board selectByPrimaryKey(Long id) {
        // 非空校验
        if (id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 返回查询的板块信息
        return boardMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Board> selectByNum(Integer num) {
        // 非空校验
        if (num <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 查询数据库并返回（无需校验结果集是否为空）
        return boardMapper.selectByNum(num);
    }

    @Override
    public void addOneArticleCountById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            // 打印日志
            log.warn("{} id: {}", ResultCode.FAILED_BOARD_ARTICLE_COUNT_UPDATE, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT_UPDATE));
        }

        // 查询对应板块信息
        Board board = boardMapper.selectByPrimaryKey(id);
        // 非空校验
        if (board == null) {
            // 打印日志
            log.warn("{} boardId: {}", ResultCode.FAILED_BOARD_NOT_EXISTS, id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }

        // 更新帖子数量
        Board updateBoard = new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount() + 1);
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        if (row != 1) {
            // 打印日志
            log.warn("{} row: {}", ResultCode.FAILED_BOARD_ARTICLE_COUNT_UPDATE, row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT_UPDATE));
        }
    }
}
