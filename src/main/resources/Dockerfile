# frontend/Dockerfile
FROM nginx:1.23-alpine

# 删除默认配置
RUN rm /etc/nginx/conf.d/default.conf

# 复制静态文件
COPY . /usr/share/nginx/html

# 可选：自定义 Nginx 配置
# COPY nginx.conf /etc/nginx/conf.d/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]