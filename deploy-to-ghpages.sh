#!/bin/bash
# GitHub Pages部署脚本 - Linux/Mac版
# 此脚本用于构建Jekyll站点并部署到GitHub Pages

echo "=================================================="
echo "图书借阅系统文档 - GitHub Pages部署脚本"
echo "=================================================="

# 检查是否安装了Git
if ! command -v git &> /dev/null; then
    echo "错误: 未找到Git，请先安装Git并添加到环境变量"
    exit 1
fi

# 获取当前目录名作为默认仓库名
REPO_NAME=$(basename "$(pwd)")

# 提示用户输入GitHub仓库信息
read -p "请输入您的GitHub用户名: " GITHUB_USERNAME
read -p "请输入仓库名[${REPO_NAME}]: " GITHUB_REPO
if [ -z "$GITHUB_REPO" ]; then
    GITHUB_REPO=$REPO_NAME
fi

# 设置GitHub仓库URL
GITHUB_URL="https://github.com/${GITHUB_USERNAME}/${GITHUB_REPO}.git"

# 创建临时目录用于构建
TEMP_DIR="_ghpages_temp"

# 检查是否已经有git环境
if [ ! -d ".git" ]; then
    echo "初始化Git仓库..."
    git init
    git config user.name "GitHub Actions"
    git config user.email "actions@github.com"
fi

# 检查远程仓库是否已添加
CURRENT_REMOTE=$(git remote -v | grep origin | awk '{print $2}')

if [ -z "$CURRENT_REMOTE" ]; then
    echo "添加远程仓库..."
    git remote add origin "$GITHUB_URL"
else
    echo "远程仓库已存在: $CURRENT_REMOTE"
fi

# 构建站点
if [ -d "$TEMP_DIR" ]; then
    rm -rf "$TEMP_DIR"
fi
mkdir "$TEMP_DIR"

echo "开始构建Jekyll站点..."

# 构建到临时目录
jekyll build -d "$TEMP_DIR" --config _config.yml
if [ $? -ne 0 ]; then
    echo "错误: Jekyll构建失败"
    exit 1
fi

echo "构建完成！"

# 部署到gh-pages分支
echo "开始部署到GitHub Pages..."

# 进入临时目录
cd "$TEMP_DIR"

# 初始化Git
git init
git config user.name "GitHub Actions"
git config user.email "actions@github.com"

# 添加所有文件
git add .
git commit -m "部署站点到GitHub Pages - $(date)"

# 推送到gh-pages分支
git push -f "$GITHUB_URL" master:gh-pages
if [ $? -ne 0 ]; then
    echo "错误: 部署失败"
    cd ..
    exit 1
fi

cd ..

# 清理临时目录
rm -rf "$TEMP_DIR"

echo "=================================================="
echo "部署成功！"
echo "您的站点将在几分钟内可访问："
echo "https://${GITHUB_USERNAME}.github.io/${GITHUB_REPO}"
echo "=================================================="