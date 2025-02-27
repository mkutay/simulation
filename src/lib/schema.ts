export type DisplayData = {
  w: number,
  h: number,
  d: {
    f: { d: FillData[] },
    c: { d: DrawCircleData[] },
    e: { d: DrawEqualTriangleData[] },
    l: { d: DrawLineData[] },
    r: { d: DrawRectData[] },
    t: { d: DrawTextData[] },
    a: { d: DrawTransparentRectData[] },
  },
} | null;

// Index, Red, Green, Blue
export type FillData = [number, number, number, number];

// Index, Red, Green, Blue, X, Y, Radius
export type DrawCircleData = [number, number, number, number, number, number, number];

// Index, Red, Green, Blue, X, Y, Radius
export type DrawEqualTriangleData = [number, number, number, number, number, number, number];

// Index, Red, Green, Blue, X1, Y1, X2, Y2
export type DrawLineData = [number, number, number, number, number, number, number, number];

// Index, Red, Green, Blue, X, Y, Width, Height, Filled
export type DrawRectData = [number, number, number, number, number, number, number, number, boolean];

// Index, Red, Green, Blue, Text, Font Size, X, Y
export type DrawTextData = [number, number, number, number, string, number, number, number];

// Index, Red, Green, Blue, X, Y, Width, Height, Alpha
export type DrawTransparentRectData = [number, number, number, number, number, number, number, number, number];