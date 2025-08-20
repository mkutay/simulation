import { Button } from '@/components/ui/button';
import simulationData from '@/../simulation_data.json';

export function StartSimulationButton({
  setWs,
}: {
  setWs: React.Dispatch<React.SetStateAction<WebSocket | null>>;
}) {
  return (
    <Button onClick={() => {
      const ws = new WebSocket(process.env.NEXT_PUBLIC_WS_URI!);
      
      ws.onclose = () => console.log('WebSocket disconnected');
      ws.onerror = (error) => console.error('WebSocket error:', error);
      ws.onopen = () => {
        console.log('WebSocket connected');
        ws.send(JSON.stringify({ type: 'start_simulation', data: JSON.stringify(simulationData) }));
        setWs(ws);
      }
    }}>
      Start Simulation
    </Button>
  );
}