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

import java.awt.Color;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/** A (JFreeChart) {@link ChartPanel} base implementation for monitoring and editing EQ parameters
 *  of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * 
 * @since 0.4.0
 * 
 */
abstract class JQVGT_EqChart
  extends ChartPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGT_EqChart.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public JQVGT_EqChart (final MidiDevice midiDevice, final JFreeChart jFreeChart)
  {
    super (jFreeChart);
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    if (jFreeChart == null)
      throw new IllegalArgumentException ();
    setMouseZoomable (false);
    final LogarithmicAxis xAxis = new LogarithmicAxis ("f [kHz]");
    xAxis.setRange (new Range (F_MIN_KHZ, F_MAX_KHZ));
    final NumberAxis yAxis = new NumberAxis ("|H| [dB]");
    yAxis.setRange (new Range (H_MIN_DB, H_MAX_DB));
    XYPlot plot = getChart ().getXYPlot ();
    plot.setDomainAxis (xAxis);
    plot.setRangeAxis (yAxis);
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer ();
    renderer.setSeriesShapesVisible (0, true);
    getChart ().setBackgroundPaint (new Color (0, 0, 0, 0));
    final XYSeriesCollection dataSet = new XYSeriesCollection ();
    dataSet.addSeries (this.series_H);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  protected final MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PLOT X/Y BOUNDARIES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static double F_MIN_KHZ = 0.01;
  
  public final static double F_MAX_KHZ = 20.0;

  public final static double H_MIN_DB = -15.0;

  public final static double H_MAX_DB = 15.0;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // XYSERIES - H
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final XYSeries series_H = new XYSeries ("|H|");
  
  protected final XYSeries getSeriesH ()
  {
    return this.series_H;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
