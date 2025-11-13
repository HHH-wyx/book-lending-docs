package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.BookCategory;
import com.library.mapper.BookCategoryMapper;
import com.library.service.BookCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图书分类Service实现类
 */
@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements BookCategoryService {

    @Override
    public List<BookCategory> getAllCategories() {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public BookCategory getCategoryById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean addCategory(BookCategory category) {
        return save(category);
    }

    @Override
    public boolean updateCategory(BookCategory category) {
        return updateById(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        return removeById(id);
    }
}