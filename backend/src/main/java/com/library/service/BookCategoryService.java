package com.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.entity.BookCategory;

import java.util.List;

/**
 * 图书分类Service接口
 */
public interface BookCategoryService extends IService<BookCategory> {

    /**
     * 获取所有分类列表，按排序升序排列
     */
    List<BookCategory> getAllCategories();

    /**
     * 根据ID获取分类信息
     */
    BookCategory getCategoryById(Long id);

    /**
     * 添加分类
     */
    boolean addCategory(BookCategory category);

    /**
     * 更新分类
     */
    boolean updateCategory(BookCategory category);

    /**
     * 删除分类
     */
    boolean deleteCategory(Long id);
}