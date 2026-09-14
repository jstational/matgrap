package mg.math;

public class Quadruple {
    private boolean sign, big, inf, nan, zero;
    private long expl, mantl;
    private int expi, manti;

    public Quadruple(boolean si, long ex, long mant) {
        sign = si;

        if(ex == 0x7FFL) {
            if(mant == 0L) {
                inf = true;
            } else {
                nan = true;
            }
            return;
        }

        if(ex < 0 && mant < 0) {
            throw new IllegalArgumentException("Neither exponent nor mantissa cannot be negative")
        }

        if(ex == 0L || mant == 0L) {
            zero = true;
            return;
        }
        zero = false;
        evalBig(ex, mant);
    }

    public Quadruple(long ex, long mant) {
        this(false, ex, mant);
    }

    public Quadruple(boolean si, int ex, int mant) {
        sign = si;
        big = false;

        if(ex == 0xFF) {
            if(mant == 0) {
                inf = true;
            } else {
                nan = true;
            }
            return;
        }

        if(ex < 0 || mant < 0) {
            throw new IllegalArgumentException("Neither exponent nor mantissa cannot be negative")
        }

        if(ex == 0 && mant == 0) {
            zero = true;
            return;
        }
        zero = false;
        expi = ex;
        manti = mant;
    }

    public Quadruple(int ex, int mant) {
        this(false, ex, mant);
    }

    public Quadruple(float num) {
        if(Float.isNaN(num)) {
            nan = true;
            return;
        }

        if(num == Float.POSITIVE_INFINITY || num == Float.NEGATIVE_INFINITY) {
            inf = true;
            sign = num == Float.NEGATIVE_INFINITY;
            return;
        }

        int bit = Float.floatToRawIntBits(num);
        sign = (bit >>> 31) != 0;
        big = false;
        if(num == 0f) {
            zero = true;
            return;
        }
        zero = false;
        expi = (bit >>> 23) & 0xFF;
        manti = bit & 0x7FFFFF;
    }

    public Quadruple(double num) {
        if(Double.isNaN(num)) {
            nan = true;
            return;
        }

        if(num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY) {
            inf = true;
            sign = num == Double.NEGATIVE_INFINITY;
            return;
        }

        long bit = Double.doubleToRawLongBits(num);
        sign = (bit & 0x8000000000000000L) != 0;

        if(num == 0d) {
            zero = true;
            return;
        }
        zero = false;
        evalBig((bit >>> 52) & 0x7FFL, bit & 0xFFFFFFFFFFFFFL);
    }

    private void evalBig(long exp, long mant) {
        if(exp <= Integer.MAX_VALUE && mant <= Integer.MAX_VALUE) {
            big = false;
            expi = (int) exp;
            manti = (int) mant;
        } else {
            big = true;
            expl = exp;
            mantl = mant;
        }
    }

    public float toFloat() {}

    public double toDouble() {}

    public StringBuilder toStringBuilder() {
        if(inf) return sign ? "-Inf" : "Inf";
        if(nan) return "NaN";
        if(zero) return "0";

        if(!big) {
            int bit = (sign ? 0x80000000 : 0) | ((expi & 0xFF) << 23) | (manti & 0x7FFFFF);
            return Float.toString(Float.intBitsToFloat(bit));
        }

        long tm = (expl == 0) ? mantl : (1L << 52) | mantl;
        int e2 = (expl == 0) ? (1 - 1023 - 52) : (int) (expl - 1023 - 52);
        int e10 = (e2 * 77) >> 8;

        double mp = fm * Math.scalb(1.0, e2);

        if(mp != 0d) {
            while(Math.abs(mp) >= 10d) {
                mp /= 10d;
                e10++;
            }
            while(Math.abs(mp) < 1d) {
                mp *= 10d;
                e10--;
            }
        }

        StringBuilder val = new StringBuilder(32);
        if(sign) val.append("-");
        return val.append(m).append("E").append(bte);
    }

    public String toString() {
        return toStringBuilder().toString();
    }
}