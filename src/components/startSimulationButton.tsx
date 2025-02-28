import { startSimulation } from '@/lib/database';
import { Button } from '@/components/ui/button';

export function StartSimulationButton({
  started,
  setStarted,
}: {
  started: boolean,
  setStarted: React.Dispatch<React.SetStateAction<boolean>>,
}) {
  return (
    <Button onClick={() => {
      startSimulation();
      setStarted(true);
    }} disabled={started}>
      Start Simulation
    </Button>
  );
}