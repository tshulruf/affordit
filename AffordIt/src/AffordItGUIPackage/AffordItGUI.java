/*
 * AffordItGUI.java
 *
 * Created on September 1, 2008, 7:07 PM
 */
package AffordItGUIPackage;

import AffordItCalculators.HomeCalculator.RangeException;
import java.awt.Color;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author  Tom
 */
public class AffordItGUI extends javax.swing.JFrame {

    private AffordItCalculators.HomeCalculator _HomeCalc;
    private JFormattedTextField _BadEntry;

    private void ClearBadEntry(java.awt.AWTEvent evt) {
        if ((evt.getSource() instanceof JFormattedTextField) &&
                (null != _BadEntry)) {
            if (evt.getSource().equals(_BadEntry)) {
                _BadEntry = null;
            }
        }
    }

    private void HandleInvalidField(JFormattedTextField TheField,
            String ErrorMessage) {
        // add error message to secondary label
        jLblDetails.setText(ErrorMessage);
        jLblMaxPrice.setText("");
        TheField.setBackground(Color.pink);
        _BadEntry = TheField;
        TheField.grabFocus();
    }

    // <editor-fold defaultstate="collapsed" desc="Updater Function Objects">
    //
    // Inputs into the algorithm.
    //
    // Base class for a template method pattern.
    private interface IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException;
    }
    
    
    // Update the APR by percentage.
    private class TheAPRPercent implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setAPRPercent(NewValue);
        }
    }
    private TheAPRPercent _TheAPRPercent;
    
    
    // Update the Debt Ratio by percentage.
    private class TheDebtRatioPercent implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setDebtRatioPercent(NewValue);
        }
    }
    private TheDebtRatioPercent _TheDebtRatioPercent;
    
    
    // Update the Down Payment.
    private class TheDownPayment implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setDownPayment(NewValue);
        }
    }
    private TheDownPayment _TheDownPayment;
    
    
    // Update the Monthly HOA.
    private class TheMonthlyHOA implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setMonthlyHOADues(NewValue);
        }
    }
    private TheMonthlyHOA _TheMonthlyHOA;
    
    
    // Update the Income.
    private class TheIncome implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setIncome(NewValue);
        }
    }
    private TheIncome _TheIncome;
    
    
    // Update the Insurance.
    private class TheInsurance implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setInsurance(NewValue);
        }
    }
    private TheInsurance _TheInsurance;
    
    
    // Update the Monthly Payments.
    private class TheMonthlyPayments implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setMonthlyAdditionalDebt(NewValue);
        }
    }
    private TheMonthlyPayments _TheMonthlyPayments;
    
    
    // Update the Tax by percent.
    private class TheTaxRatePercent implements IUpdateHomeCalculator {

        public void Update(AffordItCalculators.HomeCalculator Calculator,
                double NewValue) throws RangeException {
            Calculator.setTaxRatePercent(NewValue);
        }
    }
    private TheTaxRatePercent _TheTaxRatePercent;
    // </editor-fold>
    
    
    /** Creates new form AffordItGUI */
    public AffordItGUI() {
        initComponents();

        // Construct the actual calculator.
        _HomeCalc = new AffordItCalculators.HomeCalculator();

        // Function objects for updating the calculator inputs.
        _TheAPRPercent = new TheAPRPercent();
        _TheDebtRatioPercent = new TheDebtRatioPercent();
        _TheDownPayment = new TheDownPayment();
        _TheMonthlyHOA = new TheMonthlyHOA();
        _TheIncome = new TheIncome();
        _TheInsurance = new TheInsurance();
        _TheMonthlyPayments = new TheMonthlyPayments();
        _TheTaxRatePercent = new TheTaxRatePercent();
        
        _BadEntry = null;


        // Add formatters to the FormattedInputFields.
        AbstractFormatterFactory ThreeDigitNumber = 
                new AbstractFormatterFactory()
        {
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField arg0) {
                return new NumberFormatter(new java.text.DecimalFormat("#.000"));
            }
        };
        jFTFAPR.setFormatterFactory(ThreeDigitNumber);

        // Add formatters to the FormattedInputFields.
        AbstractFormatterFactory TwoDigitNumber = new AbstractFormatterFactory()
        {
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField arg0) {
                return new NumberFormatter(new java.text.DecimalFormat("#.00"));
            }
        };
        jFTFDebtRatio.setFormatterFactory(TwoDigitNumber);
        jFTFDown.setFormatterFactory(TwoDigitNumber);
        jFTFHOA.setFormatterFactory(TwoDigitNumber);
        jFTFIncome.setFormatterFactory(TwoDigitNumber);
        jFTFInsurance.setFormatterFactory(TwoDigitNumber);
        jFTFPayments.setFormatterFactory(TwoDigitNumber);
        jFTFTaxes.setFormatterFactory(TwoDigitNumber);

        // Get the defaults from HomeCalc.
        jFTFAPR.setValue(_HomeCalc.getAPRPercent());
        jFTFDebtRatio.setValue(_HomeCalc.getDebtRatioPercent());
        jFTFDown.setValue(_HomeCalc.getDownPayment());
        jFTFHOA.setValue(_HomeCalc.getMonthlyHOADues());
        jFTFIncome.setValue(_HomeCalc.getIncome());
        jFTFInsurance.setValue(_HomeCalc.getInsurance());
        jFTFPayments.setValue(_HomeCalc.getMonthlyAdditionalDebt());
        jFTFTaxes.setValue(_HomeCalc.getTaxRatePercent());
        UpdateLabels();
    }

    
    private synchronized void UpdateField(JFormattedTextField TheField,
            IUpdateHomeCalculator CalculatorInput)
    {
        // Debt ratio
        try {
            if (null == _BadEntry)
            {
                TheField.commitEdit();
                Number temp = (Number) TheField.getValue();
                CalculatorInput.Update(_HomeCalc, temp.doubleValue());
                _HomeCalc.Update();
                UpdateLabels();
                TheField.setBackground(Color.white);
            }
        } catch (java.text.ParseException e) {
            HandleInvalidField(TheField, "Error: Invalid entry.");
        } catch (RangeException e) {
            HandleInvalidField(TheField, e.getMessage());
        }
    }
    

    private void UpdateLabels()
    {
        NumberFormat MoxieFormatter =
                NumberFormat.getNumberInstance();
        MoxieFormatter.setMaximumFractionDigits(2);

        jLblMaxPrice.setText(
                NumberFormat.getCurrencyInstance().format(
                _HomeCalc.getMaxHomePrice()));
        jLblDetails.setText("Your Moxie is " +
                MoxieFormatter.format(_HomeCalc.getMoxie()) +
                " years.");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLblMaxPrice = new javax.swing.JLabel();
        jLblDetails = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jAPRLabel = new javax.swing.JLabel();
        jAPRPre = new javax.swing.JLabel();
        jFTFAPR = new javax.swing.JFormattedTextField();
        jAPRPost = new javax.swing.JLabel();
        jHOALabel = new javax.swing.JLabel();
        jHOAPre = new javax.swing.JLabel();
        jFTFHOA = new javax.swing.JFormattedTextField();
        jHOAPost = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jIncomeLabel = new javax.swing.JLabel();
        jIncomePre = new javax.swing.JLabel();
        jFTFIncome = new javax.swing.JFormattedTextField();
        jIncomePost = new javax.swing.JLabel();
        jPaymentsLabel1 = new javax.swing.JLabel();
        jPaymentsPre = new javax.swing.JLabel();
        jFTFPayments = new javax.swing.JFormattedTextField();
        jPaymentsPost = new javax.swing.JLabel();
        jDebtRatioLabel = new javax.swing.JLabel();
        jDebtRatioPre = new javax.swing.JLabel();
        jFTFDebtRatio = new javax.swing.JFormattedTextField();
        jDebtRatioPost = new javax.swing.JLabel();
        jDownLabel = new javax.swing.JLabel();
        jDownPre = new javax.swing.JLabel();
        jFTFDown = new javax.swing.JFormattedTextField();
        jDownPost = new javax.swing.JLabel();
        jInsuranceLabel = new javax.swing.JLabel();
        jInsurancePre = new javax.swing.JLabel();
        jFTFInsurance = new javax.swing.JFormattedTextField();
        jInsurancePost = new javax.swing.JLabel();
        jTaxesLabel = new javax.swing.JLabel();
        jTaxesPre = new javax.swing.JLabel();
        jFTFTaxes = new javax.swing.JFormattedTextField();
        jTaxesPost = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Afford That Home");
        setResizable(false);

        jLblMaxPrice.setFont(new java.awt.Font("Tahoma", 1, 24));
        jLblMaxPrice.setText("Spam");
        jLblMaxPrice.setToolTipText("Maximum price of affordable home.");

        jLblDetails.setText("Spam");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));

        jAPRLabel.setText("APR");
        jAPRLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jFTFAPR.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFAPR.setToolTipText("Annual Percentage Rate");
        jFTFAPR.setAutoscrolls(false);
        jFTFAPR.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFAPR.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFAPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFAPRActionPerformed(evt);
            }
        });
        jFTFAPR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFAPRFocusLost(evt);
            }
        });

        jAPRPost.setText("%");
        jAPRPost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jHOALabel.setText("HOA");
        jHOALabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jHOAPre.setText("$");
        jHOAPre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jFTFHOA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFHOA.setToolTipText("Monthly Home Owners Association dues");
        jFTFHOA.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFHOA.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFHOA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFHOAActionPerformed(evt);
            }
        });
        jFTFHOA.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFHOAFocusLost(evt);
            }
        });

        jHOAPost.setText("/mo.");
        jHOAPost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jAPRLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jAPRPre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFAPR, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jAPRPost))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jHOALabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jHOAPre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFHOA, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jHOAPost)))
                .addGap(143, 143, 143))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jAPRLabel, jHOALabel});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jAPRPre, jHOAPre});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jAPRPost, jHOAPost});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAPRLabel)
                    .addComponent(jAPRPre)
                    .addComponent(jAPRPost)
                    .addComponent(jFTFAPR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jHOAPre)
                    .addComponent(jHOALabel)
                    .addComponent(jHOAPost)
                    .addComponent(jFTFHOA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Constants"));

        jIncomeLabel.setText("Income");
        jIncomeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jIncomePre.setText("$");
        jIncomePre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jFTFIncome.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFIncome.setToolTipText("How much you earn a year.");
        jFTFIncome.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFIncome.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFIncomeActionPerformed(evt);
            }
        });
        jFTFIncome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFIncomeFocusLost(evt);
            }
        });

        jIncomePost.setText("/yr.");
        jIncomePost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jPaymentsLabel1.setText("Payments");
        jPaymentsLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jPaymentsPre.setText("$");
        jPaymentsPre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jFTFPayments.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFPayments.setToolTipText("Monthly payments on other debt.");
        jFTFPayments.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFPayments.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFPayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFPaymentsActionPerformed(evt);
            }
        });
        jFTFPayments.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFPaymentsFocusLost(evt);
            }
        });

        jPaymentsPost.setText("/mo.");
        jPaymentsPost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jDebtRatioLabel.setText("Debt Ratio");
        jDebtRatioLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jFTFDebtRatio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFDebtRatio.setToolTipText("Fraction of gross income (above) that can be put towards debts.");
        jFTFDebtRatio.setAutoscrolls(false);
        jFTFDebtRatio.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFDebtRatio.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFDebtRatio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFDebtRatioActionPerformed(evt);
            }
        });
        jFTFDebtRatio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFDebtRatioFocusLost(evt);
            }
        });

        jDebtRatioPost.setText("%");
        jDebtRatioPost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jDownLabel.setText("Down");
        jDownLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jDownPre.setText("$");
        jDownPre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jFTFDown.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFDown.setToolTipText("Down payment for home.");
        jFTFDown.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFDown.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFDownActionPerformed(evt);
            }
        });
        jFTFDown.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFDownFocusLost(evt);
            }
        });

        jDownPost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jInsuranceLabel.setText("Insurance");
        jInsuranceLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jInsurancePre.setText("$");
        jInsurancePre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jFTFInsurance.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFInsurance.setToolTipText("Yearly home owner's insurance premium (fire/earthquake/etc).");
        jFTFInsurance.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFInsurance.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFInsurance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFInsuranceActionPerformed(evt);
            }
        });
        jFTFInsurance.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFInsuranceFocusLost(evt);
            }
        });

        jInsurancePost.setText("/yr.");
        jInsurancePost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jTaxesLabel.setText("Taxes");
        jTaxesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jFTFTaxes.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFTFTaxes.setToolTipText("Property tax rate.");
        jFTFTaxes.setAutoscrolls(false);
        jFTFTaxes.setMaximumSize(new java.awt.Dimension(109, 20));
        jFTFTaxes.setMinimumSize(new java.awt.Dimension(109, 20));
        jFTFTaxes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFTFTaxesActionPerformed(evt);
            }
        });
        jFTFTaxes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFTFTaxesFocusLost(evt);
            }
        });

        jTaxesPost.setText("%");
        jTaxesPost.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jIncomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jIncomePre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFIncome, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTaxesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTaxesPre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFTaxes, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jInsuranceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jInsurancePre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFInsurance, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPaymentsLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPaymentsPre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFPayments, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jDebtRatioLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDebtRatioPre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFDebtRatio, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jDownLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDownPre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFTFDown, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jInsurancePost)
                    .addComponent(jTaxesPost)
                    .addComponent(jDownPost)
                    .addComponent(jDebtRatioPost)
                    .addComponent(jPaymentsPost)
                    .addComponent(jIncomePost))
                .addGap(105, 105, 105))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jDebtRatioLabel, jDownLabel, jIncomeLabel, jInsuranceLabel, jPaymentsLabel1, jTaxesLabel});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jDebtRatioPre, jDownPre, jIncomePre, jInsurancePre, jPaymentsPre, jTaxesPre});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jIncomePre)
                    .addComponent(jIncomeLabel)
                    .addComponent(jFTFIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jIncomePost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPaymentsPre)
                    .addComponent(jPaymentsLabel1)
                    .addComponent(jFTFPayments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPaymentsPost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDebtRatioLabel)
                    .addComponent(jDebtRatioPre)
                    .addComponent(jFTFDebtRatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDebtRatioPost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDownPre)
                    .addComponent(jDownLabel)
                    .addComponent(jFTFDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDownPost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jInsurancePre)
                    .addComponent(jInsuranceLabel)
                    .addComponent(jFTFInsurance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jInsurancePost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTaxesLabel)
                        .addComponent(jTaxesPre))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFTFTaxes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTaxesPost)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDebtRatioPre, jDownPre, jIncomePre, jInsurancePre, jPaymentsPre, jTaxesPre});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(11, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(10, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLblDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLblMaxPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLblMaxPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLblDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
// 
// Action Performed.
//
private void jFTFAPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFAPRActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFAPR, _TheAPRPercent);
}//GEN-LAST:event_jFTFAPRActionPerformed

private void jFTFHOAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFHOAActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFHOA, _TheMonthlyHOA);
}//GEN-LAST:event_jFTFHOAActionPerformed

private void jFTFIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFIncomeActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFIncome, _TheIncome);
}//GEN-LAST:event_jFTFIncomeActionPerformed

private void jFTFPaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFPaymentsActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFPayments, _TheMonthlyPayments);
}//GEN-LAST:event_jFTFPaymentsActionPerformed

private void jFTFDebtRatioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFDebtRatioActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFDebtRatio, _TheDebtRatioPercent);
}//GEN-LAST:event_jFTFDebtRatioActionPerformed

private void jFTFDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFDownActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFDown, _TheDownPayment);
}//GEN-LAST:event_jFTFDownActionPerformed

private void jFTFInsuranceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFInsuranceActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFInsurance, _TheInsurance);
}//GEN-LAST:event_jFTFInsuranceActionPerformed

private void jFTFTaxesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFTFTaxesActionPerformed
    ClearBadEntry(evt);
    UpdateField(jFTFTaxes, _TheTaxRatePercent);
}//GEN-LAST:event_jFTFTaxesActionPerformed

//
// Focus Lost
//
private void jFTFDebtRatioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFDebtRatioFocusLost
    UpdateField(jFTFDebtRatio, _TheDebtRatioPercent);
}//GEN-LAST:event_jFTFDebtRatioFocusLost

private void jFTFIncomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFIncomeFocusLost
    UpdateField(jFTFIncome, _TheIncome);
}//GEN-LAST:event_jFTFIncomeFocusLost

private void jFTFInsuranceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFInsuranceFocusLost
    UpdateField(jFTFInsurance, _TheInsurance);
}//GEN-LAST:event_jFTFInsuranceFocusLost

private void jFTFPaymentsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFPaymentsFocusLost
    UpdateField(jFTFPayments, _TheMonthlyPayments);
}//GEN-LAST:event_jFTFPaymentsFocusLost

private void jFTFDownFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFDownFocusLost
    UpdateField(jFTFDown, _TheDownPayment);
}//GEN-LAST:event_jFTFDownFocusLost

private void jFTFAPRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFAPRFocusLost
    UpdateField(jFTFAPR, _TheAPRPercent);
}//GEN-LAST:event_jFTFAPRFocusLost

private void jFTFHOAFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFHOAFocusLost
    UpdateField(jFTFHOA, _TheMonthlyHOA);
}//GEN-LAST:event_jFTFHOAFocusLost

private void jFTFTaxesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFTaxesFocusLost
    UpdateField(jFTFTaxes, _TheTaxRatePercent);
}//GEN-LAST:event_jFTFTaxesFocusLost

private void jFTFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFTFFocusGained
    ClearBadEntry(evt);
}//GEN-LAST:event_jFTFFocusGained

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AffordItGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jAPRLabel;
    private javax.swing.JLabel jAPRPost;
    private javax.swing.JLabel jAPRPre;
    private javax.swing.JLabel jDebtRatioLabel;
    private javax.swing.JLabel jDebtRatioPost;
    private javax.swing.JLabel jDebtRatioPre;
    private javax.swing.JLabel jDownLabel;
    private javax.swing.JLabel jDownPost;
    private javax.swing.JLabel jDownPre;
    private javax.swing.JFormattedTextField jFTFAPR;
    private javax.swing.JFormattedTextField jFTFDebtRatio;
    private javax.swing.JFormattedTextField jFTFDown;
    private javax.swing.JFormattedTextField jFTFHOA;
    private javax.swing.JFormattedTextField jFTFIncome;
    private javax.swing.JFormattedTextField jFTFInsurance;
    private javax.swing.JFormattedTextField jFTFPayments;
    private javax.swing.JFormattedTextField jFTFTaxes;
    private javax.swing.JLabel jHOALabel;
    private javax.swing.JLabel jHOAPost;
    private javax.swing.JLabel jHOAPre;
    private javax.swing.JLabel jIncomeLabel;
    private javax.swing.JLabel jIncomePost;
    private javax.swing.JLabel jIncomePre;
    private javax.swing.JLabel jInsuranceLabel;
    private javax.swing.JLabel jInsurancePost;
    private javax.swing.JLabel jInsurancePre;
    private javax.swing.JLabel jLblDetails;
    private javax.swing.JLabel jLblMaxPrice;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jPaymentsLabel1;
    private javax.swing.JLabel jPaymentsPost;
    private javax.swing.JLabel jPaymentsPre;
    private javax.swing.JLabel jTaxesLabel;
    private javax.swing.JLabel jTaxesPost;
    private javax.swing.JLabel jTaxesPre;
    // End of variables declaration//GEN-END:variables

}
