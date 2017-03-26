package org.rivierarobotics;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Dot {

    public static Dot from(double angle, double distance) {
        return new AutoValue_Dot(angle, distance);
    }

    Dot() {
    }

    public abstract double angle();

    public abstract double distance();
}
