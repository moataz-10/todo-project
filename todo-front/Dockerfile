FROM node:latest

# Set working directory
WORKDIR /app

COPY package.json .
COPY server.js .
COPY views ./views

RUN npm install

# Create the directory for cached images
RUN mkdir -p /data

# Expose the application port
EXPOSE 3000

# Start the server
CMD ["npm", "start"]
