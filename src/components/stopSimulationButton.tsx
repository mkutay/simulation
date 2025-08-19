import { Button } from '@/components/ui/button';

export function StopSimulationButton({
  setWs,
  ws,
}: {
  setWs: React.Dispatch<React.SetStateAction<WebSocket | null>>;
  ws: WebSocket,
}) {
  return (
    <Button className="w-fit" onClick={() => {
      ws.send(JSON.stringify({ type: 'stop_simulation', data: "" }));
      ws.close();
      setWs(null);
    }}>
      Stop Simulation
    </Button>
  );
}