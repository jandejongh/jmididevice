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

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} holding the name and value of a (single, simple) MIDI-Device parameter.
 * 
 * <p>
 * Both name and value components are optional.
 * This allows sub-classes to merely use the basic MIDI-device registry
 * functions of this component.
 * 
 * <p>
 * The implementation uses a single-row {@link GridLayout} to lay-out horizontally
 * the name and value components (if any is provided at all).
 * 
 * <p>
 * Note that the name, if non-{@code null} upon construction,
 * may be changed later in order to implement multi-named parameters
 * (e.g., parameters for which the meaning depends on the value of some other parameter);
 * see {@link #setDisplayName}.
 * 
 * <p>
 * Implementations are encouraged to use the super-class for setting from their constructor(s)
 * the initial value on the
 * component as follows: {@code dataValueChanged (Collections.singletonMap (getKey (), getDataValue ()));}.
 * The super-class, in turn, will invoke {@link #dataValueChanged(java.lang.Object)} on this
 * class.
 * This sequence of invocations ensures that the enabled/disabled status of the component(s)
 * is properly set from the super-class.
 * 
 * <p>
 * Ready-to-go {@link JMidiDeviceParameter}s for certain parameter types are available in the same package.
 * For instance, {@link JMidiDeviceParameter_Boolean} for a {@link Boolean} parameter.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @param <C> The value (generic) type.
 * 
 */
public class JMidiDeviceParameter<C>
  extends JMidiDeviceMultiParameter
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final static class JLabel_Private
    extends JLabel
  {
    public JLabel_Private (final String string)
    {
      super (string);
    } 
  }
  
  private static Map<String, Set<JComponent>> createComponentMap
  ( final MidiDevice midiDevice,
    final String displayName,
    final String key,
    final JComponent jValueComponent)
  {
    if (midiDevice == null || key == null || ! midiDevice.containsKey (key))
      throw new IllegalArgumentException ();
    final Map<String, Set<JComponent>> componentMap = new LinkedHashMap<> ();
    if (displayName == null && jValueComponent == null)
      componentMap.put (key, null);
    else
    {
      componentMap.put (key, new LinkedHashSet<> ());
      if (displayName != null)
        componentMap.get (key).add (new JLabel_Private (displayName));
      if (jValueComponent != null)
        componentMap.get (key).add (jValueComponent);
    }
    return componentMap;
  }
  
  /** Constructs the component.
   * 
   * <p>
   * Both name and value component are optional.
   * This allows sub-classes to merely use the basic MIDI-device registry
   * functions of this component.
   * 
   * @param midiDevice      The MIDI device, non-{@code null}.
   * @param displayName     The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key             The parameter key (must exist on the {@code midiDevice}).
   * @param jValueComponent The component to show for the parameter; if {@code null}, no value component is shown.
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceMultiParameter
   * 
   */
  public JMidiDeviceParameter
    (final MidiDevice midiDevice,
     final String displayName,
     final String key,
     final JComponent jValueComponent)
  {
    super (midiDevice, createComponentMap (midiDevice, displayName, key, jValueComponent));
    final Set<JComponent> components = getJComponents (key);
    if (components != null)
    {
      setLayout (new GridLayout (1, components.size (), 5, 5));
      for (final JComponent component : components)
        add (component);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  // GET/SET DATA VALUE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Gets the value of the parameter from the MIDI device.
   * 
   * @return The value, may be {@code null}.
   * 
   * @see JMidiDeviceMultiParameter#getDataValue
   * @see #getKey
   * @see MidiDevice#get
   * 
   */
  protected final C getDataValue ()
  {
    return (C) super.getDataValue (getKey ());
  }
  
  /** Sets the value of the parameter on the MIDI device.
   * 
   * <p>
   * The request is silently ignored if this is a read-only component, see {@link #isReadOnly}.
   * 
   * @param newDataValue The new value, non-{@code null}.
   * 
   * @throws NullPointerException If the value is {@code null} (conform {@link Map#put} and {@link MidiDevice#put}).
   * 
   * @see JMidiDeviceMultiParameter#setDataValue
   * @see #getKey
   * @see MidiDevice#put
   * @see #isReadOnly
   * 
   */
  protected final void setDataValue (final C newDataValue)
  {
    if (! this.readOnly)
      super.setDataValue (getKey (), newDataValue);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER
  //
  // DATA VALUE CHANGED
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Invokes the super method if this component is not read-only and
   *  delegates to {@link #dataValueChanged(java.lang.Object)}.
   * 
   * @param changes The changes as a map from registered keys at this object onto their new values; non-{@code null}.
   * 
   */
  @Override
  protected final void dataValueChanged (final Map<String, Object> changes)
  {
    if (! this.readOnly)
      super.dataValueChanged (changes);
    if (! changes.containsKey (getKey ()))
      throw new RuntimeException ();
    final C value = (C) changes.get (getKey ());
    dataValueChanged (value);
  }
  
  /** Notification method for sub-classes indicating that a new data value was received from the MIDI device.
   * 
   * <p>
   * Implementations must adapt their GUI component(s) to reflect the new value.
   * Beware that this method is most likely invoked from a {@link Thread}
   * <i>other</i> than the Swing EDT.
   * 
   * <p>
   * The default implementation does nothing.
   * 
   * @param newDataValue The new value; may be {@code null}.
   * 
   */
  protected void dataValueChanged (final C newDataValue)
  {
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DISPLAY NAME
  // DISPLAY NAME COMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private JLabel_Private getDisplayNameComponent ()
  {
    final Set<JComponent> components = getJComponents (getKey ());
    if (components == null)
      return null;
    if (components.isEmpty ())
      throw new RuntimeException ();
    else if (components.size () == 1)
      return (components.toArray ()[0] instanceof JLabel_Private) ? (JLabel_Private) components.toArray ()[0] : null;
    else if (components.size () == 2)
      return (JLabel_Private) components.toArray ()[0];
    else
      throw new RuntimeException ();
  }
  
  /** Gets the display name, if present.
   * 
   * @return The display name, {@code null} if not present.
   * 
   */
  public final String getDisplayName ()
  {
    final JLabel_Private displayNameComponent = getDisplayNameComponent ();
    return displayNameComponent != null ? displayNameComponent.getText () : null;
  }
  
  /** Sets the display name.
   * 
   * @param displayName The new display name.
   * 
   * @throws IllegalArgumentException If the new display name is {@code null}
   *                                    while the old one non-{@code} or vice versa.
   * 
   */
  public final void setDisplayName (final String displayName)
  {
    final JLabel_Private displayNameComponent = getDisplayNameComponent ();
    if ( (displayNameComponent == null && displayName != null)
      || (displayNameComponent != null && displayName == null))
      throw new IllegalArgumentException ();
    if (displayNameComponent == null && displayName == null)
      return;
    if (getDisplayName ().equals (displayName))
      return;
    SwingUtilsJdJ.invokeOnSwingEDT (() -> JMidiDeviceParameter.this.getDisplayNameComponent ().setText (displayName));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // KEY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Obtains the parameter key.
   * 
   * @return The parameter key, non-{@code null}, fixed, and registered at the MIDI device.
   * 
   */
  public final String getKey ()
  {
    return (String) getKeys ().toArray ()[0];
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE JCOMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Obtains the value component passed upon construction.
   * 
   * @return The value component passed upon construction, fixed but may be {@code null}.
   * 
   */
  protected final JComponent getValueComponent ()
  {
    final Set<JComponent> components = getJComponents (getKey ());
    if (components == null)
      return null;
    if (components.isEmpty ())
      throw new RuntimeException ();
    else if (components.size () == 1)
      return (components.toArray ()[0] instanceof JLabel_Private) ? null : (JComponent) components.toArray ()[0];
    else if (components.size () == 2)
      return (JComponent) components.toArray ()[1];
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // READ ONLY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private volatile boolean readOnly = false;
  
  /** Returns whether or not this is a read-only component.
   * 
   * <p>
   * Read-only components are always disabled, irrespective of the value of the parameter,
   * which is still properly shown.
   * 
   * <p>
   * The default value of this property is {@code false}.
   * 
   * @return Whether or not this is a read-only component.
   * 
   * @see #setReadOnly
   * 
   */
  public final boolean isReadOnly ()
  {
    return this.readOnly;
  }
  
  /** Sets whether or not this is a read-only component.
   * 
   * <p>
   * Read-only components are always disabled, irrespective of the value of the parameter,
   * which is still properly shown.
   * 
   * <p>
   * The default value of this property is {@code false}.
   * 
   * @param readOnly Whether or not this is a read-only component from now on.
   * 
   * @see #isReadOnly
   * 
   */
  public final void setReadOnly (final boolean readOnly)
  {
    if (readOnly != this.readOnly)
    {
      this.readOnly = readOnly;
      final Object value = getDataValue ();
      SwingUtilsJdJ.invokeOnSwingEDT (() -> SwingUtilsJdJ.enableComponentAndDescendants
        (JMidiDeviceParameter.this, value != null && ! readOnly));
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
