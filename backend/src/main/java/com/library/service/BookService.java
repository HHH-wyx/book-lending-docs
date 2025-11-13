package com.library.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.library.entity.Book;

import java.util.List;

/**
 * 图书Service接口
 */
public interface BookService extends IService<Book> {

    /**
     * 分页查询图书列表
     */
    IPage<Book> getBookPage(Integer pageNum, Integer pageSize, String keyword, Long categoryId);

    /**
     * 根据ID获取图书详情
     */
    Book getBookById(Long id);

    /**
     * 添加图书
     */
    boolean addBook(Book book);

    /**
     * 更新图书信息
     */
    boolean updateBook(Book book);

    /**
     * 删除图书
     */
    boolean deleteBook(Long id);

    /**
     * 更新图书状态
     */
    boolean updateBookStatus(Long id, Integer status);

    /**
     * 批量更新图书状态
     */
    boolean updateBatchBookStatus(List<Long> ids, Integer status);

    /**
     * 根据图书编码查询图书
     */
    Book getBookByCode(String code);
}