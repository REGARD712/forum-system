package com.me.forum.dao;

import com.me.forum.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article row);

    int insertSelective(Article row);

    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article row);

    int updateByPrimaryKeyWithBLOBs(Article row);

    int updateByPrimaryKey(Article row);

    List<Article> selectAll ();

    List<Article> selectAllByBoardId (@Param("boardId") Long boardId);

    List<Article> selectAllByUserId (@Param("userId") Long userId);

    /**
     * @param id 帖子id
     * @return Article 帖子详情
     * @description: 根据id查询帖子详情(关联三表:帖子表,用户表,板块表)
     */
    Article selectDetailById (@Param("id") Long id);
}