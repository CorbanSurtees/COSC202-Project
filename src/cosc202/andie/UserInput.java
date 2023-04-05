package cosc202.andie;

import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class UserInput extends ImageAction {

    private boolean slider = true;
    private int min, max, val, zeroVal;

    /**
     * <p>
     *      Asks user for input.
     * </p>
     * 
     * @param slider If this user input should have a slider.
     */
    UserInput(String name, ImageIcon icon, String desc, Integer mnemonic,
                boolean slider, int min, int max, int val, int zeroVal) {
        super(name, icon, desc, mnemonic);
        this.slider = slider;
        this.min = min;
        this.max = max;
        this.val = val;
        this.zeroVal = zeroVal;
    }

    /**
     * <p>
     * Callback for when the rotate-image action is triggered.
     * </p>
     * 
     * <p>
     * This method is called whenever the RotateImageAction is triggered.
     * It Rotates the image based on the degree and direction given by the
     * rotation datafield.
     * </p>
     * 
     * @param e The event triggering this callback.
     */
    public void actionPerformed(ActionEvent e) {
        try {

            // set default sizeIncrease to 100%
            int sizeIncrease = val;

            if (slider){

                // Determine the size percentage multiplier - ask the user.
                // Pop-up dialog box to ask for the sizeIncrease value.
                JSlider percentageSlider = new JSlider(JSlider.HORIZONTAL, min, max, val);

                SpinnerNumberModel percentageModel = new SpinnerNumberModel(val, min, max, 1);
                JSpinner percentageSpinner = new JSpinner(percentageModel);

                class JSliderListener implements ChangeListener{
                    public void stateChanged(ChangeEvent e){
                        try {
                            JSlider source = (JSlider)e.getSource();
                            percentageSpinner.setValue((int)source.getValue());
                            target.getImage().previewApply((ImageOperation)mutateImage((int)source.getValue()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        target.repaint();
                        target.getParent().revalidate();
                    }
                }

                class JSpinnerListener implements ChangeListener{
                    public void stateChanged(ChangeEvent e){
                        try {
                            JSpinner source = (JSpinner)e.getSource();
                            percentageSlider.setValue((int)source.getValue());
                            target.getImage().previewApply((ImageOperation)mutateImage((int)source.getValue()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        target.repaint();
                        target.getParent().revalidate();
                    }
                }

                // add listeners to slider and spinner
                percentageSlider.addChangeListener(new JSliderListener());
                percentageSpinner.addChangeListener(new JSpinnerListener());

                JPanel optionPanel = new JPanel();
                optionPanel.add(percentageSlider);
                optionPanel.add(percentageSpinner);

                int option = JOptionPane.showOptionDialog(null, optionPanel, "Enter Size(%)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                
                // Check the return value from the dialog box.
                if (option == JOptionPane.CANCEL_OPTION) {
                    target.getImage().previewApply((ImageOperation)mutateImage(zeroVal));
                    target.repaint();
                    target.getParent().revalidate();
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    sizeIncrease = (int)percentageSpinner.getValue();
                }
            }
            else {
                // Determine the size percentage multiplier - ask the user.
                // Pop-up dialog box to ask for the sizeIncrease value.

                class JSpinnerListener implements ChangeListener{
                    public void stateChanged(ChangeEvent e){
                        try {
                            JSpinner source = (JSpinner)e.getSource();
                            target.getImage().previewApply((ImageOperation)mutateImage((int)source.getValue()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        target.repaint();
                        target.getParent().revalidate();
                    }
                }

                SpinnerNumberModel percentageModel = new SpinnerNumberModel(val, min, max, 1);
                JSpinner percentageSpinner = new JSpinner(percentageModel);
                percentageSpinner.addChangeListener(new JSpinnerListener());
                int option = JOptionPane.showOptionDialog(null, percentageSpinner, "Enter Resize Percentage", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                // Check the return value from the dialog box.
                if (option == JOptionPane.CANCEL_OPTION) {
                    sizeIncrease = val;
                    target.getImage().previewApply((ImageOperation)mutateImage(zeroVal));
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    sizeIncrease = percentageModel.getNumber().intValue();
                }
            }

            target.getImage().apply((ImageOperation)mutateImage(sizeIncrease));
            target.repaint();
            target.getParent().revalidate();

        } catch (Exception ex) {
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

    abstract Object mutateImage(int input);

}

