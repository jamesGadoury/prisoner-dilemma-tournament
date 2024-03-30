import fastify from 'fastify';
import fastifyRedis from '@fastify/redis';
import pointOfView from '@fastify/view';
import pug from 'pug';
import path from 'path';

const app = fastify({ logger: true });

// Register the Redis plugin
app.register(fastifyRedis, {
    host: process.env.REDIS_HOST ? process.env.REDIS_HOST : '127.0.0.1', // Change this to your Redis server's host
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
    console.log("get request");

    const redis = app.redis;

    let winner = await redis.get("winner");
    const games: string[] = [];

    if (winner == null) {
        winner = 'N/A';
        return reply.view('/index.pug', { winner, games });
    }

    for (let i = 0;; i++) {
        const game = await redis.get(`game${i}`);
        if (game === null) {
            break;
        }
        games.push(game);
    }

    return reply.view('/index.pug', { winner, games });
});

app.post('/runButtonPressed', async (request, reply) => {
    console.log("run sim button was pressed, sending http request to simulation server...");
    // TODO should consume all configured ports as env variables
    const host = process.env.SIM_SERVER_HOST ? process.env.SIM_SERVER_HOST : '127.0.0.1';
    const port = process.env.SIM_SERVER_PORT ? process.env.SIM_SERVER_PORT : '8081';
    const response = await fetch(`http://${host}:${port}/run`, {
        method: 'POST',
    });

    const data = await response;
    console.log(data);

    return data;
})

// Run the server
const start = async () => {
    try {
        const port: number = process.env.PORT ? Number(process.env.PORT) : 3000;
        await app.listen({ host: '0.0.0.0', port: port });
    } catch (err) {
        app.log.error(err);
        process.exit(1);
    }
};

start();
