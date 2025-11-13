package com.library.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.library.common.Result;
import com.library.entity.Book;
import com.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书Controller
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "图书管理", description = "图书相关接口")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 分页查询图书列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询图书", description = "分页查询图书列表，支持搜索和分类筛选")
    public Result<IPage<Book>> getBookPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        
        IPage<Book> page = bookService.getBookPage(pageNum, pageSize, keyword, categoryId);
        return Result.success(page);
    }

    /**
     * 获取所有图书
     */
    @GetMapping
    @Operation(summary = "获取所有图书", description = "获取所有图书列表")
    public Result<List<Book>> getAllBooks() {
        List<Book> books = bookService.list();
        return Result.success(books);
    }

    /**
     * 根据ID获取图书详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取图书详情", description = "根据ID获取图书详情")
    public Result<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return Result.fail("图书不存在");
        }
        return Result.success(book);
    }

    /**
     * 根据编码获取图书
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "根据编码获取图书", description = "根据图书编码获取图书信息")
    public Result<Book> getBookByCode(@PathVariable String code) {
        Book book = bookService.getBookByCode(code);
        if (book == null) {
            return Result.fail("图书不存在");
        }
        return Result.success(book);
    }

    /**
     * 添加图书
     */
    @PostMapping
    @Operation(summary = "添加图书", description = "添加新的图书")
    public Result<Boolean> addBook(@RequestBody Book book) {
        // 检查编码是否重复
        if (bookService.getBookByCode(book.getCode()) != null) {
            return Result.fail("图书编码已存在");
        }
        
        // 默认状态为可借阅
        book.setStatus(0);
        
        boolean result = bookService.addBook(book);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("添加失败");
    }

    /**
     * 更新图书
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新图书", description = "更新图书信息")
    public Result<Boolean> updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        boolean result = bookService.updateBook(book);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("更新失败");
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除图书", description = "删除指定的图书")
    public Result<Boolean> deleteBook(@PathVariable Long id) {
        boolean result = bookService.deleteBook(id);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("删除失败");
    }

    /**
     * 批量删除图书
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除图书", description = "批量删除图书")
    public Result<Boolean> batchDeleteBooks(@RequestBody List<Long> ids) {
        boolean result = bookService.removeByIds(ids);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("删除失败");
    }
}