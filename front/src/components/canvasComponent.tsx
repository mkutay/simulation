'use client';

import React, { useRef, useEffect, useState, useCallback } from 'react';

import { getData } from '@/lib/database';
import { DisplayData, DrawCircleData, DrawEqualTriangleData, FillData } from '@/lib/schema';

export function CanvasComponent() {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  let canvasCtxRef = React.useRef<CanvasRenderingContext2D | null>(null);

  const [data, setData] = useState(null as DisplayData);

  useEffect(() => {
    if (data === null || !canvasRef.current) return;
    canvasCtxRef.current = canvasRef.current.getContext('2d');
    let context = canvasCtxRef.current;
    if (!context) return;

    console.log("a");

    let minId = 9999999;
    let minKey = "";
    const keys = Object.keys(data.d) as (keyof typeof data.d)[];
    for (const key of keys) {
      const item = data.d[key][data.d[key].length - 1];
      if (item.i < minId) {
        minId = item.i;
        minKey = key;
      }
    }

    console.log(minKey);

    if (minKey === "fill") {
      fill(context, data.w, data.h, data.d.fill[data.d.fill.length - 1]);
      data.d.fill.pop();
    } else if (minKey === "drawCircle") {
      console.log(data.d.drawCircle[data.d.drawCircle.length - 1]);
      drawCircle(context, data.d.drawCircle[data.d.drawCircle.length - 1]);
      data.d.drawCircle.pop();
    } else if (minKey === "drawEqualTriangle") {
      drawEqualTriangle(context, data.d.drawEqualTriangle[data.d.drawEqualTriangle.length - 1]);
      data.d.drawEqualTriangle.pop();
    } else if (minKey === "drawLine") {
    } else if (minKey === "drawRect") {
    } else if (minKey === "drawText") {
    } else if (minKey === "drawTransparentRect") {
    } else {
      console.error("unknown key: ", minKey);
    }
  }, []);

  useEffect(() => {
    getData()
      .then((apiData) => {
        setData(apiData);
      });
  });

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