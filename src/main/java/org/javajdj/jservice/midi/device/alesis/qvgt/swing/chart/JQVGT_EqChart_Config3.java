/* 
 * Copyright 2019 Jan de Jongh <jfcmdejongh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.javajdj.jservice.midi.device.alesis.qvgt.swing.chart;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.swing.SwingUtilsJdJ;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/** A (JFreeChart) {@link ChartPanel} for monitoring and editing Config-3 EQ parameters
 *  of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Config-3 EQ features a 11-Band Graphic Equalizer.
 * Editing the settings is supported through mouse dragging.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public final class JQVGT_EqChart_Config3
  extends JQVGT_EqChart
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGT_EqChart_Config3.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static JFreeChart createJFreeChart ()
  {
    final XYSeries series_H = new XYSeries ("|H|");
    final XYSeriesCollection dataset_H = new XYSeriesCollection ();
    dataset_H.addSeries (series_H);
    return ChartFactory.createXYLineChart (
      null, // "XY Chart",
      "f [Khz]",
      "|H| [dB]",
      dataset_H,
      PlotOrientation.VERTICAL,
      false,
      false,
      false);
  }
  
  public JQVGT_EqChart_Config3 (final MidiDevice midiDevice)
  {
    super (midiDevice, createJFreeChart ());
    this.series_H = ((XYSeriesCollection) getChart ().getXYPlot ().getDataset ()).getSeries (0);
    getMidiDevice ().addMidiDeviceListener (this.midiDeviceListener);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDeviceListener midiDeviceListener = (final Map<String, Object> changes) ->
  {
    if (changes == null)
      return;
    if (changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00016_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00032_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00062_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00126_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00250_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00500_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_01000_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_02000_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_04000_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_08000_HZ_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_16000_HZ_NAME))
    {
      SwingUtilsJdJ.invokeOnSwingEDT (
        () -> setGuiParameters ((Patch_QGVT.Configuration) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME)));
    }
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SET GUI PARAMETERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private void setGuiParameters (final Patch_QGVT.Configuration configuration)
  {
    // Make sure we cancel a pending key-dragging procedure.
    this.keyDragging = null;
    if (configuration != null)
    {
      switch (configuration)
      {
        case C1_EQ_PCH_DL_REV:
        case C2_LES_DL_REV:
        case C4_5EQ_PCH_DL:
        case C5_3EQ_REV:
        case C6_RING_DL_REV:
        case C7_RESO_DL_REV:
        case C8_SAMPLING:
          break;
        case C3_GEQ_DL:
        {
          final Integer H_00016 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00016_HZ_NAME);
          final Integer H_00032 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00032_HZ_NAME);
          final Integer H_00062 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00062_HZ_NAME);
          final Integer H_00126 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00126_HZ_NAME);
          final Integer H_00250 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00250_HZ_NAME);
          final Integer H_00500 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00500_HZ_NAME);
          final Integer H_01000 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_01000_HZ_NAME);
          final Integer H_02000 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_02000_HZ_NAME);
          final Integer H_04000 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_04000_HZ_NAME);
          final Integer H_08000 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_08000_HZ_NAME);
          final Integer H_16000 = (Integer) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_16000_HZ_NAME);
          this.series_H.clear ();
          this.series_H.add ( 0.016, H_00016 != null ? (H_00016 - 14) : null);
          this.series_H.add ( 0.032, H_00032 != null ? (H_00032 - 14) : null);
          this.series_H.add ( 0.062, H_00062 != null ? (H_00062 - 14) : null);
          this.series_H.add ( 0.126, H_00126 != null ? (H_00126 - 14) : null);
          this.series_H.add ( 0.250, H_00250 != null ? (H_00250 - 14) : null);
          this.series_H.add ( 0.500, H_00500 != null ? (H_00500 - 14) : null);
          this.series_H.add ( 1.000, H_01000 != null ? (H_01000 - 14) : null);
          this.series_H.add ( 2.000, H_02000 != null ? (H_02000 - 14) : null);
          this.series_H.add ( 4.000, H_04000 != null ? (H_04000 - 14) : null);
          this.series_H.add ( 8.000, H_08000 != null ? (H_08000 - 14) : null);
          this.series_H.add (16.000, H_16000 != null ? (H_16000 - 14) : null);
          break;
        }
        default:
          throw new RuntimeException ();
      }
    }
    // We need to validate (); but this was found through trial-and-error.
    validate ();
    repaint ();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SERIES H
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final XYSeries series_H;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // RELATIVE TOLERANCE IN PLOT-VALUE TO DEVICE KEY/VALUE CONVERSION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final double TOLERANCE = 0.1;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [CENTER] FREQUENCY -> MIDI-DEVICE KEY NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static String f_KHz2MidiDeviceKeyName (final double f_KHz)
  {
    if (f_KHz < 0.016 * (1.0 - TOLERANCE))
      return null;
    if (f_KHz >= 0.016 * (1.0 - TOLERANCE) && f_KHz <= 0.016 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00016_HZ_NAME;
    else if (f_KHz >= 0.032 * (1.0 - TOLERANCE) && f_KHz <= 0.032 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00032_HZ_NAME;
    else if (f_KHz >= 0.062 * (1.0 - TOLERANCE) && f_KHz <= 0.062 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00062_HZ_NAME;
    else if (f_KHz >= 0.126 * (1.0 - TOLERANCE) && f_KHz <= 0.126 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00126_HZ_NAME;
    else if (f_KHz >= 0.250 * (1.0 - TOLERANCE) && f_KHz <= 0.250 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00250_HZ_NAME;
    else if (f_KHz >= 0.500 * (1.0 - TOLERANCE) && f_KHz <= 0.500 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00500_HZ_NAME;
    else if (f_KHz >= 1.000 * (1.0 - TOLERANCE) && f_KHz <= 1.000 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_01000_HZ_NAME;
    else if (f_KHz >= 2.000 * (1.0 - TOLERANCE) && f_KHz <= 2.000 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_02000_HZ_NAME;
    else if (f_KHz >= 4.000 * (1.0 - TOLERANCE) && f_KHz <= 4.000 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_04000_HZ_NAME;
    else if (f_KHz >= 8.000 * (1.0 - TOLERANCE) && f_KHz <= 8.000 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_08000_HZ_NAME;
    else if (f_KHz >= 16.000 * (1.0 - TOLERANCE) && f_KHz <= 16.000 * (1.0 + TOLERANCE))
      return MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_16000_HZ_NAME;
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // |H| -> MIDI-DEVICE VALUE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static int H_MIN_DEVICE_DB = -14;
  
  public final static int H_MAX_DEVICE_DB = 14;
  
  public final static int H_OFFSET_DEVICE_DB = 14;
  
  private static Integer H_dB2MidiDeviceValue (final double H_dB)
  {
    if (H_dB < JQVGT_EqChart.H_MIN_DB || H_dB > JQVGT_EqChart.H_MAX_DB)
      return null;
    final int H_db_int_unbounded = (int) Math.round (H_dB);
    final int H_db_int = Math.max (JQVGT_EqChart_Config3.H_MIN_DEVICE_DB,
                                   Math.min (H_db_int_unbounded, JQVGT_EqChart_Config3.H_MAX_DEVICE_DB));
    return H_db_int + JQVGT_EqChart_Config3.H_OFFSET_DEVICE_DB;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MOUSE EVENT HANDLING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private volatile String keyDragging = null;
  
  /** Invokes super method and detects the start value editing through mouse dragging.
   * 
   * @param e The mouse event.
   * 
   */
  @Override
  public final void mousePressed (final MouseEvent e)
  {
    super.mousePressed (e);
    if (getChart () == null)
      return;
    if (this.keyDragging != null)
    {
      LOG.log (Level.SEVERE, "Unexpected DRAGGING state [mousePressed]!");
      throw new RuntimeException ();
    }
    //
    // Code copied and slightly modified from org.jfree.chart.ChartPanel.mouseClicked and other sources.
    //
    final Insets insets = getInsets ();
    final int x = (int) ((e.getX () - insets.left) / getScaleX ());
    final int y = (int) ((e.getY () - insets.top) / getScaleY ());
    getChart ().setNotify (true); // Force a redraw.
    final ChartRenderingInfo info = getChartRenderingInfo ();
    if (info != null)
    {
      final EntityCollection entities = info.getEntityCollection ();
      if (entities != null)
      {
        final ChartEntity entity = entities.getEntity (x, y);
        if (entity != null && entity instanceof XYItemEntity)
        {
          final XYItemEntity e_xy = (XYItemEntity) entity;
          final XYDataset d = e_xy.getDataset ();
          final int s = e_xy.getSeriesIndex ();
          final int i = e_xy.getItem();
          final XYPlot xyPlot = getChart ().getXYPlot ();
          final Rectangle2D dataArea = getChartRenderingInfo ().getPlotInfo ().getDataArea ();
          final double x_c = xyPlot.getDomainAxis ().java2DToValue (x, dataArea, xyPlot.getDomainAxisEdge ());
          final double y_c = xyPlot.getRangeAxis ().java2DToValue (y, dataArea, xyPlot.getRangeAxisEdge ());
          this.keyDragging = JQVGT_EqChart_Config3.f_KHz2MidiDeviceKeyName (x_c);
          if (this.keyDragging == null)
            throw new RuntimeException ();
          // LOG.log (Level.INFO, "DRAGGING START; x={0}, y={1}, X={2}, Y={3}, x_c={4}, y_c={5}, keyDragging={6}.",
          //   new Object[]{x, y, d.getX (s, i), d.getY (s, i), x_c, y_c, this.keyDragging});
        }
      }
    }
  }

  /** Invokes super method.
   * 
   * <p>
   * In future versions, we may opt to synchronously update the value on the {@link MidiDevice}
   * <i>while</i> dragging.
   * 
   * @param e The mouse event.
   * 
   */
  @Override
  public final void mouseDragged (final MouseEvent e)
  {
    super.mouseDragged (e);
    if (this.keyDragging != null)
    {
      // LOG.log (Level.INFO, "DRAGGING {0}.", this.keyDragging);
      // Here, may want to opt for synchronous updates to the MidiDevice...
    }
  }

  /** Invokes super method and if editing through mouse dragging, sets the new value on the {@link MidiDevice} .
   * 
   * @param e The mouse event.
   * 
   */
  @Override
  public final void mouseReleased (final MouseEvent e)
  {
    super.mouseReleased (e);
    if (this.keyDragging != null)
    {
      final Insets insets = getInsets ();
      final int x = (int) ((e.getX () - insets.left) / getScaleX ());
      final int y = (int) ((e.getY () - insets.top) / getScaleY ());
      final XYPlot xyPlot = getChart ().getXYPlot ();
      final Rectangle2D dataArea = getChartRenderingInfo ().getPlotInfo ().getDataArea ();
      final double x_c = xyPlot.getDomainAxis ().java2DToValue (x, dataArea, xyPlot.getDomainAxisEdge ());
      final double y_c = xyPlot.getRangeAxis ().java2DToValue (y, dataArea, xyPlot.getRangeAxisEdge ());
      final Integer y_c_value = H_dB2MidiDeviceValue (y_c);
      if (y_c_value == null)
      {
        LOG.log (Level.WARNING, "Got null |H| value; y={0}, y_c={1}.", new Object[]{y, y_c});
        this.keyDragging = null;
        return;
      }
      final String key = this.keyDragging;
      this.keyDragging = null;
      // LOG.log (Level.INFO, "DRAGGING END; x={0}, y={1}, x_c={2}, y_c={3}, key={4}, value={5}.",
      //   new Object[]{x, y, x_c, y_c, key, y_c_value});
      getMidiDevice ().put (key, y_c_value);
    }
  }

  /** Invokes super method and cancels (when active) any editing through mouse dragging.
   * 
   * @param e The mouse event.
   * 
   */
  @Override
  public final void mouseClicked (final MouseEvent e)
  {
    super.mouseClicked (e);
    if (this.keyDragging != null)
    {
      // LOG.log (Level.INFO, "DRAGGING ABORT {0}.", this.keyDragging);
      this.keyDragging = null;
    }
  }

  /** Invokes super method and cancels (when active) any editing through mouse dragging.
   * 
   * @param e The mouse event.
   * 
   */
  @Override
  public final void mouseExited (final MouseEvent e)
  {
    super.mouseExited (e);
    if (this.keyDragging != null)
    {
      // LOG.log (Level.INFO, "DRAGGING ABORT {0}.", this.keyDragging);
      this.keyDragging = null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
