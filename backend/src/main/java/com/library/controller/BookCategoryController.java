package com.library.controller;

import com.library.common.Result;
import com.library.entity.BookCategory;
import com.library.service.BookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书分类Controller
 */
@RestController
@RequestMapping("/api/categories")
@Tag(name = "图书分类管理", description = "图书分类相关接口")
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    /**
     * 获取所有分类列表
     */
    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有图书分类列表")
    public Result<List<BookCategory>> getAllCategories() {
        List<BookCategory> categories = bookCategoryService.getAllCategories();
        return Result.success(categories);
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    public Result<BookCategory> getCategoryById(@PathVariable Long id) {
        BookCategory category = bookCategoryService.getCategoryById(id);
        if (category == null) {
            return Result.fail("分类不存在");
        }
        return Result.success(category);
    }

    /**
     * 添加分类
     */
    @PostMapping
    @Operation(summary = "添加分类", description = "添加新的图书分类")
    public Result<Boolean> addCategory(@RequestBody BookCategory category) {
        boolean result = bookCategoryService.addCategory(category);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("添加失败");
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "更新分类信息")
    public Result<Boolean> updateCategory(@PathVariable Long id, @RequestBody BookCategory category) {
        category.setId(id);
        boolean result = bookCategoryService.updateCategory(category);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("更新失败");
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定的图书分类")
    public Result<Boolean> deleteCategory(@PathVariable Long id) {
        boolean result = bookCategoryService.deleteCategory(id);
        if (result) {
            return Result.success(true);
        }
        return Result.fail("删除失败");
    }
}