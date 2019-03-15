package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceEnumParameter;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.swing.JColorCheckBox;

/**
 *
 * @author Jan de Jongh <jfcmdejongh@gmail.com>
 */
public class JMe80Panel_CTL
  extends JPanel
{
  
  public JMe80Panel_CTL (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (6, 1, 5, 5));
    //
    final JPanel targetPanel = new JPanel ();
    targetPanel.setLayout (new GridLayout (1, 2, 5, 5));
    targetPanel.add (new JLabel ("Target"));
    this.jTarget = new JComboBox<> (MidiDevice_Me80.CtlTargetCustom.values ());
    this.jTarget.setSelectedItem (null);
    this.jTarget.setEnabled (false);
    targetPanel.add (this.jTarget);
    add (targetPanel);
    //
    final JPanel effectsPanel = new JPanel ();
    effectsPanel.setLayout (new GridLayout (1, 2, 5, 5));
    effectsPanel.add (new JLabel ("Effects"));
    this.effectsValuePanel = new JPanel ();
    this.effectsValuePanel.setLayout (new GridLayout (2, 7, 1, 1));
    this.effectsValuePanel.setEnabled (false);
    this.jEffects = new EnumMap<> (MidiDevice_Me80.CtlEffectsCustom.class);
    for (final MidiDevice_Me80.CtlEffectsCustom effect : MidiDevice_Me80.CtlEffectsCustom.values ())
    {
      final JColorCheckBox.JBoolean checkBox = new JColorCheckBox.JBoolean (JMe80Panel_CTL.EFFECT_COLOR_FUNCTION);
      checkBox.setDisplayedValue (null);
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
    knobValuePanel.add (new JLabel ("Knob Value"));
    this.jKnobValue = new JSlider (0, 99);
    this.jKnobValue.setEnabled (false);
    // this.jKnobValue.setXXX (false);
    knobValuePanel.add (this.jKnobValue);
    add (knobValuePanel);
    //
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Ctl Mode", MidiDevice_Me80.TP_CTL_MODE_NAME, MidiDevice_Me80.CtlMode.class));
    //
    add (new JPanel ());
    add (new JPanel ());
    //
    this.midiDevice.addMidiDeviceListener (this.midiDeviceListener);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;  
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final MidiDeviceListener midiDeviceListener = (final Map<String, Object> changes) ->
  {
    if (changes == null)
      throw new IllegalArgumentException ();
    if (changes.containsKey (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME))
      JMe80Panel_CTL.this.updateFromDevice (
        (MidiDevice_Me80.CtlTargetAndKnobValueCustom) changes.get (MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME));
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TARGET COMPONENT 
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final JComboBox<MidiDevice_Me80.CtlTargetCustom> jTarget;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EFFECTS COMPONENTS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Function<Boolean, Color> EFFECT_COLOR_FUNCTION = (Boolean t) -> t != null && t ? Color.RED : null;

  private final EnumMap<MidiDevice_Me80.CtlEffectsCustom, JColorCheckBox.JBoolean> jEffects;

  private final JPanel effectsValuePanel;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // KNOB VALUE COMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final JSlider jKnobValue;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // UPDATE FROM DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void updateFromDevice (final MidiDevice_Me80.CtlTargetAndKnobValueCustom ctlTargetAndKnobValueCustom)
  {
    if (! SwingUtilities.isEventDispatchThread ())
      SwingUtilities.invokeLater (() -> updateFromDevice (ctlTargetAndKnobValueCustom));
    else
    {
      if (ctlTargetAndKnobValueCustom == null)
      {
        this.jTarget.setSelectedItem (null);
        this.jTarget.setEnabled (false);
        this.effectsValuePanel.setEnabled (false);
        for (final JColorCheckBox.JBoolean checkBox : this.jEffects.values ())
          checkBox.setDisplayedValue (null);
      }
      else
      {
        this.jTarget.setSelectedItem (ctlTargetAndKnobValueCustom.getTarget ());
        this.jTarget.setEnabled (true);
        if (ctlTargetAndKnobValueCustom.getTarget () == MidiDevice_Me80.CtlTargetCustom.EFFECTS)
        {
          this.effectsValuePanel.setEnabled (true);
          final Set<MidiDevice_Me80.CtlEffectsCustom> effectsSet = ctlTargetAndKnobValueCustom.getCtlEffectsCustom ();
          for (final Map.Entry<MidiDevice_Me80.CtlEffectsCustom, JColorCheckBox.JBoolean> entry : this.jEffects.entrySet ())
            entry.getValue ().setDisplayedValue (effectsSet.contains (entry.getKey ()));
          this.jKnobValue.setEnabled (false);
        }
        else
        {
          this.effectsValuePanel.setEnabled (false);
          for (final JColorCheckBox.JBoolean checkBox : this.jEffects.values ())
            checkBox.setDisplayedValue (null);
          this.jKnobValue.setValue (ctlTargetAndKnobValueCustom.getKnobValue ());
          this.jKnobValue.setEnabled (true);
        }
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
}
