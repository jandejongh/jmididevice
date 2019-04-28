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

import org.javajdj.swing.DefaultMouseListener;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.PatchSlot_Me80;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceMultiParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_String;
import org.javajdj.swing.JColorCheckBox;
import org.javajdj.swing.SwingUtilsJdJ;

/**A {@link JPanel} for selecting the current patch on a Boss ME-80,
 *   switch between manual and banks,
 *   and showing/setting the patch name.
 * 
 * <p>
 * Subordinate to {@link JMe80Panel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMe80Panel_PatchSelector
  extends JMidiDeviceMultiParameter
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JMe80Panel_PatchSelector.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static Map<String, Set<JComponent>> createComponentMap (final MidiDevice midiDevice)
  {
    if (midiDevice == null || ! midiDevice.containsKey (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME))
      throw new IllegalArgumentException ();
    final Map<String, Set<JComponent>> componentMap = new LinkedHashMap<> ();
    componentMap.put (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, new LinkedHashSet<> ());
    final JPanel manualPanel = new JPanel ();
    componentMap.get (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME).add (manualPanel);
    final JPanel bankPanel = new JPanel ();
    componentMap.get (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME).add (bankPanel);
    final JPanel patchPanel = new JPanel ();
    componentMap.get (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME).add (patchPanel);
    componentMap.put (MidiDevice_Me80.TP_NAME_NAME, new LinkedHashSet<> ());
    final JComponent jName = new JMidiDeviceParameter_String (midiDevice, null, MidiDevice_Me80.TP_NAME_NAME, 16);
    componentMap.get (MidiDevice_Me80.TP_NAME_NAME).add (jName);
    return componentMap;
    
  }
  
  public JMe80Panel_PatchSelector (final MidiDevice midiDevice)
  {
    
    super (midiDevice, createComponentMap (midiDevice));
    
    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
    
    final JPanel manualNamePanel = new JPanel ();
    manualNamePanel.setLayout (new GridLayout (1, 2, 1, 1));
    // manualNamePanel.setLayout (new BoxLayout (manualNamePanel, BoxLayout.X_AXIS));
    this.jManual = new JColorCheckBox.JBoolean (t -> (t != null && t ? Color.green : null));
    this.jManual.addMouseListener (this.jManualMouseListener);
    final Border manualLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder manualBorder = BorderFactory.createTitledBorder (manualLineBorder, "Manual");
    final JPanel manualPanel = (JPanel) getJComponents (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME).toArray ()[0];
    manualPanel.setLayout (new GridLayout (1, 1, 5, 5));
    manualPanel.add (this.jManual);
    manualPanel.setBorder (manualBorder);
    manualNamePanel.add (manualPanel);
    final JComponent jName = (JComponent) getJComponents (MidiDevice_Me80.TP_NAME_NAME).toArray ()[0];
    final Border nameLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder nameBorder = BorderFactory.createTitledBorder (nameLineBorder, "Name");
    jName.setBorder (nameBorder);
    manualNamePanel.add (jName);
    add (manualNamePanel);
    
    this.jBank = (JPanel) getJComponents (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME).toArray ()[1];
    this.jBank.setLayout (new GridLayout (2, 9, 1, 1));
    for (final PatchSlot_Me80.ME80_BANK me80_bank : PatchSlot_Me80.ME80_BANK.values ())
    {
      this.jBank.add (new JMe80Bank (me80_bank));
    }
    final Border bankLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder bankBorder = BorderFactory.createTitledBorder (bankLineBorder, "Bank");
    this.jBank.setBorder (bankBorder);
    add (this.jBank);
    
    this.jPatch = (JPanel) getJComponents (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME).toArray ()[2];
    this.jPatch.setLayout (new GridLayout (1, 4, 1, 1));
    for (final PatchSlot_Me80.ME80_PATCH_IN_BANK me80_patch : PatchSlot_Me80.ME80_PATCH_IN_BANK.values ())
    {
      this.jPatch.add (new JMe80Patch (me80_patch));
    }
    final Border patchLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder patchBorder = BorderFactory.createTitledBorder (patchLineBorder, "Patch [Number In Bank]");
    this.jPatch.setBorder (patchBorder);
    add (this.jPatch);
    
    dataValueChanged (Collections.singletonMap
      (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, getDataValue (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME)));
    dataValueChanged (Collections.singletonMap
      (MidiDevice_Me80.TP_NAME_NAME, getDataValue (MidiDevice_Me80.TP_NAME_NAME)));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE COMPONENTS
  // VALUE COMPONENT LISTENERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JColorCheckBox.JBoolean jManual;
  
  private final MouseListener jManualMouseListener = new DefaultMouseListener ()
  {
    @Override
    public final void mouseClicked (final MouseEvent me)
    {
      super.mouseClicked (me);
      final Boolean displayedValue = JMe80Panel_PatchSelector.this.jManual.getDisplayedValue ();
      final boolean newValue = (displayedValue != null ? (! displayedValue) : true);
      if (newValue)
        JMe80Panel_PatchSelector.this.setDataValue (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, PatchSlot_Me80.MANUAL_SLOT);
      else
        // XXX We should really store the old patch? But what if there was none? How do we know for sure?
        JMe80Panel_PatchSelector.this.setDataValue
          (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME,
           new PatchSlot_Me80 (PatchSlot_Me80.ME80_BANK.U1, PatchSlot_Me80.ME80_PATCH_IN_BANK.PIB_1));
    }    
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER [JMidiDeviceParameter]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  @Override
  protected final void dataValueChanged (final Map<String, Object> changes)
  {
    super.dataValueChanged (changes);
    if (changes == null || changes.isEmpty ())
      throw new RuntimeException ();
    if (! changes.containsKey (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME))
      return;
    final PatchSlot_Me80 newDataValue = (PatchSlot_Me80) changes.get (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME);
    SwingUtilsJdJ.invokeOnSwingEDT (() ->
    {
      if (newDataValue == null)
        this.jManual.setDisplayedValue (null);
      else
      {
        this.jManual.setDisplayedValue (newDataValue.isManualPatchSlot ());
        final PatchSlot_Me80.ME80_BANK bank = newDataValue.getBank ();
        final PatchSlot_Me80.ME80_PATCH_IN_BANK patch = newDataValue.getPatchInBank ();
        for (final Entry<PatchSlot_Me80.ME80_BANK, JColorCheckBox<Boolean>> entry : this.bankMap.entrySet ())
          entry.getValue ().setDisplayedValue (entry.getKey () == bank);
        for (final Entry<PatchSlot_Me80.ME80_PATCH_IN_BANK, JColorCheckBox<Boolean>> entry : this.patchMap.entrySet ())
          entry.getValue ().setDisplayedValue (entry.getKey () == patch);
      }
    });
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK
  // PATCH IN BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JPanel jBank;
  
  private final JPanel jPatch;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK MAP
  // PATCH MAP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final EnumMap<PatchSlot_Me80.ME80_BANK, JColorCheckBox<Boolean>> bankMap
    = new EnumMap<> (PatchSlot_Me80.ME80_BANK.class);
  
  private final EnumMap<PatchSlot_Me80.ME80_PATCH_IN_BANK, JColorCheckBox<Boolean>> patchMap
    = new EnumMap<> (PatchSlot_Me80.ME80_PATCH_IN_BANK.class);
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK JCOMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static Map<Boolean, Color> COLOR_MAP = new HashMap<> ();
  
  static
  {
    COLOR_MAP.put (Boolean.FALSE, null);
    COLOR_MAP.put (Boolean.TRUE, Color.RED);
  }
  
  protected class JMe80Bank
    extends JPanel
  {
    
    private final PatchSlot_Me80.ME80_BANK me80_bank;
    
    protected JMe80Bank (final PatchSlot_Me80.ME80_BANK me80_bank)
    {
      if (me80_bank == null)
        throw new IllegalArgumentException ();
      this.me80_bank = me80_bank;
      setLayout (new GridLayout (2, 1, 1, 1));
      final JColorCheckBox<Boolean> jColorCheckBox = new JColorCheckBox.JBoolean (COLOR_MAP);
      JMe80Panel_PatchSelector.this.bankMap.put (me80_bank, jColorCheckBox);
      add (jColorCheckBox);
      jColorCheckBox.addMouseListener (new JBankSelectorMouseListener (me80_bank));
      add (new JLabel (this.me80_bank.toString ()));
    }
    
  }
  
  private class JBankSelectorMouseListener
    extends DefaultMouseListener
  {

    private final PatchSlot_Me80.ME80_BANK me80_bank;

    public JBankSelectorMouseListener (final PatchSlot_Me80.ME80_BANK me80_bank)
    {
      if (me80_bank == null)
        throw new IllegalArgumentException ();
      this.me80_bank = me80_bank;
    }
      
    @Override
    public final void mouseClicked (MouseEvent e)
    {
      if (JMe80Panel_PatchSelector.this.jBank.isEnabled ())
      {
        final PatchSlot_Me80 patchSlot =
          (PatchSlot_Me80) JMe80Panel_PatchSelector.this.getDataValue (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME);
        if (patchSlot.getBank () == this.me80_bank)
          JMe80Panel_PatchSelector.this.setDataValue
            (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, PatchSlot_Me80.MANUAL_SLOT);
        else
          JMe80Panel_PatchSelector.this.setDataValue
            (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, patchSlot.withBank (this.me80_bank));
      }
    }

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH JCOMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Patch
    extends JPanel
  {
    
    private final PatchSlot_Me80.ME80_PATCH_IN_BANK me80_patch;
    
    protected JMe80Patch (final PatchSlot_Me80.ME80_PATCH_IN_BANK me80_patch)
    {
      if (me80_patch == null)
        throw new IllegalArgumentException ();
      this.me80_patch = me80_patch;
      setLayout (new GridLayout (2, 1, 0, 0));
      final JColorCheckBox<Boolean> jColorCheckBox = new JColorCheckBox.JBoolean (COLOR_MAP);
      JMe80Panel_PatchSelector.this.patchMap.put (me80_patch, jColorCheckBox);
      add (jColorCheckBox);
      jColorCheckBox.addMouseListener (new JPatchSelectorMouseListener (me80_patch));
      add (new JLabel (this.me80_patch.toString ()));
    }
    
  }
  
  private class JPatchSelectorMouseListener
    extends DefaultMouseListener
  {
      private final PatchSlot_Me80.ME80_PATCH_IN_BANK me80_patch;

    public JPatchSelectorMouseListener (final PatchSlot_Me80.ME80_PATCH_IN_BANK me80_patch)
    {
      if (me80_patch == null)
        throw new IllegalArgumentException ();
      this.me80_patch = me80_patch;
    }
      
      @Override
      public final void mouseClicked (MouseEvent e)
      {
        if (JMe80Panel_PatchSelector.this.jPatch.isEnabled ())
        {
        final PatchSlot_Me80 patchSlot =
          (PatchSlot_Me80) JMe80Panel_PatchSelector.this.getDataValue (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME);
        if (patchSlot.getPatchInBank () == this.me80_patch)
          JMe80Panel_PatchSelector.this.setDataValue
            (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, PatchSlot_Me80.MANUAL_SLOT);
        else
          JMe80Panel_PatchSelector.this.setDataValue
            (MidiDevice_Me80.CURRENT_PATCH_SLOT_NAME, patchSlot.withPatchInBank (this.me80_patch));
        }
      }

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
