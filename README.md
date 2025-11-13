# 图书借阅系统文档站点

这是使用Jekyll构建的图书借阅系统文档站点。

## 环境要求

- Ruby 3.0+
- Bundler
- Jekyll 4.3+

## 安装和运行

1. 确保您已安装Ruby（如用户提到的Ruby 3.4.4-2-x64版本）
2. 打开"Start Command Prompt with Ruby"快捷方式
3. 导航到项目目录：
   ```
   cd d:\java\Practices
   ```
4. 安装依赖：
   ```
   gem install bundler
   bundle install
   ```
5. 运行Jekyll服务：
   ```
   bundle exec jekyll serve
   ```
6. 在浏览器中访问：http://localhost:4000

## 项目结构

- `_config.yml` - Jekyll配置文件
- `Gemfile` - Ruby依赖管理
- `_layouts/` - 页面布局模板
- `_documentation/` - 文档内容
- `_api_reference/` - API参考文档
- `_site/` - 生成的静态网站文件

## 插件说明

本项目使用了以下Jekyll插件：

- **核心插件**：jekyll-feed, jekyll-seo-tag, jekyll-sitemap, jekyll-paginate
- **增强插件**：jekyll-include-cache, jekyll-redirect-from, jekyll-last-modified-at, jekyll-toc
- **搜索功能**：jekyll-lunr-js-search
- **代码高亮**：rouge

## 自定义和扩展

1. 编辑`_config.yml`修改站点配置
2. 在`_documentation/`目录添加或修改文档
3. 在`_api_reference/`目录添加API文档
4. 修改`_layouts/default.html`自定义页面布局

## 注意事项

- 确保Ruby已正确安装并添加到系统PATH
- 如果遇到依赖安装问题，可尝试更新RubyGems：`gem update --system`
- 对于Windows用户，建议使用RubyInstaller安装的命令提示符运行Jekyll