'use server';

import { createClient } from 'redis';
import { DisplayData } from '@/lib/schema';

import simulationData from '@/../simulation_data.json';

// Create the client statically, so that we can use it in multiple functions
const client = createClient({
  url: process.env.REDIS_URI,
});
client.connect().catch(console.error);

// Don't forget to close the client
process.on('beforeExit', () => {
  client.quit().catch(console.error);
});

export async function getData(): Promise<DisplayData> {
  const result = await client.json.get('display') as DisplayData;
  return result;
}

export async function startSimulation() {
  console.log('startSimulation');
  await client.publish('simulation_data', JSON.stringify(simulationData));
}

export async function stopSimulation() {
  console.log('stopSimulation');
  await client.publish('simulation_data', "");
}