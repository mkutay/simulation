'use client';

import { useState } from 'react';

import { CanvasComponent } from '@/components/canvasComponent';
import { StartSimulationButton } from '@/components/startSimulationButton';
import { StopSimulationButton } from '@/components/stopSimulationButton';

export default function Home() {
  const [ws, setWs] = useState<WebSocket | null>(null);
  
  return (
    <div className="w-fit mx-auto px-4 py-6 flex flex-col gap-2">
      {ws && <CanvasComponent ws={ws} />}
      {!ws && <StartSimulationButton setWs={setWs} />}
      {ws && <StopSimulationButton ws={ws} setWs={setWs} />}
    </div>
  );
}
