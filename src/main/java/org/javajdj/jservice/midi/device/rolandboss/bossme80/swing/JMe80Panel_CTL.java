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
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceMultiParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Boolean;
import org.javajdj.swing.DefaultMouseListener;
import org.javajdj.swing.JColorCheckBox;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for controlling and monitoring the CTL section of a patch.
 *
 * <p>
 * Subordinate to {@link JMe80Panel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMe80Panel_CTL
  extends JMidiDeviceMultiParameter
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static Map<String, Set<JComponent>> createComponentMap (final MidiDevice midiDevice)
  {
    final Map<String, Set<JComponent>> componentMap = new LinkedHashMap<> ();
    //
    componentMap.put (MidiDevice_Me80.TP_CTL_SW_NAME, new LinkedHashSet<> ());
    componentMap.get (MidiDevice_Me80.TP_CTL_SW_NAME).add
      (new JMidiDeviceParameter_Boolean (midiDevice, "Pressed", MidiDevice_Me80.TP_CTL_SW_NAME));
    //
    componentMap.put (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME, new LinkedHashSet<> ());
    componentMap.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).add
      (new JLabel ("Target"));
    componentMap.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).add
      (new JComboBox<> (MidiDevice_Me80.CtlTargetCustom.values ()));
    componentMap.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).add
      (new JLabel ("Effects"));
    componentMap.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).add
      (new JPanel ());
    componentMap.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).add
      (new JLabel ("Knob Value"));
    componentMap.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).add
      (new JSlider (0, 99));
    //
    componentMap.put (MidiDevice_Me80.TP_CTL_MODE_NAME, new LinkedHashSet<> ());
    componentMap.get (MidiDevice_Me80.TP_CTL_MODE_NAME).add
      (new JMidiDeviceParameter_Enum (midiDevice, "Ctl Mode", MidiDevice_Me80.TP_CTL_MODE_NAME, MidiDevice_Me80.CtlMode.class));
    //
    return componentMap;
  }
  
  /** Creates a {@link JPanel} for controlling and monitoring the CTL section of a patch.
   * 
   * @param midiDevice The MIDI device, non-{@code null} and supporting the ME-80 key set.
   * 
   * @see MidiDevice_Me80
   *
   * @throws IllegalArgumentException If the device argument is {@code null},
   *                                  or does not support the required ME-80 key set.
   * 
   * @see MidiDevice_Me80#TP_CTL_SW_NAME
   * @see MidiDevice_Me80#TP_CTL_TARGET_CUSTOM_NAME
   * @see MidiDevice_Me80#TP_CTL_MODE_NAME
   * 
   */
  public JMe80Panel_CTL (final MidiDevice midiDevice)
  {
    super (midiDevice, createComponentMap (midiDevice));
    setLayout (new GridLayout (6, 1, 5, 5));
    //
    add ((JComponent) getJComponents (MidiDevice_Me80.TP_CTL_SW_NAME).toArray ()[0]);
    //
    final JPanel targetPanel = new JPanel ();
    targetPanel.setLayout (new GridLayout (1, 2, 5, 5));
    targetPanel.add ((JComponent) getJComponents (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).toArray ()[0]);
    this.jTarget = (JComboBox<MidiDevice_Me80.CtlTargetCustom>)
      getJComponents (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).toArray ()[1];
    this.jTarget.addItemListener (this.jTargetItemListener);
    targetPanel.add (this.jTarget);
    add (targetPanel);
    //
    final JPanel effectsPanel = new JPanel ();
    effectsPanel.setLayout (new GridLayout (1, 2, 5, 5));
    effectsPanel.add ((JComponent) getJComponents (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).toArray ()[2]);
    this.effectsValuePanel = (JPanel) getJComponents (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).toArray ()[3];
    this.effectsValuePanel.setLayout (new GridLayout (2, 7, 1, 1));
    this.jEffects = new EnumMap<> (MidiDevice_Me80.CtlEffectCustom.class);
    for (final MidiDevice_Me80.CtlEffectCustom effect : MidiDevice_Me80.CtlEffectCustom.values ())
    {
      final JColorCheckBox.JBoolean checkBox = new JColorCheckBox.JBoolean (JMe80Panel_CTL.EFFECT_COLOR_FUNCTION);
      checkBox.addMouseListener (new EffectValueListener (effect));
      this.jEffects.put (effect, checkBox);
      this.effectsValuePanel.add (checkBox);
    }
    if (this.jEffects.size () != 7)
      throw new RuntimeException ();
    this.effectsValuePanel.add (new JLabel (" C"));
    this.effectsValuePanel.add (new JLabel (" O"));
    this.effectsValuePanel.add (new JLabel (" M"));
    this.effectsValuePanel.add (new JLabel (" D"));
    this.effectsValuePanel.add (new JLabel (" A"));
    this.effectsValuePanel.add (new JLabel (" E"));
    this.effectsValuePanel.add (new JLabel (" R"));
    effectsPanel.add (this.effectsValuePanel);
    add (effectsPanel);
    //
    final JPanel knobValuePanel = new JPanel ();
    knobValuePanel.setLayout (new GridLayout (1, 2, 5, 5));
    knobValuePanel.add ((JComponent) getJComponents (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).toArray ()[4]);
    this.jKnobValue = (JSlider) getJComponents (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME).toArray ()[5];
    this.jKnobValue.addChangeListener (this.jKnobValueListener);
    knobValuePanel.add (this.jKnobValue);
    add (knobValuePanel);
    //
    add ((JComponent) getJComponents (MidiDevice_Me80.TP_CTL_MODE_NAME).toArray ()[0]);
    //
    add (new JPanel ());
    //
    dataValueChanged (Collections.singletonMap
      (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME,
       (MidiDevice_Me80.CtlTargetAndKnobValueCustom) getDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME)));
    //
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DATA VALUE CHANGED [SUPER-CLASS: MIDI DEVICE LISTENER]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected final void dataValueChanged (final Map<String, Object> changes)
  {
    super.dataValueChanged (changes);
    if (! changes.containsKey (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME))
      return;
    final MidiDevice_Me80.CtlTargetAndKnobValueCustom newDataValue
      = (MidiDevice_Me80.CtlTargetAndKnobValueCustom) changes.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME);
    SwingUtilsJdJ.invokeOnSwingEDT (() ->
    {
      if (newDataValue == null)
      {
        this.jTarget.setSelectedItem (null);
        for (final JColorCheckBox.JBoolean checkBox : this.jEffects.values ())
          checkBox.setDisplayedValue (null);
      }
      else
      {
        this.jTarget.setSelectedItem (newDataValue.getTarget ());
        // SwingUtilsJdJ.enableComponentAndDescendants (this.jTarget, true);
        if (newDataValue.getTarget () == MidiDevice_Me80.CtlTargetCustom.EFFECTS)
        {
          // SwingUtilsJdJ.enableComponentAndDescendants (this.effectsValuePanel, true);
          final Set<MidiDevice_Me80.CtlEffectCustom> effectsSet = newDataValue.getCtlEffectsCustom ();
          for (final Map.Entry<MidiDevice_Me80.CtlEffectCustom, JColorCheckBox.JBoolean> entry : this.jEffects.entrySet ())
            entry.getValue ().setDisplayedValue (effectsSet.contains (entry.getKey ()));
          SwingUtilsJdJ.enableComponentAndDescendants (this.jKnobValue, false);
        }
        else
        {
          SwingUtilsJdJ.enableComponentAndDescendants (this.effectsValuePanel, false);
          for (final JColorCheckBox.JBoolean checkBox : this.jEffects.values ())
            checkBox.setDisplayedValue (null);
          JMe80Panel_CTL.this.jKnobValue.setValue (newDataValue.getKnobValue ());
          JMe80Panel_CTL.this.jKnobValue.setToolTipText (Integer.toString (newDataValue.getKnobValue ()));
          // SwingUtilsJdJ.enableComponentAndDescendants (this.jKnobValue, true);
        }
      }      
    });
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TARGET COMPONENT 
  // TARGET COMPONENT ITEM LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final JComboBox<MidiDevice_Me80.CtlTargetCustom> jTarget;
  
  private final ItemListener jTargetItemListener = (final ItemEvent ie) ->
  {
    if (ie == null)
      return;
    if (ie.getStateChange() == ItemEvent.SELECTED)
    {
      final Object item = ie.getItem ();
      if (item == null)
        return;
      final MidiDevice_Me80.CtlTargetAndKnobValueCustom oldValue = (MidiDevice_Me80.CtlTargetAndKnobValueCustom)
        JMe80Panel_CTL.this.getDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME);
      if (oldValue == null)
        return;
      final MidiDevice_Me80.CtlTargetAndKnobValueCustom newValue = oldValue.withTarget ((MidiDevice_Me80.CtlTargetCustom) item);
      JMe80Panel_CTL.this.setDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME, newValue);
    }
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EFFECTS COMPONENTS
  // EFFECT VALUE LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Function<Boolean, Color> EFFECT_COLOR_FUNCTION = (Boolean t) -> t != null && t ? Color.RED : null;

  private final EnumMap<MidiDevice_Me80.CtlEffectCustom, JColorCheckBox.JBoolean> jEffects;

  private final JPanel effectsValuePanel;
  
  private final class EffectValueListener
    extends DefaultMouseListener
  {

    public EffectValueListener (final MidiDevice_Me80.CtlEffectCustom effect)
    {
      if (effect == null)
        throw new IllegalArgumentException ();
      this.effect = effect;
    }

    private final MidiDevice_Me80.CtlEffectCustom effect;
    
    @Override
    public final void mouseClicked (final MouseEvent e)
    {
      final MidiDevice_Me80.CtlTargetAndKnobValueCustom oldCustomValue = (MidiDevice_Me80.CtlTargetAndKnobValueCustom)
        JMe80Panel_CTL.this.getDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME);
      if (oldCustomValue == null)
        return;
      final MidiDevice_Me80.CtlTargetAndKnobValueCustom newValue = oldCustomValue.withEffectToggled (this.effect);
      JMe80Panel_CTL.this.setDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME, newValue);
    }

  };

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // KNOB VALUE COMPONENT
  // KNOB VALUE COMPONENT CHANGE LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final JSlider jKnobValue;
  
  private final ChangeListener jKnobValueListener = (final ChangeEvent ce) ->
  {
    if (ce.getSource () == JMe80Panel_CTL.this.jKnobValue)
    {
      if (! JMe80Panel_CTL.this.jKnobValue.getValueIsAdjusting ())
      {
        final int newKnobValue = JMe80Panel_CTL.this.jKnobValue.getValue ();
        JMe80Panel_CTL.this.jKnobValue.setToolTipText (Integer.toString (newKnobValue));
        final MidiDevice_Me80.CtlTargetAndKnobValueCustom oldCustomValue = (MidiDevice_Me80.CtlTargetAndKnobValueCustom)
          JMe80Panel_CTL.this.getDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME);
        if (oldCustomValue == null)
          return;
        final MidiDevice_Me80.CtlTargetAndKnobValueCustom newCustomValue = oldCustomValue.withKnobValue (newKnobValue);
        JMe80Panel_CTL.this.setDataValue (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME, newCustomValue);
      }
    }
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
}
