# 图书借阅系统文档 - GitHub Pages部署指南

本文档提供了将图书借阅系统Jekyll文档网站部署到GitHub Pages的详细步骤。

## 目录

- [环境要求](#环境要求)
- [准备工作](#准备工作)
- [GitHub仓库设置](#github仓库设置)
- [使用部署脚本](#使用部署脚本)
- [手动部署步骤](#手动部署步骤)
- [故障排除](#故障排除)
- [自定义域名设置（可选）](#自定义域名设置可选)

## 环境要求

### 本地开发环境

- **Git**: 版本控制系统，用于代码管理和部署
- **Ruby**: 版本 2.7 或更高
- **RubyGems**: 包管理工具
- **Bundler**: Ruby依赖管理工具
- **Jekyll**: 静态站点生成器

### GitHub要求

- GitHub账号
- 一个GitHub仓库（公共或私人）

## 准备工作

### 1. 安装必要软件

#### Windows系统

1. **安装Git**
   - 从[Git官网](https://git-scm.com/download/win)下载并安装Git
   - 安装时选择默认选项即可

2. **安装Ruby环境**
   - 推荐使用[RubyInstaller](https://rubyinstaller.org/downloads/)
   - 下载并安装Ruby+Devkit版本（推荐2.7.x或3.0.x）
   - 安装过程中勾选"Add Ruby executables to your PATH"

3. **安装Bundler**
   - 打开命令提示符（CMD）或PowerShell
   - 执行：`gem install bundler`

4. **安装Jekyll**
   - 执行：`gem install jekyll`

#### macOS系统

1. **安装Git**
   - 通过Homebrew：`brew install git`
   - 或从[Git官网](https://git-scm.com/download/mac)下载安装

2. **安装Ruby环境**
   - 使用Homebrew：`brew install ruby`
   - 或使用RVM：`\curl -sSL https://get.rvm.io | bash -s stable --ruby`

3. **安装Bundler**
   - 执行：`gem install bundler`

4. **安装Jekyll**
   - 执行：`gem install jekyll`

#### Linux系统

1. **安装Git**
   - Ubuntu/Debian: `sudo apt-get install git`
   - CentOS/RHEL: `sudo yum install git`

2. **安装Ruby环境**
   - Ubuntu/Debian: 
     ```bash
     sudo apt-get install ruby-full build-essential zlib1g-dev
     echo '# Install Ruby Gems to ~/gems' >> ~/.bashrc
     echo 'export GEM_HOME="$HOME/gems"' >> ~/.bashrc
     echo 'export PATH="$HOME/gems/bin:$PATH"' >> ~/.bashrc
     source ~/.bashrc
     ```

3. **安装Bundler**
   - 执行：`gem install bundler`

4. **安装Jekyll**
   - 执行：`gem install jekyll`

### 2. 克隆仓库（如果尚未克隆）

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
cd YOUR_REPOSITORY
```

## GitHub仓库设置

### 1. 创建新仓库（如果尚未创建）

1. 登录GitHub账号
2. 点击右上角的"+"图标，选择"New repository"
3. 输入仓库名称（如`book-lending-docs`）
4. 选择仓库可见性（Public或Private）
5. 勾选"Add a README file"（可选）
6. 点击"Create repository"

### 2. 配置GitHub Pages

创建仓库后，需要配置GitHub Pages以启用网站托管：

1. 进入仓库设置
2. 在左侧导航栏中找到并点击"Pages"
3. 在"Source"部分，选择发布来源：
   - 如果使用手动部署脚本，选择"Branch: gh-pages"（部署后会自动创建）
   - 如果直接推送代码，选择"Branch: master"或"Branch: main"和对应的文件夹
4. 点击"Save"

## 使用部署脚本

项目中已包含自动化部署脚本，可以简化部署过程。

### Windows系统

1. 确保已完成[环境要求](#环境要求)中的安装
2. 在文件浏览器中找到项目根目录
3. 双击运行 `deploy-to-ghpages.bat`
4. 按照提示输入GitHub用户名和仓库名
5. 脚本会自动构建并部署站点

### macOS/Linux系统

1. 确保已完成[环境要求](#环境要求)中的安装
2. 打开终端，进入项目根目录
3. 赋予脚本执行权限：`chmod +x deploy-to-ghpages.sh`
4. 运行脚本：`./deploy-to-ghpages.sh`
5. 按照提示输入GitHub用户名和仓库名
6. 脚本会自动构建并部署站点

## 手动部署步骤

如果不想使用自动化脚本，也可以按照以下步骤手动部署：

### 1. 配置 _config.yml

确保 `_config.yml` 文件中的配置正确：

```yaml
# GitHub Pages URL 格式: https://username.github.io/repository-name
url: "https://YOUR_USERNAME.github.io" # 替换为您的GitHub用户名
baseurl: "/YOUR_REPOSITORY" # 替换为您的仓库名
```

### 2. 安装依赖

```bash
bundle install
```

### 3. 构建站点

```bash
bundle exec jekyll build
```

### 4. 部署到GitHub Pages

#### 方法一：使用gh-pages分支

```bash
# 创建gh-pages分支
mkdir _ghpages_temp
cd _ghpages_temp
git init
git config user.name "GitHub Actions"
git config user.email "actions@github.com"

# 复制构建文件
cp -r ../_site/* .

# 提交并推送
git add .
git commit -m "部署站点到GitHub Pages"
git push -f https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git master:gh-pages

# 清理
cd ..
rm -rf _ghpages_temp
```

#### 方法二：使用GitHub Actions自动部署

1. 创建 `.github/workflows/deploy.yml` 文件：

```yaml
name: Deploy Jekyll site to GitHub Pages

on:
  push:
    branches: [ main, master ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v3

      - name: Setup Ruby
        uses: ruby/setup-ruby@v1
        with:
          ruby-version: '2.7'
          bundler-cache: true

      - name: Install dependencies
        run: |
          gem install bundler
          bundle install

      - name: Build site
        run: bundle exec jekyll build

      - name: Deploy
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./_site
```

2. 提交并推送此文件到仓库
3. GitHub Actions会自动构建并部署站点

## 故障排除

### 1. 构建错误

如果构建过程中出现错误，尝试以下解决方案：

- **Gem依赖问题**：
  ```bash
  gem update bundler
  bundle update
  ```

- **Jekyll版本兼容性**：
  使用GitHub Pages支持的Jekyll版本：
  ```bash
  gem install github-pages
  ```

- **插件兼容性**：
  确保只使用GitHub Pages支持的插件，参考 `Gemfile.github-pages` 文件

### 2. 部署错误

- **Git权限问题**：
  - 检查是否已正确设置Git用户名和邮箱
  - 确保GitHub账号有权限推送到此仓库

- **gh-pages分支问题**：
  - 尝试删除现有gh-pages分支，重新创建
  ```bash
  git push origin --delete gh-pages
  ```

- **GitHub Actions错误**：
  - 检查Actions日志，查看具体错误信息
  - 确保仓库有足够的权限运行Actions

### 3. 网站显示问题

- **页面404错误**：
  - 检查baseurl配置是否正确
  - 确保文件路径大小写正确

- **资源加载失败**：
  - 使用相对路径引用资源
  - 确保资源文件已正确构建到_site目录

- **主题未应用**：
  - 检查主题配置是否正确
  - 确保使用GitHub Pages支持的主题

## 自定义域名设置（可选）

### 1. 配置DNS

1. 登录您的域名注册商控制台
2. 添加以下DNS记录：
   - A记录：指向GitHub Pages IP地址（185.199.108.153, 185.199.109.153, 185.199.110.153, 185.199.111.153）
   - 或CNAME记录：指向 `username.github.io`

### 2. 配置GitHub Pages

1. 在仓库根目录创建一个名为 `CNAME` 的文件，内容为您的自定义域名：
   ```
   your-domain.com
   ```

2. 进入仓库设置 > Pages
3. 在"Custom domain"部分输入您的域名
4. 点击"Save"
5. （可选）勾选"Enforce HTTPS"以启用HTTPS

## 额外资源

- [GitHub Pages官方文档](https://docs.github.com/cn/pages)
- [Jekyll官方文档](https://jekyllrb.com/docs/)
- [GitHub Actions文档](https://docs.github.com/cn/actions)

---

如果您在部署过程中遇到任何问题，请参考GitHub和Jekyll的官方文档，或在GitHub上提交Issue获取帮助。