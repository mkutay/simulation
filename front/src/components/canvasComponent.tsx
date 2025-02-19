import React, { useRef, useEffect } from 'react';

export function CanvasComponent() {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const context = canvas.getContext('2d');
    
    // Drawing operations here
  }, []);

  return <canvas ref={canvasRef} width={400} height={300} />;
}