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
package org.javajdj.jservice.midi.device.swing.parameter;

import java.util.Collections;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JMidiDeviceParameter} for an {@link Integer}-valued parameter.
 * 
 * <p>
 * The value is shown in a {@link JSlider}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMidiDeviceParameter_Integer_Slider
  extends JMidiDeviceParameter<Integer>
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static JSlider createValueComponent (
    final int minValue,
    final int maxValue,
    final int displayOffset,
    final Double displayScale)
  {
    return new JSlider (minValue + displayOffset, maxValue + displayOffset);
  }

  /** Constructs the component.
   * 
   * @param midiDevice      The MIDI device, non-{@code null}.
   * @param displayName     The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key             The parameter key (must exist on the {@code midiDevice}).
   * @param minValue        The minimum parameter value (inclusive), see {@link JSlider#JSlider(int, int)}.
   * @param maxValue        The maximum parameter value (inclusive), see {@link JSlider#JSlider(int, int)}.
   * @param displayOffset   The offset, positive or negative, to apply to the parameter value in order to display it.
   * @param displayScale    The scale factor for values shown, ignored (defaults to unity) when {@code null}.
   * @param formatString    The optional format string for displaying values
   *                          (may be {@code null} which results in default formatting).
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_Integer_Slider
  ( final MidiDevice midiDevice,
    final String displayName,
    final String key,
    final int minValue,
    final int maxValue,
    final int displayOffset,
    final Double displayScale,
    final String formatString)
  {
    super (midiDevice, displayName, key, createValueComponent (minValue, maxValue, displayOffset, displayScale));
    this.displayOffset = displayOffset;
    this.displayScale = displayScale;
    this.formatString = formatString;
    getSlider ().setMajorTickSpacing (maxValue - minValue);
    final Hashtable<Integer, JLabel> labels = new Hashtable<> ();
    if (this.displayScale != null)
    {
      labels.put (minValue + this.displayOffset,
        new JLabel(getValueString (this.displayScale * (minValue + this.displayOffset))));
      labels.put (maxValue + this.displayOffset,
        new JLabel(getValueString (this.displayScale * (maxValue + this.displayOffset))));
    }
    else
    {
      labels.put (minValue + this.displayOffset, new JLabel(getValueString (minValue + this.displayOffset)));
      labels.put (maxValue + this.displayOffset, new JLabel(getValueString (maxValue + this.displayOffset)));      
    }
    getSlider ().setLabelTable (labels);
    getSlider ().setPaintLabels (true);
    getSlider ().addChangeListener (new ValueChangeListener ());
    dataValueChanged (Collections.singletonMap (getKey (), getDataValue ()));
  }
  
  /** Constructs the component.
   * 
   * <p>
   * Constructor for unity display scaling cases.
   * 
   * @param midiDevice      The MIDI device, non-{@code null}.
   * @param displayName     The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key             The parameter key (must exist on the {@code midiDevice}).
   * @param minValue        The minimum parameter value (inclusive), see {@link JSlider#JSlider(int, int)}.
   * @param maxValue        The maximum parameter value (inclusive), see {@link JSlider#JSlider(int, int)}.
   * @param displayOffset   The offset, positive or negative, to apply to the parameter value in order to display it.
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_Integer_Slider
  ( final MidiDevice midiDevice,
    final String displayName,
    final String key,
    final int minValue,
    final int maxValue,
    final int displayOffset)
  {
    this (midiDevice, displayName, key, minValue, maxValue, displayOffset, null, null);
  }
  
  /** Constructs the component.
   * 
   * <p>
   * Constructor for zero display offset cases.
   * 
   * @param midiDevice      The MIDI device, non-{@code null}.
   * @param displayName     The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key             The parameter key (must exist on the {@code midiDevice}).
   * @param minValue        The minimum parameter value (inclusive), see {@link JSlider#JSlider(int, int)}.
   * @param maxValue        The maximum parameter value (inclusive), see {@link JSlider#JSlider(int, int)}.
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_Integer_Slider
  ( final MidiDevice midiDevice,
    final String displayName,
    final String key,
    final int minValue,                                     
    final int maxValue)
  {
    this (midiDevice, displayName, key, minValue, maxValue, 0, null, null);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DISPLAY OFFSET
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final int displayOffset;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DISPLAY SCALE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final Double displayScale;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // FORMAT STRING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final String formatString;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // GET VALUE STRING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private String getValueString (final int value)
  {
    if (this.formatString == null)
      return Integer.toString (value);
    else
      return String.format (this.formatString, value);
  }
  
  private String getValueString (final double value)
  {
    if (this.formatString == null)
      return Double.toString (value);
    else
      return String.format (this.formatString, value);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE COMPONENT
  // VALUE COMPONENT LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Returns the value component, a {@link JSlider}.
   * 
   * @return The value component, non-{@code null}.
   * 
   * @see #getValueComponent
   * 
   */
  public final JSlider getSlider ()
  {
    return (JSlider) getValueComponent ();
  }
  
  private class ValueChangeListener
    implements ChangeListener
  {

    @Override
    public void stateChanged (ChangeEvent ce)
    {
      if (isReadOnly ())
        return;
      if (ce.getSource () == JMidiDeviceParameter_Integer_Slider.this.getSlider ())
      {
        if (! JMidiDeviceParameter_Integer_Slider.this.getSlider ().getValueIsAdjusting ())
        {
          final int newDisplayedValue = JMidiDeviceParameter_Integer_Slider.this.getSlider ().getValue ();
          final int newValue = newDisplayedValue - JMidiDeviceParameter_Integer_Slider.this.displayOffset;
          final String toolTipText;
          if (JMidiDeviceParameter_Integer_Slider.this.displayScale == null)
            toolTipText = Integer.toString (newDisplayedValue);
          else
            toolTipText = JMidiDeviceParameter_Integer_Slider.this.getValueString (
              JMidiDeviceParameter_Integer_Slider.this.displayScale * newDisplayedValue);
          JMidiDeviceParameter_Integer_Slider.this.getSlider ().setToolTipText (toolTipText);
          // Set the value on the device, but avoid unnecessary updates.
          // (Some components backfire non-GUI induced changes to the displayed value.)
          final Integer oldValue = getDataValue ();
          if (oldValue == null || oldValue != newValue)
            JMidiDeviceParameter_Integer_Slider.this.setDataValue (newValue);
        }
      }
    }
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DATA VALUE CHANGED [SUPER-CLASS: MIDI DEVICE LISTENER]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Sets the new value on the value component.
   * 
   * @param newDataValue The new value; may be {@code null}.
   * 
   * @see #getValueComponent
   * @see #getSlider
   * 
   */
  @Override
  protected final void dataValueChanged (final Integer newDataValue)
  {
    super.dataValueChanged (newDataValue);
    SwingUtilsJdJ.invokeOnSwingEDT (() ->
    {
      if (newDataValue != null)
        getSlider ().setValue (newDataValue + JMidiDeviceParameter_Integer_Slider.this.displayOffset);
    });
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
