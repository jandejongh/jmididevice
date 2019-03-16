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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.DefaultMidiDeviceListener;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.JColorCheckBox;

/** A {@link JPanel} for selecting the current patch on a Boss ME-80,
 *  switch between manual and banks,
 *  and showing/setting the patch name.
 * 
 * <p>
 * Subordinate to {@link JMe80Panel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMe80Panel_PatchSelector
  extends JPanel
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
  
  public JMe80Panel_PatchSelector (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    
    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
    
    final JPanel manualNamePanel = new JPanel ();
    //manualNamePanel.setLayout (new GridLayout (1, 2, 1, 1));
    manualNamePanel.setLayout (new BoxLayout (manualNamePanel, BoxLayout.X_AXIS));
    
    final JPanel manualPanel = new JPanel ();
    manualPanel.setLayout (new GridLayout (1, 1, 1, 1));
    this.jManual = new JColorCheckBox.JBoolean ((final Boolean t) -> {return t != null && t ? Color.green : null;});
    this.jManual.addMouseListener (new DefaultMouseListener ()
    {
      @Override
      public final void mouseClicked (final MouseEvent me)
      {
        super.mouseClicked (me);
        final Boolean displayedValue = JMe80Panel_PatchSelector.this.jManual.getDisplayedValue ();
        final boolean newValue = (displayedValue != null ? ! displayedValue : true);
        if (newValue)
          midiDevice.put (MidiDevice_Me80.CURRENT_PATCH_NO_NAME, 0x48);
        else
          // XXX We should really store the old patch? But what if there was none? How do we know for sure?
          midiDevice.put (MidiDevice_Me80.CURRENT_PATCH_NO_NAME, 0x00);
      }
      
    });
    manualPanel.add (this.jManual);
    final Border manualLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder manualBorder = BorderFactory.createTitledBorder (manualLineBorder, "Manual");
    manualPanel.setBorder (manualBorder);
    manualNamePanel.add (manualPanel);
    
    final JPanel namePanel = new JPanel ();
    namePanel.setLayout (new GridLayout (1, 1, 1, 1));
    final JTextField jName = new JTextField ("                ");
    jName.setOpaque (false);
    jName.setEnabled (false); // Read-Only for now. XXX
    namePanel.add (jName);
    final Border nameLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder nameBorder = BorderFactory.createTitledBorder (nameLineBorder, "Name");
    namePanel.setBorder (nameBorder);
    manualNamePanel.add (namePanel);
    
    add (manualNamePanel);
    
    final JPanel bankPanel = new JPanel ();
    bankPanel.setLayout (new GridLayout (2, 9, 1, 1));
    for (final ME80_BANK me80_bank : ME80_BANK.values ())
    {
      bankPanel.add (new JMe80Bank (me80_bank));
    }
    final Border bankLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder bankBorder = BorderFactory.createTitledBorder (bankLineBorder, "Bank");
    bankPanel.setBorder (bankBorder);
    add (bankPanel);
    final JPanel patchPanel = new JPanel ();
    patchPanel.setLayout (new GridLayout (1, 4, 1, 1));
    for (final ME80_PATCH_IN_BANK me80_patch : ME80_PATCH_IN_BANK.values ())
    {
      patchPanel.add (new JMe80Patch (me80_patch));
    }
    final Border patchLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final TitledBorder patchBorder = BorderFactory.createTitledBorder (patchLineBorder, "Patch [Number In Bank]");
    patchPanel.setBorder (patchBorder);
    add (patchPanel);

    midiDevice.addMidiDeviceListener (new DefaultMidiDeviceListener ()
    {
      @Override
      public void notifyParameterChanged (Map<String, Object> changes)
      {
        super.notifyParameterChanged (changes);
        if (changes.containsKey (MidiDevice_Me80.CURRENT_PATCH_NO_NAME))
        {
          final Integer newPatchNo = (Integer) changes.get (MidiDevice_Me80.CURRENT_PATCH_NO_NAME);
          JMe80Panel_PatchSelector.this.updateFromMe80 (newPatchNo);
        }
        if (changes.containsKey (MidiDevice_Me80.TP_NAME_NAME))
        {
          final String newValue = (String) changes.get (MidiDevice_Me80.TP_NAME_NAME);
          jName.setText (newValue);
        }
      }
    });
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  // UPDATE FROM/TO BOSS ME-80
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  private void updateFromMe80 (final Integer me80Patch)
  {
    final ME80_BANK bank = me80Patch != null ? bankFromMidiProgram (me80Patch) : null;
    final ME80_PATCH_IN_BANK patch = me80Patch != null ? patchInBankFromMidiProgram (me80Patch): null;
    synchronized (this)
    {
      this.selectedBank = bank;
      this.selectedPatch = patch;
    }
    updateManualPanel ();
    updateBankPanel ();
    updatePatchPanel ();
  }
  
  private void updateToMe80 ()
  {
    final ME80_BANK bank;
    final ME80_PATCH_IN_BANK patch;
    synchronized (this)
    {
      bank = this.selectedBank;
      patch = this.selectedPatch;
    }
    if (bank != null && patch != null)
      this.midiDevice.put (MidiDevice_Me80.CURRENT_PATCH_NO_NAME, (int) toMidiProgram (bank, patch));
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MANUAL
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JColorCheckBox.JBoolean jManual;
  
  private void updateManualPanel ()
  {
    if (! SwingUtilities.isEventDispatchThread ())
    {
      SwingUtilities.invokeLater (() -> JMe80Panel_PatchSelector.this.updateManualPanel ());
      return;
    }
    final ME80_BANK selectedBank = this.selectedBank;
    final ME80_PATCH_IN_BANK selectedPatch = this.selectedPatch;
    this.jManual.setDisplayedValue (selectedBank == null && selectedPatch == null);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK
  // PATCH IN BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public enum ME80_BANK
  {
    U1,
    U2,
    U3,
    U4,
    U5,
    U6,
    U7,
    U8,
    U9,
    P1,
    P2,
    P3,
    P4,
    P5,
    P6,
    P7,
    P8,
    P9;
  }

  public enum ME80_PATCH_IN_BANK
  {
    PIB_1 ("1"),
    PIB_2 ("2"),
    PIB_3 ("3"),
    PIB_4 ("4");

    private final String string;
    
    private ME80_PATCH_IN_BANK (final String string)
    {
      this.string = string;
    }

    @Override
    public String toString ()
    {
      return this.string;
    }
    
  }
  
  public static byte toMidiProgram (final ME80_BANK bank, final ME80_PATCH_IN_BANK patch)
  {
    return (byte) (4 * bank.ordinal () + patch.ordinal ());
  }
  
  public static ME80_BANK bankFromMidiProgram (final int midiProgram)
  {
    if (midiProgram < 0 || midiProgram > 71)
      return null;
    else
      return ME80_BANK.values ()[midiProgram / 4];    
  }
  
  public static ME80_PATCH_IN_BANK patchInBankFromMidiProgram (final int midiProgram)
  {
    if (midiProgram < 0 || midiProgram > 71)
      return null;
    else
      return ME80_PATCH_IN_BANK.values ()[midiProgram % 4];
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK MAP
  // PATCH MAP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final EnumMap<ME80_BANK, JColorCheckBox<Boolean>> bankMap = new EnumMap<> (ME80_BANK.class);
  
  private final EnumMap<ME80_PATCH_IN_BANK, JColorCheckBox<Boolean>> patchMap = new EnumMap<> (ME80_PATCH_IN_BANK.class);
  
  private void updateBankPanel ()
  {
    if (! SwingUtilities.isEventDispatchThread ())
    {
      SwingUtilities.invokeLater (() -> JMe80Panel_PatchSelector.this.updateBankPanel ());
      return;
    }
    final ME80_BANK selectedBank = this.selectedBank;
    for (final Entry<ME80_BANK, JColorCheckBox<Boolean>> entry : bankMap.entrySet ())
      entry.getValue ().setDisplayedValue (entry.getKey () == selectedBank);
  }
  
  private void updatePatchPanel ()
  {
    if (! SwingUtilities.isEventDispatchThread ())
    {
      SwingUtilities.invokeLater (() -> JMe80Panel_PatchSelector.this.updatePatchPanel ());
      return;
    }
    final ME80_PATCH_IN_BANK selectedPatch = this.selectedPatch;
    for (final Entry<ME80_PATCH_IN_BANK, JColorCheckBox<Boolean>> entry : patchMap.entrySet ())
      entry.getValue ().setDisplayedValue (entry.getKey () == selectedPatch);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SELECTED BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private volatile ME80_BANK selectedBank = null;
  
  public final ME80_BANK getSelectedBank ()
  {
    return this.selectedBank;
  }
  
  public final void setSelectedBank (final ME80_BANK selectedBank)
  {
    this.selectedBank = selectedBank;
    updateBankPanel ();
    updateToMe80 ();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK JCOMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static Map<Boolean, Color> COLOR_MAP = new HashMap<> ();
  {
    COLOR_MAP.put (Boolean.FALSE, null);
    COLOR_MAP.put (Boolean.TRUE, Color.RED);
  }
  
  protected class JMe80Bank
    extends JPanel
  {
    
    private final ME80_BANK me80_bank;
    
    protected JMe80Bank (final ME80_BANK me80_bank)
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

    private final ME80_BANK me80_bank;

    public JBankSelectorMouseListener (final ME80_BANK me80_bank)
    {
      if (me80_bank == null)
        throw new IllegalArgumentException ();
      this.me80_bank = me80_bank;
    }
      
    @Override
    public final void mouseClicked (MouseEvent e)
    {
      if (getSelectedBank () == this.me80_bank)
        setSelectedBank (null);
      else
        setSelectedBank (this.me80_bank);
    }

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SELECTED PATCH
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private volatile ME80_PATCH_IN_BANK selectedPatch = null;
  
  public final ME80_PATCH_IN_BANK getSelectedPatch ()
  {
    return this.selectedPatch;
  }
  
  public final void setSelectedPatch (final ME80_PATCH_IN_BANK selectedPatch)
  {
    this.selectedPatch = selectedPatch;
    updatePatchPanel ();
    updateToMe80 ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH JCOMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Patch
    extends JPanel
  {
    
    private final ME80_PATCH_IN_BANK me80_patch;
    
    protected JMe80Patch (final ME80_PATCH_IN_BANK me80_patch)
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
      private final ME80_PATCH_IN_BANK me80_patch;

    public JPatchSelectorMouseListener (final ME80_PATCH_IN_BANK me80_patch)
    {
      if (me80_patch == null)
        throw new IllegalArgumentException ();
      this.me80_patch = me80_patch;
    }
      
      @Override
      public final void mouseClicked (MouseEvent e)
      {
        if (getSelectedPatch () == this.me80_patch)
          setSelectedPatch (null);
        else
          setSelectedPatch (this.me80_patch);
      }

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
