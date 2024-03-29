import fastify from 'fastify';
import fastifyRedis from '@fastify/redis';
import pointOfView from '@fastify/view';
import pug from 'pug';
import path from 'path';

const app = fastify({ logger: true });

// Register the Redis plugin
app.register(fastifyRedis, {
    host: '127.0.0.1', // Change this to your Redis server's host
    port: 6379 // Change this if your Redis server uses a different port
});

// Register the point-of-view plugin for template rendering
app.register(pointOfView, {
    engine: {
        pug
    },
    includeViewExtension: true,
    root: path.join(__dirname, './views')
});

// Declare a route
app.get('/', async (request, reply) => {
    const redis = app.redis;

    const winner = await redis.get("winner");
    console.log(winner);

    const games: string[] = [];

    for (let i = 0;; i++) {
        const game = await redis.get(`game${i}`);
        if (game === null) {
            break;
        }
        games.push(game);
    }

    return reply.view('/index.pug', { winner, games });
});

// Run the server
const start = async () => {
    try {
        await app.listen({ port: 3000 });
    } catch (err) {
        app.log.error(err);
        process.exit(1);
    }
};

start();
