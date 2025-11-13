echo off

REM 图书借阅系统文档站点 - Jekyll运行脚本
REM 此脚本假设您已通过RubyInstaller安装了Ruby

setlocal enabledelayedexpansion

echo ========================================
echo 图书借阅系统文档站点 - Jekyll运行脚本
echo ========================================
echo.
echo 此脚本将帮助您启动Jekyll文档站点服务器
echo.

REM 检查是否存在Gemfile
if not exist "Gemfile" (
    echo 错误：未找到Gemfile文件！
    echo 请确保您在正确的项目目录中运行此脚本。
    pause
    exit /b 1
)

echo 请选择要执行的操作：
echo 1. 安装依赖（首次运行时需要）
echo 2. 启动Jekyll服务器
echo 3. 仅构建静态站点
echo.
set /p choice="请输入选项 (1-3): "

echo.

if "%choice%"=="1" (
    echo 正在安装依赖...
    echo 请确保您已打开"Start Command Prompt with Ruby"
    echo 或者确保Ruby已正确添加到系统PATH
    echo.
    echo 执行：gem install bundler
    gem install bundler
    if errorlevel 1 (
        echo 错误：无法安装Bundler，请检查Ruby环境
        pause
        exit /b 1
    )
    
    echo.
    echo 执行：bundle install
    bundle install
    if errorlevel 1 (
        echo 错误：无法安装项目依赖
        echo 提示：请尝试在Ruby命令提示符中运行此脚本
        pause
        exit /b 1
    )
    
    echo.
    echo 依赖安装成功！
    echo 您现在可以选择选项2来启动服务器
    pause
    
) else if "%choice%"=="2" (
    echo 启动Jekyll服务器...
    echo 请确保已安装所有依赖
    echo.
    echo 执行：bundle exec jekyll serve --watch
    bundle exec jekyll serve --watch
    
) else if "%choice%"=="3" (
    echo 构建静态站点...
    echo 输出将位于 _site 目录
    echo.
    echo 执行：bundle exec jekyll build
    bundle exec jekyll build
    
    if errorlevel 1 (
        echo 错误：构建失败
        pause
        exit /b 1
    )
    
    echo.
    echo 构建成功！静态站点文件已生成到 _site 目录
    pause
    
) else (
    echo 无效选项！
    pause
    exit /b 1
)

endlocal