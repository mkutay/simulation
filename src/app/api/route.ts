import { getData } from '@/lib/database';
import { DisplayData } from '@/lib/schema';

export async function GET() {
  const result: DisplayData = await getData();
  return Response.json({ data: result });
}