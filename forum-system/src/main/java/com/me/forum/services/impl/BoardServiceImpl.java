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
}
