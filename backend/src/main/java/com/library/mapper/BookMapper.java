package com.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.library.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图书Mapper接口
 */
public interface BookMapper extends BaseMapper<Book> {

    /**
     * 分页查询图书，支持搜索和分类筛选
     */
    IPage<Book> selectPageWithCategory(Page<?> page, 
                                          @Param("keyword") String keyword,
                                          @Param("categoryId") Long categoryId);

    /**
     * 根据ID查询图书详情，包含分类信息
     */
    Book selectByIdWithCategory(Long id);

    /**
     * 批量更新图书状态
     */
    int updateBatchStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}