package com.kasisoft.libs.common.constants

import javax.swing.border.*
import javax.swing.*

/**
 * Values to specify an alignment.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
enum class Alignment(val alignment: Int, val titledBorderX: Int, val horizontal: Boolean) {

    Left(SwingConstants.LEFT, TitledBorder.LEFT, true),

    Center(SwingConstants.CENTER, TitledBorder.CENTER, true),

    Right(SwingConstants.RIGHT, TitledBorder.RIGHT, true),

    Top(SwingConstants.TOP, TitledBorder.LEFT, false),

    Middle(SwingConstants.CENTER, TitledBorder.LEFT, false),

    Bottom(SwingConstants.BOTTOM, TitledBorder.LEFT, false);

    fun set(component: JComponent) {

        if (horizontal) {
            if (component is JLabel) {
                component.setHorizontalAlignment(alignment)
            } else if (component is JTextField) {
                component.setHorizontalAlignment(alignment)
            }
        } else {
            if (component is JLabel) {
                component.setVerticalAlignment(alignment)
            } else if (component is JTextField) {
                component.setAlignmentY(if (this == Top) 0.0f else (if (this == Middle) 0.5f else 1.0f))
            }
        }

    }

} /* ENDENUM */
