package com.kasisoft.libs.common.test.constants;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.constants.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import javax.swing.border.*;

import javax.swing.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class AlignmentTest {

  @Test
  public void values() {

    assertThat(Alignment.Left.getAlignment(), is(SwingConstants.LEFT));
    assertThat(Alignment.Left.getTitledBorderX(), is(TitledBorder.LEFT));

    assertThat(Alignment.Center.getAlignment(), is(SwingConstants.CENTER));
    assertThat(Alignment.Center.getTitledBorderX(), is(TitledBorder.CENTER));

    assertThat(Alignment.Right.getAlignment(), is(SwingConstants.RIGHT));
    assertThat(Alignment.Right.getTitledBorderX(), is(TitledBorder.RIGHT));

    assertThat(Alignment.Top.getAlignment(), is(SwingConstants.TOP));
    assertThat(Alignment.Top.getTitledBorderX(), is(TitledBorder.LEFT));

    assertThat(Alignment.Middle.getAlignment(), is(SwingConstants.CENTER));
    assertThat(Alignment.Middle.getTitledBorderX(), is(TitledBorder.LEFT));

    assertThat(Alignment.Bottom.getAlignment(), is(SwingConstants.BOTTOM));
    assertThat(Alignment.Bottom.getTitledBorderX(), is(TitledBorder.LEFT));

  }

  public static Stream<Arguments> data_set__JLabelHorizontal() {
    return Stream.of(
      Arguments.of(new JLabel("label"), Alignment.Left, SwingConstants.LEFT),
      Arguments.of(new JLabel("label"), Alignment.Center, SwingConstants.CENTER),
      Arguments.of(new JLabel("label"), Alignment.Right, SwingConstants.RIGHT)
    );
  }

  @ParameterizedTest
  @MethodSource("data_set__JLabelHorizontal")
  public void set__JLabelHorizontal(JLabel component, Alignment alignment, int value) {
    alignment.set(component);
    assertThat(component.getHorizontalAlignment(), is(value));
  }

  public static Stream<Arguments> data_set__JTextFieldHorizontal() {
    return Stream.of(
      Arguments.of(new JTextField(), Alignment.Left, SwingConstants.LEFT),
      Arguments.of(new JTextField(), Alignment.Center, SwingConstants.CENTER),
      Arguments.of(new JTextField(), Alignment.Right, SwingConstants.RIGHT)
    );
  }

  @ParameterizedTest
  @MethodSource("data_set__JTextFieldHorizontal")
  public void set__JTextFieldHorizontal(JTextField component, Alignment alignment, int value) {
    alignment.set(component);
    assertThat(component.getHorizontalAlignment(), is(value));
  }

  public static Stream<Arguments> data_set__JLabelVertical() {
    return Stream.of(
      Arguments.of(new JLabel("label"), Alignment.Top, SwingConstants.TOP),
      Arguments.of(new JLabel("label"), Alignment.Middle, SwingConstants.CENTER),
      Arguments.of(new JLabel("label"), Alignment.Bottom, SwingConstants.BOTTOM)
    );
  }

  @ParameterizedTest
  @MethodSource("data_set__JLabelVertical")
  public void set__JLabelVertical(JLabel component, Alignment alignment, int value) {
    alignment.set(component);
    assertThat(component.getVerticalAlignment(), is(value));
  }

  public static Stream<Arguments> data_set__JTextFieldVertical() {
    return Stream.of(
      Arguments.of(new JTextField(), Alignment.Top, 0.0f),
      Arguments.of(new JTextField(), Alignment.Middle, 0.5f),
      Arguments.of(new JTextField(), Alignment.Bottom, 1.0f)
    );
  }

  @ParameterizedTest
  @MethodSource("data_set__JTextFieldVertical")
  public void set__JTextFieldVertical(JTextField component, Alignment alignment, float value) {
    alignment.set(component);
    assertThat(component.getAlignmentY(), is(value));
  }

  public static Stream<Arguments> data_set__JPanel() {
    return Stream.of(
      Arguments.of(new JPanel(), Alignment.Left),
      Arguments.of(new JPanel(), Alignment.Center),
      Arguments.of(new JPanel(), Alignment.Right),
      Arguments.of(new JPanel(), Alignment.Top),
      Arguments.of(new JPanel(), Alignment.Middle),
      Arguments.of(new JPanel(), Alignment.Bottom)
    );
  }

  @ParameterizedTest
  @MethodSource("data_set__JPanel")
  public void set__JPanel(JPanel component, Alignment alignment) {
    // doesn't change a thing
    alignment.set(component);
  }

} /* ENDCLASS */
