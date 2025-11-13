@echo off
REM GitHub Pages部署脚本 - Windows版
REM 此脚本用于构建Jekyll站点并部署到GitHub Pages

setlocal enabledelayedexpansion

echo ==================================================
echo 图书借阅系统文档 - GitHub Pages部署脚本
echo ==================================================

REM 检查是否安装了Git
where git >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: 未找到Git，请先安装Git并添加到环境变量
    pause
    exit /b 1
)

REM 获取当前目录名作为默认仓库名
for %%I in (.) do set REPO_NAME=%%~nxI

REM 提示用户输入GitHub仓库信息
set /p GITHUB_USERNAME=请输入您的GitHub用户名: 
set /p GITHUB_REPO=请输入仓库名[%REPO_NAME%]: 
if "%GITHUB_REPO%" == "" set GITHUB_REPO=%REPO_NAME%

REM 设置GitHub仓库URL
set GITHUB_URL=https://github.com/%GITHUB_USERNAME%/%GITHUB_REPO%.git

REM 创建临时目录用于构建
set TEMP_DIR=_ghpages_temp

REM 检查是否已经有git环境
if not exist .git (
    echo 初始化Git仓库...
    git init
    git config user.name "GitHub Actions"
    git config user.email "actions@github.com"
)

REM 检查远程仓库是否已添加
for /f "tokens=2" %%a in ('git remote -v ^| findstr origin') do (
    set CURRENT_REMOTE=%%a
    goto remote_check_done
)
:remote_check_done

if not defined CURRENT_REMOTE (
    echo 添加远程仓库...
    git remote add origin %GITHUB_URL%
) else (
    echo 远程仓库已存在: !CURRENT_REMOTE!
)

REM 构建站点
if exist %TEMP_DIR% rmdir /s /q %TEMP_DIR%
mkdir %TEMP_DIR%

echo 开始构建Jekyll站点...

REM 构建到临时目录
call jekyll build -d %TEMP_DIR% --config _config.yml
if %errorlevel% neq 0 (
    echo 错误: Jekyll构建失败
    pause
    exit /b 1
)

echo 构建完成！

REM 部署到gh-pages分支
echo 开始部署到GitHub Pages...

REM 进入临时目录
pushd %TEMP_DIR%

REM 初始化Git
git init
git config user.name "GitHub Actions"
git config user.email "actions@github.com"

REM 添加所有文件
git add .
git commit -m "部署站点到GitHub Pages - $(date /t) $(time /t)"

REM 推送到gh-pages分支
git push -f %GITHUB_URL% master:gh-pages
if %errorlevel% neq 0 (
    echo 错误: 部署失败
    popd
    pause
    exit /b 1
)

popd

REM 清理临时目录
rmdir /s /q %TEMP_DIR%

echo ==================================================
echo 部署成功！
echo 您的站点将在几分钟内可访问：
echo https://%GITHUB_USERNAME%.github.io/%GITHUB_REPO%
echo ==================================================

pause