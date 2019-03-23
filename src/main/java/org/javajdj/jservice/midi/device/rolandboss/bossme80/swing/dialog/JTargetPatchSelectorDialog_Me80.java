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
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing.dialog;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.swing.JMe80Panel_PatchSelector;

/** A {@link JDialog} requesting a target bank and patch on an ME-80, as well as a patch name.
 *
 * <p>
 * Typically required for saving a patch on the ME-80.
 * Only saving patches to User Banks (U1 through U9) is supported.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JTargetPatchSelectorDialog_Me80
  extends JDialog
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Creates the dialog.
   * 
   * @param frame              The frame in which the dialog resides, see {@link JDialog#JDialog(java.awt.Frame)},
   *                             may be {@code null}.
   * @param initialName        The initial name of the patch, may be {@code null}.
   * @param initialBank        The initial bank selected in the dialog (must be a user bank), may be {@code null}.
   *                           Ignored if the bank is <i>not</i> a user bank (i.e., one of P1 through P9).
   * @param initialPatchInBank The initial patch selected within the bank, may be {@code null} and
   *                             is ignored if {@code initialBank == null}.
   * 
   */
  public JTargetPatchSelectorDialog_Me80
  ( final Frame frame,
    final String initialName,
    final JMe80Panel_PatchSelector.ME80_BANK initialBank,
    final JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK initialPatchInBank)
  {
    
    super (frame, "Select Target Patch", true);
    
    getContentPane ().setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
    
    this.jName = new JTextField ();
    this.jName.setOpaque (false);
    if (initialName != null)
      this.jName.setText (initialName);
    final Border nameLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final Border nameBorder = BorderFactory.createCompoundBorder (
      BorderFactory.createEmptyBorder (20, 20, 20, 20),
      BorderFactory.createTitledBorder (nameLineBorder, "Patch Name"));
    this.jName.setBorder (nameBorder);
    getContentPane ().add (this.jName);
    
    final JPanel jBank = new JPanel ();
    jBank.setLayout (new GridLayout (1, 9, 10, 10));
    final ButtonGroup bankButtonGroup = new ButtonGroup ();
    final EnumSet<JMe80Panel_PatchSelector.ME80_BANK> userBanks
      = EnumSet.range (JMe80Panel_PatchSelector.ME80_BANK.U1, JMe80Panel_PatchSelector.ME80_BANK.U9);
    for (final JMe80Panel_PatchSelector.ME80_BANK me80_bank : userBanks)
    {
      final JRadioButton jRadioButton = new JRadioButton (me80_bank.toString ());
      if (me80_bank == initialBank)
      {
        jRadioButton.setSelected (true);
        JTargetPatchSelectorDialog_Me80.this.me80_bank = me80_bank;
      }
      jRadioButton.addActionListener ((final ActionEvent ae) ->
      {
        JTargetPatchSelectorDialog_Me80.this.me80_bank = me80_bank;
      });
      jBank.add (jRadioButton);
      bankButtonGroup.add (jRadioButton);
    }
    final Border bankLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final Border bankBorder = BorderFactory.createCompoundBorder (
      BorderFactory.createEmptyBorder (20, 20, 20, 20),
      BorderFactory.createTitledBorder (bankLineBorder, "Bank"));
    jBank.setBorder (bankBorder);
    getContentPane ().add (jBank);
    
    final JPanel jPatch = new JPanel ();
    jPatch.setLayout (new GridLayout (1, 4, 10, 10));
    final ButtonGroup patchButtonGroup = new ButtonGroup ();
    final EnumSet<JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK> userPatches
      = EnumSet.range (JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK.PIB_1, JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK.PIB_4);
    for (final JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK me80_patch_in_bank : userPatches)
    {
      final JRadioButton jRadioButton = new JRadioButton (me80_patch_in_bank.toString ());
      if (JTargetPatchSelectorDialog_Me80.this.me80_bank != null && me80_patch_in_bank == initialPatchInBank)
      {
        jRadioButton.setSelected (true);
        JTargetPatchSelectorDialog_Me80.this.me80_patch_in_bank = me80_patch_in_bank;
      }
      jRadioButton.addActionListener ((final ActionEvent ae) ->
      {
        JTargetPatchSelectorDialog_Me80.this.me80_patch_in_bank = me80_patch_in_bank;
      });
      jPatch.add (jRadioButton);
      patchButtonGroup.add (jRadioButton);
    }
    final Border patchLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final Border patchBorder = BorderFactory.createCompoundBorder (
      BorderFactory.createEmptyBorder (20, 20, 20, 20),
      BorderFactory.createTitledBorder (patchLineBorder, "Patch in Bank"));
    jPatch.setBorder (patchBorder);
    getContentPane ().add (jPatch);
    
    final JPanel okCancelPanel = new JPanel ();
    okCancelPanel.setLayout (new GridLayout (1, 5, 10, 10));
    okCancelPanel.add (new JLabel ());
    final JButton okButton = new JButton ("OK");
    okButton.addActionListener ((final ActionEvent ae) ->
    {
      if (JTargetPatchSelectorDialog_Me80.this.me80_bank == null)
        JOptionPane.showMessageDialog (null,
          "No bank selected!",
          "Problem",
          JOptionPane.ERROR_MESSAGE);
      else if (JTargetPatchSelectorDialog_Me80.this.me80_patch_in_bank == null)
        JOptionPane.showMessageDialog (null,
          "No patch [in bank] selected!",
          "Problem",
          JOptionPane.ERROR_MESSAGE);
      else
      {
        JTargetPatchSelectorDialog_Me80.this.ok = true;
        JTargetPatchSelectorDialog_Me80.this.dispose ();
      }
    });
    okCancelPanel.add (okButton);
    okCancelPanel.add (new JLabel ());
    final JButton cancelButton = new JButton ("Cancel");
    cancelButton.addActionListener ((final ActionEvent ae) ->
    {
      JTargetPatchSelectorDialog_Me80.this.ok = false;
      JTargetPatchSelectorDialog_Me80.this.dispose ();
    });
    okCancelPanel.add (cancelButton);
    okCancelPanel.add (new JLabel ());
    final Border okCancelBorder = BorderFactory.createEmptyBorder (20, 20, 20, 20);
    okCancelPanel.setBorder (okCancelBorder);
    getContentPane ().add (okCancelPanel);
    
    pack ();
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // OK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private boolean ok = false;
  
  /** Returns true if the dialog was closed and disposed of through the OK button, and
   *  parameters entered were valid.
   * 
   * @return True if the user dismissed the dialog with the "OK" button with valid parameters entered.
   * 
   */
  public final boolean isOk ()
  {
    return this.ok;
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JTextField jName;
  
  /** Returns the patch name.
   * 
   * <p>
   * Null is returned if the dialog was somehow canceled.
   * 
   * @return The patch name entered, {@code null} if the dialog was canceled.
   * 
   * @see #isOk
   * 
   */
  public final String getPatchName ()
  {
    if (this.ok)
      return this.jName.getText ();
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [SELECTED] BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private JMe80Panel_PatchSelector.ME80_BANK me80_bank;
  
  /** Returns the selected bank.
   * 
   * <p>
   * Null is returned if the dialog was somehow canceled.
   * 
   * @return The selected bank, {@code null} if the dialog was canceled.
   * 
   * @see #isOk
   * @see #getPatchInBank
   * 
   */
  public final JMe80Panel_PatchSelector.ME80_BANK getBank ()
  {
    if (this.ok)
      return this.me80_bank;
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [SELECTED] PATCH IN BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK me80_patch_in_bank;
  
  /** Returns the selected patch in the (selected) bank.
   * 
   * <p>
   * Null is returned if the dialog was somehow canceled.
   * 
   * @return The selected patch in the (selected) bank, {@code null} if the dialog was canceled.
   * 
   * @see #isOk
   * @see #getBank
   * 
   */
  public final JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK getPatchInBank ()
  {
    if (this.ok)
      return this.me80_patch_in_bank;
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
