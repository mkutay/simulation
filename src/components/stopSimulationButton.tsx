import { Button } from '@/components/ui/button';
import { stopSimulation } from '@/lib/database';

export function StopSimulationButton({
  started,
  setStarted,
}: {
  started: boolean,
  setStarted: React.Dispatch<React.SetStateAction<boolean>>,
}) {
  return (
    <Button className="w-fit" onClick={() => {
      stopSimulation();
      setStarted(false);
    }} disabled={!started}>
      Stop Simulation
    </Button>
  );
}