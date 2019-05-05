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

import org.javajdj.swing.DefaultMouseListener;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.JColorCheckBox;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JMidiDeviceParameter} for a {@link Boolean}-valued parameter.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMidiDeviceParameter_Boolean
  extends JMidiDeviceParameter<Boolean>
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static JColorCheckBox.JBoolean createValueComponent ()
  {
    final Map<Boolean, Color> colorMap = new HashMap<> ();
    colorMap.put (false, null);
    colorMap.put (true, Color.red);
    return new JColorCheckBox.JBoolean (colorMap);
  }

  /** Constructs the component.
   * 
   * @param midiDevice      The MIDI device, non-{@code null}.
   * @param displayName     The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key             The parameter key (must exist on the {@code midiDevice}).
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_Boolean (final MidiDevice midiDevice,
                                      final String displayName,
                                      final String key)
  {
    super (midiDevice, displayName, key, createValueComponent ());
    getCheckBox ().addMouseListener (new ValueMouseListener ());
    dataValueChanged (Collections.singletonMap (getKey (), getDataValue ()));
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE COMPONENT
  // VALUE COMPONENT LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Returns the value component, a {@link JColorCheckBox.JBoolean}.
   * 
   * @return The value component, non-{@code null}.
   * 
   * @see #getValueComponent
   * 
   */
  protected final JColorCheckBox.JBoolean getCheckBox ()
  {
    return (JColorCheckBox.JBoolean) getValueComponent ();
  }
  
  private class ValueMouseListener
    extends DefaultMouseListener
  {
    
    @Override
    public final void mouseClicked (MouseEvent e)
    {
      if (isReadOnly ())
        return;
      final Boolean currentValue = JMidiDeviceParameter_Boolean.this.getCheckBox ().getDisplayedValue ();
      final boolean newValue = currentValue != null ? (! currentValue) : true;
      JMidiDeviceParameter_Boolean.this.setDataValue (newValue);
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
   * @see #getCheckBox
   * 
   */
  @Override
  protected final void dataValueChanged (final Boolean newDataValue)
  {
    super.dataValueChanged (newDataValue);
    SwingUtilsJdJ.invokeOnSwingEDT (() -> getCheckBox ().setDisplayedValue (newDataValue));
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
