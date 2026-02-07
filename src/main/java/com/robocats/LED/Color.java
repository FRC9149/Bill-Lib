package com.robocats.LED;


public class Color {
        private int hex = 0;

        public Color(int hex) {
            this.hex = hex;
        }
        public Color(int r, int g, int b) {
            this.hex = (r << 16) | (g << 8) | b;
        }
        /** Color is slightly off, but mostly correct */
        public Color(double h, double s, double v) {
            h = Math.min(360.0, Math.max(0.0, h));
            s = Math.min(1.0, Math.max(0.0, s));
            v = Math.min(1.0, Math.max(0.0, v));

            double c = (v * s);
            double x = (c * (1.0 -Math.abs( ((h / 60.0) % 2.0) - 1.0 )));
            double m = (v - c);

            double r1, g1, b1;
            if ((h / 60.0) < 1) {
                r1 = c; g1 = x; b1 = 0;
            } else if ((h / 60.0) < 2) {
                r1 = x; g1 = c; b1 = 0;
            } else if ((h / 60.0) < 3) {
                r1 = 0; g1 = c; b1 = x;
            } else if ((h / 60.0) < 4) {
                r1 = 0; g1 = x; b1 = c;
            } else if ((h / 60.0) < 5) {
                r1 = x; g1 = 0; b1 = c;
            } else {
                r1 = c; g1 = 0; b1 = x;
            }

            int r = (int)((r1 + m) * 255.0);
            int g = (int)((g1 + m) * 255.0);
            int b = (int)((b1 + m) * 255.0);
            hex = (r << 16) | (g << 8) | b;
        }

        public int hex() { return hex; }
        public int[] rgb() {
            int r = (hex >> 16) & 0xFF;
            int g = (hex >> 8) & 0xFF;
            int b = hex & 0xFF;
            return new int[]{r, g, b};
        }
        public double[] hsv() {
            double r = ((hex >> 16) & 0xFF) / 255.0;
            double g = ((hex >> 8) & 0xFF) / 255.0;
            double b = (hex & 0xFF) / 255.0;

            double max = Math.max(r, Math.max(g, b)); 
            double min = Math.min(r, Math.min(g, b));
            double delta = max - min;

            double h, s, v = max;

            s = max == 0 ? 0 : (delta / max);

            if (delta == 0) {
                h = 0;
            } else if (max == r) {
                h = 60 * (((g - b) / delta) % 6);
            } else if (max == g) {
                h = 60 * (((b - r) / delta) + 2);
            } else {
                h = 60 * (((r - g) / delta) + 4);
            }
            
            h += h < 0 ? 360 : 0;

            return new double[]{h, s, v};
        }
    }   