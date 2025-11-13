package com.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图书实体类
 */
@Data
@TableName("book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 图书编码
     */
    private String code;

    /**
     * 书名
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称（关联查询）
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 图书状态（0:可借阅, 1:已借出, 2:下架）
     */
    private Integer status;

    /**
     * 简介
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 获取状态名称
     */
    @TableField(exist = false)
    public String getStatusName() {
        switch (status) {
            case 0:
                return "可借阅";
            case 1:
                return "已借出";
            case 2:
                return "下架";
            default:
                return "未知";
        }
    }
}