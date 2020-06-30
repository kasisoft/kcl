package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

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
  
} /* ENDCLASS */
