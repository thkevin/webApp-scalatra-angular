# Stage 1 : Building Angular project on a node container
FROM node:8 as builder

COPY angularMowit /angulaMowit

WORKDIR /angularMowit

RUN npm install

RUN $(npm bin)/ng build


# Stage 2 : Creating the webserver with nginx ( dist folder created on previous node container and copied in the nginx one)
FROM nginx

COPY --from=builder /angularMowit/dist/* /usr/share/nginx/html/

EXPOSE 80
