package com.me.forum.services;

import com.me.forum.model.Board;

import java.util.List;

/**
 * @InterfaceName：IBoradService
 * @Author: JacksonLee
 * @Date: 2026/9/14
 * @Description: 版块相关业务逻辑的接口
 */
public interface IBoardService {
    /**
     * @param num 要查询的板块条数
     * @return List<Board> 返回的结果集
     * @description: 查询前Num条有效的版块记录
     */
    List<Board> selectByNum (Integer num);
}
