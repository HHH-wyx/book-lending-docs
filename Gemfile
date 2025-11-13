source 'https://rubygems.org'

# Jekyll core gem
gem 'jekyll', '~> 4.3'

# 核心插件
gem 'jekyll-feed', '~> 0.17.0'           # RSS 订阅功能
gem 'jekyll-seo-tag', '~> 2.8.0'         # SEO 优化标签
gem 'jekyll-sitemap', '~> 1.4.0'         # 站点地图生成
gem 'jekyll-paginate', '~> 1.1.0'        # 分页功能

# 增强型插件
gem 'jekyll-include-cache', '~> 0.2.1'    # 缓存包含文件，提高性能
gem 'jekyll-redirect-from', '~> 0.16.0'   # 页面重定向功能
gem 'jekyll-last-modified-at', '~> 1.3.0' # 显示文件最后修改时间
gem 'jekyll-toc', '~> 0.18.0'            # 自动生成目录
gem 'jekyll-remote-theme', '~> 0.4.3'     # 远程主题支持

# 代码高亮和语法增强
gem 'rouge', '~> 4.0'                    # 代码语法高亮

# 文档搜索功能
gem 'jekyll-lunr-js-search', '~> 3.3.0'   # 客户端搜索功能

# 开发环境
group :development do
  gem 'webrick'                              # Ruby 3.0+ 兼容
  gem 'jekyll-watch', '~> 2.2.0'         # 文件变更监控
end

# 操作系统特定依赖
platforms :mingw, :x64_mingw, :mswin, :jruby do
  gem 'tzinfo-data'
  gem 'wdm', '>= 0.1.1', platforms: [:mingw, :x64_mingw, :mswin]
end