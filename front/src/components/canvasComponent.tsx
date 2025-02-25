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
    }, 16);

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
        if (data.d[key].length === 0) continue;
        const item = data.d[key][data.d[key].length - 1];
        if (item.i < minId) {
          minId = item.i;
          minKey = key;
        }
      }

      if (minKey === "") break;

      if (minKey === "f") {
        fill(context, data.w, data.h, data.d.f[data.d.f.length - 1]);
        data.d.f.pop();
      } else if (minKey === "c") {
        drawCircle(context, data.d.c[data.d.c.length - 1]);
        data.d.c.pop();
      } else if (minKey === "e") {
        drawEqualTriangle(context, data.d.e[data.d.e.length - 1]);
        data.d.e.pop();
      } else if (minKey === "l") {
        drawLine(context, data.d.l[data.d.l.length - 1]);
        data.d.l.pop();
      } else if (minKey === "r") {
        drawRectangle(context, data.d.r[data.d.r.length - 1]);
        data.d.r.pop();
      } else if (minKey === "t") {
        drawText(context, data.d.t[data.d.t.length - 1]);
        data.d.t.pop();
      } else if (minKey === "a") {
        drawTransparentRectangle(context, data.d.a[data.d.a.length - 1]);
        data.d.a.pop();
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
  context.fillStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;
  context.fillRect(0, 0, width, height);
}

function drawCircle(context: CanvasRenderingContext2D, d: DrawCircleData) {
  context.fillStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;
  context.beginPath();
  context.arc(d.x, d.y, d.r, 0, 2 * Math.PI);
  context.closePath();
  context.fill();
}

function drawEqualTriangle(context: CanvasRenderingContext2D, d: DrawEqualTriangleData) {
  context.fillStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;

  const xPoints: number[] = [];
  const yPoints: number[] = [];

  for (let i = 0; i < 3; i++) {
    const angle = Math.PI / 2 + i * 2 * Math.PI / 3;
    xPoints[i] = d.x + (d.r * Math.cos(angle));
    yPoints[i] = d.y - (d.r * Math.sin(angle));
  }

  context.beginPath();
  context.moveTo(xPoints[0], yPoints[0]);
  context.lineTo(xPoints[1], yPoints[1]);
  context.lineTo(xPoints[2], yPoints[2]);
  context.closePath();
  context.fill();
}

function drawRectangle(context: CanvasRenderingContext2D, d: DrawRectData) {
  context.fillStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;
  context.strokeStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;

  if (d.f) {
    context.fillRect(d.x, d.y, d.w, d.h);
  } else {
    context.strokeRect(d.x, d.y, d.w, d.h);
  }
}

function drawTransparentRectangle(context: CanvasRenderingContext2D, d: DrawTransparentRectData) {
  context.fillStyle = `rgba(${d.c[0]}, ${d.c[1]}, ${d.c[2]}, ${d.a})`;
  context.fillRect(d.x, d.y, d.w, d.h);
}

function drawLine(context: CanvasRenderingContext2D, d: DrawLineData) {
  context.strokeStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;
  context.beginPath();
  context.moveTo(d.x1, d.y1);
  context.lineTo(d.x2, d.y2);
  context.closePath();
  context.stroke();
}

function drawText(context: CanvasRenderingContext2D, d: DrawTextData) {
  context.fillStyle = `rgb(${d.c[0]}, ${d.c[1]}, ${d.c[2]})`;
  context.font = `${d.s}px sans-serif`;
  context.fillText(d.t, d.x, d.y);
}