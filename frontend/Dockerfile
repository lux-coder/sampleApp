FROM node:12.11.0-alpine AS builder
COPY . ./frontend
WORKDIR /frontend
RUN npm i
RUN $(npm bin)/ng build --prod

FROM nginx:1.17.4-alpine
COPY --from=builder /frontend/dist/frontend/ /usr/share/nginx/html
