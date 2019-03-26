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
package org.javajdj.jservice.midi.device.swing;

import java.util.Collections;
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
  
  private static JSlider createValueComponent (final int minValue, final int maxValue)
  {
    return new JSlider (minValue, maxValue);
  }

  /** Constructs the component.
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
  public JMidiDeviceParameter_Integer_Slider (final MidiDevice midiDevice,
                                         final String displayName,
                                         final String key,
                                         final int minValue,
                                         final int maxValue)
  {
    super (midiDevice, displayName, key, createValueComponent (minValue, maxValue));
    getSlider ().setMajorTickSpacing (maxValue - minValue);
    // getSlider ().setPaintTicks (true);
    getSlider ().setPaintLabels (true);
    getSlider ().addChangeListener (new ValueChangeListener ());
    dataValueChanged (Collections.singletonMap (getKey (), getDataValue ()));
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
          final int newValue = JMidiDeviceParameter_Integer_Slider.this.getSlider ().getValue ();
          JMidiDeviceParameter_Integer_Slider.this.getSlider ().setToolTipText (Integer.toString (newValue));
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
        getSlider ().setValue (newDataValue);
    });
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
