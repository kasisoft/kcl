package com.kasisoft.libs.common.types;

import jakarta.validation.constraints.*;

import java.awt.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KSize {

    private int width;

    private int height;

    public KSize() {
    }

    public KSize(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    public KSize(@NotNull Dimension dim) {
        width  = dim.width;
        height = dim.height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + height;
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KSize other = (KSize) obj;
        if (height != other.height)
            return false;
        if (width != other.width)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "KSize [width=" + width + ", height=" + height + "]";
    }

} /* ENDCLASS */
