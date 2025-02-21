'use server';

import { createClient } from 'redis';
import { DisplayData } from '@/lib/schema';

export async function getData(): Promise<DisplayData> {
  const client = createClient({
    url: process.env.REDIS_URI,
  });

  await client.connect();
  const result = await client.json.get('display') as DisplayData;
  client.quit();
  return result;
}