'use client';

import { useState } from 'react';

import { CanvasComponent } from '@/components/canvasComponent';
import { StartSimulationButton } from '@/components/startSimulationButton';
import { StopSimulationButton } from '@/components/stopSimulationButton';

export default function Home() {
  const [started, setStarted] = useState(false);
  
  return (
    <div className="w-fit mx-auto px-4 py-6 flex flex-col gap-2">
      {started && <CanvasComponent started={started} />}
      {!started && <StartSimulationButton started={started} setStarted={setStarted} />}
      {started && <StopSimulationButton started={started} setStarted={setStarted} />}
    </div>
  );
}
