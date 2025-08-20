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
export type FillData = number;

// Index, Red, Green, Blue, X, Y, Radius
export type DrawCircleData = number;

// Index, Red, Green, Blue, X, Y, Radius
export type DrawEqualTriangleData = number;

// Index, Red, Green, Blue, X1, Y1, X2, Y2
export type DrawLineData = number;

// Index, Red, Green, Blue, X, Y, Width, Height, Filled
export type DrawRectData = number | boolean;

// Index, Red, Green, Blue, Text, Font Size, X, Y
export type DrawTextData = number | string;

// Index, Red, Green, Blue, X, Y, Width, Height, Alpha
export type DrawTransparentRectData = number;