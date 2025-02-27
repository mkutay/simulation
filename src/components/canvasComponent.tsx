'use client';

import React, { useRef, useEffect, useState } from 'react';

import { DisplayData, DrawCircleData, DrawEqualTriangleData, DrawLineData, DrawRectData, DrawTextData, DrawTransparentRectData, FillData } from '@/lib/schema';
import { getData } from '@/lib/database';

export function CanvasComponent() {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const canvasCtxRef = React.useRef<CanvasRenderingContext2D | null>(null);

  const [data, setData] = useState(null as DisplayData);

  useEffect(() => {
    const intervalId = setInterval(() => {
      getData().then((apiData) => {
        setData(apiData);
      });
    }, 0);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    if (data === null || !canvasRef.current || data == undefined || !data.d) return;
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
        const item = data.d[key].d[len - 1];
        if (item[0] < minId) {
          minId = item[0];
          minKey = key;
        }
      }

      if (minKey === "") break;

      if (minKey === "f") {
        fill(context, data.w, data.h, data.d.f.d[data.d.f.d.length - 1]);
        data.d.f.d.pop();
      } else if (minKey === "c") {
        drawCircle(context, data.d.c.d[data.d.c.d.length - 1]);
        data.d.c.d.pop();
      } else if (minKey === "e") {
        drawEqualTriangle(context, data.d.e.d[data.d.e.d.length - 1]);
        data.d.e.d.pop();
      } else if (minKey === "l") {
        drawLine(context, data.d.l.d[data.d.l.d.length - 1]);
        data.d.l.d.pop();
      } else if (minKey === "r") {
        drawRectangle(context, data.d.r.d[data.d.r.d.length - 1]);
        data.d.r.d.pop();
      } else if (minKey === "t") {
        drawText(context, data.d.t.d[data.d.t.d.length - 1]);
        data.d.t.d.pop();
      } else if (minKey === "a") {
        drawTransparentRectangle(context, data.d.a.d[data.d.a.d.length - 1]);
        data.d.a.d.pop();
      } else {
        console.error("unknown key: ", minKey);
      }
    }
  }, [data]);

  return (
    <>
      {data !== null && <canvas ref={canvasRef} width={data.w} height={data.h} />}
    </>
  )
}

function fill(context: CanvasRenderingContext2D, width: number, height: number, d: FillData) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.fillRect(0, 0, width, height);
}

function drawCircle(context: CanvasRenderingContext2D, d: DrawCircleData) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.beginPath();
  context.arc(d[4], d[5], d[6], 0, 2 * Math.PI);
  context.closePath();
  context.fill();
}

function drawEqualTriangle(context: CanvasRenderingContext2D, d: DrawEqualTriangleData) {
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

function drawRectangle(context: CanvasRenderingContext2D, d: DrawRectData) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.strokeStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;

  if (d[8]) {
    context.fillRect(d[4], d[5], d[6], d[7]);
  } else {
    context.strokeRect(d[4], d[5], d[6], d[7]);
  }
}

function drawTransparentRectangle(context: CanvasRenderingContext2D, d: DrawTransparentRectData) {
  context.fillStyle = `rgba(${d[1]}, ${d[2]}, ${d[3]}, ${d[8]})`;
  context.fillRect(d[4], d[5], d[6], d[7]);
}

function drawLine(context: CanvasRenderingContext2D, d: DrawLineData) {
  context.strokeStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.beginPath();
  context.moveTo(d[4], d[5]);
  context.lineTo(d[6], d[7]);
  context.closePath();
  context.stroke();
}

function drawText(context: CanvasRenderingContext2D, d: DrawTextData) {
  context.fillStyle = `rgb(${d[1]}, ${d[2]}, ${d[3]})`;
  context.font = `${d[5]}px sans-serif`;
  context.fillText(d[4], d[6], d[7]);
}