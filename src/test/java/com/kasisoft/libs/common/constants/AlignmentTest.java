package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class AlignmentTest {

  @Test(groups = "all")
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
  
  @DataProvider(name = "data_set__JLabelHorizontal")
  public Object[][] data_set__JLabelHorizontal() {
    return new Object[][] {
      {new JLabel("label"), Alignment.Left, SwingConstants.LEFT},
      {new JLabel("label"), Alignment.Center, SwingConstants.CENTER},
      {new JLabel("label"), Alignment.Right, SwingConstants.RIGHT},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_set__JLabelHorizontal")
  public void set__JLabelHorizontal(JLabel component, Alignment alignment, int value) {
    alignment.set(component);
    assertThat(component.getHorizontalAlignment(), is(value));
  }

  @DataProvider(name = "data_set__JTextFieldHorizontal")
  public Object[][] data_set__JTextFieldHorizontal() {
    return new Object[][] {
      {new JTextField(), Alignment.Left, SwingConstants.LEFT},
      {new JTextField(), Alignment.Center, SwingConstants.CENTER},
      {new JTextField(), Alignment.Right, SwingConstants.RIGHT},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_set__JTextFieldHorizontal")
  public void set__JTextFieldHorizontal(JTextField component, Alignment alignment, int value) {
    alignment.set(component);
    assertThat(component.getHorizontalAlignment(), is(value));
  }

  @DataProvider(name = "data_set__JLabelVertical")
  public Object[][] data_set__JLabelVertical() {
    return new Object[][] {
      {new JLabel("label"), Alignment.Top, SwingConstants.TOP},
      {new JLabel("label"), Alignment.Middle, SwingConstants.CENTER},
      {new JLabel("label"), Alignment.Bottom, SwingConstants.BOTTOM},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_set__JLabelVertical")
  public void set__JLabelVertical(JLabel component, Alignment alignment, int value) {
    alignment.set(component);
    assertThat(component.getVerticalAlignment(), is(value));
  }

  @DataProvider(name = "data_set__JTextFieldVertical")
  public Object[][] data_set__JTextFieldVertical() {
    return new Object[][] {
      {new JTextField(), Alignment.Top, 0.0f},
      {new JTextField(), Alignment.Middle, 0.5f},
      {new JTextField(), Alignment.Bottom, 1.0f},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_set__JTextFieldVertical")
  public void set__JTextFieldVertical(JTextField component, Alignment alignment, float value) {
    alignment.set(component);
    assertThat(component.getAlignmentY(), is(value));
  }

  @DataProvider(name = "data_set__JPanel")
  public Object[][] data_set__JPanel() {
    return new Object[][] {
      {new JPanel(), Alignment.Left},
      {new JPanel(), Alignment.Center},
      {new JPanel(), Alignment.Right},
      {new JPanel(), Alignment.Top},
      {new JPanel(), Alignment.Middle},
      {new JPanel(), Alignment.Bottom},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_set__JPanel")
  public void set__JPanel(JPanel component, Alignment alignment) {
    // doesn't change a thing
    alignment.set(component);
  }

} /* ENDCLASS */
