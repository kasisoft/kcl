package com.kasisoft.libs.common.ui.layout;

import com.kasisoft.libs.common.util.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.awt.*;

/**
 * The <code>SmartGridLayout</code> has nearly the same functionality as it's superclass <code>GridLayout</code>. The 
 * only modification is the calculation and layouting algorithm. The <code>GridLayout</code> forces each cell to be 
 * equal-sized. Sometimes this doesn't look good, especially if you want to write forms in an easy way.
 * Therefore this <code>LayoutManager</code> allows each row and column to have different sizes. The width for a column 
 * depends on the widest <code>Component</code> in this column, where the height depends on the largest 
 * <code>Component</code> in a row.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmartGridLayout extends GridLayout implements LayoutManager2 {

  private static final int MASK_MINWIDTH        = 0x08; // %001000
  private static final int MASK_MAXWIDTH        = 0x10; // %010000
  private static final int MASK_PREFWIDTH       = 0x18; // %011000
  private static final int MASK_UNLIMITEDWIDTH  = 0x20; // %100000

  private static final int MASK_MINHEIGHT       = 0x01; // %000001
  private static final int MASK_MAXHEIGHT       = 0x02; // %000010
  private static final int MASK_PREFHEIGHT      = 0x03; // %000011
  private static final int MASK_UNLIMITEDHEIGHT = 0x04; // %000100

  private static final int MASK_WIDTH           = 0x38; // %111000
  private static final int MASK_HEIGHT          = 0x07; // %000111

  // Internal constants for controlling purposes
  private enum SizeType {
    
    Minimum,
    Preferred,
    Maximum;
    
    public Dimension getSize( Component component ) {
      if( this == Minimum ) {
        return component.getMinimumSize();
      } else if( this == Maximum ) {
        return component.getMaximumSize();
      } else {
        return component.getPreferredSize();
      }
    }
    
  }

  /** Delivers minimal width.                                     */
  public static final Object FIXMINWIDTH                  = Integer.valueOf( MASK_MINWIDTH  );

  /** Delivers minimal height.                                    */
  public static final Object FIXMINHEIGHT                 = Integer.valueOf( MASK_MINHEIGHT );

  /** Delivers minimal width and preferred height.                */
  public static final Object FIXMINWIDTHPREFHEIGHT        = Integer.valueOf( MASK_MINWIDTH | MASK_PREFHEIGHT      );

  /** Delivers minimal width and maximal height.                  */
  public static final Object FIXMINWIDTHMAXHEIGHT         = Integer.valueOf( MASK_MINWIDTH | MASK_MAXHEIGHT       );

  /** Delivers minimal width and unlimited height.                */
  public static final Object FIXMINWIDTHUNLIMITEDHEIGHT   = Integer.valueOf( MASK_MINWIDTH | MASK_UNLIMITEDHEIGHT );

  /** Delivers preferred width.                                   */
  public static final Object FIXPREFWIDTH                 = Integer.valueOf( MASK_PREFWIDTH  );

  /** Delivers preferred height.                                  */
  public static final Object FIXPREFHEIGHT                = Integer.valueOf( MASK_PREFHEIGHT );

  /** Delivers preferred width and minimal height.                */
  public static final Object FIXPREFWIDTHMINHEIGHT        = Integer.valueOf( MASK_PREFWIDTH | MASK_MINHEIGHT       );

  /** Delivers preferred width and maximal height.                */
  public static final Object FIXPREFWIDTHMAXHEIGHT        = Integer.valueOf( MASK_PREFWIDTH | MASK_MAXHEIGHT       );

  /** Delivers preferred width and unlimited height.              */
  public static final Object FIXPREFWIDTHUNLIMITEDHEIGHT  = Integer.valueOf( MASK_PREFWIDTH | MASK_UNLIMITEDHEIGHT );

  /** Delivers maximal width.                                     */
  public static final Object FIXMAXWIDTH                  = Integer.valueOf( MASK_MAXWIDTH  );

  /** Delivers maximal height.                                    */
  public static final Object FIXMAXHEIGHT                 = Integer.valueOf( MASK_MAXHEIGHT );

  /** Delivers maximal width and minimal height.                  */
  public static final Object FIXMAXWIDTHMINHEIGHT         = Integer.valueOf( MASK_MAXWIDTH | MASK_MINHEIGHT       );

  /** Delivers maximal width and preferred height.                */
  public static final Object FIXMAXWIDTHPREFHEIGHT        = Integer.valueOf( MASK_MAXWIDTH | MASK_PREFHEIGHT      );

  /** Delivers maximal width and unlimited height.                */
  public static final Object FIXMAXWIDTHUNLIMITEDHEIGHT   = Integer.valueOf( MASK_MAXWIDTH | MASK_UNLIMITEDHEIGHT );

  /** Delivers unlimited width.                                   */
  public static final Object FIXUNLIMITEDWIDTH            = Integer.valueOf( MASK_UNLIMITEDWIDTH  );

  /** Delivers unlimited height.                                  */
  public static final Object FIXUNLIMITEDHEIGHT           = Integer.valueOf( MASK_UNLIMITEDHEIGHT );

  /** Delivers unlimited width and minimal height.                */
  public static final Object FIXUNLIMITEDWIDTHMINHEIGHT   = Integer.valueOf( MASK_UNLIMITEDWIDTH | MASK_MINHEIGHT   );

  /** Delivers unlimited width and preferred height.              */
  public static final Object FIXUNLIMITEDWIDTHPREFHEIGHT  = Integer.valueOf( MASK_UNLIMITEDWIDTH | MASK_PREFHEIGHT  );

  /** Delivers unlimited width and maximal height.                */
  public static final Object FIXUNLIMITEDWIDTHMAXHEIGHT   = Integer.valueOf( MASK_UNLIMITEDWIDTH | MASK_MAXHEIGHT   );

  /** Delivers minmal size.                                       */
  public static final Object FIXMINSIZE                   = Integer.valueOf( MASK_MINWIDTH       | MASK_MINHEIGHT       );

  /** Delivers preferred size.                                    */
  public static final Object FIXPREFSIZE                  = Integer.valueOf( MASK_PREFWIDTH      | MASK_PREFHEIGHT      );

  /** Delivers maximal size.                                      */
  public static final Object FIXMAXSIZE                   = Integer.valueOf( MASK_MAXWIDTH       | MASK_MAXHEIGHT       );

  /** Delivers an unlimited size.                                 */
  public static final Object FIXUNLIMITEDSIZE             = Integer.valueOf( MASK_UNLIMITEDWIDTH | MASK_UNLIMITEDHEIGHT );


  // The grid which holds all components.
  Component[][]            grid;

  // Keeps a map of pairs (Component,Integer{Constraint}) where a constraint must be one of the constants defined above.
  Map<Component,Integer>   constraints ;

  // This array is valid after the call of {@link calcLayoutSize( int, Container ) calcLayoutSize} until an additional 
  // object is added.
  int[]                    colwidth;

  // This array is valid after the call of {@link calcLayoutSize( int, Container ) calcLayoutSize} until an additional 
  // object is added. It corresponds to colwidth but holds the maximum widths of each column.
  int[]                    maxwidth;
  
  // This array is valid after the call of {@link calcLayoutSize( int, Container ) calcLayoutSize} until an additional 
  // object will be added.
  int[]                    rowheight;

  // This array is valid after the call of {@link calcLayoutSize( int, Container ) calcLayoutSize} until an additional 
  // object is added. It corresponds to colheight but holds the maximum heights of each column.
  int[]                    maxheight;

  // This property specifies whether objects will be expandable or not. Due to the fact that a component may get more 
  // space than it's needed it got two options:
  //
  //   1. The object will be forced to consume the provided space (value = true).
  //   2. The object will be placed in the middle of the provided space (value = false).
  //
  boolean                  expansion;

  /**
   * Simple constructor which defines a grid where each row contains one column.
   */
  public SmartGridLayout() {
    this( true );
  }

  /**
   * Simple constructor which defines a grid where each row contains one column.
   *
   * @param forceexp   true <=> Objects will be expanded if necessary.
   */
  public SmartGridLayout( boolean forceexp ) {
    super( 1, 0, 0, 0 );
    constraints = new HashMap<>(7);
    expansion   = forceexp;
  }

  /**
   * Creates a grid layout with the specified number of rows and columns.
   * 
   * One, but not both, of <code>rows</code> and <code>cols</code> can be zero, which means that any number of objects 
   * can be placed in a row or in a column.
   *
   * @param rows   The rows, with the value zero meaning any number of rows.
   * @param cols   The columns, with the value zero meaning any number of columns.
   */
  public SmartGridLayout( int rows, int cols ) {
    this( rows, cols, true );
  }

  /**
   * Creates a grid layout with the specified number of rows and columns.
   *
   * One, but not both, of <code>rows</code> and <code>cols</code> can be zero, which means that any number of objects 
   * can be placed in a row or in a column.
   *
   * @param rows       The rows, with the value zero meaning any number of rows.
   * @param cols       The columns, with the value zero meaning any number of columns.
   * @param forceexp   <code>true</code> <=> Objects will be expanded if necessary.
   */
  public SmartGridLayout( int rows, int cols, boolean forceexp ) {
    super( rows, cols );
    constraints = new HashMap<>(7);
    expansion   = forceexp;
  }

  /**
   * Creates a grid layout with the specified number of rows and columns.
   * 
   * In addition, the horizontal and vertical gaps are set to the specified values. Horizontal gaps are placed at the 
   * left and right edges, and between each of the columns. Vertical gaps are placed at the top and bottom edges, and 
   * between each of the rows.
   * 
   * One, but not both, of <code>rows</code> and <code>cols</code> can be zero, which means that any number of objects 
   * can be placed in a row or in a column.
   *
   * @param rows   The rows, with the value zero meaning any number of rows.
   * @param cols   The columns, with the value zero meaning any number of columns.
   * @param hgap   The horizontal gap.
   * @param vgap   The vertical gap.
   *
   * @throws   IllegalArgumentException  if the of <code>rows</code> or <code>cols</code> is invalid.
   */
  public SmartGridLayout( int rows, int cols, int hgap, int vgap ) {
    this( rows, cols, hgap, vgap, true );
  }

  /**
   * Creates a grid layout with the specified number of rows and columns.
   * 
   * In addition, the horizontal and vertical gaps are set to the specified values. Horizontal gaps are placed at the 
   * left and right edges, and between each of the columns. Vertical gaps are placed at the top and bottom edges, and 
   * between each of the rows.
   * 
   * One, but not both, of <code>rows</code> and <code>cols</code> can be zero, which means that any number of objects 
   * can be placed in a row or in a column.
   *
   * @param rows       The rows, with the value zero meaning any number of rows.
   * @param cols       The columns, with the value zero meaning any number of columns.
   * @param hgap       The horizontal gap.
   * @param vgap       The vertical gap.
   * @param forceexp   <code>true</code> <=> Objects will be expanded if necessary.
   *
   * @throws  IllegalArgumentException if the of <code>rows</code> or <code>cols</code> is invalid.
   */
  public SmartGridLayout( int rows, int cols, int hgap, int vgap, boolean forceexp ) {
    super( rows, cols, hgap, vgap );
    constraints = new HashMap<>(7);
    expansion   = forceexp;
  }

  @Override
  public Dimension minimumLayoutSize( @NonNull Container parent ) {
    return calcLayoutSize( SizeType.Minimum, parent );
  }

  @Override
  public Dimension preferredLayoutSize( @NonNull Container parent ) {
    return calcLayoutSize( SizeType.Preferred, parent );
  }

  @Override
  public Dimension maximumLayoutSize( @NonNull Container parent ) {
    return calcLayoutSize( SizeType.Maximum, parent );
  }

  @Override
  public void layoutContainer( @NonNull Container parent ) {

    synchronized( parent.getTreeLock() ) {

      // First we need to calculate the minimal width/height of each row/column.
      calcLayoutSize( SizeType.Minimum, parent );

      int    rows    = grid.length;
      int    columns = grid[0].length;
      Insets insets  = parent.getInsets();

      // Get the full size we could occupy
      int width   = parent.getWidth  () - insets.left - insets.right;
      int height  = parent.getHeight () - insets.top  - insets.bottom;
      width      -= columns * super.getHgap();
      height     -= rows    * super.getVgap();

      int swidth  = ArrayFunctions.sumInt( colwidth );
      int sheight = ArrayFunctions.sumInt( rowheight );

      int[] srowheight = new int[ rows    ];
      int[] scolwidth  = new int[ columns ];

      scaleDimension( srowheight , rowheight , maxheight , height , sheight );
      scaleDimension( scolwidth  , colwidth  , maxwidth  , width  , swidth  );

      int ypos = insets.top;
      for( int y = 0; y < rows; y++ ) {

        Component[] componentrow = grid   [y];

        int h    = srowheight[y];

        int xpos = insets.left;
        for( int x = 0; x < columns; x++ ) {

          int w  = scolwidth[x];

          if( componentrow[x] == null ) {
            break;
          } else {

            int rxpos = xpos;
            int rypos = ypos;
            int rw    = w;
            int rh    = h;

            if( ! expansion ) {

              Dimension dim = getDimensionOfComponent( SizeType.Maximum, componentrow[x], true );

              // The user doesn't want expansion, so change the bounds to place the component in the middle.
              if( dim.width < rw ) {
                int diff = (rw - dim.width) / 2;
                rxpos    = rxpos + diff;
                rw       = dim.width;
              }

              if( dim.height < rh ) {
                int diff = (rh - dim.height) / 2;
                rypos    = rypos + diff;
                rh       = dim.height;
              }

            }

            componentrow[x].setBounds( rxpos, rypos, rw, rh );

          }

          xpos  += w + super.getHgap();

        }

        ypos += h + super.getVgap();

      }

    }

  }

  /**
   * This function scales the entries of an array according to the available space.
   *
   * @param result      The result array which values will be overwritten.
   * @param source      The unscaled dimensions for each entry.
   * @param max         The maximum allowed dimensions for each entry.
   * @param available   The available space.
   * @param min         The minimum required space.
   */
  private void scaleDimension( int[] result, int[] source, int[] max, int available, int min ) {

    if( (available <= 0) || (min <= 0) ) {
      System.arraycopy( source, 0, result, 0, source.length );
      return;
    }

    // Calculating scaling factor
    double scale = available / min;

    // Usually this check should be necessary but Java's layouting mechanism allows that some objects will consume 
    // lesser space than their minimum requested, so we'll ignoring this fault simply. Unfortunately this may lead to 
    // bad looking layouts.
    if( scale < 1.0 ) {
      scale = 1.0;
    }

    int consumed = 0;
    for( int i = 0; i < source.length; i++ ) {
      result[i] = (int) (source[i] * scale);
      if( result[i] > max[i] ) {
        result[i] = max[i];
      }
      consumed += result[i];
    }

    if( consumed < available ) {
      redistributeSpace( result, max, available - consumed );
    }

  }

  /**
   * This function redistributes space for the case that some entries rejected some space in case this would exceed  
   * their limit.
   *
   * @param result         The already scaled entries.
   * @param max            The maximum allowed dimensions.
   * @param todistribute   The space which needs to be redistributed.
   */
  private void redistributeSpace( int[] result, int[] max, int todistribute ) {

    double  step    = todistribute / result.length;
    int     ext     = (int) step;
    double  diff    = step - ext;
    double  cum     = 0.0;
    boolean nodist  = true;
    int     newdist = todistribute;

    for( int i = 0; i < result.length; i++ ) {

      int oldval = result[i];
      if( result[i] < max[i] ) {

        nodist      = false;
        result[i]  += ext;
        cum        += diff;
        if( cum >= 1.0 ) {
          int cumext  = (int) cum;
          result[i]  += cumext;
          cum        -= cumext;
        }

        if( result[i] > max[i] ) {
          int unconsumable  = result[i] - max[i];
          result[i]         = max[i];
          newdist          += unconsumable;
        }

      }

      int dist      = result[i] - oldval;
      newdist      -= dist;

    }

    if( (newdist < todistribute) && (! nodist) ) {
      redistributeSpace( result, max, newdist );
    }

  }

  @Override
  public void addLayoutComponent( @NonNull String name, @NonNull Component comp ) {
  }

  @Override
  public void addLayoutComponent( @NonNull Component comp, @NonNull Object constraint ) {
    if( constraint instanceof Integer ) {
      constraints.put( comp, (Integer) constraint );
    }
  }

  @Override
  public void removeLayoutComponent( @NonNull Component comp ) {
    if( constraints.containsKey( comp ) ) {
      constraints.remove( comp );
    }
  }

  @Override
  public float getLayoutAlignmentX( @NonNull Container parent ) {
    return 0.5F;
  }

  @Override
  public float getLayoutAlignmentY( @NonNull Container parent ) {
    return 0.5F;
  }

  @Override
  public void invalidateLayout( @NonNull Container parent ) {
    // Nothing to do here since we need the available size for calculation, so the layouting will be done within
    // layoutContainer().
  }

  /**
   * This function calculates the dimensions of each column/row according to the requested needs.
   *
   * @param desired   Specifies which sizes should be calculated.
   * @param parent    The container which should be layouted.
   *
   * @return   The calculated size. Not <code>null</code>.
   */
  private Dimension calcLayoutSize( SizeType desired, Container parent ) {

    synchronized( parent.getTreeLock() ) {

      int numcomps  = parent.getComponentCount();
      int nrows     = super.getRows();
      int ncols     = super.getColumns();
      int rrows     = nrows;
      int rcols     = ncols;

      if( nrows > 0 ) {
        ncols       = (numcomps + nrows - 1) / nrows;
      } else {
        nrows       = (numcomps + ncols - 1) / ncols;
      }

      grid          = createGrid( parent, ncols, nrows );
      Insets insets = parent.getInsets();

      rrows         = grid.length;
      rcols         = grid[0].length;

      colwidth      = new int[ rcols ];
      rowheight     = new int[ rrows ];
      maxwidth      = new int[ rcols ];
      maxheight     = new int[ rrows ];

      for( int y = 0; y < rrows; y++ ) {
        Component[] row = grid[y];
        for( int x = 0; x < rcols; x++ ) {
          Component current = row[x];
          if( current != null ) {
            Dimension cdim    = getDimensionOfComponent( desired          , current, true );
            Dimension mdim    = getDimensionOfComponent( SizeType.Maximum , current, false );
            maxwidth  [x]     = Math.max( maxwidth  [x], mdim.width  );
            maxheight [y]     = Math.max( maxheight [y], mdim.height );
            colwidth  [x]     = Math.max( colwidth  [x], cdim.width  );
            rowheight [y]     = Math.max( rowheight [y], cdim.height );
          };
        };
      };
      
      for( int i = 0; i < colwidth.length; i++ ) {
        colwidth[i] = Math.max( colwidth[i], 0 );
      }

      for( int i = 0; i < rowheight.length; i++ ) {
        rowheight[i] = Math.max( rowheight[i], 0 );
      }

      int swidth  = ArrayFunctions.sumInt( colwidth );
      int sheight = ArrayFunctions.sumInt( rowheight );

      Dimension result = new Dimension(
        insets.left + insets.right  + swidth  + (rcols - 1) * super.getHgap(),
        insets.top  + insets.bottom + sheight + (rrows - 1) * super.getVgap()
      );

      // Make sure no dimension is negative. This can especially happen when the maximum size was requested.
      if( result.width < 0 ) {
        result.width  = Short.MAX_VALUE;
      }
      if( result.height < 0 ) {
        result.height = Short.MAX_VALUE;
      }

      return result;

    }

  }

  /**
   * This function computes the dimension of a component while determining the constraint used for this component (if 
   * there's one).
   *
   * @param desired    The size that is being requested. Not <code>null</code>.
   * @param comp       The component which size is requested.
   * @param nolimits   <code>true</code> <=> We don't want to try out the unlimited values. This flag is necessary while 
   *                   we would assign unlimited number of pixels to the constrained dimension otherwise.
   *
   * @return   The dimension of a single object depending on the needs and the optional constraint. Not <code>null</code>.
   */
  private final Dimension getDimensionOfComponent( SizeType desired, Component comp, boolean nolimits ) {
    Dimension result     = desired.getSize( comp );
    Integer   constraint = constraints.get( comp );
    if( constraint != null ) {
      overruleDimension( constraint.intValue(), result, comp, nolimits );
    }
    return result;
  }
  
  private void overruleDimension( int mask, Dimension result, Component comp, boolean nolimits ) {
    
    int info = 0;
    
    info = mask & MASK_WIDTH;
    if( info != 0 ) {
             if( info == MASK_MINWIDTH  ) { result.width = comp.getMinimumSize   ().width;
      } else if( info == MASK_MAXWIDTH  ) { result.width = comp.getMaximumSize   ().width;
      } else if( info == MASK_PREFWIDTH ) { result.width = comp.getPreferredSize ().width;
      } else if( ! nolimits             ) { result.width = Short.MAX_VALUE;
      }
    }

    info = mask & MASK_HEIGHT;
    if( info != 0 ) {
             if( info == MASK_MINHEIGHT  ) { result.height = comp.getMinimumSize   ().height;
      } else if( info == MASK_MAXHEIGHT  ) { result.height = comp.getMaximumSize   ().height;
      } else if( info == MASK_PREFHEIGHT ) { result.height = comp.getPreferredSize ().height;
      } else if( ! nolimits              ) { result.height = Short.MAX_VALUE;
      }
    }
    
  }
  

  /**
   * Sets up a new grid which only contains the visible components.
   *
   * @param parent  The container which should be layouted. Not <code>null</code>.
   * @param cols    The number of columns.
   * @param rows    The number of rows.
   *
   * @return  The grid which contains the elements. Not <code>null</code>.
   */
  private static Component[][] createGrid( Container parent, int cols, int rows ) {

    ArrayList<Component> resultlist = new ArrayList<>(7);
    boolean[]            colmask    = new boolean[ cols ];
    boolean[]            rowmask    = new boolean[ rows ];

    Arrays.fill( colmask, true );
    Arrays.fill( rowmask, true );

    int count = parent.getComponentCount();

    for( int y = 0; y < rows; y++ ) {
      for( int x = 0; x < cols; x++ ) {
        int idx  = y * cols + x;
        if( idx < count ) {
          Component comp = parent.getComponent( idx );
          if( comp.isVisible() ) {
            resultlist.add( comp );
            colmask[x] = false;
            rowmask[y] = false;
          } else {
            resultlist.add( null );
          }
        } else {
          resultlist.add( null );
        }
      }
    }

    int newcols = cols;
    int newrows = rows;
    for( int i = cols - 1; i >= 0; i-- ) {
      if( colmask[i] ) {
        removeColumn( resultlist, cols, rows, i );
        newcols--;
      }
    }

    for( int i = rows - 1; i >= 0; i-- ) {
      if( rowmask[i] ) {
        removeRow( resultlist, newcols, rows, i );
        newrows--;
      }
    }

    Component[][] result = new Component[ newrows ][ newcols ];
    for( int y = 0; y < newrows; y++ ) {
      Component[] row = result[y];
      for( int x = 0; x < newcols; x++ ) {
        row[x] = resultlist.get( y * newcols + x );
      }
    }

    return result;

  }

  /**
   * Removes a row from a given grid.
   *
   * @param grid   The grid which row should be removed. Not <code>null</code>
   * @param cols   The number of columns used in that grid.
   * @param rows   The number of rows used in that grid.
   * @param row    The row which should be removed.
   */
  private static void removeRow( ArrayList<Component> grid, int cols, int rows, int row ) {
    for( int i = cols - 1; i >= 0; i-- ) {
      grid.remove( row * cols + i );
    }
  }

  /**
   * Removes a column from a given grid.
   *
   * @param grid     The grid which row should be removed. Not <code>null</code>.
   * @param cols     The number of columns used in that grid.
   * @param rows     The number of rows used in that grid.
   * @param column   The column which should be removed.
   */
  private static void removeColumn( ArrayList<Component> grid, int cols, int rows, int column ) {
    for( int i = rows - 1; i >= 0; i-- ) {
      grid.remove( i * cols + column );
    }
  }

} /* ENDCLASS */
