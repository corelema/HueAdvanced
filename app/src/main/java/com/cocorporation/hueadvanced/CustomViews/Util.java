package com.cocorporation.hueadvanced.customviews;

import android.graphics.Color;

/**
 * Created by Corentin on 7/9/2016.
 */
public class Util {
    public static Point extendSegmentToDistance(float x1, float y1, float x2, float y2, float radius) {
        Double numerator = (double)(radius * Math.abs(x1 - x2));
        Double denominator = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        float x3, y3;
        if (x2 < x1) {
            x3 = (float) (x1 - numerator / denominator);
        } else {
            x3 = (float) (x1 + numerator / denominator);
        }

        float a = (y2 - y1) / (x2 - x1);
        float b = y1 - a * x1;

        y3 = a * x3 + b;

        // Should never happen if your view is positioned at the bottom
        if (y3 > y1) {
            y3 = y1;
            if (x3 < x1) {
                x3 = x1 - radius;
            } else {
                x3 = x1 + radius;
            }
        }

        return new Point(x3, y3);
    }

    public static float angleBetweenSegments(float x1, float y1, float x2, float y2) {
        float hypotenus = (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        float adj = (Math.abs(x2 - x1));

        float angle = (float) Math.acos(adj / hypotenus);

        if (x2 > x1) {
            angle = (float) (Math.PI - angle);
        }

        return angle;
    }

    public static int colorFromAngle(float angle) {
        float piBy6 = (float) Math.PI / 6;
        int firstColor;
        int secondColor;
        float percentage;

        if (angle < piBy6) {
            firstColor = Color.RED;
            secondColor = Color.rgb(255, 127, 0);
            percentage = percentageOfArc(0, piBy6, angle);
        } else if (angle < 2 * piBy6) {
            firstColor = Color.rgb(255, 127, 0);
            secondColor = Color.YELLOW;
            percentage = percentageOfArc(piBy6, 2 * piBy6, angle);
        } else if (angle < 3 * piBy6) {
            firstColor = Color.YELLOW;
            secondColor = Color.GREEN;
            percentage = percentageOfArc(2 * piBy6, 3 *piBy6, angle);
        } else if (angle < 4 * piBy6) {
            firstColor = Color.GREEN;
            secondColor = Color.BLUE;
            percentage = percentageOfArc(3 * piBy6, 4 *piBy6, angle);
        } else if (angle < 5 * piBy6) {
            firstColor = Color.BLUE;
            secondColor = Color.rgb(75, 0, 130);
            percentage = percentageOfArc(4 * piBy6, 5 *piBy6, angle);
        } else {
            firstColor = Color.rgb(75, 0, 130);
            secondColor = Color.rgb(148, 0, 211);
            percentage = percentageOfArc(5 * piBy6, 6 *piBy6, angle);
        }

        float restOfPercentage = 1 - percentage;
        int red = (int) (Color.red(firstColor) * restOfPercentage + Color.red(secondColor) * percentage);
        int green = (int) (Color.green(firstColor) * restOfPercentage + Color.green(secondColor) * percentage);
        int blue = (int) (Color.blue(firstColor) * restOfPercentage + Color.blue(secondColor) * percentage);

        return Color.rgb(red, green, blue);
    }

    private static float percentageOfArc(float orig, float dest, float angle) {
        return (angle - orig) / (dest - orig);
    }
}
