'use client';

import React, { useRef, useEffect, useState } from 'react';

import { DisplayData, DrawCircleData, DrawEqualTriangleData, DrawLineData, DrawRectData, DrawTextData, DrawTransparentRectData, FillData } from '@/lib/schema';
import { getData } from '@/lib/database';

export function CanvasComponent({ started }: { started: boolean }) {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const canvasCtxRef = React.useRef<CanvasRenderingContext2D | null>(null);
  const [data, setData] = useState(null as DisplayData);
  
  useEffect(() => {
    if (!started) return;
    const intervalId = setInterval(() => {
      getData().then((apiData) => {
        setData(apiData);
      });
    }, 30);

    return () => clearInterval(intervalId);
  }, [started]);

  useEffect(() => {
    if (!started || data === null || !canvasRef.current || data == undefined || !data.d) return;
    canvasCtxRef.current = canvasRef.current.getContext('2d');
    const context = canvasCtxRef.current;
    if (!context) return;

    // Go through all the items in the data
    while (true) {
      let minId = 9999999;
      let minKey = "";
      const keys = Object.keys(data.d) as (keyof typeof data.d)[];
      for (const key of keys) {
        const len = data.d[key].d.length;
        if (len === 0) continue;
        const itemId = data.d[key].d[0] as number;
        if (itemId < minId) {
          minId = itemId;
          minKey = key;
        }
      }

      if (minKey === "") break;

      if (minKey === "f") {
        const fillData: FillData[] = data.d.f.d.splice(0, getLengthFromKey("f"));
        fill(context, data.w, data.h, fillData);
      } else if (minKey === "c") {
        const circleData: DrawCircleData[] = data.d.c.d.splice(0, getLengthFromKey("c"));
        drawCircle(context, circleData);
      } else if (minKey === "e") {
        const equalTriangleData: DrawEqualTriangleData[] = data.d.e.d.splice(0, getLengthFromKey("e"));
        drawEqualTriangle(context, equalTriangleData);
      } else if (minKey === "l") {
        const lineData: DrawLineData[] = data.d.l.d.splice(0, getLengthFromKey("l"));
        drawLine(context, lineData);
      } else if (minKey === "r") {
        const rectData: DrawRectData[] = data.d.r.d.splice(0, getLengthFromKey("r"));
        drawRectangle(context, rectData);
      } else if (minKey === "t") {
        const textData: DrawTextData[] = data.d.t.d.splice(0, getLengthFromKey("t"));
        drawText(context, textData);
      } else if (minKey === "a") {
        const transparentRectData: DrawTransparentRectData[] = data.d.a.d.splice(0, getLengthFromKey("a"));
        drawTransparentRectangle(context, transparentRectData);
      } else {
        console.error("unknown key: ", minKey);
      }
    }
  }, [data, started]);

  return (
    <>
      {data !== null && started && <canvas ref={canvasRef} width={data.w} height={data.h} />}
    </>
  )
}

function getLengthFromKey(key: string) {
  if (key === "f") return 4;
  if (key === "c") return 7;
  if (key === "e") return 7;
  if (key === "l") return 8;
  if (key === "r") return 9;
  if (key === "t") return 8;
  if (key === "a") return 9;
  return 0;
}

function fill(context: CanvasRenderingContext2D, width: number, height: number, d: FillData[]) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.fillRect(0, 0, width, height);
}

function drawCircle(context: CanvasRenderingContext2D, d: DrawCircleData[]) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.beginPath();
  context.arc(d[4], d[5], d[6], 0, 2 * Math.PI);
  context.closePath();
  context.fill();
}

function drawEqualTriangle(context: CanvasRenderingContext2D, d: DrawEqualTriangleData[]) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;

  const xPoints: number[] = [];
  const yPoints: number[] = [];

  for (let i = 0; i < 3; i++) {
    const angle = Math.PI / 2 + i * 2 * Math.PI / 3;
    xPoints[i] = d[4] + (d[6] * Math.cos(angle));
    yPoints[i] = d[5] - (d[6] * Math.sin(angle));
  }

  context.beginPath();
  context.moveTo(xPoints[0], yPoints[0]);
  context.lineTo(xPoints[1], yPoints[1]);
  context.lineTo(xPoints[2], yPoints[2]);
  context.closePath();
  context.fill();
}

function drawRectangle(context: CanvasRenderingContext2D, d: DrawRectData[]) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.strokeStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;

  if (d[8]) {
    context.fillRect(d[4] as number, d[5] as number, d[6] as number, d[7] as number);
  } else {
    context.strokeRect(d[4] as number, d[5] as number, d[6] as number, d[7] as number);
  }
}

function drawTransparentRectangle(context: CanvasRenderingContext2D, d: DrawTransparentRectData[]) {
  context.fillStyle = `rgba(${d[1]}, ${d[2]}, ${d[3]}, ${d[8]})`;
  context.fillRect(d[4], d[5], d[6], d[7]);
}

function drawLine(context: CanvasRenderingContext2D, d: DrawLineData[]) {
  context.strokeStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.beginPath();
  context.moveTo(d[4], d[5]);
  context.lineTo(d[6], d[7]);
  context.closePath();
  context.stroke();
}

function drawText(context: CanvasRenderingContext2D, d: DrawTextData[]) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.font = `${d[5]}px sans-serif`;
  context.fillText(d[4] as string, d[6] as number, d[7] as number);
}