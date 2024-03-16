const fastify = require('fastify')({ logger: true });
const path = require('path');

// Register the Redis plugin
fastify.register(require('@fastify/redis'), {
  host: '127.0.0.1', // Change this to your Redis server's host
  port: 6379 // Change this if your Redis server uses a different port
});

// Register the point-of-view plugin for template rendering
fastify.register(require('point-of-view'), {
  engine: {
    pug: require('pug')
  },
  includeViewExtension: true,
  root: path.join(__dirname, 'views')
});

// Declare a route
fastify.get('/', async (request, reply) => {
  const redis = fastify.redis;
  // Retrieve value from Redis
  const value = await redis.get('game0');
  // Render template with the value
  return reply.view('/index.pug', { value });
});

// Run the server
const start = async () => {
  try {
    await fastify.listen({ port: 3000 });
    console.log(`Server listening on ${fastify.server.address().port}`);
  } catch (err) {
    fastify.log.error(err);
    process.exit(1);
  }
};

start();
