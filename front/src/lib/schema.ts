export type DisplayData = {
  w: number,
  h: number,
  d: {
    "f": FillData[],
    "c": DrawCircleData[],
    "e": DrawEqualTriangleData[],
    "l": DrawLineData[],
    "r": DrawRectData[],
    "t": DrawTextData[],
    "a": DrawTransparentRectData[],
  },
} | null;

export type FillData = {
  i: number,
  c: number[],
};

export type DrawCircleData = {
  i: number,
  c: number[],
  x: number,
  y: number,
  r: number,
};

export type DrawEqualTriangleData = {
  i: number,
  c: number[],
  x: number,
  y: number,
  r: number,
};

export type DrawLineData = {
  i: number,
  c: number[],
  x1: number,
  y1: number,
  x2: number,
  y2: number,
}

export type DrawRectData = {
  i: number,
  c: number[],
  x: number,
  y: number,
  w: number,
  h: number,
  f: boolean, // filled?
}

export type DrawTextData = {
  i: number,
  c: number[],
  t: string, // text
  s: number, // font size
  x: number,
  y: number,
};

export type DrawTransparentRectData = {
  i: number,
  c: number[],
  x: number,
  y: number,
  w: number,
  h: number,
  a: number, // alpha
};