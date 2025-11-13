package com.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.entity.Book;
import com.library.mapper.BookMapper;
import com.library.service.BookService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 图书Service实现类
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public IPage<Book> getBookPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId) {
        Page<Book> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectPageWithCategory(page, keyword, categoryId);
    }

    @Override
    public Book getBookById(Long id) {
        return baseMapper.selectByIdWithCategory(id);
    }

    @Override
    public boolean addBook(Book book) {
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        return save(book);
    }

    @Override
    public boolean updateBook(Book book) {
        book.setUpdateTime(LocalDateTime.now());
        return updateById(book);
    }

    @Override
    public boolean deleteBook(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateBookStatus(Long id, Integer status) {
        Book book = new Book();
        book.setId(id);
        book.setStatus(status);
        book.setUpdateTime(LocalDateTime.now());
        return updateById(book);
    }

    @Override
    public boolean updateBatchBookStatus(List<Long> ids, Integer status) {
        return baseMapper.updateBatchStatus(ids, status) > 0;
    }

    @Override
    public Book getBookByCode(String code) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return baseMapper.selectOne(queryWrapper);
    }
}