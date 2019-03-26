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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} registering, monitoring and controlling multiple parameters at a MIDI-Device.
 * 
 * <p>
 * This class is intended to be used as a base class for
 * {@link JPanel}s that display and control multiple parameters on a given {@link MidiDevice}
 * through multiple (key-specific) Swing {@link JComponent}s.
 * 
 * <p>
 * Upon construction,
 * this class registers as a {@link MidiDeviceListener} to the {@link MidiDevice} supplied.
 * It takes a {@link Map} as second argument holding the parameter names to be
 * monitored in its key set.
 * These names must all be present as valid parameter names in the {@link Map#keySet}
 * of the {@link MidiDevice}.
 * Each parameter may map onto a {@link Set} of {@link JComponent}s,
 * which are enabled or disabled depending on the corresponding value
 * being non-{@code null} or {@code null},
 * respectively.
 * If a parameter maps onto {@code null},
 * no {@link JComponent}s are being controlled for that key (parameter name).
 * 
 * <p>
 * Note that this class does not add the {@link JComponent}s
 * to this {@link JPanel}; that is up to the sub-classes to implement
 * (since there is no way to guess how the components are to be laid out, if at all).
 * Sub-classes are not obliged to even use object's of this class as their main {@link JPanel}.
 * 
 * <p>
 * Ready-to-go {@link JMidiDeviceMultiParameter}s for <i>single</i> parameters are available in the same package.
 * For instance, {@link JMidiDeviceParameter_Boolean} for a {@link Boolean} parameter.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see JMidiDeviceParameter
 * 
 */
public class JMidiDeviceMultiParameter
  extends JPanel
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Constructs the component.
   * 
   * @param midiDevice   The MIDI device, non-{@code null}.
   * @param componentMap The mapping of keys onto {@link JComponent}s to be monitored and controlled.
   * 
   * @throws IllegalArgumentException If the device or map is {@code null},
   *                                    the map contains null or unregistered keys (at the MIDI device),
   *                                    or if the non-{@code null} values contain {@code null} entries.
   * 
   * @see JMidiDeviceMultiParameter
   * 
   */
  public JMidiDeviceMultiParameter
    (final MidiDevice midiDevice,
     final Map<String, Set<JComponent>> componentMap)
  {
    super ();
    if (midiDevice == null || componentMap == null || ! midiDevice.keySet ().containsAll (componentMap.keySet ()))
      throw new IllegalArgumentException ();
    for (Set<JComponent> component : componentMap.values ())
      if (component != null && component.contains (null))
        throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    this.componentMap = componentMap;
    getMidiDevice ().addMidiDeviceListener (this.midiDeviceListener);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  // GET/SET DATA VALUE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  /** Returns the MIDI device set upon construction.
   * 
   * @return The MIDI device, non-{@code null} and fixed.
   * 
   */
  private MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }

  /** Gets the value of given parameter from the MIDI device.
   * 
   * @param key The key, must be non-{@code null} and registered upon construction.
   * 
   * @return The value, may be {@code null}.
   * 
   * @throws IllegalArgumentException If the key is {@code null} or not registered.
   * 
   * @see MidiDevice#get
   * 
   */
  protected final Object getDataValue (final String key)
  {
    if (! this.componentMap.containsKey (key))
      throw new IllegalArgumentException ();
    return getMidiDevice ().get (key);    
  }
  
  /** Sets the value of given parameter on the MIDI device.
   * 
   * @param key          The key, must be non-{@code null} and registered upon construction.
   * @param newDataValue The new value, non-{@code null}.
   * 
   * @throws IllegalArgumentException If the key is {@code null} or not registered.
   * @throws NullPointerException     If the value is {@code null} (conform {@link Map#put} and {@link MidiDevice#put}).
   * 
   * @see MidiDevice#put
   * 
   */
  protected final void setDataValue (final String key, final Object newDataValue)
  {
    if (! this.componentMap.containsKey (key))
      throw new IllegalArgumentException ();
    getMidiDevice ().put (key, newDataValue);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER
  //
  // DATA VALUE CHANGED
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDeviceListener midiDeviceListener = (final Map<String, Object> changes) ->
  {
    if (changes == null || changes.isEmpty ())
      throw new RuntimeException ();
    final Map<String, Object> relevantChanges = new LinkedHashMap<> (changes);
    relevantChanges.keySet ().retainAll (JMidiDeviceMultiParameter.this.componentMap.keySet ());
    if (relevantChanges.isEmpty ())
      return;
    dataValueChanged (relevantChanges);
  };
  
  /** Notification method for sub-classes indicating that new data values were received from the MIDI device.
   * 
   * <p>
   * Implementations must adapt their GUI component(s) to reflect the new value(s).
   * Beware that this method is most likely invoked from a {@link Thread}
   * <i>other</i> than the Swing EDT.
   * 
   * <p>
   * The method is only called with a non-empty {@code Map}
   * of which the keys are registered at this object (upon construction).
   * Note that the new value(s) may be {@code null}.
   * 
   * <p>
   * The default implementation disables registered {@link JComponent}s for which
   * the corresponding value has turned {@code null},
   * or enables them is the value has turned non-{@code null}.
   * Some sub-classes depend on this (and only this) behavior in order to implement
   * {@link JComponent}s for read-only parameters,
   * so be careful modifying and/or extending the default implementation.
   * 
   * @param changes The changes as a map from registered keys at this object onto their new values; non-{@code null}.
   * 
   */
  protected void dataValueChanged (final Map<String, Object> changes)
  {
    if (changes == null || changes.isEmpty ())
      throw new RuntimeException ();
    if (! this.componentMap.keySet ().containsAll (changes.keySet ()))
      throw new IllegalArgumentException ();
    SwingUtilsJdJ.invokeOnSwingEDT (()->
    {
      for (final Map.Entry<String, Object> entry : changes.entrySet ())
        if (JMidiDeviceMultiParameter.this.componentMap.get (entry.getKey ()) != null)
          for (final JComponent jComponent : JMidiDeviceMultiParameter.this.componentMap.get (entry.getKey ()))
            SwingUtilsJdJ.enableComponentAndDescendants (jComponent, entry.getValue () != null);
    });
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // COMPONENT MAP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final Map<String, Set<JComponent>> componentMap;
  
  /** Obtains the set of keys being monitored.
   * 
   * @return An unmodifiable {@code Set} view of the keys being monitored (non-{@code null}, non-empty and fixed.
   * 
   */
  public final Set<String> getKeys ()
  {
    return Collections.unmodifiableSet (this.componentMap.keySet ());
  }
  
  /** Obtains the {@link JComponent}s being monitored for given key, if any.
   * 
   * @param key The key (parameter name).
   * 
   * @return An unmodifiable {@code Set} view on the {@link JComponent}s being monitored for given key,
   *         or {@code null} if no such components were registered upon construction for given key.
   * 
   * @throws IllegalArgumentException If the key is {@code null} or not registered at construction time.
   * 
   */
  protected final Set<JComponent> getJComponents (final String key)
  {
    if (key == null || ! this.componentMap.containsKey (key))
      throw new IllegalArgumentException ();
    if (this.componentMap.get (key) != null)
      return Collections.unmodifiableSet (this.componentMap.get (key));
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
